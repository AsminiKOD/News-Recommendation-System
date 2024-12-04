package org.example.newsrecommendation.App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.newsrecommendation.Service.SignFunctions;
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

    private SignFunctions signFunctions;

    @FXML
    private void initialize() {
        signFunctions = new SignFunctions();
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
    private void saveUserToDatabase() {
        String name = signFunctions.formatName(Sign_name.getText().trim());
        String email = Sign_email.getText();
        String ageText = Sign_age.getText();
        String username = Sign_User.getText();
        String password = Sign_pwd.getText();
        String confirmPassword = Sign_con_pwd.getText();
        String gender = Sign_gen.getValue();

        List<String> preferences = collectPreferences();

        List<String> errorMessages = signFunctions.validateInputs(name, email, ageText, username, password, confirmPassword, gender, preferences);

        if (!errorMessages.isEmpty()) {
            MainLogics.Alert(Alert.AlertType.ERROR, "Error", String.join("\n", errorMessages));
            return;
        }

        int age = Integer.parseInt(ageText);
        signFunctions.saveUserData(name, email, age, username, password, gender, preferences);
        Main.setLoggedInUsername(username);
        ArticleScene.setCurrentUsername(username);
        Sign_Pane_Thank.toFront();
        resetFields();
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
