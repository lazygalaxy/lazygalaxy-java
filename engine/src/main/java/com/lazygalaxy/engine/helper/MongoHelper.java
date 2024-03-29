package com.lazygalaxy.engine.helper;

import com.lazygalaxy.engine.domain.MongoDocument;
import com.lazygalaxy.engine.merge.Merge;
import com.lazygalaxy.engine.util.GeneralUtil;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MongoHelper<T extends MongoDocument> {

    private static final Logger LOGGER = LogManager.getLogger(MongoHelper.class);
    private static Map<Class, MongoHelper> helperMap = new HashMap<Class, MongoHelper>();
    private final MongoCollection<T> collection;

    private MongoHelper(Class<T> clazz) throws Exception {
        this.collection = MongoConnectionHelper.INSTANCE.getDatabase(clazz.getField("DATABASE").get(null).toString())
                .getCollection(clazz.getSimpleName().toLowerCase(), clazz);
    }

    public static <T extends MongoDocument> MongoHelper<T> getHelper(Class<T> clazz) throws Exception {
        if (helperMap.containsKey(clazz)) {
            return helperMap.get(clazz);
        }

        MongoHelper<T> helper = new MongoHelper<T>(clazz);
        helperMap.put(clazz, helper);
        return helper;
    }

    public MongoCollection<T> getCollection() {
        return collection;
    }

    public T getDocumentByField(String field, Object value) {
        return collection.find(Filters.eq(field, value)).first();
    }

    public List<T> getDocumentsByFilters(Bson sort, Bson... find) {
        if (find.length == 0) {
            if (sort == null) {
                return collection.find().into(new ArrayList<T>());
            } else {
                return collection.find().sort(sort).into(new ArrayList<T>());
            }
        } else {
            if (sort == null) {
                return collection.find(Filters.and(find)).into(new ArrayList<T>());
            } else {
                return collection.find(Filters.and(find)).sort(sort).into(new ArrayList<T>());
            }
        }
    }

    public T getDocumentById(String id) {
        return getDocumentByField("_id", id);
    }

    public boolean deleteDocumentById(String id) {
        LOGGER.info("deleting  id: " + id);
        return collection.deleteOne(Filters.eq("_id", id)).wasAcknowledged();
    }

    public DeleteResult deleteDocumentByFilters(Bson find) {
        return collection.deleteMany(find);
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
                LOGGER.info("inserting id: " + newDocument.id + " " + newDocument);
                collection.insertOne(newDocument);
            } else if (!newDocument.equals(storedDocument)) {
                LOGGER.info("replacing id: " + newDocument.id + " " + newDocument + " from: " + storedDocument);
                collection.replaceOne(Filters.eq("_id", newDocument.id), newDocument);
            }
        }
    }

}
