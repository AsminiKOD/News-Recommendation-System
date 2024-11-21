package org.example.newsrecommendation;

public class LoginHistory {
    private String date;
    private String time;

    public LoginHistory(String date, String time) {
        this.date = date;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
