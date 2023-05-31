
package com.example.ktasks;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class TMController {

    ProcessInfo selProc, savedProc;
    @FXML
    private Label lblCompName;

    @FXML
    private Label lblElapTime;

    @FXML
    private Label lblThCount;

    @FXML
    private TableView<ProcessInfo> tableview;
    @FXML
    private TableColumn<ProcessInfo, String> clmName;

    @FXML
    private TableColumn<ProcessInfo, String> clmPid;

    @FXML
    private TableColumn<ProcessInfo, String> clmStTime;
    private static final Logger LOGGER = Logger.getLogger(TaskManager.class.getName());

    int ThCount=0;
    SharedMemory sharedMemory = new SharedMemory();
    ObservableList<ProcessInfo> pidList;
    FileHandler fileHandler;

    @FXML
    void OnClickSaveToFile(ActionEvent event) {
            fileHandler.close();
    }

    public void setStage(Stage stage){
        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
    }
    @FXML
    void initialize() throws IOException {
        fileHandler = new FileHandler("app.log");
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);

        LOGGER.addHandler(fileHandler);

        tableview.setRowFactory(tv ->{
            TableRow<ProcessInfo> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                selProc = row.getItem();
            });

            return row;
        });

        try
        {
            lblCompName.setText("Computer name: "+ InetAddress.getLocalHost().getHostName());
            LOGGER.log(Level.INFO, lblCompName.getText());
        }
        catch (UnknownHostException ex)
        {
            System.out.println("Hostname can not be resolved");
        }

        Thread mainThread = Thread.currentThread();
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Runnable updater = new Runnable() {
                    @Override
                    public void run() {
                        update();
                    }
                };

                while (!mainThread.isInterrupted()) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                    Platform.runLater(updater);
                }
            }

        });

        sharedMemory.openSharedMemory();
        pidList = FXCollections.observableList(sharedMemory.getSharedPID());

        thread.setDaemon(true);
        thread.start();

        tableview.setItems(pidList);
        clmPid.setCellValueFactory(new PropertyValueFactory<>("Pid"));
        clmName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        clmStTime.setCellValueFactory(new PropertyValueFactory<>("StartTime"));
    }

    void update(){
            try {
                if(!Objects.isNull(selProc)){
                    savedProc = selProc;
                    Duration duration = Duration.ofMillis(Instant.now().toEpochMilli() - selProc.getStTimeInMsec());

                    long hours = duration.toHours();
                    long minutes = duration.toMinutes() % 60;
                    long seconds = duration.getSeconds() % 60;
                    long milliseconds = duration.toMillis() % 1000;
                    lblElapTime.setText("Elapsed time of " + selProc.getPid() + ":\n" + hours +":"+ minutes+":"+ seconds+"."+ milliseconds);
                    LOGGER.log(Level.INFO, lblElapTime.getText());
                } else selProc = savedProc;

                pidList = FXCollections.observableList(sharedMemory.getSharedPID());

                ThCount = 0;
                pidList.forEach(processInfo -> {
                    ThCount +=processInfo.getAllThCount();
                });
                lblThCount.setText("Threads count: "+ThCount);
                LOGGER.log(Level.INFO, lblThCount.getText());

                tableview.setItems(pidList);
                tableview.refresh();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }
}