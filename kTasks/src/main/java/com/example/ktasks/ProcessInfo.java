package com.example.ktasks;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.*;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

public class ProcessInfo {
    StringProperty pid;
    StringProperty name;
    StringProperty startTime;
    long stTimeMsec;
    static int allThCount=0;

    private String getNameFromPid(long pid){
        try {
            Process process = Runtime.getRuntime().exec("jps -l");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(Long.toString(pid))) {
                    String[] parts = line.split(" ");
                    return new File(parts[1]).getName();
                }
            }

            reader.close();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


        return null;
    }
    public ProcessInfo(long pid) throws IOException {
        this.pid = new SimpleStringProperty(String.valueOf(pid));

            Process process = new ProcessBuilder("ps", "-T", "-p", String.valueOf(pid))
                    .redirectErrorStream(true)
                    .start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            int threadCount = 0;

            // Считываем вывод команды ps и подсчитываем количество строк (за исключением заголовка)
            while ((line = reader.readLine()) != null) {
                threadCount++;
            }

            // Вычитаем 1, чтобы исключить заголовок
            threadCount--;

            //ThreadSpace.setThreadCount(ThreadSpace.getThreadCount()+threadCount);
            allThCount = threadCount;

        this.name = new SimpleStringProperty(getNameFromPid(pid));

        Optional<ProcessHandle> processHandle = ProcessHandle.of(pid);
        if (processHandle.isPresent()) {
            ProcessHandle.Info processInfo = processHandle.get().info();

            this.stTimeMsec =processInfo.startInstant().get().toEpochMilli();
            Date stTime = new Date(processInfo.startInstant().get().toEpochMilli());
            this.startTime = new SimpleStringProperty(stTime.toString());
        }

    }

    public String getPid() {
        return pid.get();
    }

    public String getName() {
        return name.get();
    }

    public String getStartTime() {
        return startTime.get();
    }

    public long getStTimeInMsec() {
        return stTimeMsec;
    }

    public int getAllThCount() {
        return allThCount;
    }
}
