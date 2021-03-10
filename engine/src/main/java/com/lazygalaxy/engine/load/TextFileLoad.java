package com.lazygalaxy.engine.load;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.engine.domain.MongoDocument;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.merge.Merge;

public abstract class TextFileLoad<T extends MongoDocument> {
	private static final Logger LOGGER = LogManager.getLogger(TextFileLoad.class);

	private final MongoHelper<T> helper;

	public TextFileLoad(Class<T> clazz) throws Exception {
		this.helper = MongoHelper.getHelper(clazz);
	}

	public void load(String file) throws Exception {
		load(file, 0, null);
	}

	public void load(String file, long skipLines, Merge<T> merge) throws Exception {
		Stream<String> lines = Files.lines(Paths.get(ClassLoader.getSystemResource(file).toURI())).skip(skipLines);
		lines.forEach(s -> {
			try {
				if (!StringUtils.isBlank(s)) {
					List<T> documents = getMongoDocument(s);
					if (documents != null) {
						for (T document : documents) {
							helper.upsert(document, merge);
						}
					}
				}
			} catch (Exception e) {
				LOGGER.error("could not process: " + s, e);
			}
		});
		lines.close();
	}

	protected abstract List<T> getMongoDocument(String line) throws Exception;
}
