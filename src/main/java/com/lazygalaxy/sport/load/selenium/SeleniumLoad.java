package com.lazygalaxy.sport.load.selenium;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.lazygalaxy.sport.domain.MongoDocument;
import com.lazygalaxy.sport.helpers.MongoHelper;

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

	public void load(String link) throws Exception {
		Set<String> linkSet = getLinks(link);
		for (String documentLink : linkSet) {
			upsert(documentLink);
		}
	}

	public void upsert(String link) throws Exception {
		T document = getMongoDocument(link);
		helper.upsert(document);
	}

	public void saveHTML(String link) throws Exception {
		WebDriver driver = getHTMLDocument(link);
		MongoDocument mongoDocument = getMongoDocument(link);

		Path path = Paths.get(mongoDocument.id + ".html");
		Files.write(path, driver.getPageSource().getBytes(), StandardOpenOption.CREATE_NEW);
	}

	public abstract Set<String> getLinks(String html) throws Exception;

	public abstract T getMongoDocument(String html) throws Exception;
}