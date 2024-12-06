package org.example.newsrecommendation.Service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.bson.Document;
import org.example.newsrecommendation.DataBase.DatabaseHandler;
import org.example.newsrecommendation.Model.Admin;
import org.example.newsrecommendation.Model.Article;
import org.example.newsrecommendation.Model.LoginHistory;
import org.example.newsrecommendation.Model.User;

import java.util.ArrayList;
import java.util.List;

public class AdminLogics {
    private static final String CONNECTION_STRING = "mongodb+srv://Vinethma:2003Asmi15@cluster0.xrhve.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
    private static final String DATABASE_NAME = "NewsRecommendations";

    public Document findDocument(String collectionName, Document query) {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(collectionName);
            return collection.find(query).first();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching document: " + e.getMessage(), e);
        }
    }

    public List<Document> findDocuments(String collectionName, Document query) {
        List<Document> documents = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(collectionName);
            collection.find(query).into(documents);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching documents: " + e.getMessage(), e);
        }
        return documents;
    }

    public Admin convertToAdmin(Document adminDoc) {
        if (adminDoc == null) return null;
        return new Admin(
                adminDoc.getString("name"),
                adminDoc.getString("email"),
                adminDoc.getInteger("age"),
                adminDoc.getString("gender"),
                adminDoc.getString("password"),
                adminDoc.getString("adminId")
        );
    }

    public List<LoginHistory> convertToLoginHistory(List<Document> historyDocs) {
        List<LoginHistory> loginHistory = new ArrayList<>();
        for (Document doc : historyDocs) {
            String loginTime = doc.getString("login_time");
            String date = loginTime.split("T")[0];
            String time = loginTime.split("T")[1].split("\\.")[0];
            loginHistory.add(new LoginHistory(date, time));
        }
        return loginHistory;
    }

    public Document getAdminDetails(String adminId) {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection("Admin");
            return collection.find(new Document("adminId", adminId)).first();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching admin details: " + e.getMessage(), e);
        }
    }

    public void updateAdminDetails(String adminId, String newName, String newEmail, int newAge) {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection("Admin");

            Document updatedUser = new Document("name", newName)
                    .append("email", newEmail)
                    .append("age", newAge);

            Document query = new Document("adminId", adminId);

            collection.updateOne(query, new Document("$set", updatedUser));
        } catch (Exception e) {
            throw new RuntimeException("Error updating admin details: " + e.getMessage(), e);
        }
    }

    public void changePassword(String adminId, String newPassword) {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection("Admin");

            Document updatedUser = new Document("password", newPassword);

            Document query = new Document("adminId", adminId);

            collection.updateOne(query, new Document("$set", updatedUser));
        } catch (Exception e) {
            throw new RuntimeException("Error updating password: " + e.getMessage(), e);
        }
    }

    public List<User> loadUserDetails() {
        List<User> userData = new ArrayList<>();

        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> userCollection = database.getCollection("User");

            List<Document> users = userCollection.find(new Document()).into(new ArrayList<>());

            for (Document userDoc : users) {
                String name = userDoc.getString("name");
                String username = userDoc.getString("username");
                String email = userDoc.getString("email");
                int age = userDoc.getInteger("age", 0);
                String gender = userDoc.getString("gender");
                String password = userDoc.getString("password");
                List<String> preferences = userDoc.getList("preference", String.class);

                User user = new User(name, username, email, age, gender, password, preferences);
                userData.add(user);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user details: " + e.getMessage(), e);
        }

        return userData;
    }

    public Document searchUserByUsername(String username) {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> userCollection = database.getCollection("User");

            Document query = new Document("username", username);
            return userCollection.find(query).first();
        } catch (Exception e) {
            throw new RuntimeException("Database Error: " + e.getMessage(), e);
        }
    }

    public static boolean removeUserFromDatabase(String username) {
        try (DatabaseHandler dbHandler = new DatabaseHandler()) {
            MongoCollection<Document> userCollection = dbHandler.getDatabase().getCollection("User");

            // Find and delete the user by username
            Document result = userCollection.findOneAndDelete(new Document("username", username));

            if (result != null) {
                return true;  // User successfully removed
            } else {
                MainLogics.Alert(Alert.AlertType.ERROR, "Error", "No user found with username: " + username);
                return false;  // User not found
            }
        } catch (Exception e) {
            MainLogics.Alert(Alert.AlertType.ERROR, "Database Error", "Failed to remove user: " + e.getMessage());
            return false;
        }
    }

    public void addArticle(String heading, String date, String content) {
        // Categorize the article
        String category = ArticleCategorizer.categorizeArticle(content);

        // Create the article document
        Document article = new Document("heading", heading)
                .append("date", date)
                .append("content", content)
                .append("category", category);

        // Insert into MongoDB
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> articleCollection = database.getCollection("Article");
            articleCollection.insertOne(article);
        } catch (Exception e) {
            throw new RuntimeException("Failed to insert article: " + e.getMessage(), e);
        }
    }

    public List<Article> loadArticles() {
        List<Article> articles = new ArrayList<>();

        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> articleCollection = database.getCollection("Article");

            // Fetch all articles from the collection
            for (Document articleDoc : articleCollection.find()) {
                String heading = articleDoc.getString("heading");
                String date = articleDoc.getString("date");
                String category = articleDoc.getString("category");

                // Create an Article object
                Article article = new Article(heading, date, category);
                articles.add(article);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch articles: " + e.getMessage(), e);
        }

        return articles;
    }

    public static ObservableList<Article> fetchArticlesFromDatabase(List<String> categories) {
        ObservableList<Article> articleData = FXCollections.observableArrayList();

        try (DatabaseHandler dbHandler = new DatabaseHandler()) {
            // Build MongoDB query
            Document query = new Document();

            // If categories are selected, add to the query
            if (!categories.isEmpty()) {
                query.append("category", new Document("$in", categories));
            }

            // Fetch articles from MongoDB
            List<Document> articles = dbHandler.findDocuments("Article", query);

            for (Document doc : articles) {
                String heading = doc.getString("heading");
                String date = doc.getString("date"); // MongoDB stores date as a String in "MM/dd/yyyy" format
                String category = doc.getString("category");

                // Create Article object and add to the list
                articleData.add(new Article(heading, date, category));
            }
        } catch (Exception e) {
            MainLogics.Alert(Alert.AlertType.ERROR, "Database Error", "Failed to fetch articles: " + e.getMessage());
        }

        // Return as ObservableList for TableView
        return articleData;
    }

    public static boolean deleteArticleFromDatabase(Article article) {
        try (DatabaseHandler dbHandler = new DatabaseHandler()) {
            // Create query to match the article by heading
            Document query = new Document("heading", article.getHeading());

            // Delete the article from the collection
            Document result = dbHandler.findDocument("Article", query);

            if (result != null) {
                dbHandler.getDatabase().getCollection("Article").deleteOne(query); // Delete the article
                return true;  // Article successfully deleted
            } else {
                MainLogics.Alert(Alert.AlertType.ERROR, "Error", "No article found with heading: " + article.getHeading());
                return false;  // Article not found
            }
        } catch (Exception e) {
            MainLogics.Alert(Alert.AlertType.ERROR, "Database Error", "Failed to delete article: " + e.getMessage());
            return false;
        }
    }
}
