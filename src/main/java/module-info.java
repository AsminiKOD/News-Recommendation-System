module org.example.newsrecommendation {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.core;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires commons.csv;


    opens org.example.newsrecommendation to javafx.fxml;
    exports org.example.newsrecommendation.App;
    opens org.example.newsrecommendation.App to javafx.fxml;
    exports org.example.newsrecommendation.Model;
    opens org.example.newsrecommendation.Model to javafx.fxml;
}