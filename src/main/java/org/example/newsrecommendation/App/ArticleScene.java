package org.example.newsrecommendation.App;

import javafx.event.ActionEvent;
import org.bson.Document;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.newsrecommendation.Article;
import org.example.newsrecommendation.DataBase.DatabaseHandler;

import java.util.List;

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
    private DatabaseHandler dbHandler;

    public ArticleScene() {
        dbHandler = new DatabaseHandler();
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
        updatePreference("liked", "disliked", true);
    }

    @FXML
    private void handleDislike(ActionEvent event) {
        updatePreference("disliked", "liked", false);
    }

    private void updatePreference(String addToList, String removeFromList, boolean isLike) {
        String articleHeading = label_Heading.getText();
        String category = label_Category.getText();

        // Find the user document from "Interaction" collection
        Document userDoc = dbHandler.findDocument("Interaction", new Document("username", currentUsername));
        if (userDoc == null) {
            userDoc = new Document("username", currentUsername)
                    .append("liked", List.of())
                    .append("disliked", List.of())
                    .append("save", List.of());
            dbHandler.insertDocument("Interaction", userDoc);
        }

        List<String> addList = userDoc.getList(addToList, String.class);
        List<String> removeList = userDoc.getList(removeFromList, String.class);

        if (!addList.contains(articleHeading)) {
            addList.add(articleHeading);
            removeList.remove(articleHeading);

            // Update user document in "Interaction" collection
            dbHandler.updateDocument(
                    "Interaction",
                    new Document("username", currentUsername),
                    new Document("$set", new Document(addToList, addList).append(removeFromList, removeList))
            );

            // Update category points in "Preferences" collection
            Document categoryDoc = dbHandler.findDocument("Preferences", new Document("username", currentUsername));
            if (categoryDoc == null) {
                categoryDoc = new Document("username", currentUsername);
                for (String cat : new String[]{"Entertainment", "Healthcare", "Politics", "Finance", "Technology", "Science", "Sports", "World"}) {
                    categoryDoc.append(cat, 0);
                }
                dbHandler.insertDocument("Preferences", categoryDoc);
            }

            int currentPoints = categoryDoc.getInteger(category, 0);
            int updatedPoints = isLike ? currentPoints + 3 : Math.max(currentPoints - 3, 0);

            dbHandler.updateDocument(
                    "Preferences",
                    new Document("username", currentUsername),
                    new Document("$set", new Document(category, updatedPoints))
            );
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        String articleHeading = label_Heading.getText();

        // Find the user document from "Interaction" collection
        Document userDoc = dbHandler.findDocument("Interaction", new Document("username", currentUsername));
        if (userDoc == null) {
            userDoc = new Document("username", currentUsername)
                    .append("liked", List.of())
                    .append("disliked", List.of())
                    .append("save", List.of(articleHeading));
            dbHandler.insertDocument("Interaction", userDoc);
        } else {
            List<String> savedList = userDoc.getList("save", String.class);
            if (!savedList.contains(articleHeading)) {
                savedList.add(articleHeading);
                dbHandler.updateDocument(
                        "Interaction",
                        new Document("username", currentUsername),
                        new Document("$set", new Document("save", savedList))
                );
            }
        }
    }
}
