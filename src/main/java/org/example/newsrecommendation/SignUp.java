package org.example.newsrecommendation;

import com.mongodb.client.MongoClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.client.*;
import com.mongodb.client.MongoClient;
import com.mongodb.ConnectionString;
import org.bson.Document;

public class SignUp {
    @FXML
    private Button Sign_Con_Home;
    @FXML
    private Button Sign_Con_Close;
    @FXML
    private Button button_signup;
    @FXML
    private Button button_signup_close;
    @FXML
    private Button Signup_back;

    @FXML
    private Pane Sign_pane_user;
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

    private MongoClient mongoClient;
    private MongoDatabase database;

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
        // Retrieve input values from TextFields
        String name = Sign_name.getText();
        String email = Sign_email.getText();
        int age = Integer.parseInt(Sign_age.getText());
        String username = Sign_User.getText();
        String password = Sign_pwd.getText();
        String gender = (String) Sign_gen.getValue();

        // Gather selected preferences from CheckBoxes
        List<String> preferences = new ArrayList<>();
        if (SignUp_Pre_Enter.isSelected()) preferences.add("Entertainment");
        if (SignUp_Pre_Health.isSelected()) preferences.add("Healthcare");
        if (SignUp_Pre_Politics.isSelected()) preferences.add("Politics");
        if (SignUp_Pre_Finan.isSelected()) preferences.add("Finance");
        if (SignUp_Pre_Tech.isSelected()) preferences.add("Technology");
        if (SignUp_Pre_Science.isSelected()) preferences.add("Science");
        if (SignUp_Pre_Sport.isSelected()) preferences.add("Sports");
        if (SignUp_Pre_World.isSelected()) preferences.add("World");

        // Create a MongoDB Document
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("NewsRecommendations");
            MongoCollection<Document> collection = database.getCollection("User");

            Document userDoc = new Document("name", name)
                    .append("email", email)
                    .append("age", age)
                    .append("gender", gender)
                    .append("preferences", preferences)
                    .append("username", username)
                    .append("password", password);

            collection.insertOne(userDoc);
            Sign_Pane_Thank.toFront();
        } catch (Exception e) {
            System.out.println("Error connecting to MongoDB: " + e.getMessage());
        }
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

    // Make sure to close MongoClient when it's no longer needed
    public void close() {
        mongoClient.close();
    }
}
