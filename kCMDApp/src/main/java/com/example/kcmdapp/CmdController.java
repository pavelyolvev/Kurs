package com.example.kcmdapp;

import com.example.kcmdapp.lib.JNAMethods;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.UnaryOperator;

public class CmdController extends Application{

    String line;
    final String cmdPointer = ">> ";
    TextField cmdField;
    TextArea txtAreaCmd;

    String command;

    @Override
    public void start(Stage stage) throws Exception {
        Stage cmdStage = new Stage();
        BorderPane pane = new BorderPane();
        txtAreaCmd = new TextArea();
        cmdField = setTextField(txtAreaCmd);
        txtAreaCmd.setEditable(false);

        pane.setCenter(txtAreaCmd);
        pane.setBottom(cmdField);

        cmdStage.setTitle("SuperApp cmd");
        Scene scene = new Scene(pane);
        cmdStage.setScene(scene);
        cmdStage.show();
    }

    TextField setTextField(TextArea cmd){

        TextField textField = new TextField();
        textField.setText(cmdPointer);
        textField.setOnAction(event -> {

            line = textField.getText();
            cmd.appendText(line+'\n');
            textField.setText(cmdPointer);
            textField.positionCaret(cmdPointer.length());
            command = new StringBuilder(line).delete(0, cmdPointer.length()).toString();
            commandReader(command);

        });
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.BACK_SPACE && !event.isShortcutDown() && textField.getText().length()<=cmdPointer.length()) {
                textField.setText(cmdPointer);
                textField.positionCaret(cmdPointer.length());
            }
        });
        return textField;
    }

    void commandReader(String command){
        String[] splCom = command.split(" ");
        switch (splCom[0]){
            case "getpid" -> txtAreaCmd.appendText(JNAMethods.Process.getPid() + "\n");
            case "kill" -> {
                if(Objects.equals(splCom[2], "2") || Objects.equals(splCom[2], "15")) //2 - SIGINT, 15 - SIGTERM. Args for soft process shutdown
                {
                    if(JNAMethods.Process.kill(Integer.parseInt(splCom[1]), Integer.parseInt(splCom[2])) == 0)
                        txtAreaCmd.appendText("process with pid " + splCom[1] + " was shutdown" + "\n");
                    else txtAreaCmd.appendText("Failed to shutdown process with pid " + splCom[1] + "\n");
                }
                if(Objects.equals(splCom[2], "9")) //9 - SIGKILL. Arg for kill process
                {
                    if(JNAMethods.Process.kill(Integer.parseInt(splCom[1]), Integer.parseInt(splCom[2])) == 0)
                        txtAreaCmd.appendText("process with pid " + splCom[1] + " was killed" + "\n");
                    else txtAreaCmd.appendText("Failed to kill process with pid " + splCom[1] + "\n");
                }
            }
            case "exec" -> {
                txtAreaCmd.appendText("executed " + splCom[1] + " with args " + Arrays.toString(Arrays.copyOfRange(splCom, 2, splCom.length)) + "\n");
                JNAMethods.Process.execvp(splCom[1], Arrays.copyOfRange(splCom, 2, splCom.length));
            }
            case "get_priority" -> {
                int priority;
                if((priority = JNAMethods.Process.getPriority(Integer.parseInt(splCom[1]), 0)) != -1)
                    txtAreaCmd.appendText("priority: " + priority + "\n");
                else txtAreaCmd.appendText("failed to get priority: " + "\n");
            }
            case "set_priority" -> {
                if(JNAMethods.Process.setPriority(Integer.parseInt(splCom[1]), 0, Integer.parseInt(splCom[2])) == 0)
                    txtAreaCmd.appendText("set priority: " + splCom[2] + "\n");
                else txtAreaCmd.appendText("failed to set priority." + "\n");
            }
            case "open" ->{
                int fd;
                if((fd = JNAMethods.IO.open(splCom[1])) != -1)
                    txtAreaCmd.appendText("file opened with fd " + fd + "\n");
                else txtAreaCmd.appendText("failed to open "+ splCom[1] + "\n");
            }
            case "close" ->{
                if(JNAMethods.IO.close(Integer.parseInt(splCom[1])) != -1)
                    txtAreaCmd.appendText("file closed" + "\n");
                else txtAreaCmd.appendText("failed to close " + splCom[1] + "\n");
            }
            case "read" ->{
                txtAreaCmd.appendText(JNAMethods.IO.read(Integer.parseInt(splCom[1])) + "\n");
            }
            case "write" ->{
                txtAreaCmd.appendText(JNAMethods.IO.write(Integer.parseInt(splCom[1]), splCom[2]) + "\n");
            }
            case "mkdir" ->{
                txtAreaCmd.appendText(JNAMethods.IO.mkdir(splCom[1]) + "\n");
            }
        }

    }


    public static void main(String[] args) {
        launch();
    }
}
