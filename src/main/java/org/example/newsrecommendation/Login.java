package org.example.newsrecommendation;

import com.mongodb.client.*;
import com.mongodb.ConnectionString;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.ResourceBundle;

public class Login implements Initializable {
    @FXML
    private Button Login_button_sign;
    @FXML
    private Button button_login;
    @FXML
    private Button Login_button_LogAd;
    @FXML
    private Button login_button_admin_logLog;
    @FXML
    private Button Login_button_backlog;

    @FXML
    private Pane Login_pane_User;
    @FXML
    private Pane Login_Pane_Admin;

    @FXML
    private TextField Login_Admin_Id;
    @FXML
    private TextField Login_user_Admin;
    @FXML
    private TextField Login_pwd_ad;
    @FXML
    private TextField Login_user;
    @FXML
    private TextField Login_pwd;

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> userDetailsCollection;
    private MongoCollection<Document> userLoginDetailsCollection;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Connect to MongoDB
            mongoClient = MongoClients.create("mongodb://localhost:27017");
            database = mongoClient.getDatabase("NewsRecommendations");
            userDetailsCollection = database.getCollection("User");
            userLoginDetailsCollection = database.getCollection("User_Login");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Connection Error", "Could not connect to MongoDB.");
        }
    }

    private boolean checkCredentials(String username, String password) {
        try {
            // Find user with matching username and password
            Document user = userDetailsCollection.find(new Document("username", username)
                    .append("password", password)).first();
            return user != null;
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Login Error", "An error occurred while checking credentials.");
        }
        return false;
    }

    private void saveLoginDetails(String username) {
        try {
            // Insert login record
            Document loginRecord = new Document("username", username)
                    .append("login_time", LocalDateTime.now().toString());
            userLoginDetailsCollection.insertOne(loginRecord);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not save login details.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void Login_Login_button() throws IOException {
        String username = Login_user.getText();
        String password = Login_pwd.getText();

        if (checkCredentials(username, password)) {
            saveLoginDetails(username);
            showAlert(Alert.AlertType.INFORMATION, "Login", "Welcome " + username);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
            Parent signUpRoot = loader.load();

            Stage stage = (Stage) button_login.getScene().getWindow();
            stage.setScene(new Scene(signUpRoot));
            stage.show();
        } else {
            showAlert(Alert.AlertType.ERROR, "Login", "Incorrect username or password");
        }
    }

    @FXML
    private void Login_SignUp_button() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
        Parent signUpRoot = loader.load();

        Stage stage = (Stage) Login_button_sign.getScene().getWindow();
        stage.setScene(new Scene(signUpRoot));
        stage.show();
    }
    @FXML
    private void Login_AdminLogin_button() throws IOException {
        Parent mainRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Administrator.fxml")));
        Stage stage = (Stage) login_button_admin_logLog.getScene().getWindow();
        Scene scene = stage.getScene();
        scene.setRoot(mainRoot);
        stage.sizeToScene();
    }


    @FXML
    public void logPaneNav(ActionEvent actionEvent){
        if (actionEvent.getSource() == Login_button_LogAd){
            Login_Pane_Admin.toFront();
        }
        if (actionEvent.getSource() == Login_button_backlog){
            Login_pane_User.toFront();
        }
    }
}