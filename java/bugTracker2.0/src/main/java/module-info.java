module com.example.bugtracker20 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires javafx.web;
    requires java.desktop;
    requires org.kordamp.ikonli.materialdesign;
    requires javafx.graphics;

    opens com.example.bugtracker20 to javafx.fxml;
    exports com.example.bugtracker20;
}