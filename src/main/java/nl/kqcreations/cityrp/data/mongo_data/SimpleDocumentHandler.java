package nl.kqcreations.cityrp.data.mongo_data;

import com.mongodb.client.MongoCollection;
import org.bson.Document;


public interface SimpleDocumentHandler<T> {

	/**
	 * Creates a document from the given object
	 *
	 * @param t
	 * @return
	 */
	Document toDocument(T t);

	/**
	 * Creates a object from the given document
	 *
	 * @param document
	 * @return
	 */
	T fromDocument(Document document);

	/**
	 * Creates a default object
	 *
	 * @param object
	 * @param <V>
	 * @return
	 */
	<V> T createDefault(V v);

	/**
	 * Returns a mongo collection
	 *
	 * @return
	 */
	MongoCollection<Document> getCollection();


	/**
	 * Returns the first find document from the given document
	 *
	 * @param document
	 * @return
	 */
	Document findFirst(Document document);

	/**
	 * Updates the object into the mongo db collection
	 *
	 * @param t
	 */
	void update(T t);
}
