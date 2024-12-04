package org.example.newsrecommendation.Model;

import java.util.List;

public class User extends Common {
    private final List<String> preference;

    public User(String name, String username, String email, int age, String gender, String password, List<String> preference) {
        super(name, username, email, age, gender, password);
        this.preference = preference;
    }

    public User(String name, String email, int age, String gender, String password, List<String> preference) {
        super(name, email, age, gender, password);
        this.preference = preference;
    }

    public User(String name, String email, int age, List<String> preference) {
        super(name, email, age);
        this.preference = preference;
    }

    public List<String> getPreference() {
        return preference;
    }



}
