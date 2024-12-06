package org.example.newsrecommendation.App;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import org.bson.Document;
import org.example.newsrecommendation.Model.Article;
import org.example.newsrecommendation.Model.LoginHistory;
import org.example.newsrecommendation.Model.User;
import org.example.newsrecommendation.DataBase.DatabaseHandler;
import org.example.newsrecommendation.Service.MainLogics;
import org.example.newsrecommendation.Service.RecommendationAlgorithm;

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
    private Button Main_button_Saved;

    @FXML
    private Button Main_button_Home;

    @FXML
    private Button Main_button_Recomm;

    @FXML
    private Button Main_button_Articles;

    @FXML
    private Button Main_button_profile;

    @FXML
    private Button buttonView;

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
    private TableView<Article> tableArticles;

    @FXML
    private TableColumn<Article, String> tabColHeading;

    @FXML
    private TableColumn<Article, String> tabColCategory;

    @FXML
    private TableColumn<Article, String> tabColDate;

    @FXML
    private TableView<Article> table_Saved;

    @FXML
    private TableColumn<Article, String> tabCol_heading;

    @FXML
    private TableColumn<Article, String> tabCol_category;

    @FXML
    private TableColumn<Article, String> tabCol_date;

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
    private Pane Main_Save;

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

    RecommendationAlgorithm recommendationService = new RecommendationAlgorithm();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the TableView columns
        Profile_login_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        Profile_login_time.setCellValueFactory(new PropertyValueFactory<>("time"));

        // Clear TableView data on initialization
        Profile_login_histroy.setItems(FXCollections.observableArrayList());
        loadAndDisplayArticles(loggedInUsername);
        loadArticlesIntoTable();

    }

    @FXML
    public void userPaneNav(ActionEvent actionEvent) {
        if (actionEvent.getSource() == Main_button_Home) {
            Main_HomePage.toFront();
        }
        if (actionEvent.getSource() == Main_button_Recomm) {
            loadAndDisplayArticles(loggedInUsername);
            Main_RecommendPage.toFront();
        }
        if (actionEvent.getSource() == Main_button_Articles) {
            Main_SavePage.toFront();
        }
        if (actionEvent.getSource() == Main_button_Saved) {
            loadSavedArticles(loggedInUsername);
            Main_Save.toFront();
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

    @FXML
    private void showUserProfile() {
        if (loggedInUsername != null) {
            // Fetch user profile and login history using MainLogic
            User user = MainLogics.fetchUserProfile(loggedInUsername);
            List<LoginHistory> loginHistory = MainLogics.fetchLoginHistory(loggedInUsername);

            if (user != null) {
                // Update the UI with user details
                Main_Profile_label_Name.setText(user.getName());
                Main_Profile_label_Email.setText(user.getEmail());
                Main_Profile_label_Age.setText(String.valueOf(user.getAge()));
                Main_Profile_label_gender.setText(user.getGender());
                Main_Profile_label_prefere.setText(user.getPreference() != null ? String.join(", ", user.getPreference()) : "No preferences set");

                // Update login history table
                ObservableList<LoginHistory> historyData = FXCollections.observableArrayList(loginHistory);
                Profile_login_histroy.setItems(historyData);
            } else {
                MainLogics.Alert(Alert.AlertType.ERROR, "User Not Found", "No user found with the username: " + loggedInUsername);
                clearUserProfile();
            }
        }
    }

    private void clearUserProfile() {
        Main_Profile_label_Name.setText("");
        Main_Profile_label_Email.setText("");
        Main_Profile_label_Age.setText("");
        Main_Profile_label_gender.setText("");
        Main_Profile_label_prefere.setText("No preferences set");
        Profile_login_histroy.getItems().clear();
    }

    // This method handles navigation to edit profile.
    @FXML
    public void handleEditProfile(ActionEvent actionEvent) {
        if (loggedInUsername != null) {
            // Fetch user details for editing using MainLogic
            User user = MainLogics.fetchUserForEdit(loggedInUsername);

            if (user != null) {
                // Populate the edit fields with current user details
                Text_edit_name.setText(user.getName());
                Text_edit_email.setText(user.getEmail());
                Text_edit_age.setText(String.valueOf(user.getAge()));

                // Check the preferences checkboxes based on user data
                List<String> preferences = user.getPreference();
                check_edit_tech.setSelected(preferences.contains("AI and Technology"));
                check_edit_prefer.setSelected(preferences.contains("Entertainment"));
                check_edit_finance.setSelected(preferences.contains("Finance"));
                check_edit_health.setSelected(preferences.contains("Healthcare"));
                check_edit_politics.setSelected(preferences.contains("Politics"));
                check_edit_world.setSelected(preferences.contains("World"));
                check_edit_sport.setSelected(preferences.contains("Sport"));
                check_edit_science.setSelected(preferences.contains("Science"));
            } else {
                MainLogics.Alert(Alert.AlertType.ERROR, "User Not Found", "Failed to fetch user details.");
            }
        }
        // Show the edit profile page
        Pane_edit_profil.toFront();
    }

    // This method handles the "Confirm" button click, saving the changes to the database.
    @FXML
    public void handleEditConfirm(ActionEvent actionEvent) {
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

        if (loggedInUsername != null) {
            // Call MainLogic to update user details
            boolean updateSuccess = MainLogics.updateUserDetails(loggedInUsername, newName, newEmail, newAge, updatedPreferences);

            if (updateSuccess) {
                // Update the profile UI after the successful update
                Main_Profile_label_Name.setText(newName);
                Main_Profile_label_Email.setText(newEmail);
                Main_Profile_label_Age.setText(String.valueOf(newAge));
                Main_Profile_label_prefere.setText(String.join(", ", updatedPreferences));

                MainLogics.Alert(Alert.AlertType.INFORMATION, "Success", "Successfully updated");
            } else {
                MainLogics.Alert(Alert.AlertType.ERROR, "Database Error", "Failed to update user details.");
            }
        }
        Main_ProfilePage.toFront();
    }

    @FXML
    public void handlePasswordChangeConfirm(ActionEvent actionEvent) {
        // Get the new password and confirm password from the input fields
        String newPassword = Text_new_pwd.getText();
        String confirmPassword = Text_new_confirm_pwd.getText();

        // Call MainLogic to validate and update password
        boolean updateSuccess = MainLogics.validateAndUpdatePassword(loggedInUsername, newPassword, confirmPassword);

        if (updateSuccess) {
            // Show success message
            MainLogics.Alert(Alert.AlertType.INFORMATION, "Success", "Successfully updated password.");

            // Optionally, reset the fields or navigate back to profile page
            Text_new_pwd.clear();
            Text_new_confirm_pwd.clear();
            Main_ProfilePage.toFront();  // Navigate back to Profile Page (Optional)
        } else {
            // Show error message based on validation failure
            if (!newPassword.equals(confirmPassword)) {
                MainLogics.Alert(Alert.AlertType.ERROR, "Password Mismatch", "The new password and confirm password do not match.");
            } else if (newPassword.length() < 6) {
                MainLogics.Alert(Alert.AlertType.ERROR, "Password Mismatch", "Password must be at least 6 characters long.");
            } else {
                MainLogics.Alert(Alert.AlertType.ERROR, "Database Error", "Failed to update password.");
            }
        }
    }

    //for articles table
    public void loadArticlesIntoTable() {
        List<Article> articles = MainLogics.fetchAllArticles();

        ObservableList<Article> articleList = FXCollections.observableArrayList(articles);

        tabColHeading.setCellValueFactory(new PropertyValueFactory<>("heading"));
        tabColCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        tabColDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        tableArticles.setItems(articleList);
    }

    public void loadAndDisplayArticles(String username) {
        List<Article> articles = recommendationService.fetchArticlesBasedOnPoints(username); // Fetch articles based on user points
        displayArticles(articles); // Populate the GridPane
    }

    public void displayArticles(List<Article> articles) {
        main_rec_grid.getChildren().clear();
        main_rec_grid.getRowConstraints().clear();

        int row = 0;

        try {
            for (int i = 0; i < Math.min(articles.size(), 20); i++) {
                Article article = articles.get(i);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/newsrecommendation/Recommended.fxml"));
                AnchorPane articlePane = loader.load();

                Recommended controller = loader.getController();
                controller.setArticleData(article.getHeading(), article.getDate(), article.getCategory());

                main_rec_grid.add(articlePane, 0, row);

                RowConstraints rowConstraints = new RowConstraints();
                rowConstraints.setPrefHeight(AnchorPane.USE_COMPUTED_SIZE); // Set height to fit the content
                main_rec_grid.getRowConstraints().add(rowConstraints);

                row++;

                GridPane.setMargin(articlePane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onViewButtonClick() {
        Article selectedArticle = tableArticles.getSelectionModel().getSelectedItem();
        if (selectedArticle != null) {
            MainLogics.viewArticleDetails(selectedArticle);
        }
    }

    @FXML
    private void loadSavedArticles(String username) {
        ObservableList<Article> savedArticleList = MainLogics.loadSavedArticles(username);

        tabCol_heading.setCellValueFactory(new PropertyValueFactory<>("heading"));
        tabCol_category.setCellValueFactory(new PropertyValueFactory<>("category"));
        tabCol_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        table_Saved.setItems(savedArticleList);
    }

    @FXML
    private void handleRemoveArticle(ActionEvent event) {
        Article selectedArticle = table_Saved.getSelectionModel().getSelectedItem();
        if (selectedArticle != null) {
            String headingToRemove = selectedArticle.getHeading();

            MainLogics.removeArticle(loggedInUsername, headingToRemove);

            table_Saved.getItems().remove(selectedArticle);
        }
    }

    @FXML
    private void handleOpenArticle() {
        Article selectedArticle = table_Saved.getSelectionModel().getSelectedItem();
        if (selectedArticle == null) {
            return; // No article selected
        }

        // Fetch the full article content from the MainLogics class
        String fullArticle = MainLogics.getFullArticle(selectedArticle.getHeading());
        if (fullArticle != null) {
            try {
                // Load ArticleScene.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/newsrecommendation/ArticleScene.fxml"));
                Parent root = loader.load();

                // Pass the article details to ArticleScene
                ArticleScene controller = loader.getController();
                ArticleScene.setCurrentUsername(loggedInUsername); // Pass current username
                controller.setArticleDetails(selectedArticle, fullArticle);

                // Display the new scene
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
