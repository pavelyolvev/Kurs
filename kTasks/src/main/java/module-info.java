module com.example.ktasks {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens com.example.ktasks to javafx.fxml;
    exports com.example.ktasks;
}