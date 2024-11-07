package org.example.newsrecommendation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
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
    public void signPaneNav(ActionEvent actionEvent){
        if (actionEvent.getSource() == button_signup){
            Sign_Pane_Thank.toFront();
        }
    }
}
