module com.pal.frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;

    requires org.json;
    opens com.pal.ui to javafx.fxml;
    exports com.pal.ui;
    exports com.pal.ui.screen;
    opens com.pal.ui.screen to javafx.fxml;
    exports com.pal.ui.login;
    opens com.pal.ui.login to javafx.fxml;
    exports com.pal.ui.task;
    opens com.pal.ui.task to javafx.fxml;
    exports com.pal.ui.main;
    opens com.pal.ui.main to javafx.fxml;
}
