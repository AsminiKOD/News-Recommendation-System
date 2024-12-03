package org.example.newsrecommendation.Service;

import javafx.scene.control.Alert;

public class MainLogics {

    public static void Alert(Alert.AlertType alertType, String heading, String msg) {
        Alert alert = new Alert(alertType);
        alert.setTitle(heading);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

}
