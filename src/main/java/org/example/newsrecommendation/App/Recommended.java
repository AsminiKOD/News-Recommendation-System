package org.example.newsrecommendation.App;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import org.bson.Document;
import org.example.newsrecommendation.DataBase.DatabaseHandler;
import org.example.newsrecommendation.Service.MainLogics;

public class Recommended {

    @FXML
    private Label label_date;

    @FXML
    private Label label_heading;

    @FXML
    private Label label_preferences;

    private final DatabaseHandler dbHandler;

    public Recommended() {
        dbHandler = new DatabaseHandler();
    }

    public void setArticleData(String heading, String date, String category) {
        label_heading.setText(heading);
        label_date.setText(date);
        label_preferences.setText(category);
    }

    @FXML
    private void onButtonArticleClick() {
        String heading = label_heading.getText();

        if (heading != null && !heading.isEmpty()) {
            Document result = MainLogics.fetchArticleDetails(dbHandler, heading);

            if (result != null) {
                String fullArticle = result.getString("article");
                String category = result.getString("category");
                String date = result.getString("date");

                MainLogics.loadArticleScene(heading, date, category, fullArticle);
            } else {
                MainLogics.Alert(Alert.AlertType.ERROR, "Error", "Article not found in the database.");
            }
        } else {
            MainLogics.Alert(Alert.AlertType.WARNING, "Warning", "No article selected.");
        }
    }
}
