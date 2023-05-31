package com.example.kcmdapp.lib;

import com.sun.jna.Library;
import com.sun.jna.Native;
import java.util.ArrayList;

public class JNAMethods {
    public JNAMethods() {
    }

    public interface IOCommands extends Library {
        IOCommands INSTANCE = (IOCommands)Native.load("c", IOCommands.class);

        int open(String pathname, int flag);

        int close(int fd);

        int read(int fd, byte[] buffer, int count);

        int write(int fd, byte[] buffer, int count);
        int mkdir(String path, int mode);
    }

    public interface ProcComm extends Library {
        ProcComm INSTANCE = (ProcComm)Native.load("c", ProcComm.class);


        int getpid();

        int kill(int pid, int signal);

        int fork();

        int execvp(String command, String[] args);

        int getpriority(int type, int pid);

        int setpriority(int type, int pid, int priority);
    }

    public static class IO {
        static byte[] buffer;
        static int fd;

        public IO() {
        }

        public static int open(String pathname) {
            fd = JNAMethods.IOCommands.INSTANCE.open(pathname, 2); //2 - флаг открытия для чтения или записи
            return fd;
        }

        public static int close(int fd) {
            return JNAMethods.IOCommands.INSTANCE.close(fd);
        }

        public static String mkdir(String path){
            int mode = 0777; // Режим доступа к каталогу

            int result = IOCommands.INSTANCE.mkdir(path, mode);
            if (result == 0) {
                return "Path has been created";
            } else {
                return "Error in creating path";
            }
        }
        public static String read(int fd) {
            String readedData;
            if (fd != -1) {
                int bufferSize = 1024; // Размер буфера чтения
                byte[] buffer = new byte[bufferSize];
                int count = bufferSize;

                int bytesRead = IOCommands.INSTANCE.read(fd, buffer, count);
                if (bytesRead != -1) {
                    String data = new String(buffer, 0, bytesRead);
                    readedData = "Readed bytes: " + bytesRead + "\nFile contain:\n" + data;
                } else {
                    readedData = "Error in reading.";
                }

            } else {
                readedData = "Error in opening file.";
            }
            return readedData;
        }

        public static String write(int fd, String msg) {
            if (fd != -1) {

                byte[] buffer = msg.getBytes();
                int count = buffer.length;

                int bytesWritten = IOCommands.INSTANCE.write(fd, buffer, count);
                if (bytesWritten != -1) {
                    return "bytes writed: " + bytesWritten;
                } else {
                    return "Error in writing.";
                }

            } else {
                return "Error in opening file.";
            }

        }
    }

    public static class Process {
        public Process() {
        }

        public static int getPid() {
            return JNAMethods.ProcComm.INSTANCE.getpid();
        }

        public static int kill(int pid, int signal) {
            return JNAMethods.ProcComm.INSTANCE.kill(pid, signal);
        }

        public static boolean execvp(String command, String[] args) {
            int pid = JNAMethods.ProcComm.INSTANCE.fork();
            if (pid == 0) {
                JNAMethods.ProcComm.INSTANCE.execvp(command, args);
            }

            return pid == 0;
        }

        public static int getPriority(int pid, int type) {
            return JNAMethods.ProcComm.INSTANCE.getpriority(type, pid);
        }

        public static int setPriority(int pid, int type, int priority) {
            return JNAMethods.ProcComm.INSTANCE.setpriority(type, pid, priority);
        }
    }
}
