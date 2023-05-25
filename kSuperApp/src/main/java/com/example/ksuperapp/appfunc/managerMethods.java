package com.example.ksuperapp.appfunc;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public class managerMethods {

    String lastCopied;

    Path root;


    private File protect(File source){

        if(!Objects.isNull(source) && (
                source.toString().startsWith(new File(root + "/System").toString())
                || source.toString().startsWith(new File(root + "/Trash can").toString()))
                || source.toString().endsWith(String.valueOf(root))){
            throw new RuntimeException("Can't perform task System Folder");
        }
        else return source;
    }

    public void setRoot(Path root){
        this.root = root;
    }

    public boolean moveFile(File source, File dest){
        return protect(source).renameTo(new File(dest.toString() + "/" + source.getName()));
    }

    public boolean deleteFile(File source){
        try {
            return moveFile(protect(source), new File(root + "/Trash can/"));
        }catch (RuntimeException e){
            alert("Нельзя удалить");
            return false;
        }

    }

    public boolean permDeleteFile(File source){
        try {
            return protect(source).delete();
        }catch (RuntimeException e){
            alert("Нельзя удалить");
            return false;
        }

    }
    public boolean createFile(File source){
        FieldDialog dialog = new FieldDialog("File", source);
        File file = new File(source.toString()+ "/" + dialog.getResult());
        try {
            return file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String makeDir(File source){
        FieldDialog dialog = new FieldDialog("Folder", source);
        try {
            File newDir = new File(protect(source).toString() + "/" + dialog.getResult());
            if(newDir.mkdir())
                return newDir.getName();
            else throw new RuntimeException("Fail to create Folder");
        }catch (Exception e) {
            alert("Нельзя создать папку в этом месте");
            return null;
        }

    }

    public String renameFile(File source){

        FieldDialog dialog = new FieldDialog("Rename", source);
        String newName;
        if(!Objects.isNull(newName = dialog.getResult())){
            try {
                if(protect(source).renameTo(new File(source.getParent() + "/" + newName)))
                    return newName;

            }catch (RuntimeException e){
                alert("Нельзя переименовать");
                return null;
            }

        }
        return null;
    }

    public boolean copy(File source){
        try {
            return Copy.CopyToBuffer(protect(source).toPath(), false);
        }catch (Exception e){
            alert("Нельзя скопировать");
            return false;
        }
    }
    public boolean cut(File source){
        try {
            return Copy.CopyToBuffer(protect(source).toPath(), true);
        }catch (Exception e){
            alert("Нельзя вырезать");
            return false;
        }

    }
    public void paste(File dest){
        try {
            new Thread(new Copy(protect(dest).toPath())).start();
        }catch (Exception e){
            alert("Нельзя вставить");
        }

    }
    void alert(String msg){
        Alert alert=new Alert(Alert.AlertType.ERROR,msg, ButtonType.OK);
        alert.showAndWait();
    }
    public String Dialog(String windowName){
        FieldDialog dialog = new FieldDialog(windowName, null);
        return dialog.getResult();
    }

}
class FieldDialog {
    String result;
    public FieldDialog(String title, File selFile) {
        Stage dialog = new Stage();
        dialog.setTitle(title);

        TextField textField = new TextField();
        if(!Objects.isNull(selFile))
            textField.setText(selFile.getName());

        Button ok = new Button("OK");
        Button cancel = new Button("Cancel");
        HBox hBox = new HBox(ok, cancel);
        hBox.setSpacing(10);
        VBox vBox = new VBox(textField, hBox);
        vBox.setAlignment(Pos.CENTER);
        hBox.setAlignment(Pos.CENTER);

        Scene dialogScene = new Scene(vBox, 250, 50);


        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                result = textField.getText();
                dialog.close();
            }
        });
        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    public String getResult(){
        return result;
    }
}
