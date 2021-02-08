package com.lazygalaxy.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.lazygalaxy.util.PropertiesUtil;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConnectionHelper {
	private static final Logger LOGGER = LogManager.getLogger(MongoConnectionHelper.class);

	public static final MongoConnectionHelper INSTANCE = new MongoConnectionHelper();

	private final MongoClient client;

	private MongoConnectionHelper() {

		ConnectionString connectionString = new ConnectionString(PropertiesUtil.getURI());
		CodecRegistry pojoCodecRegistry = CodecRegistries
				.fromProviders(PojoCodecProvider.builder().automatic(true).build());
		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				pojoCodecRegistry);
		MongoClientSettings clientSettings = MongoClientSettings.builder().applyConnectionString(connectionString)
				.codecRegistry(codecRegistry).build();

		client = MongoClients.create(clientSettings);
		client.listDatabaseNames().forEach(name -> LOGGER.info(name));

	}

	public MongoDatabase getDatabase(String database) {
		return client.getDatabase(database);
	}

	public void close() {
		client.close();
	}
}
