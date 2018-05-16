package com.lazygalaxy.sport.load.csv;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.lazygalaxy.sport.domain.MongoDocument;
import com.lazygalaxy.sport.helpers.MongoHelper;

public abstract class CSVLoad<T extends MongoDocument> {
	private final MongoHelper<T> helper;

	public CSVLoad(Class<T> clazz) {
		this.helper = MongoHelper.getHelper(clazz);
	}

	public void load(String file) throws Exception {
		Stream<String> lines = Files.lines(Paths.get(ClassLoader.getSystemResource(file).toURI()));
		lines.forEach(s -> {
			String[] tokens = s.split(",");
			T document = getMongoDocument(tokens);
			helper.upsertByLabel(document);
		});
		lines.close();
	}

	protected abstract T getMongoDocument(String[] tokens);
}
