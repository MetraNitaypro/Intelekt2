module com.example.skynet {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;

    opens com.example.skynet to javafx.fxml;
    exports com.example.skynet;
}