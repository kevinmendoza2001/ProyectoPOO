module ProyectoPOO {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens app to javafx.fxml;
    opens controller to javafx.fxml;
    opens model to javafx.fxml;

    exports app;
}