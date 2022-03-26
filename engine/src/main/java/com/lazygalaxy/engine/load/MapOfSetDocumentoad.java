package com.lazygalaxy.engine.load;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.lazygalaxy.engine.domain.MongoDocument;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.merge.Merge;

public abstract class MapOfSetDocumentoad<T1 extends MongoDocument> {

	private final MongoHelper<T1> helper;

	public MapOfSetDocumentoad(Class<T1> readClazz) throws Exception {
		this.helper = MongoHelper.getHelper(readClazz);
	}

	public void load(Map<String, Set<T1>> mapObjects) throws Exception {
		load(null, mapObjects);
	}

	public void load(Merge<T1> merge, Map<String, Set<T1>> mapObjects) throws Exception {
		for (Entry<String, Set<T1>> entry : mapObjects.entrySet()) {
			Collection<T1> writeDocuments = getMongoDocument(entry.getKey(), entry.getValue());
			if (writeDocuments != null) {
				for (T1 writeDocument : writeDocuments) {
					helper.upsert(writeDocument, merge);
				}
			}
		}

	}

	protected abstract Collection<T1> getMongoDocument(String key, Set<T1> storedDocument) throws Exception;

}
