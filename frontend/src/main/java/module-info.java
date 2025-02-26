module com.pal.frontend {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive java.net.http;

    requires transitive org.json;
    requires transitive org.slf4j;
    requires transitive java.rmi;
    requires transitive com.fasterxml.jackson.datatype.jsr310;
    requires transitive com.fasterxml.jackson.databind;
    requires transitive spring.context;

    // Open only the necessary packages for reflection (JavaFX, Jackson, etc.)
    opens com.pal.ui to javafx.fxml;
    opens com.pal.ui.model to javafx.fxml;
    opens com.pal.ui.sprite to javafx.fxml;
    opens com.pal.ui.api to javafx.fxml;

    // Exported packages for use by other modules
    exports com.pal.ui;
    exports com.pal.ui.model;
    exports com.pal.ui.sprite;
    exports com.pal.ui.api;
    exports com.pal.ui.character;

}
