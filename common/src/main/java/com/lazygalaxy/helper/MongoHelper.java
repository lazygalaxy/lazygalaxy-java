package com.lazygalaxy.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.domain.MongoDocument;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class MongoHelper<T extends MongoDocument> {

	private static Map<Class, MongoHelper> helperMap = new HashMap<Class, MongoHelper>();

	public static <T extends MongoDocument> MongoHelper<T> getHelper(Class<T> clazz) {
		if (helperMap.containsKey(clazz)) {
			return helperMap.get(clazz);
		}

		MongoHelper<T> helper = new MongoHelper<T>(clazz);
		helperMap.put(clazz, helper);
		return helper;
	}

	private static final Logger LOGGER = LogManager.getLogger(MongoHelper.class);

	private final MongoCollection<T> collection;

	private MongoHelper(Class<T> clazz) {
		this.collection = MongoConnectionHelper.INSTANCE.getDatabase()
				.getCollection(clazz.getSimpleName().toLowerCase(), clazz);
	}

	public MongoCollection<T> getCollection() {
		return collection;
	}

	public T getDocumentByField(String field, Object value) {
		return collection.find(Filters.eq(field, value)).first();
	}

	public T getDocumentById(String id) {
		return getDocumentByField("_id", id);
	}

	public boolean deleteDocumentById(String id) {
		LOGGER.info("deleting  id: " + id);
		return collection.deleteOne(Filters.eq("_id", id)).wasAcknowledged();

	}

	public T getDocumentByLabels(Set<String> labels) throws Exception {
		FindIterable<T> iterable = collection.find(Filters.in("labels", labels));
		ArrayList<T> documents = iterable.into(new ArrayList<T>());
		if (documents.size() == 0) {
			return null;
		}

		if (documents.size() > 1) {
			int size = documents.size();
			for (T document : documents) {
				if (document.updateDateTime == null) {
					deleteDocumentById(document.id);
					size = size - 1;
				}
				if (size == 1) {
					break;
				}
			}
			iterable = collection.find(Filters.in("labels", labels));
			documents = iterable.into(new ArrayList<T>());
		}

		if (documents.size() > 1) {
			throw new Exception("multiple documents found for labels: " + labels);
		}

		return documents.get(0);
	}

	public T getDocumentByLabel(String label) throws Exception {
		FindIterable<T> iterable = collection.find(Filters.in("labels", label));
		ArrayList<T> documents = iterable.into(new ArrayList<T>());
		if (documents.size() == 0) {
			return null;
		}
		if (documents.size() > 1) {
			throw new Exception("multiple documents found for label: " + label);
		}
		return documents.get(0);
	}

	public void upsert(T document) throws Exception {
		T oldDocument = merge(document);

		if (oldDocument == null) {
			LOGGER.info("inserting  id: " + document.id + ", labels: " + document.labels + " " + document);
			collection.insertOne(document);
		} else if (!document.equals(oldDocument)) {
			LOGGER.info("replacing id: " + document.id + ", labels: " + document.labels + " " + document);
			collection.replaceOne(Filters.eq("_id", document.id), document);
		}
	}

	private T merge(T newDocument) throws Exception {
		T oldDocument = null;
		if (newDocument.labels != null) {
			oldDocument = getDocumentByLabels(newDocument.labels);
		}

		if (oldDocument == null) {
			oldDocument = getDocumentById(newDocument.id);
		}

		if (oldDocument != null) {
			newDocument.id = oldDocument.id;
			newDocument.labels.addAll(oldDocument.labels);
			return oldDocument;
		}

		return null;
	}

}
