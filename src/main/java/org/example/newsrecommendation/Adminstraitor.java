package org.example.newsrecommendation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class Adminstraitor {
    @FXML
    private Button Admin_button_delete;
    @FXML
    private Button Admin_button_Add;
    @FXML
    private Button Admin_button_View;

    @FXML
    private Pane Admin_pane_Delete;
    @FXML
    private Pane Admin_Pane_Add;
    @FXML
    private Pane Admin_Pane_User_records;

    @FXML
    public void adminPaneNav(ActionEvent actionEvent){
        if (actionEvent.getSource() == Admin_button_delete){
            Admin_pane_Delete.toFront();
        }
        if (actionEvent.getSource() == Admin_button_Add){
            Admin_Pane_Add.toFront();
        }
        if (actionEvent.getSource() == Admin_button_View) {
            Admin_Pane_User_records.toFront();
        }
    }
}
