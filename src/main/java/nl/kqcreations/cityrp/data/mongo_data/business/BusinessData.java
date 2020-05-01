package nl.kqcreations.cityrp.data.mongo_data.business;

import com.mongodb.client.model.Sorts;
import nl.kqcreations.cityrp.data.mongo_data.SimpleDocumentHandler;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

public class BusinessData {

	private static SimpleDocumentHandler<Business> documentHandler;

	static {
		documentHandler = new BusinessDocumentHandler();
	}

	private static Map<Integer, Business> businessesMap = new HashMap<>();

	/**
	 * Adds a business to the business map
	 *
	 * @param business A business object
	 */
	public static void addBusiness(Business business) {
		businessesMap.putIfAbsent(business.getBusinessId(), business);
	}

	/**
	 * Adds a business to the business map and saves it immediately to the mongo collection
	 *
	 * @param business A business object
	 */
	public static void addNewBusiness(Business business) {
		addBusiness(business);
		documentHandler.update(business);
	}

	/**
	 * Generates a new business id.
	 *
	 * @return the newly generated business id
	 */
	public static int getNewId() {
		Document document = documentHandler.getCollection().find().sort(Sorts.descending("business-id")).first();

		if (document == null)
			return 1;

		return document.getInteger("business-id") + 1;
	}
}
