package com.example.ksuperapp;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;

public class SharedMemory {
    public static ArrayList<Long> pidList = new ArrayList<>();
    private static final int BUFFER_SIZE = 1024;
    private static final long FILE_SIZE = 4096L;
    static File file;
    static File appPath = new File(SuperApp.class.getProtectionDomain().getCodeSource().getLocation().getPath());

    public SharedMemory() {
    }

    public static void addToPidList(long pid) {
        pidList = (ArrayList<Long>) pidList.stream()
                .filter(pidc -> ProcessHandle.of(pidc).isPresent())
                .collect(Collectors.toList());
        pidList.add(pid);
        try {
            share();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void share() throws IOException {
        file = new File(appPath.getParent() + "/shared_memory.bin");
        FileChannel fileChannel = FileChannel.open( file.toPath(), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE );

        MappedByteBuffer buffer = fileChannel.map(MapMode.READ_WRITE, 0, 4096);
        LongBuffer longBuffer = buffer.asLongBuffer();



        /*
        for (long pidCh: pidList) {
            if(ProcessHandle.of(pidCh).isEmpty())
                pidList.remove(pidCh);
        }

         */


        System.out.println(pidList.size());
        longBuffer.put(0, pidList.size());
        //longBuffer.put(1, pidList.get(0));


        for (int i = 1; i <= pidList.size(); i++) {
            longBuffer.put(i, pidList.get(i-1));
        }


        buffer.force();
        fileChannel.close();
    }

    public static void deleteFile(){
        new File(appPath.getParent() + "/shared_memory.bin").delete();
    }
}

