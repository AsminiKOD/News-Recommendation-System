package org.example.newsrecommendation.App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.bson.Document;
import org.example.newsrecommendation.DataBase.DatabaseHandler;
import org.example.newsrecommendation.Service.MainLogics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SignUp {
    @FXML
    private Button Sign_Con_Home;
    @FXML
    private Button Signup_back;
    @FXML
    private Pane Sign_Pane_Thank;
    @FXML
    private TextField Sign_name;
    @FXML
    private TextField Sign_email;
    @FXML
    private TextField Sign_age;
    @FXML
    private TextField Sign_User;
    @FXML
    private TextField Sign_pwd;
    @FXML
    private TextField Sign_con_pwd;
    @FXML
    private CheckBox SignUp_Pre_Enter;
    @FXML
    private CheckBox SignUp_Pre_Health;
    @FXML
    private CheckBox SignUp_Pre_Politics;
    @FXML
    private CheckBox SignUp_Pre_Finan;
    @FXML
    private CheckBox SignUp_Pre_Tech;
    @FXML
    private CheckBox SignUp_Pre_Science;
    @FXML
    private CheckBox SignUp_Pre_Sport;
    @FXML
    private CheckBox SignUp_Pre_World;
    @FXML
    private ChoiceBox<String> Sign_gen;

    private DatabaseHandler dbHandler;

    @FXML
    private void initialize() {
        dbHandler = new DatabaseHandler();
        setUpCheckBoxListeners();
    }

    private void setUpCheckBoxListeners() {
        SignUp_Pre_Enter.setOnAction(event -> System.out.println("Entertainment checkbox clicked"));
        SignUp_Pre_Health.setOnAction(event -> System.out.println("Health checkbox clicked"));
        SignUp_Pre_Finan.setOnAction(event -> System.out.println("Finance checkbox clicked"));
        SignUp_Pre_Politics.setOnAction(event -> System.out.println("Politics checkbox clicked"));
        SignUp_Pre_Sport.setOnAction(event -> System.out.println("Sport checkbox clicked"));
        SignUp_Pre_Science.setOnAction(event -> System.out.println("Science checkbox clicked"));
        SignUp_Pre_Tech.setOnAction(event -> System.out.println("Technology checkbox clicked"));
        SignUp_Pre_World.setOnAction(event -> System.out.println("World checkbox clicked"));
    }

    @FXML
    private void SignUp_Home_button() throws IOException {
        loadScene("/org/example/newsrecommendation/Main.fxml");
    }

    @FXML
    private void SignUp_Back_button() throws IOException {
        loadScene("/org/example/newsrecommendation/Login.fxml");
    }

    private void loadScene(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Stage stage = (Stage) Sign_Con_Home.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void saveUserToDatabase() {
        String name = formatName(Sign_name.getText().trim());
        String email = Sign_email.getText();
        String ageText = Sign_age.getText();
        String username = Sign_User.getText();
        String password = Sign_pwd.getText();
        String confirmPassword = Sign_con_pwd.getText();
        String gender = Sign_gen.getValue();

        List<String> preferences = collectPreferences();

        List<String> errorMessages = validateInputs(name, email, ageText, username, password, confirmPassword, gender, preferences);

        if (!errorMessages.isEmpty()) {
            MainLogics.Alert(Alert.AlertType.ERROR, "Error", String.join("\n", errorMessages));
            return;
        }

        int age = Integer.parseInt(ageText);
        saveUserData(name, email, age, username, password, gender, preferences);
        Main.setLoggedInUsername(username);
        ArticleScene.setCurrentUsername(username);
    }

    private String formatName(String name) {
        if (name.isEmpty()) return "";
        String[] nameParts = name.split(" ");
        for (int i = 0; i < nameParts.length; i++) {
            nameParts[i] = nameParts[i].substring(0, 1).toUpperCase() + nameParts[i].substring(1).toLowerCase();
        }
        return String.join(" ", nameParts);
    }

    private List<String> collectPreferences() {
        List<String> preferences = new ArrayList<>();
        if (SignUp_Pre_Enter.isSelected()) preferences.add("Entertainment");
        if (SignUp_Pre_Health.isSelected()) preferences.add("Healthcare");
        if (SignUp_Pre_Politics.isSelected()) preferences.add("Politics");
        if (SignUp_Pre_Finan.isSelected()) preferences.add("Finance");
        if (SignUp_Pre_Tech.isSelected()) preferences.add("Technology");
        if (SignUp_Pre_Science.isSelected()) preferences.add("Science");
        if (SignUp_Pre_Sport.isSelected()) preferences.add("Sports");
        if (SignUp_Pre_World.isSelected()) preferences.add("World");
        return preferences;
    }

    private List<String> validateInputs(String name, String email, String ageText, String username, String password,
                                        String confirmPassword, String gender, List<String> preferences) {
        List<String> errors = new ArrayList<>();

        if (name.isEmpty()) errors.add("Name is required.");
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) errors.add("Please enter a valid email address.");
        try {
            int age = Integer.parseInt(ageText);
            if (age <= 0 || age > 120) errors.add("Please enter a valid age.");
        } catch (NumberFormatException e) {
            errors.add("Age must be a valid number.");
        }
        if (username.isEmpty() || username.length() < 3) errors.add("Username must be at least 3 characters long.");
        if (isUsernameTaken(username)) errors.add("Username is already taken. Please choose a different one.");
        if (password.isEmpty() || password.length() < 6) errors.add("Password must be at least 6 characters long.");
        if (!password.equals(confirmPassword)) errors.add("Passwords do not match.");
        if (gender == null || gender.isEmpty()) errors.add("Please select a gender.");
        if (preferences.isEmpty()) errors.add("Please select at least one preference.");

        return errors;
    }

    private boolean isUsernameTaken(String username) {
        Document query = new Document("username", username);
        return dbHandler.findDocument("User", query) != null;
    }

    private void saveUserData(String name, String email, int age, String username, String password, String gender,
                              List<String> preferences) {
        try {
            // Create the user document
            Document userDoc = new Document("name", name)
                    .append("email", email)
                    .append("age", age)
                    .append("gender", gender)
                    .append("preferences", preferences)
                    .append("username", username)
                    .append("password", password);

            // Insert the user document into the "User" collection
            dbHandler.insertDocument("User", userDoc);

            // Create the preference document
            Document preferenceDoc = new Document("username", username);

            // Set default values for all preferences
            preferenceDoc.append("Entertainment", preferences.contains("Entertainment") ? 5 : 0)
                    .append("Healthcare", preferences.contains("Healthcare") ? 5 : 0)
                    .append("Politics", preferences.contains("Politics") ? 5 : 0)
                    .append("Finance", preferences.contains("Finance") ? 5 : 0)
                    .append("Technology", preferences.contains("Technology") ? 5 : 0)
                    .append("Science", preferences.contains("Science") ? 5 : 0)
                    .append("Sports", preferences.contains("Sports") ? 5 : 0)
                    .append("World", preferences.contains("World") ? 5 : 0);

            // Insert the preference document into the "Preferences" collection
            dbHandler.insertDocument("Preferences", preferenceDoc);

            Sign_Pane_Thank.toFront();
            resetFields();
        } catch (Exception e) {
            // Show an alert if there's an error
            MainLogics.Alert(Alert.AlertType.ERROR, "Error", "Error saving user data: " + e.getMessage());
        }
    }

    private void resetFields() {
        Sign_name.clear();
        Sign_email.clear();
        Sign_age.clear();
        Sign_User.clear();
        Sign_pwd.clear();
        Sign_con_pwd.clear();
        SignUp_Pre_Enter.setSelected(false);
        SignUp_Pre_Health.setSelected(false);
        SignUp_Pre_Politics.setSelected(false);
        SignUp_Pre_Finan.setSelected(false);
        SignUp_Pre_Tech.setSelected(false);
        SignUp_Pre_Science.setSelected(false);
        SignUp_Pre_Sport.setSelected(false);
        SignUp_Pre_World.setSelected(false);
    }
}
