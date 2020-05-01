package nl.kqcreations.cityrp.data.mongo_data.bank;

import com.mongodb.client.MongoCollection;
import nl.kqcreations.cityrp.data.mongo_data.MongoConnector;
import nl.kqcreations.cityrp.data.mongo_data.SimpleDocumentGenerator;
import org.bson.Document;

public class BankDocumentGenerator implements SimpleDocumentGenerator<BankAccount> {

	public static MongoCollection<Document> collection = MongoConnector.getInstance().getCollection("bank-accounts");

	@Override
	public Document toDocument(BankAccount bankAccount) {
		return null;
	}

	@Override
	public BankAccount fromDocument(Document document) {
		return null;
	}

	@Override
	public <V> BankAccount createDefault(V object) {
		return null;
	}

	@Override
	public void save(Document document) {

	}
}
