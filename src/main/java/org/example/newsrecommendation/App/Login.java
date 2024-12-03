package org.example.newsrecommendation.App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.bson.Document;
import org.example.newsrecommendation.DataBase.DatabaseHandler;
import org.example.newsrecommendation.Service.MainLogics;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class Login implements Initializable {
    @FXML
    private Button Login_button_sign;
    @FXML
    private Button button_login;
    @FXML
    private Button Login_button_LogAd;
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

    private DatabaseHandler dbHandler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            dbHandler = new DatabaseHandler();
        } catch (Exception e) {
            e.printStackTrace();
            MainLogics.Alert(Alert.AlertType.ERROR, "Database Connection Error", "Could not connect to MongoDB.");
        }
    }

    private boolean checkCredentials(String username, String password) {
        try {
            Document query = new Document("username", username).append("password", password);
            Document user = dbHandler.findDocument("User", query);
            return user != null;
        } catch (Exception e) {
            e.printStackTrace();
            MainLogics.Alert(Alert.AlertType.ERROR, "Login Error", "An error occurred while checking credentials.");
            return false;
        }
    }

    private boolean checkCredentialsAdmin(String username, String password, String adminID) {
        try {
            Document query = new Document("username", username).append("password", password).append("adminId", adminID);
            Document admin = dbHandler.findDocument("Admin", query);
            return admin != null;
        } catch (Exception e) {
            e.printStackTrace();
            MainLogics.Alert(Alert.AlertType.ERROR, "Login Error", "An error occurred while checking credentials.");
            return false;
        }
    }

    private void saveLoginDetails(String username) {
        try {
            Document loginRecord = new Document("username", username).append("login_time", LocalDateTime.now().toString());
            dbHandler.insertDocument("User_Login", loginRecord);
        } catch (Exception e) {
            e.printStackTrace();
            MainLogics.Alert(Alert.AlertType.ERROR, "Database Error", "Could not save login details.");
        }
    }

    private void saveLoginDetailsAdmin(String adminID) {
        try {
            Document loginRecord = new Document("adminId", adminID).append("login_time", LocalDateTime.now().toString());
            dbHandler.insertDocument("Admin_Login", loginRecord);
        } catch (Exception e) {
            e.printStackTrace();
            MainLogics.Alert(Alert.AlertType.ERROR, "Database Error", "Could not save login details.");
        }
    }

    @FXML
    private void Login_Login_button() throws IOException {
        String username = Login_user.getText();
        String password = Login_pwd.getText();

        if (checkCredentials(username, password)) {
            saveLoginDetails(username);
            MainLogics.Alert(Alert.AlertType.INFORMATION, "Login", "Welcome " + username);
            Main.setLoggedInUsername(username);
            ArticleScene.setCurrentUsername(username);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/newsrecommendation/Main.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) button_login.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            MainLogics.Alert(Alert.AlertType.ERROR, "Login", "Incorrect username or password");
        }
    }

    @FXML
    private void Login_AdminLogin_button() throws IOException {
        String username = Login_user_Admin.getText();
        String password = Login_pwd_ad.getText();
        String adminID = Login_Admin_Id.getText();

        if (checkCredentialsAdmin(username, password, adminID)) {
            saveLoginDetailsAdmin(adminID);
            MainLogics.Alert(Alert.AlertType.INFORMATION, "Login", "Welcome Admin " + username);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/newsrecommendation/Administrator.fxml"));
            Parent root = loader.load();
            Administrator.setLoggedInAdminID(adminID);
            Stage stage = (Stage) button_login.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            MainLogics.Alert(Alert.AlertType.ERROR, "Login", "Incorrect admin credentials");
        }
    }

    @FXML
    private void Login_SignUp_button() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/newsrecommendation/SignUp.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) Login_button_sign.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void logPaneNav(ActionEvent actionEvent) {
        if (actionEvent.getSource() == Login_button_LogAd) {
            Login_Pane_Admin.toFront();
        }
        if (actionEvent.getSource() == Login_button_backlog) {
            Login_pane_User.toFront();
        }
    }
}
