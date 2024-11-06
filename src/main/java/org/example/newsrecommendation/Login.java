package org.example.newsrecommendation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Login {
    @FXML
    private Button Login_button_sign;
    @FXML
    private Button button_login;
    @FXML
    private Button Login_button_LogAd;
    @FXML
    private Button Login_button_LogAd1;
    @FXML
    private Button Login_button_sign_admin;

    @FXML
    private Pane Login_pane_User;
    @FXML
    private Pane Login_Pane_Admin;

    @FXML
    private void Login_Login_button() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent signUpRoot = loader.load();

        Stage stage = (Stage) button_login.getScene().getWindow();
        stage.setScene(new Scene(signUpRoot));
        stage.show();
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
    private void Login_Admin_button() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Adminstraitor.fxml"));
        Parent signUpRoot = loader.load();

        Stage stage = (Stage) Login_button_LogAd1.getScene().getWindow();
        stage.setScene(new Scene(signUpRoot));
        stage.show();
    }

    @FXML
    private void Login_AdminSignUp_button() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
        Parent signUpRoot = loader.load();

        Stage stage = (Stage) Login_button_sign_admin.getScene().getWindow();
        stage.setScene(new Scene(signUpRoot));
        stage.show();
    }

    @FXML
    public void logPaneNav(ActionEvent actionEvent){
        if (actionEvent.getSource() == Login_button_LogAd){
            Login_Pane_Admin.toFront();
        }
    }
}