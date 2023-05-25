module com.example.filemanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.apache.commons.io;
    requires java.logging;


    opens com.example.filemanager to javafx.fxml;
    exports com.example.filemanager;
    exports com.example.filemanager.appfunc;
    opens com.example.filemanager.appfunc to javafx.fxml;
}