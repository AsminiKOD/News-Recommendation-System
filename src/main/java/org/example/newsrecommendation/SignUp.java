package org.example.newsrecommendation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

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
}
