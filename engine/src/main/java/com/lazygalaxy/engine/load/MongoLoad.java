package com.lazygalaxy.engine.load;

import java.util.List;

import org.bson.conversions.Bson;

import com.lazygalaxy.engine.domain.MongoDocument;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.merge.Merge;

public abstract class MongoLoad<T1 extends MongoDocument, T2 extends MongoDocument> {

	private final MongoHelper<T1> readHelper;
	private final MongoHelper<T2> writeHelper;

	public MongoLoad(Class<T1> readClazz, Class<T2> writeClazz) throws Exception {
		this.readHelper = MongoHelper.getHelper(readClazz);
		this.writeHelper = MongoHelper.getHelper(writeClazz);
	}

	public void load(Bson... filters) throws Exception {
		load(null, filters);
	}

	public void load(Merge<T2> merge, Bson... filters) throws Exception {
		List<T1> documents = readHelper.getDocumentsByFilters(filters);
		for (T1 storedDocument : documents) {
			T2 document = getMongoDocument(storedDocument);
			writeHelper.upsert(document, merge);
		}

	}

	protected abstract T2 getMongoDocument(T1 storedDocument) throws Exception;

}
