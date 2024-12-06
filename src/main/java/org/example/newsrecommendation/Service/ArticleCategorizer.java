package org.example.newsrecommendation.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticleCategorizer {

    private static final Map<String, List<String>> categoryKeywordsMap = new HashMap<>();

    static {
        categoryKeywordsMap.put("AI and Technology", List.of(
                "technology", "innovation", "automation", "cloud computing", "big data", "IoT", "blockchain",
                "quantum computing", "edge computing", "5G technology", "wearable tech", "artificial intelligence",
                "machine learning", "natural language processing", "neural networks", "deep learning",
                "reinforcement learning", "computer vision", "generative AI", "ethical AI", "explainable AI",
                "chatbots", "virtual reality", "augmented reality", "cyber security", "drones", "tech gadgets",
                "robotics", "biometrics", "tech startups", "data science", "smartphones", "programming languages",
                "app development", "cloud storage", "software updates", "tech innovation", "semiconductors",
                "computing power", "open-source technology", "smart devices", "automation tools", "IT industry",
                "digital transformation", "3D printing", "voice assistants", "nanotechnology", "self-driving cars",
                "technology policies"
        ));
        categoryKeywordsMap.put("Politics", List.of(
                "politics", "democracy", "election campaigns", "political parties", "policy reform",
                "international relations", "trade agreements", "geopolitics", "public policy", "political advocacy",
                "government legislation", "diplomacy", "political debates", "foreign policy", "election results",
                "political corruption", "state governance", "national security", "voter turnout",
                "parliamentary decisions", "political ideologies", "political scandals", "policy analysis",
                "campaign funding", "political rallies", "grassroots movements", "political polarization",
                "voting rights", "legislative bills", "executive orders", "government reforms", "political stability",
                "human rights", "political protests", "social justice", "economic policy", "constitutional rights",
                "public administration", "political transparency", "minority representation", "partisan politics",
                "political coalitions", "civic engagement", "policy debates", "federal government",
                "political milestones", "electoral boundaries", "peace treaties", "president", "prime minister",
                "political party", "government"
        ));
        categoryKeywordsMap.put("Healthcare", List.of(
                "healthcare", "telemedicine", "personalized medicine", "genomics", "public health",
                "health informatics", "medical devices", "vaccines", "health equity", "mental health",
                "doctor", "hospital", "medicine", "health", "disease", "therapy", "pandemic", "epidemic",
                "nutrition", "fitness", "health insurance", "patient care", "medical research", "surgical procedures",
                "pharmaceuticals", "biotechnology", "health technology", "wellness", "public safety", "disease prevention",
                "clinical trials", "drug development", "diagnostics", "emergency medicine", "medical ethics",
                "community health", "infectious diseases", "occupational health", "chronic diseases",
                "health awareness", "therapeutic innovations", "mental wellness", "alternative medicine",
                "health systems", "elderly care", "child health", "health policies", "global health", "virus", "bacteria",
                "fungi", "brain", "heart", "organs", "mental", "caregiving"
        ));
        categoryKeywordsMap.put("Entertainment", List.of(
                "entertainment", "streaming platforms", "box office", "celebrity news", "music trends",
                "movie releases", "gaming industry", "social media influencers", "pop culture", "award shows",
                "indie films", "television shows", "cinema", "binge-watching", "music albums", "video games",
                "entertainment news", "reality TV", "entertainment apps", "Hollywood", "Bollywood", "music videos",
                "live performances", "stage plays", "dance trends", "comedy shows", "entertainment technology",
                "film festivals", "online gaming", "animation movies", "streaming wars", "web series",
                "entertainment marketing", "movie reviews", "celebrity interviews", "concert tours",
                "music streaming", "fan culture", "entertainment events", "media production", "blockbusters",
                "entertainment deals", "fashion shows", "music awards", "celebrity endorsements", "entertainment podcasts",
                "art exhibitions", "visual effects", "documentaries", "independent music"
        ));
        categoryKeywordsMap.put("Science", List.of(
                "science", "astronomy", "physics", "biology", "chemistry", "geology", "astrophysics",
                "genetic engineering", "climate science", "nanotechnology", "renewable energy",
                "scientific research", "environmental science", "scientific discovery", "space exploration",
                "marine biology", "paleontology", "scientific experiments", "research papers", "DNA sequencing",
                "planetary science", "robotics in science", "scientific breakthroughs", "forensic science",
                "meteorology", "ecology", "evolutionary biology", "scientific journals", "cosmology", "quantum physics",
                "nuclear science", "biochemistry", "materials science", "earth science", "oceanography",
                "theoretical physics", "energy science", "space missions", "geophysics", "computational science",
                "science communication", "microscopy", "bioinformatics", "environmental sustainability",
                "scientific ethics", "planetary geology", "anthropology", "scientific innovation"
        ));
        categoryKeywordsMap.put("Sports", List.of(
                "sports", "football", "basketball", "tennis", "cricket", "athletics", "esports",
                "olympics", "world championships", "fitness training", "tournament", "team sports", "soccer",
                "rugby", "volleyball", "baseball", "badminton", "swimming", "cycling", "skiing", "snowboarding",
                "golf", "hockey", "gymnastics", "karate", "martial arts", "powerlifting", "skateboarding",
                "yoga", "sports news", "sports analysis", "sports events", "sports management", "athlete profiles",
                "training camps", "sports technology", "sports injuries", "sports sponsorship", "sports psychology",
                "sports medicine", "race events", "stadiums", "sports history", "sports data", "cheerleading",
                "fan culture", "player transfers", "coaching techniques", "match reviews", "sports broadcasts",
                "world cup", "cricketer", "netball", "diving", "water polo", "games"
        ));
        categoryKeywordsMap.put("Finance", List.of(
                "business", "startups", "entrepreneurship", "market trends", "corporate governance",
                "financial analysis", "e-commerce", "supply chain", "investment strategies", "marketing analytics",
                "mergers and acquisitions", "stock", "oil", "market", "economic growth", "retail",
                "business strategy", "real estate", "small businesses", "business funding", "management",
                "consulting", "business operations", "corporate culture", "leadership", "business news",
                "economic policies", "venture capital", "angel investors", "business networking", "B2B",
                "B2C", "customer retention", "sales strategies", "franchises", "profit margins",
                "market research", "business ethics", "business regulations", "corporate social responsibility",
                "business schools", "workplace culture", "business automation", "business intelligence",
                "logistics", "brand management", "business contracts", "outsourcing"
        ));
        categoryKeywordsMap.put("World", List.of(
                "global news","international news", "breaking news","world headlines","current events","world affairs",
                "political news", "global economy", "world politics","international relations","economic news", "global health",
                "natural disasters", "climate change", "technology news", "international conflicts", "human rights",
                "geopolitical updates", "global security", "international trade", "world leaders", "global diplomacy",
                "immigration", "foreign policy", "international organizations", "peacekeeping", "humanitarian crises",
                "war and conflict", "global development", "global partnerships", "sustainable development", "international cooperation",
                "world health initiatives", "international law", "global education", "international tourism", "world sports", "global media",
                "global innovation", "world environment", "international economy", "cross-border trade"
        ));
    }


    // Method to categorize article based on the content
    public static String categorizeArticle(String articleText) {
        int maxMatchCount = 0;
        String bestCategory = "Uncategorized";

        String lowerCaseArticleText = articleText.toLowerCase();

        for (Map.Entry<String, List<String>> entry : categoryKeywordsMap.entrySet()) {
            String category = entry.getKey();
            List<String> keywords = entry.getValue();
            int matchCount = 0;

            for (String keyword : keywords) {
                if (lowerCaseArticleText.contains(keyword.toLowerCase())) {
                    matchCount++;
                }
            }

            if (matchCount > maxMatchCount) {
                maxMatchCount = matchCount;
                bestCategory = category;
            }
        }

        return bestCategory;
    }
}