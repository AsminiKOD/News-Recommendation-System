package org.example.newsrecommendation;

import com.mongodb.client.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import org.bson.Document;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
    private Button Admin_button_log;

    @FXML
    private Pane Admin_pane_Delete;

    @FXML
    private TableView<User> Admin_table;

    @FXML
    private Button Button_add_edit;

    @FXML
    private Pane Front_Pane;

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
    private TableView<LoginHistory> tabView_LogHis;

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

    private static String loggedInAdminID;

    public static void setLoggedInAdminID(String username) {
        loggedInAdminID = username;
    }

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
        // Initialize the TableView columns
        tabCol_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        tabCol_time.setCellValueFactory(new PropertyValueFactory<>("time"));

        // Clear TableView data on initialization
        tabView_LogHis.setItems(FXCollections.observableArrayList());

        // Initialize the TableView columns
        tabCol_user.setCellValueFactory(new PropertyValueFactory<>("username")); // Display username
        tabCol_name.setCellValueFactory(new PropertyValueFactory<>("name")); // Display name
        tabCol_email.setCellValueFactory(new PropertyValueFactory<>("email")); // Display email

        // Clear TableView data on initialization
        Admin_table.setItems(FXCollections.observableArrayList());

    }

    private void showUserProfile() {
        if (loggedInAdminID != null) {
            try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                MongoDatabase database = mongoClient.getDatabase("NewsRecommendations");
                MongoCollection<Document> userCollection = database.getCollection("Admin");
                MongoCollection<Document> loginHistoryCollection = database.getCollection("Admin_Login");

                Document adminDoc = userCollection.find(new Document("adminId", loggedInAdminID)).first();
                if (adminDoc != null) {
                    Admin admin = new Admin(
                            adminDoc.getString("name"),
                            adminDoc.getString("email"),
                            adminDoc.getInteger("age"),
                            adminDoc.getString("gender"),
                            adminDoc.getString("password"),
                            adminDoc.getString("adminId")
                    );

                    label_add_name.setText(admin.getName());
                    label_add_email.setText(admin.getEmail());
                    label_add_age.setText(String.valueOf(admin.getAge()));
                    label_add_gender.setText(admin.getGender());
                    label_add_ID.setText(admin.getAdmin_ID());

                    // Fetch login history
                    List<LoginHistory> loginHistory = new ArrayList<>();
                    FindIterable<Document> historyDocs = loginHistoryCollection.find(new Document("adminId", loggedInAdminID));
                    for (Document doc : historyDocs) {
                        String loginTime = doc.getString("login_time");

                        // Parse login_time into date and time
                        String date = loginTime.split("T")[0]; // Extract date
                        String time = loginTime.split("T")[1].split("\\.")[0]; // Extract time

                        loginHistory.add(new LoginHistory(date, time));
                    }

                    // Populate login history in table
                    ObservableList<LoginHistory> historyData = FXCollections.observableArrayList(loginHistory);
                    tabView_LogHis.setItems(historyData);
                } else {
                    showAlert("User Not Found", "No user found with the username: " + loggedInAdminID);
                    clearUserProfile();
                }
            } catch (Exception e) {
                showAlert("Database Error", "Failed to fetch user details: " + e.getMessage());
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

    // This method handles navigation to edit profile page and pre-filling user data.
    @FXML
    public void handleEditProfile() {
        if (loggedInAdminID != null) {
            try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                MongoDatabase database = mongoClient.getDatabase("NewsRecommendations");
                MongoCollection<Document> userCollection = database.getCollection("Admin");

                Document adminDoc = userCollection.find(new Document("adminId", loggedInAdminID)).first();
                if (adminDoc != null) {
                    // Populate the edit fields with current user details
                    text_adm_name.setText(adminDoc.getString("name"));
                    text_adm_email.setText(adminDoc.getString("email"));
                    text_adm_age.setText(String.valueOf(adminDoc.getInteger("age")));
                }
            } catch (Exception e) {
                showAlert("Database Error", "Failed to fetch user details: " + e.getMessage());
            }
        }
    }

    // This method handles the "Confirm" button click, saving the changes to the database.
    @FXML
    public boolean handleEditConfirm() {
        // Get the updated user details from the form
        String newName = text_adm_name.getText();
        String newEmail = text_adm_email.getText();
        int newAge = Integer.parseInt(text_adm_age.getText());

        if (newName.isEmpty()) {
            showAlert("Error","Name is required.");
            return false;
        } else {
            // Capitalize first and last names
            String[] nameParts = newName.split(" ");
            for (int i = 0; i < nameParts.length; i++) {
                nameParts[i] = nameParts[i].substring(0, 1).toUpperCase() + nameParts[i].substring(1).toLowerCase();
            }
            newName = String.join(" ", nameParts);
        }

        // Email validation
        if (!newEmail.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
            showAlert("Error","Please enter a valid email address.");
            return false;
        }

        // Age validation
        if (newAge>120 || newAge<0){
            showAlert("Error","Please enter a valid age.");
            return false;
        }

        // Update the user document in MongoDB
        if (loggedInAdminID != null) {
            try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                MongoDatabase database = mongoClient.getDatabase("NewsRecommendations");
                MongoCollection<Document> userCollection = database.getCollection("Admin");

                Document updatedUser = new Document("name", newName)
                        .append("email", newEmail)
                        .append("age", newAge);

                // Update the document in the database
                userCollection.updateOne(new Document("adminId", loggedInAdminID),
                        new Document("$set", updatedUser));

                // Reflect the changes in the profile page
                label_add_name.setText(newName);
                label_add_email.setText(newEmail);
                label_add_age.setText(String.valueOf(newAge));

                // Show success message
                showAlerts();
            } catch (Exception e) {
                showAlert("Database Error", "Failed to update user details: " + e.getMessage());
            }
        }
        return true;
    }

    @FXML
    public boolean handlePasswordChangeConfirm() {
        // Get the new password and confirm password from the input fields
        String newPassword = text_add_new_pwd.getText();
        String confirmPassword = text_adm_cofirm_pwd.getText();

        // Validate that the passwords match
        if (!newPassword.equals(confirmPassword)) {
            showAlert("Password Mismatch", "The new password and confirm password do not match.");
            return false;
        }
        if (newPassword.length() < 6) {
            showAlert("Password Mismatch","Password must be at least 6 characters long.");
            return false;
        }

        // If valid, update the password in the database
        if (loggedInAdminID != null) {
            try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                MongoDatabase database = mongoClient.getDatabase("NewsRecommendations");
                MongoCollection<Document> userCollection = database.getCollection("Admin");

                // Update the password field in the database
                Document updatedUser = new Document("password", newPassword);
                userCollection.updateOne(new Document("adminId", loggedInAdminID),
                        new Document("$set", updatedUser));

                // Show success message
                showAlerts();

                // Optionally, reset the fields or navigate back to profile page
                text_add_new_pwd.clear();
                text_adm_cofirm_pwd.clear();
                return true;
            } catch (Exception e) {
                showAlert("Database Error", "Failed to update password: " + e.getMessage());
            }
        }
        return false;
    }

    private void loadUserDetails() {
        ObservableList<User> userData = FXCollections.observableArrayList();

        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("NewsRecommendations");
            MongoCollection<Document> userCollection = database.getCollection("User");

            FindIterable<Document> users = userCollection.find();
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
            showAlert("Database Error", "Failed to fetch user details: " + e.getMessage());
        }

        // Set data into TableView
        Admin_table.setItems(userData);
    }

    @FXML
    public boolean handleUserSearch() {
        String usernameToSearch = text_ad_search_username.getText().trim();

        if (usernameToSearch.isEmpty()) {
            showAlert("Error", "Please enter a username to search.");
            return false;
        }

        // Fetch user data from the database based on the entered username
        if (!searchUserByUsername(usernameToSearch)) {
            showAlert("User Not Found", "No user found with the username: " + usernameToSearch);
            return false;
        }
        return true;
    }


    private boolean searchUserByUsername(String username) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("NewsRecommendations");
            MongoCollection<Document> userCollection = database.getCollection("User");

            // Search for the user by username in the User collection
            Document userDoc = userCollection.find(new Document("username", username)).first();

            if (userDoc != null) {
                // Populate the labels with the user data
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
                return false;  // User not found, return false
            }
        } catch (Exception e) {
            showAlert("Database Error", "Failed to fetch user details: " + e.getMessage());
            clearUserLabels();
            return false;  // Database error, return false
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
            showAlert("Error", "No user selected or username is empty.");
            return;
        }

        // Remove the user from the database
        if (removeUserFromDatabase(usernameToRemove)) {
            // Update UI or provide success message
            showAlert("Success", "User " + usernameToRemove + " has been removed successfully.");

            // Optionally, refresh the table or reset fields
            clearUserLabels();
            loadUserDetails(); // Reload user details into the table view
        }
    }

    private boolean removeUserFromDatabase(String username) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("NewsRecommendations");
            MongoCollection<Document> userCollection = database.getCollection("User");

            // Find and delete the user by username
            Document result = userCollection.findOneAndDelete(new Document("username", username));

            if (result != null) {
                return true;  // User successfully removed
            } else {
                showAlert("Error", "No user found with username: " + username);
                return false;  // User not found
            }
        } catch (Exception e) {
            showAlert("Database Error", "Failed to remove user: " + e.getMessage());
            return false;
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
