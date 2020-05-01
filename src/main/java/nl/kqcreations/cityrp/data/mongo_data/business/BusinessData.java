package nl.kqcreations.cityrp.data.mongo_data.business;

import com.mongodb.client.MongoCollection;
import nl.kqcreations.cityrp.data.mongo_data.MongoConnector;
import nl.kqcreations.cityrp.data.mongo_data.bank.BankAccount;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

public class BusinessData {

	private static MongoCollection<Document> businessesCollection = MongoConnector.getInstance().getCollection("businesses");

	private static Map<String, BankAccount> businessesMap = new HashMap<>();


}
