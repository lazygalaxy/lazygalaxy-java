package com.lazygalaxy.helper;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.lazygalaxy.util.PropertiesUtil;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class MongoConnectionHelper {
	public static final MongoConnectionHelper INSTANCE = new MongoConnectionHelper();

	private final MongoClient client;
	private final MongoDatabase database;

	private MongoConnectionHelper() {
		client = new MongoClient(new MongoClientURI(PropertiesUtil.getMongoDBURI()));
		MongoDatabase clientDatabase = client.getDatabase("lazygalaxy");

		CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));

		this.database = clientDatabase.withCodecRegistry(pojoCodecRegistry);
	}

	public MongoDatabase getDatabase() {
		return database;
	}

	public void close() {
		client.close();
	}
}
