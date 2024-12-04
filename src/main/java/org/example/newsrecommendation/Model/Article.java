package org.example.newsrecommendation.Model;

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

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
