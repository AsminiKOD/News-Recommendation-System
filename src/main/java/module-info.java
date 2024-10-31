module org.example.newsrecommendation {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.newsrecommendation to javafx.fxml;
    exports org.example.newsrecommendation;
}