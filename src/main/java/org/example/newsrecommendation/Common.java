package org.example.newsrecommendation;

public class Common {
    private String name;
    private String username;
    private String email;
    private int age;
    private String gender;
    private String password;

    public Common(String name, String username, String email, int age, String gender, String password) {
        this.name = name;
        this.username =username;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.password = password;
    }

    public Common(String name, String email, int age, String gender, String password) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
