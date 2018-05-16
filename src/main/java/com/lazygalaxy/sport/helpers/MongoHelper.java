package com.lazygalaxy.sport.helpers;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.sport.domain.MongoDocument;
import com.lazygalaxy.sport.utils.GeneralUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class MongoHelper<T extends MongoDocument> {

	private static Map<Class, MongoHelper> map = new HashMap<Class, MongoHelper>();

	public static <T extends MongoDocument> MongoHelper<T> getHelper(Class<T> clazz) {
		if (map.containsKey(clazz)) {
			return map.get(clazz);
		}

		MongoHelper<T> helper = new MongoHelper<>(clazz);
		map.put(clazz, helper);
		return helper;
	}

	private static final Logger LOGGER = LogManager.getLogger(MongoHelper.class);

	private final MongoCollection<T> collection;

	private final Map<String, T> documentHashMap = new HashMap<String, T>();;

	private MongoHelper(Class<T> clazz) {
		this.collection = MongoConnectionHelper.INSTANCE.getDatabase()
				.getCollection(clazz.getSimpleName().toLowerCase(), clazz);
	}

	public T getDocumentByLabel(String label) {
		label = GeneralUtil.normalize(label);
		if (documentHashMap.containsKey(label)) {
			return documentHashMap.get(label);
		} else {
			T document = collection.find(Filters.in("labels", label)).first();
			updateHashMap(document);
			return document;
		}
	}

	public void upsertByLabel(T document) {
		T dbDocument = getDocumentByLabel(document.getName());
		if (dbDocument == null) {
			LOGGER.info("inserting: " + document);
			collection.insertOne(document);
			updateHashMap(document);
		} else if (!document.equals(dbDocument)) {
			LOGGER.info("replacing: " + document);
			collection.replaceOne(Filters.eq("_id", dbDocument.getId()), document);
			updateHashMap(document);
		}
	}

	private void updateHashMap(T document) {
		if (document != null) {
			for (String label : document.getLabels()) {
				documentHashMap.put(label, document);
			}
		}
	}
}
