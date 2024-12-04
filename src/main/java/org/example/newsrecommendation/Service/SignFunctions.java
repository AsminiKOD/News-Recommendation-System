package org.example.newsrecommendation.Service;

import javafx.scene.control.Alert;
import org.bson.Document;
import org.example.newsrecommendation.DataBase.DatabaseHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SignFunctions {
    private DatabaseHandler dbHandler;

    public SignFunctions() {
        try {
            dbHandler = new DatabaseHandler();
        } catch (Exception e) {
            e.printStackTrace();
            MainLogics.Alert(Alert.AlertType.ERROR, "Database Connection Error", "Could not connect to MongoDB.");
        }
    }

    public boolean checkCredentials(String username, String password) {
        try {
            Document query = new Document("username", username).append("password", password);
            Document user = dbHandler.findDocument("User", query);
            return user != null;
        } catch (Exception e) {
            e.printStackTrace();
            MainLogics.Alert(Alert.AlertType.ERROR, "Login Error", "An error occurred while checking credentials.");
            return false;
        }
    }

    public boolean checkCredentialsAdmin(String username, String password, String adminID) {
        try {
            Document query = new Document("username", username).append("password", password).append("adminId", adminID);
            Document admin = dbHandler.findDocument("Admin", query);
            return admin != null;
        } catch (Exception e) {
            e.printStackTrace();
            MainLogics.Alert(Alert.AlertType.ERROR, "Login Error", "An error occurred while checking credentials.");
            return false;
        }
    }

    public void saveLoginDetails(String username) {
        try {
            Document loginRecord = new Document("username", username).append("login_time", LocalDateTime.now().toString());
            dbHandler.insertDocument("User_Login", loginRecord);
        } catch (Exception e) {
            e.printStackTrace();
            MainLogics.Alert(Alert.AlertType.ERROR, "Database Error", "Could not save login details.");
        }
    }

    public void saveLoginDetailsAdmin(String adminID) {
        try {
            Document loginRecord = new Document("adminId", adminID).append("login_time", LocalDateTime.now().toString());
            dbHandler.insertDocument("Admin_Login", loginRecord);
        } catch (Exception e) {
            e.printStackTrace();
            MainLogics.Alert(Alert.AlertType.ERROR, "Database Error", "Could not save login details.");
        }
    }

    public String formatName(String name) {
        if (name.isEmpty()) return "";
        String[] nameParts = name.split(" ");
        for (int i = 0; i < nameParts.length; i++) {
            nameParts[i] = nameParts[i].substring(0, 1).toUpperCase() + nameParts[i].substring(1).toLowerCase();
        }
        return String.join(" ", nameParts);
    }

    public List<String> validateInputs(String name, String email, String ageText, String username, String password,
                                       String confirmPassword, String gender, List<String> preferences) {
        List<String> errors = new ArrayList<>();

        if (name.isEmpty()) errors.add("Name is required.");
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) errors.add("Please enter a valid email address.");
        try {
            int age = Integer.parseInt(ageText);
            if (age <= 0 || age > 120) errors.add("Please enter a valid age.");
        } catch (NumberFormatException e) {
            errors.add("Age must be a valid number.");
        }
        if (username.isEmpty() || username.length() < 3) errors.add("Username must be at least 3 characters long.");
        if (isUsernameTaken(username)) errors.add("Username is already taken. Please choose a different one.");
        if (password.isEmpty() || password.length() < 6) errors.add("Password must be at least 6 characters long.");
        if (!password.equals(confirmPassword)) errors.add("Passwords do not match.");
        if (gender == null || gender.isEmpty()) errors.add("Please select a gender.");
        if (preferences.isEmpty()) errors.add("Please select at least one preference.");

        return errors;
    }

    public boolean isUsernameTaken(String username) {
        Document query = new Document("username", username);
        return dbHandler.findDocument("User", query) != null;
    }

    public void saveUserData(String name, String email, int age, String username, String password, String gender,
                             List<String> preferences) {
        try {
            Document userDoc = new Document("name", name)
                    .append("email", email)
                    .append("age", age)
                    .append("gender", gender)
                    .append("preferences", preferences)
                    .append("username", username)
                    .append("password", password);

            dbHandler.insertDocument("User", userDoc);

            Document preferenceDoc = new Document("username", username)
                    .append("Entertainment", preferences.contains("Entertainment") ? 5 : 0)
                    .append("Healthcare", preferences.contains("Healthcare") ? 5 : 0)
                    .append("Politics", preferences.contains("Politics") ? 5 : 0)
                    .append("Finance", preferences.contains("Finance") ? 5 : 0)
                    .append("Technology", preferences.contains("Technology") ? 5 : 0)
                    .append("Science", preferences.contains("Science") ? 5 : 0)
                    .append("Sports", preferences.contains("Sports") ? 5 : 0)
                    .append("World", preferences.contains("World") ? 5 : 0);

            dbHandler.insertDocument("Preferences", preferenceDoc);
        } catch (Exception e) {
            e.printStackTrace();
            MainLogics.Alert(Alert.AlertType.ERROR, "Database Error", "Error saving user data: " + e.getMessage());
        }
    }
}
