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
import org.example.newsrecommendation.Service.MainLogics;
import org.example.newsrecommendation.Service.SignFunctions;

import java.io.IOException;
import java.net.URL;
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

    private SignFunctions signFunctions;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        signFunctions = new SignFunctions();
    }

    @FXML
    private void Login_Login_button() throws IOException {
        String username = Login_user.getText();
        String password = Login_pwd.getText();

        if (signFunctions.checkCredentials(username, password)) {
            signFunctions.saveLoginDetails(username);
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

        if (signFunctions.checkCredentialsAdmin(username, password, adminID)) {
            signFunctions.saveLoginDetailsAdmin(adminID);
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
