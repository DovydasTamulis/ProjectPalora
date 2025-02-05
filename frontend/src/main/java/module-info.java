module com.pal.frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;
    requires java.net.http;

    opens com.pal.ui to javafx.fxml;
    exports com.pal.ui;
}
