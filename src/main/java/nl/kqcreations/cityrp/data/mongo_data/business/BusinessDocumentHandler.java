package nl.kqcreations.cityrp.data.mongo_data.business;

import com.mongodb.client.MongoCollection;
import nl.kqcreations.cityrp.data.mongo_data.SimpleDocumentHandler;
import org.bson.Document;

public class BusinessDocumentHandler implements SimpleDocumentHandler<Business> {
	@Override
	public Document toDocument(Business business) {
		return null;
	}

	@Override
	public Business fromDocument(Document document) {
		return null;
	}

	@Override
	public <V> Business createDefault(V v) {
		return null;
	}

	@Override
	public MongoCollection<Document> getCollection() {
		return null;
	}

	@Override
	public Document findFirst(Document document) {
		return null;
	}

	@Override
	public void update(Business business) {

	}
}
