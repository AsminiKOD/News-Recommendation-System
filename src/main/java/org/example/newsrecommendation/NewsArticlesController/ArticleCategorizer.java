package org.example.newsrecommendation.NewsArticlesController;

import java.util.HashMap;
import java.util.Map;

public class ArticleCategorizer {

    private static final String[] ENTERTAINMENT_KEYWORDS = {
            "movie", "film", "actor", "actress", "celebrity", "show", "tv", "music", "album", "premiere", "pop", "hollywood"
    };
    private static final String[] AI_TECHNOLOGY_KEYWORDS = {
            "ai", "artificial intelligence", "machine learning", "technology", "robotics", "automation", "data science", "deep learning", "neural networks", "innovation", "cloud", "big data"
    };
    private static final String[] FINANCE_KEYWORDS = {
            "finance", "economy", "stock", "investment", "market", "bank", "trading", "currency", "cryptocurrency", "budget", "investment", "economics"
    };
    private static final String[] HEALTHCARE_KEYWORDS = {
            "healthcare", "medicine", "doctor", "hospital", "treatment", "disease", "patient", "clinic", "surgery", "doctor", "vaccine", "therapy", "health", "wellness"
    };
    private static final String[] POLITICS_KEYWORDS = {
            "politics", "government", "election", "vote", "president", "party", "senate", "congress", "policy", "legislation", "campaign", "leader", "democracy"
    };
    private static final String[] SCIENCE_KEYWORDS = {
            "science", "research", "experiment", "discovery", "scientific", "physics", "chemistry", "biology", "study", "laboratory", "quantum", "genetics", "space", "astronomy"
    };
    private static final String[] SPORTS_KEYWORDS = {
            "sports", "football", "basketball", "baseball", "tennis", "game", "match", "team", "player", "competition", "championship", "tournament", "goal", "score"
    };
    private static final String[] WORLD_KEYWORDS = {
            "world", "global", "international", "countries", "united nations", "foreign", "earth", "worldwide", "nation", "international relations", "world news"
    };

    // Method to categorize articles based on the content using keyword count
    public static String categorizeArticle(String content) {
        content = content.toLowerCase();

        // Map to store category names and their corresponding keyword match counts
        Map<String, Integer> categoryCounts = new HashMap<>();

        // Categorize based on matching keyword count
        categoryCounts.put("Entertainment", countKeywords(content, ENTERTAINMENT_KEYWORDS));
        categoryCounts.put("AI and Technology", countKeywords(content, AI_TECHNOLOGY_KEYWORDS));
        categoryCounts.put("Finance", countKeywords(content, FINANCE_KEYWORDS));
        categoryCounts.put("Healthcare", countKeywords(content, HEALTHCARE_KEYWORDS));
        categoryCounts.put("Politics", countKeywords(content, POLITICS_KEYWORDS));
        categoryCounts.put("Science", countKeywords(content, SCIENCE_KEYWORDS));
        categoryCounts.put("Sports", countKeywords(content, SPORTS_KEYWORDS));
        categoryCounts.put("World", countKeywords(content, WORLD_KEYWORDS));

        // Find the category with the highest keyword match count
        String bestCategory = "Uncategorized";
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                bestCategory = entry.getKey();
            }
        }

        return bestCategory;
    }

    // Helper method to count the number of matching keywords in the content for a given category
    private static int countKeywords(String content, String[] keywords) {
        int count = 0;
        for (String keyword : keywords) {
            if (content.contains(keyword)) {
                count++;
            }
        }
        return count;
    }
}
