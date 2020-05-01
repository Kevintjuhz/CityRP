package nl.kqcreations.cityrp.data.mongo_data;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import lombok.Setter;
import nl.kqcreations.cityrp.data.mongo_data.bank.BankAccount;
import nl.kqcreations.cityrp.data.mongo_data.bank.BankAccountData;
import nl.kqcreations.cityrp.settings.Settings;
import org.bson.Document;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

@Getter
@Setter
public final class PlayerData extends MongoData {

	private static MongoCollection<Document> players = MongoConnector.getInstance().getCollection("players");

	private static Map<UUID, PlayerData> playersCacheMap = new HashMap<>();

	private int level = 1;
	private String rank;
	private String job;
	private String cityColor;
	private List<BankAccount> bankAccounts = new ArrayList<>();

	public PlayerData() {
		super("players");
	}

	public boolean addBankAccount(BankAccount bankAccount) {
		for (BankAccount bankAccount1 : bankAccounts) {
			if (bankAccount1.getAccountId() == bankAccount.getAccountId())
				return false;
		}

		bankAccounts.add(bankAccount);
		return true;
	}

	/**
	 * Gets all the accountids from all the bankaccounts in the
	 * arraylist.
	 *
	 * @return a list of accountids the player has access to
	 */
	public List<Integer> getBankAccountIds() {
		List<Integer> accountIds = new ArrayList<>();

		for (BankAccount bankAccount : bankAccounts) {
			accountIds.add(bankAccount.getAccountId());
		}

		return accountIds;
	}

	/**
	 * Filters bankaccounts by a list of accounttypes.
	 *
	 * @param types
	 * @return
	 */
	public List<BankAccount> getFilteredBankAccounts(List<BankAccount.AccountType> types) {
		List<BankAccount> accounts = new ArrayList<>();

		for (BankAccount.AccountType type : types)
			for (BankAccount bankAccount : bankAccounts)
				if (bankAccount.getType().equals(type))
					accounts.add(bankAccount);

		return accounts;
	}

	/**
	 * Filters the bankaccounts by the given type.
	 *
	 * @param type
	 * @return
	 */
	public List<BankAccount> getFilteredBankAccounts(BankAccount.AccountType type) {
		List<BankAccount> accounts = new ArrayList<>();

		for (BankAccount bankAccount : bankAccounts) {
			if (bankAccount != null)
				if (bankAccount.getType().equals(type))
					accounts.add(bankAccount);
		}

		return accounts;
	}

	/**
	 * Gets the players main bankaccount
	 *
	 * @return
	 */
	public BankAccount getMainAccount() {
		for (BankAccount bankAccount : bankAccounts) {
			if (bankAccount.isMain())
				return bankAccount;
		}

		return null;
	}

	/**
	 * This method is used onPlayerLogin, it will get the document
	 * from the MongoDB. If that doesn't exist it will create a new one.
	 * Then it will get the PlayerData object from the document.
	 * And insert it in the playerCacheMap.
	 *
	 * @param uuid
	 */
	public static void onPlayerLogin(UUID uuid) {

		playersCacheMap.remove(uuid);
		Document object = new Document("uuid", uuid.toString());

		Document found = players.find(object).first();
		if (found == null) {
			found = createPlayer(uuid);
		}

		PlayerData playerData = getPlayerDataFromDocument(found);


		playersCacheMap.put(uuid, playerData);
	}

	/**
	 * This method is used when the player leaves the server. It will store the new
	 * player data in the mongodb.
	 *
	 * @param uuid
	 */
	public static void onPlayerLeave(UUID uuid) {
		final PlayerData playerData = getPlayerData(uuid);

		// if player somehow doesn't exist then return
		if (playerData == null) {
			return;
		}

		// Updates the player in the mongodb
		players.updateOne(
				eq("uuid", uuid.toString()),
				combine(set("level", playerData.getLevel()),
						set("rank", playerData.getRank()),
						set("job", playerData.getJob()),
						set("city-color", playerData.getCityColor()),
						set("banks", playerData.getBankAccountIds())
				)
		);

		// removes the player from the player cache map
		playersCacheMap.remove(uuid);
	}

	/**
	 * This method is used to create a new player instance if the data doesn't exist
	 * in the mongodb. This will be checked on every join
	 *
	 * @param uuid
	 * @return
	 */
	private static Document createPlayer(UUID uuid) {
		final int accountId = BankAccountData.createMainPlayerBankAccount(uuid);

		List<Integer> banks = new ArrayList<>();
		banks.add(accountId);

		Document obj = new Document("uuid", uuid.toString())
				.append("level", 1)
				.append("rank", Settings.PlayerData.DEFAULT_RANK)
				.append("job", Settings.PlayerData.DEFAULT_JOB)
				.append("city-color", Settings.PlayerData.DEFAULT_CITY_COLOR)
				.append("banks", banks);

		players.insertOne(obj);
		return obj;
	}

	/**
	 * This method converts a MongoDB Document to a PlayerData object.
	 *
	 * @param document
	 * @return The PlayerData object for that specific player
	 */
	private static PlayerData getPlayerDataFromDocument(Document document) {
		PlayerData playerData = new PlayerData();
		playerData.setLevel(document.getInteger("level"));
		playerData.setRank(document.getString("rank"));
		playerData.setCityColor(document.getString("city-color"));
		playerData.setJob(document.getString("job"));

		if (document.get("banks") != null) {

			List<Integer> bankAccountIds = document.getList("banks", Integer.class);

			// This stores all the bankAccount objects the player has access to.
			playerData.setBankAccounts(BankAccountData.getBankAccounts(bankAccountIds));
		}

		return playerData;
	}


	/**
	 * This method should be called when you want to get the playerdata object
	 * from a certain player.
	 *
	 * @param playeruuid
	 * @return the playerdata object
	 */
	public static PlayerData getPlayerData(UUID playeruuid) {
		return playersCacheMap.get(playeruuid);
	}
}
