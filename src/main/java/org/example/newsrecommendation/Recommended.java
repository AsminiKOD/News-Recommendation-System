package org.example.newsrecommendation;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Recommended {

    @FXML
    private VBox VBox_Articles;

    @FXML
    private Button button_article;

    @FXML
    private Label label_date;

    @FXML
    private Label label_heading;

    @FXML
    private Label label_preferences;

    public void setArticleData(String heading, String date, String category) {
        label_heading.setText(heading);
        label_date.setText(date);
        label_preferences.setText(category);
    }


}
