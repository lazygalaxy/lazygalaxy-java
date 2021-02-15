package com.lazygalaxy.engine.merge;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;

import com.lazygalaxy.engine.domain.MongoDocument;

public class FieldMerge<T extends MongoDocument> implements Merge<T> {

	@Override
	public void apply(T newDocument, T storedDocument) throws Exception {
		for (Field field : newDocument.getClass().getFields()) {
			if (!MongoDocument.EXCLUDE_FIELDS.contains(field.getName())) {
				Object newValue = field.get(newDocument);
				Object storedValue = field.get(storedDocument);

				if (newValue == null && storedValue != null) {
					field.set(newDocument, storedValue);
					if (StringUtils.equals(field.getName(), "name")) {
						newDocument.labels.clear();
						newDocument.addLabel(newDocument.name);
					}
				}
			}
		}
	}
}
