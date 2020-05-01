package nl.kqcreations.cityrp.data.mongo_data;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import org.bson.Document;

public class MongoData {

	@Getter
	private static MongoCollection<Document> collection;


	public MongoData(String collectionName) {
		collection = MongoConnector.getInstance().getCollection(collectionName);
	}

}
