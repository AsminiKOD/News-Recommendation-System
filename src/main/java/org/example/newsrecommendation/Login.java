package org.example.newsrecommendation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class Login {
    @FXML
    private Button Login_button_sign;
    @FXML
    private Button button_login;

    @FXML
    private void Login_SignUp_button() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
        Parent signUpRoot = loader.load();

        Stage stage = (Stage) Login_button_sign.getScene().getWindow();
        stage.setScene(new Scene(signUpRoot));
        stage.show();
    }

}