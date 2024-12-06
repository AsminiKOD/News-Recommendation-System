package org.example.newsrecommendation.App;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import org.bson.Document;
import org.example.newsrecommendation.Model.Admin;
import org.example.newsrecommendation.Model.Article;
import org.example.newsrecommendation.Model.LoginHistory;
import org.example.newsrecommendation.Service.MainLogics;
import org.example.newsrecommendation.Service.AdminLogics;
import org.example.newsrecommendation.Model.User;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class Administrator implements Initializable {
    @FXML
    private Pane Admin_Pane_Add;

    @FXML
    private Pane Admin_Pane_User_records;

    @FXML
    private Button Admin_button_Add;

    @FXML
    private Button Admin_button_Your_profile;

    @FXML
    private Button Admin_button_View;

    @FXML
    private Button Admin_button_delete;

    @FXML
    private Pane Admin_pane_Delete;

    @FXML
    private TableView<User> Admin_table;

    @FXML
    private Button Button_add_edit;

    @FXML
    private Pane Pane_user_show_details;

    @FXML
    private Pane Pane_user_search;

    @FXML
    private Pane Pane_adm_view_details;

    @FXML
    private Pane Pane_adm_edit;

    @FXML
    private Pane Pane_admin_profile;

    @FXML
    private Pane Pane_change_pwd;

    @FXML
    private Pane Pane_user_person;

    @FXML
    private Button button_add_back_edit;

    @FXML
    private Button button_add_change_back;

    @FXML
    private Button button_add_change_confirm;

    @FXML
    private Button button_add_change_pwd;

    @FXML
    private Button button_add_confirm_edit;

    @FXML
    private Button button_back_view;

    @FXML
    private Button button_removeUser;

    @FXML
    private Button button_user_enter;

    @FXML
    private Button button_view;

    @FXML
    private Button button_user_back_search;

    @FXML
    private Label label_add_ID;

    @FXML
    private Label label_add_email;

    @FXML
    private Label label_add_gender;

    @FXML
    private Label label_add_name;

    @FXML
    private Label label_user_age;

    @FXML
    private Label label_user_email;

    @FXML
    private Label label_user_gender;

    @FXML
    private Label label_user_name;

    @FXML
    private Label label_user_preference;

    @FXML
    private Label label_user_username;

    @FXML
    private Label label_add_age;

    @FXML
    private TableColumn<LoginHistory, String> tabCol_date;

    @FXML
    private TableColumn<User, String> tabCol_email;

    @FXML
    private TableColumn<User, String> tabCol_name;

    @FXML
    private TableColumn<LoginHistory, String> tabCol_time;

    @FXML
    private TableColumn<User, String> tabCol_user;

    @FXML
    private TableColumn<Article, String>tabCol_heading;

    @FXML
    private TableColumn<Article, String>tabCol_delete_date;

    @FXML
    private TableColumn<Article, String>tabCol_delete_cate;

    @FXML
    private TableView<LoginHistory> tabView_LogHis;

    @FXML
    private TableView<Article> table_delete;

    @FXML
    private TextField text_add_new_pwd;

    @FXML
    private TextField text_ad_search_username;

    @FXML
    private TextField text_adm_age;

    @FXML
    private TextField text_adm_cofirm_pwd;

    @FXML
    private TextField text_adm_email;

    @FXML
    private TextField text_adm_name;

    @FXML
    private TextField text_adm_AddHeading;

    @FXML
    private TextArea textArea_adm_AddContent;

    @FXML
    private DatePicker DatePicker_adm_date;

    @FXML
    private CheckBox Admin_Cat_Enter;

    @FXML
    private CheckBox Admin_Cat_Politics;

    @FXML
    private CheckBox Admin_Cat_Science;

    @FXML
    private CheckBox Admin_Cat_finance;

    @FXML
    private CheckBox Admin_Cat_health;

    @FXML
    private CheckBox Admin_Cat_sport;

    @FXML
    private CheckBox Admin_Cat_tech;

    @FXML
    private CheckBox Admin_Cat_world;

    private static String loggedInAdminID;

    public static void setLoggedInAdminID(String username) {
        loggedInAdminID = username;
    }

    // navigate between pane
    @FXML
    public void adminPaneNav(ActionEvent actionEvent){
        if (actionEvent.getSource() == Admin_button_Add){
            Admin_Pane_Add.toFront();
        }
        if (actionEvent.getSource() == Admin_button_View){
            loadUserDetails();
            Admin_Pane_User_records.toFront();
        }
        if (actionEvent.getSource() == Admin_button_delete) {
            loadArticles();
            Admin_pane_Delete.toFront();
        }
        if (actionEvent.getSource() == Button_add_edit) {
            handleEditProfile();
            Pane_adm_edit.toFront();
        }
        if (actionEvent.getSource() == button_add_back_edit) {
            Pane_adm_view_details.toFront();
        }
        if (actionEvent.getSource() == button_add_confirm_edit) {
            if(!handleEditConfirm()){
                return;
            }
            Pane_adm_view_details.toFront();
        }
        if (actionEvent.getSource() == Admin_button_Your_profile) {
            showUserProfile();
            Pane_admin_profile.toFront();
            Pane_adm_view_details.toFront();
            text_adm_name.clear();
            text_adm_email.clear();
            text_adm_age.clear();
            text_add_new_pwd.clear();
            text_adm_cofirm_pwd.clear();
        }
        if (actionEvent.getSource() == button_add_change_pwd) {
            Pane_change_pwd.toFront();
        }
        if (actionEvent.getSource() == button_add_change_back) {
            Pane_admin_profile.toFront();
        }
        if (actionEvent.getSource() == button_add_change_confirm) {
            if (!handlePasswordChangeConfirm()){
                return;
            }
            Pane_admin_profile.toFront();
        }
        if (actionEvent.getSource() == button_view) {
            Pane_user_person.toFront();
        }
        if (actionEvent.getSource() == button_user_enter) {
            if (!handleUserSearch()){
                return;
            }
            Pane_user_show_details.toFront();
        }
        if (actionEvent.getSource() == button_user_back_search) {
            Pane_user_search.toFront();
        }
        if (actionEvent.getSource() == button_removeUser) {
            handleRemoveUser();
            Admin_Pane_User_records.toFront();
        }
        if (actionEvent.getSource() == button_back_view) {
            Admin_Pane_User_records.toFront();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the TableView columns of Login history
        tabCol_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        tabCol_time.setCellValueFactory(new PropertyValueFactory<>("time"));

        // Clear TableView data on initialization
        tabView_LogHis.setItems(FXCollections.observableArrayList());

        // Initialize the TableView  of user details columns
        tabCol_user.setCellValueFactory(new PropertyValueFactory<>("username"));
        tabCol_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        tabCol_email.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Clear TableView data on initialization
        Admin_table.setItems(FXCollections.observableArrayList());

        // Initialize the TableView of delete columns
        tabCol_heading.setCellValueFactory(new PropertyValueFactory<>("heading"));
        tabCol_delete_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        tabCol_delete_cate.setCellValueFactory(new PropertyValueFactory<>("category"));
    }

    //to check admin profile
    private void showUserProfile() {
        if (loggedInAdminID != null) {
            AdminLogics adminLogics = new AdminLogics();
            try {
                Document adminDoc = adminLogics.findDocument("Admin", new Document("adminId", loggedInAdminID));
                Admin admin = adminLogics.convertToAdmin(adminDoc);

                if (admin != null) {
                    label_add_name.setText(admin.getName());
                    label_add_email.setText(admin.getEmail());
                    label_add_age.setText(String.valueOf(admin.getAge()));
                    label_add_gender.setText(admin.getGender());
                    label_add_ID.setText(admin.getAdmin_ID());

                    List<Document> historyDocs = adminLogics.findDocuments("Admin_Login", new Document("adminId", loggedInAdminID));
                    List<LoginHistory> loginHistory = adminLogics.convertToLoginHistory(historyDocs);
                    ObservableList<LoginHistory> historyData = FXCollections.observableArrayList(loginHistory);
                    tabView_LogHis.setItems(historyData);
                } else {
                    MainLogics.Alert(Alert.AlertType.ERROR, "User Not Found", "No user found with the username: " + loggedInAdminID);
                    clearUserProfile();
                }
            } catch (Exception e) {
                MainLogics.Alert(Alert.AlertType.ERROR, "Database Error", "Failed to fetch user details: " + e.getMessage());
                clearUserProfile();
            }
        }
    }


    private void clearUserProfile() {
        label_add_name.setText("");
        label_add_email.setText("");
        label_add_age.setText("");
        label_add_gender.setText("");
        label_add_ID.setText("");
        tabView_LogHis.setItems(FXCollections.observableArrayList());
    }

    // This method to navigate edit profile page.
    @FXML
    public void handleEditProfile() {
        if (loggedInAdminID != null) {
            AdminLogics adminLogics = new AdminLogics();
            try {
                Document adminDoc = adminLogics.getAdminDetails(loggedInAdminID);
                if (adminDoc != null) {
                    text_adm_name.setText(adminDoc.getString("name"));
                    text_adm_email.setText(adminDoc.getString("email"));
                    text_adm_age.setText(String.valueOf(adminDoc.getInteger("age")));
                }
            } catch (Exception e) {
                MainLogics.Alert(Alert.AlertType.ERROR, "Database Error", "Failed to fetch user details: " + e.getMessage());
            }
        }
    }

    // handles the "Confirm" button clicks and save the changes to database.
    @FXML
    public boolean handleEditConfirm() {
        String newName = text_adm_name.getText();
        String newEmail = text_adm_email.getText();
        int newAge = Integer.parseInt(text_adm_age.getText());

        if (newName.isEmpty()) {
            MainLogics.Alert(Alert.AlertType.ERROR, "Database Error", "Name is required.");
            return false;
        } else {
            String[] nameParts = newName.split(" ");
            for (int i = 0; i < nameParts.length; i++) {
                nameParts[i] = nameParts[i].substring(0, 1).toUpperCase() + nameParts[i].substring(1).toLowerCase();
            }
            newName = String.join(" ", nameParts);
        }

        if (!newEmail.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
            MainLogics.Alert(Alert.AlertType.ERROR, "Database Error", "Please enter a valid email address.");
            return false;
        }

        if (newAge > 120 || newAge < 0) {
            MainLogics.Alert(Alert.AlertType.ERROR, "Database Error", "Please enter a valid age.");
            return false;
        }

        if (loggedInAdminID != null) {
            AdminLogics adminLogics = new AdminLogics();
            try {
                // Update the admin details in the database
                adminLogics.updateAdminDetails(loggedInAdminID, newName, newEmail, newAge);

                // Update the UI with new information
                label_add_name.setText(newName);
                label_add_email.setText(newEmail);
                label_add_age.setText(String.valueOf(newAge));

                MainLogics.Alert(Alert.AlertType.INFORMATION, "Success", "Profile updated successfully.");
            } catch (Exception e) {
                MainLogics.Alert(Alert.AlertType.ERROR, "Database Error", "Failed to update user details: " + e.getMessage());
            }
        }
        return true;
    }

    //handle the password change and save details to database
    @FXML
    public boolean handlePasswordChangeConfirm() {
        String newPassword = text_add_new_pwd.getText();
        String confirmPassword = text_adm_cofirm_pwd.getText();

        if (!newPassword.equals(confirmPassword)) {
            MainLogics.Alert(Alert.AlertType.ERROR, "Password Mismatch", "The new password and confirm password do not match.");
            return false;
        }
        if (newPassword.length() < 6) {
            MainLogics.Alert(Alert.AlertType.ERROR, "Password Mismatch", "Password must be at least 6 characters long.");
            return false;
        }

        if (loggedInAdminID != null) {
            AdminLogics adminLogics = new AdminLogics();
            try {
                // Update the password in the database
                adminLogics.changePassword(loggedInAdminID, newPassword);

                MainLogics.Alert(Alert.AlertType.INFORMATION, "Success", "Password changed successfully.");

                text_add_new_pwd.clear();
                text_adm_cofirm_pwd.clear();
                return true;
            } catch (Exception e) {
                MainLogics.Alert(Alert.AlertType.ERROR, "Database Error", "Failed to update password: " + e.getMessage());
            }
        }
        return false;
    }

    //user table
    @FXML
    private void loadUserDetails() {
        ObservableList<User> userData = FXCollections.observableArrayList();

        AdminLogics adminLogics = new AdminLogics();
        try {
            List<User> users = adminLogics.loadUserDetails();
            userData.addAll(users);
        } catch (Exception e) {
            MainLogics.Alert(Alert.AlertType.ERROR, "Database Error", "Failed to fetch user details: " + e.getMessage());
        }

        Admin_table.setItems(userData);
    }

    //handle username search
    @FXML
    public boolean handleUserSearch() {
        String usernameToSearch = text_ad_search_username.getText().trim();

        if (usernameToSearch.isEmpty()) {
            MainLogics.Alert(Alert.AlertType.ERROR, "Database Error",  "Please enter a username to search.");
            return false;
        }

        AdminLogics adminLogics = new AdminLogics();
        Document userDoc = adminLogics.searchUserByUsername(usernameToSearch);

        if (userDoc != null) {
            label_user_name.setText(userDoc.getString("name"));
            label_user_email.setText(userDoc.getString("email"));
            label_user_age.setText(String.valueOf(userDoc.getInteger("age", 0)));
            label_user_gender.setText(userDoc.getString("gender"));
            label_user_username.setText(userDoc.getString("username"));

            List<String> preferences = userDoc.getList("preferences", String.class);
            if (preferences != null && !preferences.isEmpty()) {
                label_user_preference.setText(String.join(", ", preferences));
            } else {
                label_user_preference.setText("No preferences set.");
            }
            return true;
        } else {
            clearUserLabels();
            MainLogics.Alert(Alert.AlertType.ERROR, "User Not Found", "No user found with the username: " + usernameToSearch);
            return false;
        }
    }

    private void clearUserLabels() {
        label_user_name.setText("");
        label_user_email.setText("");
        label_user_age.setText("");
        label_user_gender.setText("");
        label_user_username.setText("");
        label_user_preference.setText("");
    }


    @FXML
    public void handleRemoveUser() {
        String usernameToRemove = label_user_username.getText().trim();  // Assuming the username is displayed on the label after search

        if (usernameToRemove.isEmpty()) {
            MainLogics.Alert(Alert.AlertType.ERROR, "Error", "No user selected or username is empty.");
            return;
        }

        if (AdminLogics.removeUserFromDatabase(usernameToRemove)) {
            MainLogics.Alert(Alert.AlertType.INFORMATION, "Success", "User " + usernameToRemove + " has been removed successfully.");


            clearUserLabels();
            loadUserDetails();
        }
    }

    @FXML
    void addArticle(ActionEvent event) {
        String heading = text_adm_AddHeading.getText();
        String date = DatePicker_adm_date.getValue() != null ? DatePicker_adm_date.getValue().toString() : "";
        String content = textArea_adm_AddContent.getText();

        if (heading.isEmpty() || date.isEmpty() || content.isEmpty()) {
            MainLogics.Alert(Alert.AlertType.ERROR, "Input Error", "All fields must be filled out!");
            return;
        }

        // Call AdminLogics to add the article
        AdminLogics adminLogics = new AdminLogics();
        try {
            adminLogics.addArticle(heading, date, content);
            MainLogics.Alert(Alert.AlertType.INFORMATION, "Success", "Article added successfully!");
        } catch (Exception e) {
            MainLogics.Alert(Alert.AlertType.ERROR, "Database Error", "Failed to add article: " + e.getMessage());
        }

        text_adm_AddHeading.clear();
        DatePicker_adm_date.setValue(null);
        textArea_adm_AddContent.clear();
    }

    @FXML
    private void loadArticles() {
        ObservableList<Article> articleData = FXCollections.observableArrayList();

        AdminLogics adminLogics = new AdminLogics();
        try {
            List<Article> articles = adminLogics.loadArticles();

            // Add the articles to the ObservableList
            articleData.addAll(articles);
        } catch (Exception e) {
            MainLogics.Alert(Alert.AlertType.ERROR, "Database Error", "Failed to fetch articles: " + e.getMessage());
        }

        // Set the data into the TableView
        tabCol_heading.setCellValueFactory(new PropertyValueFactory<>("heading"));
        tabCol_delete_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        tabCol_delete_cate.setCellValueFactory(new PropertyValueFactory<>("category"));

        // Populate the TableView
        table_delete.setItems(articleData);
    }


    // Method to handle the sort button click
    @FXML
    public void handleSortButton(ActionEvent event) {
        // Get selected categories
        List<String> selectedCategories = new ArrayList<>();
        if (Admin_Cat_Enter.isSelected()) selectedCategories.add("Entertainment");
        if (Admin_Cat_finance.isSelected()) selectedCategories.add("Finance");
        if (Admin_Cat_Politics.isSelected()) selectedCategories.add("Politics");
        if (Admin_Cat_health.isSelected()) selectedCategories.add("Healthcare");
        if (Admin_Cat_tech.isSelected()) selectedCategories.add("AI and Technology");
        if (Admin_Cat_Science.isSelected()) selectedCategories.add("Science");
        if (Admin_Cat_sport.isSelected()) selectedCategories.add("Sport");
        if (Admin_Cat_world.isSelected()) selectedCategories.add("World");

        // Fetch filtered data from MongoDB based on selected categories
        ObservableList<Article> filteredArticles = AdminLogics.fetchArticlesFromDatabase(selectedCategories);

        // Display the filtered articles in the table
        table_delete.setItems(filteredArticles);
    }

    @FXML
    public void handleDeleteButton(ActionEvent event) {
        // Get the selected article from the table
        Article selectedArticle = table_delete.getSelectionModel().getSelectedItem();

        if (selectedArticle == null) {
            MainLogics.Alert(Alert.AlertType.ERROR, "Error", "Please select an article to delete.");
            return;
        }

        // Confirm deletion
        Alert confirmDeleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDeleteAlert.setTitle("Confirm Deletion");
        confirmDeleteAlert.setHeaderText(null);
        confirmDeleteAlert.setContentText("Are you sure you want to delete the article: " + selectedArticle.getHeading() + "?");

        Optional<ButtonType> result = confirmDeleteAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // If confirmed, delete the article from the database
            if (AdminLogics.deleteArticleFromDatabase(selectedArticle)) {
                MainLogics.Alert(Alert.AlertType.INFORMATION, "Success", "Article deleted successfully.");

                // Reload the articles in the table after deletion
                loadArticles();
            } else {
                MainLogics.Alert(Alert.AlertType.ERROR, "Error", "Failed to delete the article.");
            }
        }
    }
}
