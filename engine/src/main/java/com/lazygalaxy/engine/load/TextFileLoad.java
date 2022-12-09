package com.lazygalaxy.engine.load;

import com.lazygalaxy.engine.domain.MongoDocument;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.merge.Merge;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public abstract class TextFileLoad<T extends MongoDocument> {
    private static final Logger LOGGER = LogManager.getLogger(TextFileLoad.class);

    private final MongoHelper<T> helper;

    public TextFileLoad(Class<T> clazz) throws Exception {
        this.helper = MongoHelper.getHelper(clazz);
    }

    public void load(String file) throws Exception {
        load(file, 0, null);
    }

    public boolean load(String file, long skipLines, Merge<T> merge) throws Exception {
        URL url = ClassLoader.getSystemResource(file);
        if (url != null) {
            Stream<String> lines = Files.lines(Paths.get(url.toURI())).skip(skipLines);
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
            doAfterLoad();
            return true;
        }
        return false;
    }

    protected void doAfterLoad() {

    }

    protected abstract List<T> getMongoDocument(String line) throws Exception;
}
