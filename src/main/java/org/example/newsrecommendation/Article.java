package org.example.newsrecommendation;

public class Article {
    private String heading;
    private String date;
    private String category;

    public Article(String heading, String date, String category) {
        this.heading = heading;
        this.date = date;
        this.category = category;
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
