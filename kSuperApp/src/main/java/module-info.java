module com.example.ksuperapp {
    requires javafx.controls;
    requires javafx.fxml;



    requires java.management;
    //requires java.desktop;
    requires jdk.accessibility;
    requires java.logging;

    opens com.example.ksuperapp to javafx.fxml;
    exports com.example.ksuperapp;
    opens com.example.ksuperapp.appfunc.mainMenu to javafx.fxml;
    exports com.example.ksuperapp.appfunc.mainMenu;

    exports com.example.ksuperapp.appfunc;
    opens com.example.ksuperapp.appfunc to javafx.fxml;
}