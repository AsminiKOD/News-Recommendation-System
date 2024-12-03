package org.example.newsrecommendation.App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.bson.Document;
import org.example.newsrecommendation.Article;
import org.example.newsrecommendation.DataBase.DatabaseHandler;

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
            try {
                // Fetch the article details using DatabaseHandler
                Document query = new Document("heading", heading);
                Document result = dbHandler.findDocument("Article", query);

                if (result != null) {
                    String fullArticle = result.getString("article");
                    String category = result.getString("category");
                    String date = result.getString("date");

                    // Load the new FXML and pass the article details
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/newsrecommendation/ArticleScene.fxml"));
                    Parent root = loader.load();

                    ArticleScene controller = loader.getController();
                    controller.setArticleDetails(new Article(heading, date, category), fullArticle);

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Article Details");
                    stage.show();
                } else {
                    System.out.println("Article not found in the database.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
