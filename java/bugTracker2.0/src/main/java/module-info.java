module com.example.bugtracker20 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
//    requires com.example.bugtracker20;

    opens com.example.bugtracker20 to javafx.fxml;
    exports com.example.bugtracker20;
}