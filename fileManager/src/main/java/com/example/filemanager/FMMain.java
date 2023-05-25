package com.example.filemanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class FMMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        Parameters args = getParameters();
        FXMLLoader fxmlLoader = new FXMLLoader(FMMain.class.getResource("views/FM-view.fxml"));
        String path = FMMain.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        Scene scene = new Scene(fxmlLoader.load());
        FMController controller = fxmlLoader.getController();
        if(!args.getRaw().isEmpty()){
            controller.setRoot(args.getRaw().get(0));
            System.out.println(args.getRaw().get(0));
        } else controller.setRoot(new File(URLDecoder.decode(path, StandardCharsets.UTF_8)).toPath().getParent().getParent().toString());



        System.out.println(path);

        stage.setTitle("File Manager");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
