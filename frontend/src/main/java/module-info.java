module com.pal.frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.net.http;

    requires org.json;
    requires org.slf4j;
    requires java.rmi;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;
    requires spring.context;
    opens com.pal.ui to javafx.fxml;
    exports com.pal.ui;
    exports com.pal.ui.model;
    opens com.pal.ui.model to javafx.fxml;
    exports com.pal.ui.sprite;
    opens com.pal.ui.sprite to javafx.fxml;
    exports com.pal.ui.api;
    opens com.pal.ui.api to javafx.fxml;
}
