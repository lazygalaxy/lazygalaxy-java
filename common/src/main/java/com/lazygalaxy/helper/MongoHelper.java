package com.lazygalaxy.helper;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.domain.MongoDocument;
import com.lazygalaxy.util.GeneralUtil;
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

	public T getDocumentByLabel(String label) {
		label = GeneralUtil.alphanumerify(label);
		return collection.find(Filters.in("labels", label)).first();
	}

	public void upsert(T document) {
		T dbDocument = getDocumentById(document.id);
		if (dbDocument == null && document.labels != null) {
			// TODO: relook into this idea
			dbDocument = getDocumentByLabel(document.name);
		}

		if (dbDocument == null) {
			LOGGER.info("inserting: " + document);
			collection.insertOne(document);
		} else if (!document.equals(dbDocument)) {
			LOGGER.info("replacing: " + document);
			collection.replaceOne(Filters.eq("_id", dbDocument.id), document);
		}
	}
}
