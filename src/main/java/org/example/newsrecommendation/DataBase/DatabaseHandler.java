package org.example.newsrecommendation.DataBase;

import com.mongodb.client.*;
import org.bson.Document;

import java.util.List;

public class DatabaseHandler implements AutoCloseable {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "NewsRecommendations";

    private MongoClient mongoClient;
    private MongoDatabase database;

    public DatabaseHandler() {
        connect();
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    private void connect() {
        try {
            mongoClient = MongoClients.create(CONNECTION_STRING);
            database = mongoClient.getDatabase(DATABASE_NAME);
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to MongoDB", e);
        }
    }

    public void insertDocument(String collectionName, Document document) {
        try {
            MongoCollection<Document> collection = database.getCollection(collectionName);
            collection.insertOne(document);
        } catch (Exception e) {
            throw new RuntimeException("Failed to insert document into collection: " + collectionName, e);
        }
    }

    public Document findDocument(String collectionName, Document query) {
        try {
            MongoCollection<Document> collection = database.getCollection(collectionName);
            return collection.find(query).first();
        } catch (Exception e) {
            throw new RuntimeException("Failed to find document in collection: " + collectionName, e);
        }
    }

    public List<Document> findDocuments(String collectionName, Document query) {
        try {
            MongoCollection<Document> collection = database.getCollection(collectionName);
            return collection.find(query).into(new java.util.ArrayList<>());
        } catch (Exception e) {
            throw new RuntimeException("Failed to find documents in collection: " + collectionName, e);
        }
    }

    public void updateDocument(String collectionName, Document query, Document update) {
        try {
            MongoCollection<Document> collection = database.getCollection(collectionName);
            collection.updateOne(query, update);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update document in collection: " + collectionName, e);
        }
    }

    @Override
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
