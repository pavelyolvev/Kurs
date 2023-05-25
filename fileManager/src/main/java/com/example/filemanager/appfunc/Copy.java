package com.example.filemanager.appfunc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Copy implements Runnable {
    static Path bufferFile;
    static boolean cut;
    Path toPaste;

    public Copy(Path toPaste) {
        this.toPaste = toPaste;
    }

    static public boolean CopyToBuffer(Path file, boolean cutFile) {
        bufferFile = file;
        cut = cutFile;
        return true;
    }

    @Override
    public void run() {
        pasteFromBuffer(toPaste);
    }

    static public boolean pasteFromBuffer(Path fileToPaste) {
        if(cut)
            new managerMethods().moveFile(bufferFile.toFile(), fileToPaste.toFile());
        else {

            try {
                Files.walk(bufferFile).forEach(file -> {
                    try {
                        if (Files.isDirectory(file)) {
                            Files.createDirectories(fileToPaste.resolve(bufferFile.toFile().getName() + "/" + bufferFile.relativize(file)));
                        } else {
                            //copyFile(file, fileToPaste);
                            Files.copy(file, fileToPaste.resolve(bufferFile.toFile().getName() + "/" + bufferFile.relativize(file)));
                        }
                    } catch (IOException ex) {
                        System.out.println("Ошибка при копировании файла " + file + ": " + ex.getMessage());
                    }
                });
            } catch (IOException ex) {
                return false;
            }
        }
        return true;
    }
}

