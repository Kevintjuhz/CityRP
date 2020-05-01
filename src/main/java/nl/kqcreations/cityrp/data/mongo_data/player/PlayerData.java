package nl.kqcreations.cityrp.data.mongo_data.player;

import lombok.Getter;
import lombok.Setter;
import nl.kqcreations.cityrp.data.mongo_data.SimpleDocumentHandler;
import nl.kqcreations.cityrp.data.mongo_data.bank.BankAccount;
import org.bson.Document;

import java.util.*;


@Getter
@Setter
public final class PlayerData {

	private static Map<UUID, PlayerData> playersCacheMap = new HashMap<>();

	private static SimpleDocumentHandler<PlayerData> documentHandler;

	static {
		documentHandler = new PlayerDocumentGenerator();
	}

	private UUID uuid;
	private int level = 1;
	private String rank;
	private String job;
	private String cityColor;
	private List<BankAccount> bankAccounts = new ArrayList<>();


	public boolean addBankAccount(BankAccount bankAccount) {
		for (BankAccount bankAccount1 : bankAccounts) {
			if (bankAccount1 != null && bankAccount != null)
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
	 * @return A bank account
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
		PlayerData playerData = null;

		Document found = documentHandler.findFirst(object);
		if (found == null) {
			playerData = documentHandler.createDefault(uuid);
		} else {
			playerData = documentHandler.fromDocument(found);
		}

		playersCacheMap.put(uuid, playerData);
	}

	/**
	 * This method is used when the player leaves the server. It will store the new
	 * player data in the mongodb.
	 *
	 * @param uuid player uuid
	 */
	public static void onPlayerLeave(UUID uuid) {
		final PlayerData playerData = getPlayerData(uuid);

		// if player somehow doesn't exist then return
		if (playerData == null) {
			return;
		}

		// Saves the player to the mongo collection
		documentHandler.update(playerData);

		// removes the player from the player cache map
		playersCacheMap.remove(uuid);
	}


	/**
	 * This method should be called when you want to get the playerdata object
	 * from a certain player.
	 *
	 * @param playeruuid player uuid
	 * @return the playerdata object
	 */
	public static PlayerData getPlayerData(UUID playeruuid) {
		return playersCacheMap.get(playeruuid);
	}
}
