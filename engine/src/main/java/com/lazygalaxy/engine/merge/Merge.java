package com.lazygalaxy.engine.merge;

import com.lazygalaxy.engine.domain.MongoDocument;

public interface Merge<T extends MongoDocument> {
	public void apply(T newDocument, T storedDocument) throws Exception;
}
