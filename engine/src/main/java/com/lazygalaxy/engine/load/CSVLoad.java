package com.lazygalaxy.engine.load;

import com.lazygalaxy.engine.domain.MongoDocument;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.merge.Merge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public abstract class CSVLoad<T extends MongoDocument> {
    private static final Logger LOGGER = LogManager.getLogger(CSVLoad.class);

    private final MongoHelper<T> helper;

    public CSVLoad(Class<T> clazz) throws Exception {
        this.helper = MongoHelper.getHelper(clazz);
    }

    public void load(String file) throws Exception {
        load(file, 0, ",", null);
    }

    public void load(String file, long skipLines, String delimiter, Merge<T> merge) throws Exception {
        Stream<String> lines = Files.lines(Paths.get(ClassLoader.getSystemResource(file).toURI())).skip(skipLines);
        lines.forEach(s -> {
            String[] tokens = s.split(delimiter + "(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            // String[] tokens = split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            for (int i = 0; i < tokens.length; i++) {
                tokens[i] = tokens[i].trim();
                tokens[i] = tokens[i].replaceAll("[\\\"]", "");
            }
            try {
                List<T> documents = getMongoDocument(tokens);
                if (documents != null) {
                    for (T document : documents) {
                        helper.upsert(document, merge);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("could not process: " + s, e);
            }
        });
        lines.close();
    }

    protected abstract List<T> getMongoDocument(String[] tokens) throws Exception;
}
