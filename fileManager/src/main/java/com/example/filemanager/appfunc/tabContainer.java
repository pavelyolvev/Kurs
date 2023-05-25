package com.example.filemanager.appfunc;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.Date;

public class tabContainer {
    File file;
    StringProperty name;
    StringProperty type;
    StringProperty lastMod;
    StringProperty size;
    StringProperty source;
    //SimpleObjectProperty<Image> icon;

    //Image Folder = new Image("F:/4 семестр/Курсовая/Super App/src/main/resources/com/example/super_app/Folder.png");
    //Image File = new Image("F:/4 семестр/Курсовая/Super App/src/main/resources/com/example/super_app/File.png");

    public tabContainer(File file){
        this.file = file;
        name = new SimpleStringProperty(file.getName());
        source = new SimpleStringProperty(file.toString());

        lastMod = new SimpleStringProperty(new Date(file.lastModified()).toString());
        if(file.isFile()){
            type = new SimpleStringProperty(FilenameUtils.getExtension(file.getName()));
            size = new SimpleStringProperty(String.valueOf(file.length()/1024 + " KB"));
        }

        else{
            size = new SimpleStringProperty();
            type = new SimpleStringProperty("Folder");
        }

        /*
        if(getType().isEmpty()){
            icon = new SimpleObjectProperty<>(Folder);
        }else{

            icon = new SimpleObjectProperty<>(File);
        }

         */

    }

    @Override
    public String toString(){
        return file.getName();
    }

    public void setName(String str){
        name.set(str);
    }
    public String getName(){
        return name.get();
    }
    public String getType(){
        return type.get();
    }
    public String getLastMod(){
        return lastMod.get();
    }
    public String getSize(){
        return size.get();
    }
    public String getSource(){
        return source.get();
    }
    public File getFile(){
        return file;
    }
}


