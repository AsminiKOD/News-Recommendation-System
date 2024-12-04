package org.example.newsrecommendation.App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.newsrecommendation.Model.Article;
import org.example.newsrecommendation.Service.RecommendationAlgorithm;

public class ArticleScene {

    @FXML
    private Label label_Heading;
    @FXML
    private Label label_Article;
    @FXML
    private Label label_Category;
    @FXML
    private Label label_Date;
    @FXML
    private Button button_Like;
    @FXML
    private Button button_Dislike;
    @FXML
    private Button button_Save;

    private static String currentUsername;
    private final RecommendationAlgorithm recommendationAlgorithm;

    public ArticleScene() {
        recommendationAlgorithm = new RecommendationAlgorithm();
    }

    public static void setCurrentUsername(String username) {
        currentUsername = username;
    }

    public void setArticleDetails(Article article, String fullArticle) {
        label_Heading.setText(article.getHeading());
        label_Article.setText(fullArticle);
        label_Category.setText(article.getCategory());
        label_Date.setText(article.getDate());
    }

    @FXML
    private void handleLike(ActionEvent event) {
        String articleHeading = label_Heading.getText();
        String category = label_Category.getText();
        recommendationAlgorithm.updateUserPreference(currentUsername, articleHeading, category, true);
    }

    @FXML
    private void handleDislike(ActionEvent event) {
        String articleHeading = label_Heading.getText();
        String category = label_Category.getText();
        recommendationAlgorithm.updateUserPreference(currentUsername, articleHeading, category, false);
    }

    @FXML
    private void handleSave(ActionEvent event) {
        String articleHeading = label_Heading.getText();
        recommendationAlgorithm.saveArticle(currentUsername, articleHeading);
    }
}
