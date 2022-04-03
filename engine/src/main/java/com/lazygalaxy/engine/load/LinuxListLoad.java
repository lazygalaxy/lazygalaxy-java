package com.lazygalaxy.engine.load;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.engine.domain.MongoDocument;

public abstract class LinuxListLoad<T extends MongoDocument> extends TextFileLoad<T> {
	private static final Logger LOGGER = LogManager.getLogger(LinuxListLoad.class);

	public LinuxListLoad(Class<T> clazz) throws Exception {
		super(clazz);
	}

	protected List<T> getMongoDocument(String line) throws Exception {
		String[] tokens = StringUtils.split(line, " ");
		if (tokens.length == 9) {
			return getMongoDocumentByList(tokens[8], Long.parseLong(tokens[4]));
		} else {
			return getMongoDocumentByList(StringUtils.join(Arrays.copyOfRange(tokens, 8, tokens.length), " "),
					Long.parseLong(tokens[4]));
		}
	}

	protected abstract List<T> getMongoDocumentByList(String file, long fileSize) throws Exception;

}
