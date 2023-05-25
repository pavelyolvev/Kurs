package com.example.filemanager.appfunc;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import java.io.File;
import java.util.Objects;

public class ContextMenuFactory {

    File selectedFile;
    managerMethods methods = new managerMethods();
    ContextMenu menu = new ContextMenu();
    Menu itemCreate = new Menu("Create");
    MenuItem itemFolder = new MenuItem("Folder");
    MenuItem itemFile = new MenuItem("File");
    MenuItem itemRename = new MenuItem("Rename");
    MenuItem itemCopy = new MenuItem("Copy");
    MenuItem itemCut = new MenuItem("Cut");
    MenuItem itemPaste = new MenuItem("Paste");
    MenuItem itemDelete = new MenuItem("Delete");
    MenuItem itemFullDelete = new MenuItem("Full Delete");

    public void setFile(File selectedFile){
        this.selectedFile = selectedFile;
    }

    public managerMethods getMethods(){
        return methods;
    }

    public ContextMenu createInstance(TextField logger){
        itemCreate.getItems().addAll(itemFolder, itemFile);

        menu.getItems().addAll(itemCreate, itemCopy, itemCut, itemPaste, itemRename, itemDelete, itemFullDelete);

        itemCopy.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
        itemCut.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));
        itemPaste.setAccelerator(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN));
        itemDelete.setAccelerator(new KeyCodeCombination(KeyCode.DELETE));
        itemFullDelete.setAccelerator(new KeyCodeCombination(KeyCode.DELETE, KeyCombination.SHIFT_DOWN));

        Handlers(logger);
        return menu;
    }

    private void Handlers(TextField logger) {

        itemCopy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(Copy.CopyToBuffer(selectedFile.toPath(), false)){
                    logger.setText("File " + selectedFile.getName() + " was copied");
                }
            }
        });
        itemCut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(methods.cut(selectedFile)){
                    logger.setText("File " + selectedFile.getName() + " was cut");
                }
            }
        });
        itemPaste.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                methods.paste(selectedFile);
            }
        });
        itemRename.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                String oldName = selectedFile.getName();
                String newName;
                    if(!Objects.isNull(newName = methods.renameFile(selectedFile)))
                        logger.setText("File " + oldName + " renamed to " + newName);
                 else logger.setText("File renaming canceled");
            }
        });

        itemDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(methods.deleteFile(selectedFile))
                    logger.setText("File " + selectedFile.getName() + " moved to trash can");

            }
        });

        itemFullDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(methods.permDeleteFile(selectedFile))
                    logger.setText("File " + selectedFile.getName() + " was permanently deleted");
            }
        });

        itemFolder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String folderName;
                if(!Objects.isNull(folderName = methods.makeDir(selectedFile)))
                    logger.setText("Folder " + folderName + " created in " + selectedFile.toString());
                else logger.setText("Folder creating canceled");
            }
        });

        itemFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean created;
                if(selectedFile.isDirectory())
                    created = methods.createFile(selectedFile);
                else created = methods.createFile(selectedFile.getParentFile());
                if(created){
                    logger.setText("File created");
                } else logger.setText("File creating canceled");
            }
        });
    }
}


