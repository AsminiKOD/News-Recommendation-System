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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ArticleFetcher {

    private static final String CSV_FILE_PATH = "src/main/resources/org/example/newsrecommendation/Datasets/Articles.csv";
    private static final String DATABASE_URL = "mongodb+srv://Vinethma:2003Asmi15@cluster0.xrhve.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
    private static final String DATABASE_NAME = "NewsRecommendations";
    private static final String COLLECTION_NAME = "Article";
    private static final int THREAD_COUNT = 4;

    public static void main(String[] args) {
        try {
            List<CSVRecord> records = readCSV(CSV_FILE_PATH);
            categorizeAndStoreArticles(records);
            System.out.println("Articles fetched and stored successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<CSVRecord> readCSV(String filePath) throws IOException {
        FileReader fileReader = new FileReader(filePath);
        CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withHeader("Article", "Date", "Heading").withIgnoreHeaderCase().withTrim());
        return csvParser.getRecords();
    }

    private static void categorizeAndStoreArticles(List<CSVRecord> records) {
        MongoClient mongoClient = MongoClients.create(DATABASE_URL);
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        ArticleCategorizer categorizer = new ArticleCategorizer();

        for (CSVRecord record : records) {
            executorService.submit(() -> {
                try {
                    String heading = record.get("Heading");
                    String article = record.get("Article");
                    String date = record.get("Date");

                    String category = categorizer.categorizeArticle(article);

                    if (!category.equals("Uncategorized")) {
                        Document document = new Document("heading", heading)
                                .append("article", article)
                                .append("category", category)
                                .append("date", date);

                        synchronized (collection) {
                            collection.insertOne(document);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

        mongoClient.close();
    }
}
