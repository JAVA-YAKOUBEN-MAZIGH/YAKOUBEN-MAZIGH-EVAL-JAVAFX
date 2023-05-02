module com.example.eval1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires gson;


    opens com.example.eval1 to javafx.fxml;
    exports com.example.eval1;
}