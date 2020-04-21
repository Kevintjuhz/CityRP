package nl.kqcreations.cityrp.data;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mineacademy.fo.Common;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MongoConnector {

	@Getter
	private static final MongoConnector instance = new MongoConnector();

	private MongoClient mongoClient;
	private MongoDatabase database;

	public void connect() {

		try {
			mongoClient = MongoClients.create();
			database = mongoClient.getDatabase("cityrp");
			Common.log("&cSuccesfully connected to the database named cityrp");
		} catch (IllegalArgumentException e) {
			Common.error(true, e, "Database not found");
		}

	}

	public MongoCollection getCollection(String name) {
		return database.getCollection(name);
	}

}
