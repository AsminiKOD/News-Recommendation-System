package org.example.newsrecommendation.Service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

            // Create the update document directly, without using $set here, since updateDocument already adds it
            Document updates = new Document("name", newName)
                    .append("email", newEmail)
                    .append("age", newAge)
                    .append("preferences", updatedPreferences);

            // Call updateDocument from DatabaseHandler to update the User document
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

    public static void viewArticleDetails(Article selectedArticle) {
        if (selectedArticle != null) {
            try (DatabaseHandler dbHandler = new DatabaseHandler()) {
                // Query the database for the full article based on the heading
                String heading = selectedArticle.getHeading();
                Document query = new Document("heading", heading);

                // Fetch the article document using DatabaseHandler
                Document result = dbHandler.findDocument("Article", query);

                if (result != null) {
                    String fullArticle = result.getString("article");

                    // Load the new FXML and pass the article details
                    FXMLLoader loader = new FXMLLoader(MainLogics.class.getResource("/org/example/newsrecommendation/ArticleScene.fxml"));
                    Parent root = loader.load();

                    ArticleScene controller = loader.getController();
                    controller.setArticleDetails(selectedArticle, fullArticle);

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Article Details");
                    stage.show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static ObservableList<Article> loadSavedArticles(String username) {
        List<Article> savedArticles = new ArrayList<>();
        try (DatabaseHandler dbHandler = new DatabaseHandler()) {
            // Fetch saved article headings for the user
            Document savedDoc = dbHandler.findDocument("Interaction", new Document("username", username));
            if (savedDoc != null) {
                List<String> savedHeadings = savedDoc.getList("save", String.class);

                // Fetch full article details from the "Article" collection
                for (String heading : savedHeadings) {
                    Document articleDoc = dbHandler.findDocument("Article", new Document("heading", heading));
                    if (articleDoc != null) {
                        String articleHeading = articleDoc.getString("heading");
                        String articleCategory = articleDoc.getString("category");
                        String articleDate = articleDoc.getString("date");
                        savedArticles.add(new Article(articleHeading, articleDate, articleCategory));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Return the list of saved articles
        return FXCollections.observableArrayList(savedArticles);
    }

    public static void removeArticle(String username, String headingToRemove) {
        try (DatabaseHandler dbHandler = new DatabaseHandler()) {
            Document query = new Document("username", username);
            Document update = new Document("$pull", new Document("save", headingToRemove));

            dbHandler.updateDocument("Interaction", query, update);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getFullArticle(String heading) {
        try (DatabaseHandler dbHandler = new DatabaseHandler()) {
            Document articleDoc = dbHandler.findDocument("Article", new Document("heading", heading));
            if (articleDoc != null) {
                return articleDoc.getString("article");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
