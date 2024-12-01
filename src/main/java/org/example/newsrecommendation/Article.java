package org.example.newsrecommendation;

public class Article {
    private String heading;
    private String date;
    private String category;
    private String body;
    private int points;

    public Article(String heading, String date, String category) {
        this.heading = heading;
        this.date = date;
        this.category = category;
    }

    public Article(String heading, String date, String category, int points) {
        this.heading = heading;
        this.date = date;
        this.category = category;
        this.points = points;
    }

    public String getHeading() {
        return heading;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }
}
