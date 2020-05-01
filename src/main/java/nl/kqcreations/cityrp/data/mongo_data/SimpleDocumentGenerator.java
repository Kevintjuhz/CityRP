package nl.kqcreations.cityrp.data.mongo_data;

import org.bson.Document;


public interface SimpleDocumentGenerator<T> {

	Document toDocument(T t);

	T fromDocument(Document document);

	<V> T createDefault(V object);

	void save(Document document);
}
