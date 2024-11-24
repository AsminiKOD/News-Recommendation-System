package org.example.newsrecommendation.NewsArticlesController;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ArticleFetcher {

    private static final String CSV_FILE_PATH = "src/main/resources/org/example/newsrecommendation/Datasets/Articles.csv"; // Replace with the actual path of your CSV file

    public static void main(String[] args) {
        fetchAndCategorizeArticles();
    }

    // Method to fetch articles from the CSV file, categorize them, and store them in MongoDB
    public static void fetchAndCategorizeArticles() {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(",");  // Assuming CSV has columns: heading, date, content
                if (columns.length >= 3) {
                    String heading = columns[0].trim();
                    String date = columns[1].trim();
                    String content = columns[2].trim();

                    // Categorize the article using ArticleCategorizer
                    String category = ArticleCategorizer.categorizeArticle(content);

                    // Store the article in MongoDB
                    storeArticleInDatabase(heading, date, content, category);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to store the article in MongoDB
    private static void storeArticleInDatabase(String heading, String date, String content, String category) {
        try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("NewsRecommendations");
            MongoCollection<Document> articleCollection = database.getCollection("Article");

            // Create a document for the article
            Document articleDocument = new Document("heading", heading)
                    .append("date", date)
                    .append("content", content)
                    .append("category", category);

            // Insert the article into the MongoDB collection
            articleCollection.insertOne(articleDocument);
            System.out.println("Article stored successfully: " + heading);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
