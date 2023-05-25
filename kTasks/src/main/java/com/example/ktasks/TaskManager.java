package com.example.ktasks;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TaskManager extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TaskManager.class.getResource("view/TM-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Task Manager");
        stage.setScene(scene);
        stage.show();
        TMController controller = fxmlLoader.getController();
        controller.setStage(stage);
    }

    @Override
    public void stop(){
        System.out.println("Stage is closing");
    }

    public static void main(String[] args) {
        launch();
    }
}