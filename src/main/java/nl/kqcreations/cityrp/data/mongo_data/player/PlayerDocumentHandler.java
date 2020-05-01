package nl.kqcreations.cityrp.data.mongo_data.player;

import com.mongodb.client.MongoCollection;
import nl.kqcreations.cityrp.data.mongo_data.MongoConnector;
import nl.kqcreations.cityrp.data.mongo_data.SimpleDocumentHandler;
import nl.kqcreations.cityrp.data.mongo_data.bank.BankAccountData;
import nl.kqcreations.cityrp.settings.Settings;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class PlayerDocumentHandler implements SimpleDocumentHandler<PlayerData> {

	@Override
	public MongoCollection<Document> getCollection() {
		return MongoConnector.getInstance().getCollection("players");
	}

	@Override
	public Document toDocument(PlayerData playerData) {
		return null;
	}

	@Override
	public PlayerData fromDocument(Document document) {
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

	@Override
	public <V> PlayerData createDefault(V v) {
		if (!(v instanceof UUID)) {
			throw new IllegalArgumentException("Please specify a valid UUID as a parameter");
		}

		final UUID uuid = (UUID) v;

		final int accountId = BankAccountData.createMainPlayerBankAccount(uuid);

		List<Integer> banks = new ArrayList<>();
		banks.add(accountId);

		Document obj = new Document("uuid", uuid.toString())
				.append("level", 1)
				.append("rank", Settings.PlayerData.DEFAULT_RANK)
				.append("job", Settings.PlayerData.DEFAULT_JOB)
				.append("city-color", Settings.PlayerData.DEFAULT_CITY_COLOR)
				.append("banks", banks);

		getCollection().insertOne(obj);
		return fromDocument(obj);
	}

	@Override
	public Document findFirst(Document document) {
		return getCollection().find(document).first();
	}

	@Override
	public void update(PlayerData playerData) {
		Document document = new Document("uuid", playerData.getUuid().toString());
		Document playerDocument = toDocument(playerData);
		if (getCollection().find(document).first() == null) {
			getCollection().insertOne(playerDocument);
		} else {
			getCollection().replaceOne(
					eq("uuid", playerData.getUuid().toString()),
					playerDocument
			);
		}
	}
}
