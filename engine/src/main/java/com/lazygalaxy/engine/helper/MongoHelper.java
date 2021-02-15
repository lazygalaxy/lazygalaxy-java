package com.lazygalaxy.engine.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.conversions.Bson;

import com.lazygalaxy.engine.domain.MongoDocument;
import com.lazygalaxy.engine.merge.Merge;
import com.lazygalaxy.engine.util.GeneralUtil;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class MongoHelper<T extends MongoDocument> {

	private static Map<Class, MongoHelper> helperMap = new HashMap<Class, MongoHelper>();

	public static <T extends MongoDocument> MongoHelper<T> getHelper(Class<T> clazz) throws Exception {
		if (helperMap.containsKey(clazz)) {
			return helperMap.get(clazz);
		}

		MongoHelper<T> helper = new MongoHelper<T>(clazz);
		helperMap.put(clazz, helper);
		return helper;
	}

	private static final Logger LOGGER = LogManager.getLogger(MongoHelper.class);

	private final MongoCollection<T> collection;

	private MongoHelper(Class<T> clazz) throws Exception {
		this.collection = MongoConnectionHelper.INSTANCE.getDatabase(clazz.getField("DATABASE").get(null).toString())
				.getCollection(clazz.getSimpleName().toLowerCase(), clazz);
	}

	public MongoCollection<T> getCollection() {
		return collection;
	}

	public T getDocumentByField(String field, Object value) {
		return collection.find(Filters.eq(field, value)).first();
	}

	public List<T> getDocumentByFilters(Bson... filters) {

		return collection.find(Filters.and(filters)).into(new ArrayList<T>());
	}

	public T getDocumentById(String id) {
		id = GeneralUtil.alphanumerify(id);
		return getDocumentByField("_id", id);
	}

	public boolean deleteDocumentById(String id) {
		id = GeneralUtil.alphanumerify(id);
		LOGGER.info("deleting  id: " + id);
		return collection.deleteOne(Filters.eq("_id", id)).wasAcknowledged();

	}

	public List<T> getDocumentsByLabel(String label) throws Exception {
		label = GeneralUtil.alphanumerify(label);
		FindIterable<T> iterable = collection.find(Filters.in("labels", label));
		return iterable.into(new ArrayList<T>());
	}

	public void upsert(T newDocument) throws Exception {
		upsert(newDocument, null);
	}

	public void upsert(T newDocument, Merge<T> merge) throws Exception {
		if (newDocument != null) {
			T storedDocument = getDocumentById(newDocument.id);

			if (storedDocument != null && merge != null && !newDocument.equals(storedDocument)) {
				merge.apply(newDocument, storedDocument);
			}

			if (storedDocument == null) {
				LOGGER.info("inserting  id: " + newDocument.id + ", labels: " + newDocument.labels + " " + newDocument);
				collection.insertOne(newDocument);
			} else if (!newDocument.equals(storedDocument)) {
				LOGGER.info("replacing id: " + newDocument.id + ", labels: " + newDocument.labels + " " + newDocument);
				collection.replaceOne(Filters.eq("_id", newDocument.id), newDocument);
			}
		}
	}

}
