package com.example.kcmdapp.lib;

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
import org.apache.commons.io.FileUtils;

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
    public boolean createFile(File source, String name){
        File file = new File(source.toString()+ "/" + name);
        try {
            return file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String makeDir(File source, String folderName){
        File newDir = new File(protect(source).toString() + "/" + folderName);
        if(newDir.mkdir())
            return newDir.getName();
        else throw new RuntimeException("Fail to create Folder");
    }

    public String renameFile(File source, String toName){

        if(!Objects.isNull(toName)){
            try {
                if(protect(source).renameTo(new File(source.getParent() + "/" + toName)))
                    return toName;

            }catch (RuntimeException e){
                alert("Нельзя переименовать");
                return null;
            }

        }
        return null;
    }
    public boolean copyFileTo(Path source, Path dest){
        Copy.CopyToBuffer(protect(source.toFile()).toPath(), false);
        return Copy.pasteFromBuffer(protect(dest.toFile()).toPath());

    }

    void alert(String msg){
        Alert alert=new Alert(Alert.AlertType.ERROR,msg, ButtonType.OK);
        alert.showAndWait();
    }

}
