package com.example.ksuperapp;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.example.ksuperapp.appfunc.managerMethods;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Controller {
    @FXML
    Scene scene;
    @FXML
    private BorderPane VboxRoot;
    @FXML
    private Button btnCmd;
    @FXML
    private Button btnFlleMngr;
    @FXML
    private Button btnTasks;
    @FXML
    private Button btnTerminal;
    @FXML
    private TextArea txtAreaLogs;
    File appPath = new File(SuperApp.class.getProtectionDomain().getCodeSource().getLocation().getPath());

    public Controller() {
    }

    @FXML
    void initialize() {
        this.scene = this.VboxRoot.getScene();
        SharedMemory.deleteFile();
        SharedMemory.addToPidList(ProcessHandle.current().pid());
    }

    @FXML
    void startAbout(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SuperApp.class.getResource("views/about-view.fxml"));
        Scene about = new Scene((Parent)fxmlLoader.load());
        Stage stageAbout = new Stage();
        stageAbout.setTitle("Super App");
        stageAbout.setScene(about);
        stageAbout.setResizable(false);
        stageAbout.show();
    }

    @FXML
    void openCmd(ActionEvent event) throws IOException {
        File jarPath = new File(appPath.getParent() + "/kCMDApp.jar");
        //////////////////////////////////////////////////////////////////////////
        ProcessBuilder cmd = new ProcessBuilder("java", "--module-path", new File(appPath.getParent() + "/lib/").toString(), "--add-modules", "javafx.controls,javafx.fxml","-jar", jarPath.toString());
        Process process = cmd.start();

        SharedMemory.addToPidList(process.pid());
        txtAreaLogs.appendText("[" + LocalDateTime.now() + "] [" + ProcessHandle.current().pid() + " Super App] execute Super App CMD\n");
    }

    @FXML
    void SaveToLog(ActionEvent event) throws IOException {
        final Logger LOGGER = Logger.getLogger(SuperApp.class.getName());
        managerMethods methods = new managerMethods();
        String logNm = methods.Dialog("Log name");
        FileHandler log = new FileHandler(logNm+".log");
        SimpleFormatter formatter = new SimpleFormatter();
        log.setFormatter(formatter);

        LOGGER.addHandler(log);
        LOGGER.log(Level.INFO, txtAreaLogs.getText());
    }
    @FXML
    void openFM(ActionEvent event) throws IOException, URISyntaxException {

        File jarPath = new File(appPath.getParent() + "/fileManager.jar");
        //////////////////////////////////////////////////////////////////////////
        ProcessBuilder FM = new ProcessBuilder("java", "--module-path", new File(appPath.getParent() + "/lib/").toString(), "--add-modules", "javafx.controls,javafx.fxml","-jar", jarPath.toString());
        Process process = FM.start();

        SharedMemory.addToPidList(process.pid());

        txtAreaLogs.appendText("[" + LocalDateTime.now() + "] [" + ProcessHandle.current().pid() + " Super App] execute File Manager with pid " + process.pid() + "\n");
    }
    @FXML
    void openFMFlash(ActionEvent event) throws IOException, URISyntaxException {

        String pathTodrives = "/media/" + System.getProperty("user.name")+"/";
        File jarPath = new File(appPath.getParent() + "/fileManager.jar");
        //////////////////////////////////////////////////////////////////////////
        ProcessBuilder FM = new ProcessBuilder("java", "--module-path", new File(appPath.getParent() + "/lib/").toString(), "--add-modules", "javafx.controls,javafx.fxml","-jar", jarPath.toString(), pathTodrives);
        Process process = FM.start();

        SharedMemory.addToPidList(process.pid());

        txtAreaLogs.appendText("[" + LocalDateTime.now() + "] [" + ProcessHandle.current().pid() + " Super App] execute File Manager with pid " + process.pid() + "\n");
    }

    @FXML
    void openTasks(ActionEvent event) throws IOException {


        File jarPath = new File(appPath.getParent() + "/kTasks.jar");
        //////////////////////////////////////////////////////////////////////////
        ProcessBuilder tasks = new ProcessBuilder("java", "--module-path", new File(appPath.getParent() + "/lib/").toString(), "--add-modules", "javafx.controls,javafx.fxml","-jar", jarPath.toString());
        Process process = tasks.start();

        SharedMemory.addToPidList(process.pid());

        try {
            (new SharedMemory()).share();
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }

        txtAreaLogs.appendText("[" + LocalDateTime.now() + "] [" + ProcessHandle.current().pid() + " Super App] execute Tasks with pid "+process.pid()+"\n");


    }

    @FXML
    void openTerminal(ActionEvent event) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("x-terminal-emulator");
        Process process = pb.start();
        txtAreaLogs.appendText("[" + LocalDateTime.now() + "] [" + ProcessHandle.current().pid() + " Super App] execute Terminal with pid " + process.pid() + "\n");
    }

    @FXML
    void openTop(ActionEvent event) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("x-terminal-emulator", "-e", "top");
        Process process = pb.start();
        txtAreaLogs.appendText("[" + LocalDateTime.now() + "] [" + ProcessHandle.current().pid() + " Super App] execute Top with pid " + process.pid() + "\n");
    }
    @FXML
    void openDisks(ActionEvent event) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("gnome-disks");
        Process process = pb.start();
        txtAreaLogs.appendText("[" + LocalDateTime.now() + "] [" + ProcessHandle.current().pid() + " Super App] execute Disks with pid " + process.pid() + "\n");
    }
    @FXML
    void openCalc(ActionEvent event) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("gnome-calculator");
        Process process = pb.start();
        txtAreaLogs.appendText("[" + LocalDateTime.now() + "] [" + ProcessHandle.current().pid() + " Super App] execute Calculator with pid " + process.pid() + "\n");
    }
    @FXML
    void openSysMon(ActionEvent event) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("gnome-system-monitor");
        Process process = pb.start();
        txtAreaLogs.appendText("[" + LocalDateTime.now() + "] [" + ProcessHandle.current().pid() + " Super App] execute System monitor with pid " + process.pid() + "\n");
    }
    @FXML
    void openTxtEditor(ActionEvent event) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("gnome-text-editor");
        Process process = pb.start();
        txtAreaLogs.appendText("[" + LocalDateTime.now() + "] [" + ProcessHandle.current().pid() + " Super App] execute Text Editor with pid " + process.pid() + "\n");
    }
}

