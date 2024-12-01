package org.example.newsrecommendation;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Main implements Initializable {
    @FXML
    private Pane Main_AboutPage;

    @FXML
    private Pane Main_HomePage;

    @FXML
    private Pane Main_ProfilePage;

    @FXML
    private Label Main_Profile_label_Age;

    @FXML
    private Label Main_Profile_label_Email;

    @FXML
    private Label Main_Profile_label_Name;

    @FXML
    private Label Main_Profile_label_gender;

    @FXML
    private Label Main_Profile_label_prefere;

    @FXML
    private Pane Main_RecommendPage;

    @FXML
    private Pane Main_SavePage;

    @FXML
    private Button Main_button_About;

    @FXML
    private Button Main_button_Home;

    @FXML
    private Button Main_button_Recomm;

    @FXML
    private Button Main_button_Saved;

    @FXML
    private Button Main_button_profile;

    @FXML
    private Pane Pane_change_pwd;

    @FXML
    private Pane Pane_edit_profil;

    @FXML
    private Pane Pane_enter_pwd;

    @FXML
    private TableView<LoginHistory> Profile_login_histroy;

    @FXML
    private TableColumn<LoginHistory, String> Profile_login_date;

    @FXML
    private TableColumn<LoginHistory, String> Profile_login_time;

    @FXML
    private TextField Text_edit_age;

    @FXML
    private TextField Text_edit_email;

    @FXML
    private TextField Text_edit_name;

    @FXML
    private Button button_adit_confirm;

    @FXML
    private Button button_confirm_pwd;

    @FXML
    private Button button_change_pwd;

    @FXML
    private Button button_edit;

    @FXML
    private Button button_edit_back;

    @FXML
    private CheckBox check_edit_finance;

    @FXML
    private CheckBox check_edit_health;

    @FXML
    private CheckBox check_edit_politics;

    @FXML
    private CheckBox check_edit_prefer;

    @FXML
    private CheckBox check_edit_science;

    @FXML
    private CheckBox check_edit_sport;

    @FXML
    private CheckBox check_edit_tech;

    @FXML
    private CheckBox check_edit_world;

    @FXML
    private Pane home_pane;

    @FXML
    private TextField Text_new_pwd;

    @FXML
    private TextField Text_new_confirm_pwd;

    @FXML
    private ScrollPane scrol_pane_main_rec;

    @FXML
    private GridPane main_rec_grid;

    private static String loggedInUsername;

    public static void setLoggedInUsername(String username) {
        loggedInUsername = username;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the TableView columns
        Profile_login_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        Profile_login_time.setCellValueFactory(new PropertyValueFactory<>("time"));

        // Clear TableView data on initialization
        Profile_login_histroy.setItems(FXCollections.observableArrayList());
        loadAndDisplayArticles(loggedInUsername);
    }

    @FXML
    public void userPaneNav(ActionEvent actionEvent) {
        if (actionEvent.getSource() == Main_button_Home) {
            Main_HomePage.toFront();
        }
        if (actionEvent.getSource() == Main_button_Recomm) {
            Main_RecommendPage.toFront();
        }
        if (actionEvent.getSource() == Main_button_Saved) {
            Main_SavePage.toFront();
        }
        if (actionEvent.getSource() == Main_button_About) {
            Main_AboutPage.toFront();
        }
        if (actionEvent.getSource() == Main_button_profile) {
            showUserProfile();
            Main_ProfilePage.toFront();
        }
        if (actionEvent.getSource() == button_edit) {
            Pane_edit_profil.toFront();
        }
        if (actionEvent.getSource() == button_change_pwd) {
            Pane_change_pwd.toFront();
        }
        if (actionEvent.getSource() == button_edit_back) {
            Main_ProfilePage.toFront();
        }
        if (actionEvent.getSource() == Main_ProfilePage) {
            Main_ProfilePage.toFront();
        }

    }

    private void showUserProfile() {
        if (loggedInUsername != null) {
            try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                MongoDatabase database = mongoClient.getDatabase("NewsRecommendations");
                MongoCollection<Document> userCollection = database.getCollection("User");
                MongoCollection<Document> loginHistoryCollection = database.getCollection("User_Login");

                Document userDoc = userCollection.find(new Document("username", loggedInUsername)).first();
                if (userDoc != null) {
                    User user = new User(
                            userDoc.getString("name"),
                            userDoc.getString("email"),
                            userDoc.getInteger("age"),
                            userDoc.getString("gender"),
                            userDoc.getString("password"),
                            userDoc.getList("preferences", String.class)
                    );

                    Main_Profile_label_Name.setText(user.getName());
                    Main_Profile_label_Email.setText(user.getEmail());
                    Main_Profile_label_Age.setText(String.valueOf(user.getAge()));
                    Main_Profile_label_gender.setText(user.getGender());
                    Main_Profile_label_prefere.setText(user.getPreference() != null ? String.join(", ", user.getPreference()) : "No preferences set");

                    // Fetch login history
                    List<LoginHistory> loginHistory = new ArrayList<>();
                    FindIterable<Document> historyDocs = loginHistoryCollection.find(new Document("username", loggedInUsername));
                    for (Document doc : historyDocs) {
                        String loginTime = doc.getString("login_time");

                        // Parse login_time into date and time
                        String date = loginTime.split("T")[0]; // Extract date
                        String time = loginTime.split("T")[1].split("\\.")[0]; // Extract time

                        loginHistory.add(new LoginHistory(date, time));
                    }

                    // Populate login history in table
                    ObservableList<LoginHistory> historyData = FXCollections.observableArrayList(loginHistory);
                    Profile_login_histroy.setItems(historyData);
                } else {
                    showAlert("User Not Found", "No user found with the username: " + loggedInUsername);
                    clearUserProfile();
                }
            } catch (Exception e) {
                showAlert("Database Error", "Failed to fetch user details: " + e.getMessage());
                clearUserProfile();
            }
        }
    }

    private void clearUserProfile() {
        Main_Profile_label_Name.setText("");
        Main_Profile_label_Email.setText("");
        Main_Profile_label_Age.setText("");
        Main_Profile_label_gender.setText("");
        Main_Profile_label_prefere.setText("");
        Profile_login_histroy.setItems(FXCollections.observableArrayList());
    }

    // This method handles navigation to edit profile page and pre-filling user data.
    @FXML
    public void handleEditProfile(ActionEvent actionEvent) {
        if (loggedInUsername != null) {
            try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                MongoDatabase database = mongoClient.getDatabase("NewsRecommendations");
                MongoCollection<Document> userCollection = database.getCollection("User");

                Document userDoc = userCollection.find(new Document("username", loggedInUsername)).first();
                if (userDoc != null) {
                    // Populate the edit fields with current user details
                    Text_edit_name.setText(userDoc.getString("name"));
                    Text_edit_email.setText(userDoc.getString("email"));
                    Text_edit_age.setText(String.valueOf(userDoc.getInteger("age")));
                    // Check the preferences checkboxes based on user data
                    List<String> preferences = userDoc.getList("preferences", String.class);
                    check_edit_tech.setSelected(preferences.contains("AI and Technology"));
                    check_edit_prefer.setSelected(preferences.contains("Entertainment"));
                    check_edit_finance.setSelected(preferences.contains("Finance"));
                    check_edit_health.setSelected(preferences.contains("Healthcare"));
                    check_edit_politics.setSelected(preferences.contains("Politics"));
                    check_edit_world.setSelected(preferences.contains("World"));
                    check_edit_sport.setSelected(preferences.contains("Sport"));
                    check_edit_science.setSelected(preferences.contains("Science"));
                }
            } catch (Exception e) {
                showAlert("Database Error", "Failed to fetch user details: " + e.getMessage());
            }
        }
        // Show the edit profile page
        Pane_edit_profil.toFront();
    }

    // This method handles the "Confirm" button click, saving the changes to the database.
    @FXML
    public void handleEditConfirm(ActionEvent actionEvent) {
        // Get the updated user details from the form
        String newName = Text_edit_name.getText();
        String newEmail = Text_edit_email.getText();
        int newAge = Integer.parseInt(Text_edit_age.getText());

        List<String> updatedPreferences = new ArrayList<>();
        if (check_edit_tech.isSelected()) updatedPreferences.add("AI and Technology");
        if (check_edit_prefer.isSelected()) updatedPreferences.add("Entertainment");
        if (check_edit_finance.isSelected()) updatedPreferences.add("Finance");
        if (check_edit_health.isSelected()) updatedPreferences.add("Healthcare");
        if (check_edit_politics.isSelected()) updatedPreferences.add("Politics");
        if (check_edit_world.isSelected()) updatedPreferences.add("World");
        if (check_edit_sport.isSelected()) updatedPreferences.add("Sport");
        if (check_edit_science.isSelected()) updatedPreferences.add("Science");

        // Update the user document in MongoDB
        if (loggedInUsername != null) {
            try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                MongoDatabase database = mongoClient.getDatabase("NewsRecommendations");
                MongoCollection<Document> userCollection = database.getCollection("User");

                Document updatedUser = new Document("name", newName)
                        .append("email", newEmail)
                        .append("age", newAge)
                        .append("preferences", updatedPreferences);

                // Update the document in the database
                userCollection.updateOne(new Document("username", loggedInUsername),
                        new Document("$set", updatedUser));

                // Reflect the changes in the profile page
                Main_Profile_label_Name.setText(newName);
                Main_Profile_label_Email.setText(newEmail);
                Main_Profile_label_Age.setText(String.valueOf(newAge));
                Main_Profile_label_prefere.setText(String.join(", ", updatedPreferences));

                // Show success message
                showAlerts();
            } catch (Exception e) {
                showAlert("Database Error", "Failed to update user details: " + e.getMessage());
            }
        }
    }

    @FXML
    public void handleChangePassword(ActionEvent actionEvent) {
        Pane_change_pwd.toFront();
    }

    @FXML
    public void handlePasswordChangeConfirm(ActionEvent actionEvent) {
        // Get the new password and confirm password from the input fields
        String newPassword = Text_new_pwd.getText();
        String confirmPassword = Text_new_confirm_pwd.getText();

        // Validate that the passwords match
        if (!newPassword.equals(confirmPassword)) {
            showAlert("Password Mismatch", "The new password and confirm password do not match.");
            return;
        }
        if (newPassword.length() < 6) {
            showAlert("Password Mismatch","Password must be at least 6 characters long.");
            return;
        }

        // If valid, update the password in the database
        if (loggedInUsername != null) {
            try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                MongoDatabase database = mongoClient.getDatabase("NewsRecommendations");
                MongoCollection<Document> userCollection = database.getCollection("User");

                // Update the password field in the database
                Document updatedUser = new Document("password", newPassword);
                userCollection.updateOne(new Document("username", loggedInUsername),
                        new Document("$set", updatedUser));

                // Show success message
                showAlerts();

                // Optionally, reset the fields or navigate back to profile page
                Text_new_pwd.clear();
                Text_new_confirm_pwd.clear();
                Main_ProfilePage.toFront();  // Navigate back to Profile Page (Optional)
            } catch (Exception e) {
                showAlert("Database Error", "Failed to update password: " + e.getMessage());
            }
        }
    }

    public List<Article> fetchArticlesBasedOnPoints(String username) {
        List<Article> articles = new ArrayList<>();
        try {
            // Connect to MongoDB
            MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongoClient.getDatabase("NewsRecommendations");
            MongoCollection<Document> articlesCollection = database.getCollection("Article");
            MongoCollection<Document> userPointsCollection = database.getCollection("Preferences");

            // Retrieve the user's points document
            Document userPointsDoc = userPointsCollection.find(Filters.eq("username", username)).first();
            if (userPointsDoc == null) {
                System.out.println("No points found for user: " + username);
                return articles; // Return an empty list if no points are found
            }

            // Calculate the total points across all categories
            Map<String, Integer> categoryPoints = new HashMap<>();
            int totalPoints = 0;

            for (String key : userPointsDoc.keySet()) {
                if (!key.equals("_id") && !key.equals("username")) {
                    int points = userPointsDoc.getInteger(key, 0);
                    categoryPoints.put(key, points);
                    totalPoints += points;
                }
            }

            // If total points are zero, return empty list
            if (totalPoints == 0) {
                System.out.println("No points assigned to any category for user: " + username);
                return articles;
            }

            // Calculate the proportional distribution of articles per category
            Map<String, Integer> categoryArticleQuota = new HashMap<>();
            int totalQuota = 40; // Total number of articles to display (you want 20, adjust accordingly)
            for (Map.Entry<String, Integer> entry : categoryPoints.entrySet()) {
                int quota = Math.round((float) entry.getValue() / totalPoints * totalQuota);
                categoryArticleQuota.put(entry.getKey(), quota);
            }

            // Retrieve and shuffle articles for each category
            for (Map.Entry<String, Integer> entry : categoryArticleQuota.entrySet()) {
                String category = entry.getKey();
                int quota = entry.getValue();

                if (quota > 0) {
                    List<Document> categoryArticles = articlesCollection.find(Filters.eq("category", category))
                            .into(new ArrayList<>());

                    // Shuffle and limit the articles to the quota
                    Collections.shuffle(categoryArticles);
                    for (int i = 0; i < Math.min(quota, categoryArticles.size()); i++) {
                        Document doc = categoryArticles.get(i);
                        String heading = doc.getString("heading");
                        String date = doc.getString("date");
                        Article article = new Article(heading, date, category, categoryPoints.get(category));
                        articles.add(article);
                    }
                }
            }

            // Shuffle the final list to mix categories
            Collections.shuffle(articles);

            // Limit the final list to 20 articles
            if (articles.size() > 20) {
                articles = articles.subList(0, 20);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return articles;
    }



    public void loadAndDisplayArticles(String username) {
        List<Article> articles = fetchArticlesBasedOnPoints(username); // Fetch articles based on user points
        displayArticles(articles); // Populate the GridPane
    }

    public void displayArticles(List<Article> articles) {
        main_rec_grid.getChildren().clear(); // Clear existing articles

        int columns = 2; // Set the number of articles per row
        int row = 0, col = 1;

        try {
            for (Article article : articles) {
                // Load the article pane
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Recommended.fxml"));
                AnchorPane articlePane = loader.load();

                // Set the article data using the controller
                Recommended controller = loader.getController();
                controller.setArticleData(article.getHeading(), article.getDate(), article.getCategory());

                // Add the article pane to the GridPane at the correct column and row
                main_rec_grid.add(articlePane, col, row);

                // Move to the next column
                col++;

                // If 5 articles are added in a row, move to the next row
                if (col == columns) {
                    col = 1; // Reset column to 1
                    row++;   // Increment the row
                }

                // Optionally add margins for better spacing
                GridPane.setMargin(articlePane, new Insets(40));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlerts() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Profile updated successfully.");
        alert.showAndWait();
    }
}
