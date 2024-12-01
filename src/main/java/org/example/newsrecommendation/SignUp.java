package org.example.newsrecommendation;

import com.mongodb.client.MongoClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.client.*;
import org.bson.Document;
import com.mongodb.client.MongoCursor;


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
    private ChoiceBox Sign_gen;

    @FXML
    private void SignUp_Home_button() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent signUpRoot = loader.load();

        Stage stage = (Stage) Sign_Con_Home.getScene().getWindow();
        stage.setScene(new Scene(signUpRoot));
        stage.show();
    }

    @FXML
    private void SignUp_Back_button() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent signUpRoot = loader.load();

        Stage stage = (Stage) Signup_back.getScene().getWindow();
        stage.setScene(new Scene(signUpRoot));
        stage.show();
    }
    @FXML
    public void saveUserToDatabase() {
        String name = Sign_name.getText().trim();
        String email = Sign_email.getText();
        String ageText = Sign_age.getText();
        String username = Sign_User.getText();
        String password = Sign_pwd.getText();
        String confirmPassword = Sign_con_pwd.getText();
        String gender = (String) Sign_gen.getValue();

        List<String> errorMessages = new ArrayList<>();

        if (name.isEmpty()) {
            errorMessages.add("Name is required.");
        } else {
            String[] nameParts = name.split(" ");
            for (int i = 0; i < nameParts.length; i++) {
                nameParts[i] = nameParts[i].substring(0, 1).toUpperCase() + nameParts[i].substring(1).toLowerCase();
            }
            name = String.join(" ", nameParts);
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
            errorMessages.add("Please enter a valid email address.");
        }

        int age = 0;
        try {
            age = Integer.parseInt(ageText);
            if (age <= 0 || age > 120) {
                errorMessages.add("Please enter a valid age.");
            }
        } catch (NumberFormatException e) {
            errorMessages.add("Age must be a valid number.");
        }

        if (username.isEmpty() || username.length() < 3) {
            errorMessages.add("Username must be at least 3 characters long.");
        } else if (isUsernameTaken(username)) {
            errorMessages.add("Username is already taken. Please choose a different one.");
        }

        if (password.isEmpty() || password.length() < 6) {
            errorMessages.add("Password must be at least 6 characters long.");
        }
        if (!password.equals(confirmPassword)) {
            errorMessages.add("Passwords do not match.");
        }

        List<String> preferences = new ArrayList<>();
        if (SignUp_Pre_Enter.isSelected()) preferences.add("Entertainment");
        if (SignUp_Pre_Health.isSelected()) preferences.add("Healthcare");
        if (SignUp_Pre_Politics.isSelected()) preferences.add("Politics");
        if (SignUp_Pre_Finan.isSelected()) preferences.add("Finance");
        if (SignUp_Pre_Tech.isSelected()) preferences.add("Technology");
        if (SignUp_Pre_Science.isSelected()) preferences.add("Science");
        if (SignUp_Pre_Sport.isSelected()) preferences.add("Sports");
        if (SignUp_Pre_World.isSelected()) preferences.add("World");

        if (preferences.isEmpty()) {
            errorMessages.add("Please select at least one preference.");
        }

        if (gender == null || gender.isEmpty()) {
            errorMessages.add("Please select a gender.");
        }

        if (!errorMessages.isEmpty()) {
            showAlert("Error", String.join("\n", errorMessages));
            return;
        }

        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("NewsRecommendations");
            MongoCollection<Document> userCollection = database.getCollection("User");

            Document userDoc = new Document("name", name)
                    .append("email", email)
                    .append("age", age)
                    .append("gender", gender)
                    .append("preferences", preferences)
                    .append("username", username)
                    .append("password", password);

            userCollection.insertOne(userDoc);

            MongoCollection<Document> preferenceCollection = database.getCollection("Preferences");

            Document preferenceDoc = new Document("username", username)
                    .append("Entertainment", preferences.contains("Entertainment") ? 5 : 0)
                    .append("Healthcare", preferences.contains("Healthcare") ? 5 : 0)
                    .append("Politics", preferences.contains("Politics") ? 5 : 0)
                    .append("Finance", preferences.contains("Finance") ? 5 : 0)
                    .append("Technology", preferences.contains("Technology") ? 5 : 0)
                    .append("Science", preferences.contains("Science") ? 5 : 0)
                    .append("Sports", preferences.contains("Sports") ? 5 : 0)
                    .append("World", preferences.contains("World") ? 5 : 0);

            preferenceCollection.insertOne(preferenceDoc);

            Sign_Pane_Thank.toFront();
            resetFields();
        } catch (Exception e) {
            showAlert("Error", "Error connecting to MongoDB: " + e.getMessage());
        }
    }

    // Method to check if the username is already taken
    private boolean isUsernameTaken(String username) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("NewsRecommendations");
            MongoCollection<Document> collection = database.getCollection("User");

            Document query = new Document("username", username);
            MongoCursor<Document> cursor = collection.find(query).iterator();

            if (cursor.hasNext()) {
                return true; // Username already exists
            }
        } catch (Exception e) {
            showAlert("Error", "Error checking username availability: " + e.getMessage());
        }
        return false;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to reset the form fields after saving the user
    private void resetFields() {
        Sign_name.clear();
        Sign_email.clear();
        Sign_age.clear();
        Sign_User.clear();
        Sign_pwd.clear();
        SignUp_Pre_Enter.setSelected(false);
        SignUp_Pre_Health.setSelected(false);
        SignUp_Pre_Politics.setSelected(false);
        SignUp_Pre_Finan.setSelected(false);
        SignUp_Pre_Tech.setSelected(false);
        SignUp_Pre_Science.setSelected(false);
        SignUp_Pre_Sport.setSelected(false);
        SignUp_Pre_World.setSelected(false);
    }

    @FXML
    private void initialize() {
        SignUp_Pre_Enter.setOnAction(event -> System.out.println("Entertainment checkbox clicked"));
        SignUp_Pre_Health.setOnAction(event -> System.out.println("Health checkbox clicked"));
        SignUp_Pre_Finan.setOnAction(event -> System.out.println("Finance checkbox clicked"));
        SignUp_Pre_Politics.setOnAction(event -> System.out.println("Politics checkbox clicked"));
        SignUp_Pre_Sport.setOnAction(event -> System.out.println("Sport checkbox clicked"));
        SignUp_Pre_Science.setOnAction(event -> System.out.println("Science checkbox clicked"));
        SignUp_Pre_Tech.setOnAction(event -> System.out.println("Technology checkbox clicked"));
        SignUp_Pre_World.setOnAction(event -> System.out.println("World checkbox clicked"));
    }
}
