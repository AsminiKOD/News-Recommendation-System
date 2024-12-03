package org.example.newsrecommendation.NewsArticlesController;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.newsrecommendation.Service.ArticleCategorizer;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class ArticleFetcher {

    private static final String CSV_FILE_PATH = "src/main/resources/org/example/newsrecommendation/Datasets/Articles.csv";
    private static final String DATABASE_URL = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "NewsRecommendations";
    private static final String COLLECTION_NAME = "Article";

    public static void main(String[] args) {
        try {
            // Step 1: Read articles from CSV
            List<CSVRecord> records = readCSV(CSV_FILE_PATH);

            // Step 2: Categorize and store articles
            categorizeAndStoreArticles(records);

            System.out.println("Articles fetched and stored successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to read articles from CSV file
    private static List<CSVRecord> readCSV(String filePath) throws IOException {
        FileReader fileReader = new FileReader(filePath);
        CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withHeader("Article", "Date", "Heading").withIgnoreHeaderCase().withTrim());
        return csvParser.getRecords();
    }

    // Method to categorize and store articles in MongoDB
    private static void categorizeAndStoreArticles(List<CSVRecord> records) {
        MongoClient mongoClient = MongoClients.create(DATABASE_URL);
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

        // Initialize the categorizer
        ArticleCategorizer categorizer = new ArticleCategorizer();

        for (CSVRecord record : records) {
            String heading = record.get("Heading");
            String article = record.get("Article");
            String date = record.get("Date");


            // Categorize the article
            String category = categorizer.categorizeArticle(article);

            // Only store the article if it has a valid category (not Uncategorized)
            if (!category.equals("Uncategorized")) {
                // Create a document to store in MongoDB
                Document document = new Document("heading", heading)
                        .append("article", article)
                        .append("category", category)
                        .append("date", date);

                collection.insertOne(document);
            }
        }

        mongoClient.close();
    }
}
