package org.example.newsrecommendation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

public class Main {
    @FXML
    private Button Main_button_Home;
    @FXML
    private Button Main_button_Recomm;
    @FXML
    private Button Main_button_Saved;
    @FXML
    private Button Main_button_About;
    @FXML
    private Button Main_button_profile;
    @FXML
    private Button Main_button_close;

    @FXML
    private Pane Main_HomePage;
    @FXML
    private Pane Main_RecommendPage;
    @FXML
    private Pane Main_SavePage;
    @FXML
    private Pane Main_AboutPage;
    @FXML
    private Pane Main_ProfilePage;


    @FXML
    public void userPaneNav(ActionEvent actionEvent){
        if (actionEvent.getSource() == Main_button_Home){
            Main_HomePage.toFront();
        }
        if (actionEvent.getSource() == Main_button_Recomm){
            Main_RecommendPage.toFront();
        }
        if (actionEvent.getSource() == Main_button_Saved) {
            Main_SavePage.toFront();
        }
        if (actionEvent.getSource() == Main_button_About) {
            Main_AboutPage.toFront();
        }
        if (actionEvent.getSource() == Main_button_profile) {
            Main_ProfilePage.toFront();
        }
    }
}
