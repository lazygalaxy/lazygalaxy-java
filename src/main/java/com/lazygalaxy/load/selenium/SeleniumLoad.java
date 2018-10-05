package com.lazygalaxy.load.selenium;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.lazygalaxy.domain.MongoDocument;
import com.lazygalaxy.helpers.MongoHelper;

public abstract class SeleniumLoad<T extends MongoDocument> {

	private final WebDriver driver;
	private final MongoHelper<T> helper;

	public SeleniumLoad(Class<T> clazz) {
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("--headless");
		this.driver = new ChromeDriver(chromeOptions);
		this.helper = MongoHelper.getHelper(clazz);
	}

	protected WebDriver getHTMLDocument(String link) throws Exception {

		if (link.startsWith("http")) {
			driver.get(link);
		} else {
			File file = new File(SeleniumLoad.class.getClassLoader().getResource(link).getFile());
			driver.get("file:" + file.getPath());
		}

		return driver;
	}

	public void upsertLinks(String link, int minLinks) throws Exception {
		Set<String> links = getLinks(link, minLinks);
		for (String documentLink : links) {
			upsert(documentLink);
		}
	}

	public void saveSourceLinks(String location, String link, int minLinks) throws Exception {
		Set<String> links = getLinks(link, minLinks);
		for (String documentLink : links) {
			saveSource(location, documentLink);
		}
	}

	public void upsert(String link) throws Exception {
		T document = getMongoDocument(link, true);
		helper.upsert(document);
	}

	public void saveSource(String location, String link) throws Exception {
		WebDriver driver = getHTMLDocument(link);
		// clickActions(driver);
		// MongoDocument mongoDocument = getMongoDocument(link, false);

		String[] parts = link.split("/");
		Path path = Paths.get(location + parts[parts.length - 1] + ".html");
		Files.write(path, driver.getPageSource().getBytes(), StandardOpenOption.CREATE_NEW);
	}

	public abstract void clickActions(WebDriver driver) throws Exception;

	public abstract Set<String> getLinks(String html, int minLinks) throws Exception;

	public abstract T getMongoDocument(String html, boolean complete) throws Exception;
}