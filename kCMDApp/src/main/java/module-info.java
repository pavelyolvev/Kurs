module com.example.kcmdapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.sun.jna;
    requires org.apache.commons.io;


    opens com.example.kcmdapp to javafx.fxml;
    exports com.example.kcmdapp;
}