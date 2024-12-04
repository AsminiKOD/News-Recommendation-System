package org.example.newsrecommendation.Service;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.bson.Document;
import org.example.newsrecommendation.Model.Article;
import org.example.newsrecommendation.App.ArticleScene;
import org.example.newsrecommendation.DataBase.DatabaseHandler;
import org.example.newsrecommendation.Model.LoginHistory;
import org.example.newsrecommendation.Model.User;

import java.util.ArrayList;
import java.util.List;

public class MainLogics {

    public static void Alert(Alert.AlertType alertType, String heading, String msg) {
        Alert alert = new Alert(alertType);
        alert.setTitle(heading);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static Document fetchArticleDetails(DatabaseHandler dbHandler, String heading) {
        try {
            Document query = new Document("heading", heading);
            return dbHandler.findDocument("Article", query);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void loadArticleScene(String heading, String date, String category, String fullArticle) {
        try {
            FXMLLoader loader = new FXMLLoader(MainLogics.class.getResource("/org/example/newsrecommendation/ArticleScene.fxml"));
            Parent root = loader.load();

            ArticleScene controller = loader.getController();
            controller.setArticleDetails(new Article(heading, date, category), fullArticle);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Article Details");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static User fetchUserProfile(String loggedInUsername) {
        try (DatabaseHandler dbHandler = new DatabaseHandler()) {
            Document userDoc = dbHandler.findDocument("User", new Document("username", loggedInUsername));

            if (userDoc != null) {
                return new User(
                        userDoc.getString("name"),
                        userDoc.getString("email"),
                        userDoc.getInteger("age"),
                        userDoc.getString("gender"),
                        userDoc.getString("password"),
                        userDoc.getList("preferences", String.class)
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<LoginHistory> fetchLoginHistory(String loggedInUsername) {
        List<LoginHistory> loginHistory = new ArrayList<>();
        try (DatabaseHandler dbHandler = new DatabaseHandler()) {
            Document query = new Document("username", loggedInUsername);
            List<Document> historyDocs = dbHandler.findDocuments("User_Login", query);

            for (Document doc : historyDocs) {
                String loginTime = doc.getString("login_time");
                String date = loginTime.split("T")[0]; // Extract date
                String time = loginTime.split("T")[1].split("\\.")[0]; // Extract time
                loginHistory.add(new LoginHistory(date, time));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loginHistory;
    }

    public static User fetchUserForEdit(String loggedInUsername) {
        try (DatabaseHandler dbHandler = new DatabaseHandler()) {
            Document userDoc = dbHandler.findDocument("User", new Document("username", loggedInUsername));

            if (userDoc != null) {
                return new User(
                        userDoc.getString("name"),
                        userDoc.getString("email"),
                        userDoc.getInteger("age"),
                        userDoc.getList("preferences", String.class)
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean updateUserDetails(String loggedInUsername, String newName, String newEmail, int newAge, List<String> updatedPreferences) {
        try (DatabaseHandler dbHandler = new DatabaseHandler()) {
            Document query = new Document("username", loggedInUsername);

            Document updates = new Document("$set", new Document("name", newName)
                    .append("email", newEmail)
                    .append("age", newAge)
                    .append("preferences", updatedPreferences));

            dbHandler.updateDocument("User", query, updates);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean validateAndUpdatePassword(String loggedInUsername, String newPassword, String confirmPassword) {
        // Validate that the passwords match
        if (!newPassword.equals(confirmPassword)) {
            return false; // Passwords don't match
        }
        // Validate that the password is at least 6 characters long
        if (newPassword.length() < 6) {
            return false; // Password is too short
        }

        // If validation passes, update the password in the database
        try (DatabaseHandler dbHandler = new DatabaseHandler()) {
            Document updatedUser = new Document("password", newPassword);
            dbHandler.updateDocument("User", new Document("username", loggedInUsername), updatedUser);
            return true; // Update successful
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Update failed
        }
    }

    public static List<Article> fetchAllArticles() {
        List<Article> articles = new ArrayList<>();
        try (DatabaseHandler dbHandler = new DatabaseHandler()) {
            // Fetch all articles from the Article collection
            List<Document> articleDocs = dbHandler.findDocuments("Article", new Document());

            for (Document doc : articleDocs) {
                String heading = doc.getString("heading");
                String date = doc.getString("date");
                String category = doc.getString("category");

                articles.add(new Article(heading, date, category));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return articles;
    }
}
