package com.lazygalaxy.main;

import java.lang.reflect.Constructor;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.domain.WikipediaPage;
import com.lazygalaxy.helper.MongoConnectionHelper;
import com.lazygalaxy.helper.MongoHelper;
import com.lazygalaxy.load.jsoup.JSoupElementProcessor;
import com.lazygalaxy.load.wikipedia.WHSParentProcessor;
import com.lazygalaxy.load.wikipedia.WikipediaPageJSoupLoad;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;

public class UpsertWHSWikipediaMain {
	private static final Logger LOGGER = LogManager.getLogger(UpsertWHSWikipediaMain.class);

	public static void main(String[] args) {
		try {
			WikipediaPageJSoupLoad loader = new WikipediaPageJSoupLoad();
			MongoHelper<WikipediaPage> helper = loader.getHelper();

			long documentCount = loader.getHelper().getCollection().countDocuments();
			if (documentCount == 0) {
				loader.setProcessor(new WHSParentProcessor());
				loader.upsert("World_Heritage_sites_by_country");
				loader = new WikipediaPageJSoupLoad();
			} else {
				LocalDateTime latestUpdateDateTime = LocalDateTime.now().minusMinutes(1);
				// find pages that need to be updated
				FindIterable<WikipediaPage> pages = helper.getCollection()
						.find(Filters.and(Filters.eq("mustUpdate", true),
								Filters.or(
										Filters.and(Filters.exists("updateDateTime"),
												Filters.lt("updateDateTime", latestUpdateDateTime)),
										Filters.exists("updateDateTime", false))));
				// iterate through pages and update the
				for (WikipediaPage page : pages) {
					if (!StringUtils.isBlank(page.processor)) {
						Class<?> clazz = Class.forName(page.processor);
						Constructor<?> ctor = clazz.getConstructor();
						Object processor = ctor.newInstance();
						loader.setProcessor((JSoupElementProcessor<List<WikipediaPage>>) processor);
					}
					loader.upsert(page.name);
				}
			}
		} catch (Exception e) {
			LOGGER.error("exception thrown in main", e);
		} finally {
			MongoConnectionHelper.INSTANCE.close();
		}
	}
}
