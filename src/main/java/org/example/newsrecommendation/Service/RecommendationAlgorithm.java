package org.example.newsrecommendation.Service;

import org.bson.Document;
import org.example.newsrecommendation.Model.Article;
import org.example.newsrecommendation.DataBase.DatabaseHandler;

import java.util.*;

public class RecommendationAlgorithm {

    public List<Article> fetchArticlesBasedOnPoints(String username) {
        List<Article> articles = new ArrayList<>();
        try (DatabaseHandler dbHandler = new DatabaseHandler()) {
            Document userPointsDoc = dbHandler.findDocument("Preferences", new Document("username", username));
            if (userPointsDoc == null) {
                System.out.println("No points found for user: " + username);
                return articles;
            }

            Map<String, Integer> categoryPoints = new HashMap<>();
            int totalPoints = 0;

            for (String key : userPointsDoc.keySet()) {
                if (!key.equals("_id") && !key.equals("username")) {
                    int points = userPointsDoc.getInteger(key, 0);
                    categoryPoints.put(key, points);
                    totalPoints += points;
                }
            }

            if (totalPoints == 0) {
                System.out.println("No points assigned to any category for user: " + username);
                return articles;
            }

            Map<String, Integer> categoryArticleQuota = new HashMap<>();
            int totalQuota = 40;
            for (Map.Entry<String, Integer> entry : categoryPoints.entrySet()) {
                int quota = Math.round((float) entry.getValue() / totalPoints * totalQuota);
                categoryArticleQuota.put(entry.getKey(), quota);
            }

            for (Map.Entry<String, Integer> entry : categoryArticleQuota.entrySet()) {
                String category = entry.getKey();
                int quota = entry.getValue();

                if (quota > 0) {
                    List<Document> categoryArticles = dbHandler.findDocuments("Article", new Document("category", category));

                    Collections.shuffle(categoryArticles);
                    for (int i = 0; i < Math.min(quota, categoryArticles.size()); i++) {
                        Document doc = categoryArticles.get(i);
                        String heading = doc.getString("heading");
                        String date = doc.getString("date");
                        Article article = new Article(heading, date, category, categoryPoints.get(category));
                        articles.add(article);
                    }
                }
            }

            Collections.shuffle(articles);

            if (articles.size() > 20) {
                articles = articles.subList(0, 20);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return articles;
    }

    public void updateUserPreference(String username, String articleHeading, String category, boolean isLike) {
        try (DatabaseHandler dbHandler = new DatabaseHandler()) {
            Document userDoc = dbHandler.findDocument("Interaction", new Document("username", username));
            if (userDoc == null) {
                userDoc = new Document("username", username)
                        .append("liked", List.of())
                        .append("disliked", List.of())
                        .append("save", List.of());
                dbHandler.insertDocument("Interaction", userDoc);
            }

            String addToList = isLike ? "liked" : "disliked";
            String removeFromList = isLike ? "disliked" : "liked";

            List<String> addList = userDoc.getList(addToList, String.class);
            List<String> removeList = userDoc.getList(removeFromList, String.class);

            if (!addList.contains(articleHeading)) {
                addList.add(articleHeading);
                removeList.remove(articleHeading);

                dbHandler.updateDocument(
                        "Interaction",
                        new Document("username", username),
                        new Document("$set", new Document(addToList, addList).append(removeFromList, removeList))
                );

                Document categoryDoc = dbHandler.findDocument("Preferences", new Document("username", username));
                if (categoryDoc == null) {
                    categoryDoc = new Document("username", username);
                    for (String cat : new String[]{"Entertainment", "Healthcare", "Politics", "Finance", "Technology", "Science", "Sports", "World"}) {
                        categoryDoc.append(cat, 0);
                    }
                    dbHandler.insertDocument("Preferences", categoryDoc);
                }

                int currentPoints = categoryDoc.getInteger(category, 0);
                int updatedPoints = isLike ? currentPoints + 3 : Math.max(currentPoints - 3, 0);

                dbHandler.updateDocument(
                        "Preferences",
                        new Document("username", username),
                        new Document("$set", new Document(category, updatedPoints))
                );
            }
        }
    }

    public void saveArticle(String username, String articleHeading) {
        try (DatabaseHandler dbHandler = new DatabaseHandler()) {
            Document userDoc = dbHandler.findDocument("Interaction", new Document("username", username));
            if (userDoc == null) {
                userDoc = new Document("username", username)
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
                            new Document("username", username),
                            new Document("$set", new Document("save", savedList))
                    );
                }
            }
        }
    }
}
