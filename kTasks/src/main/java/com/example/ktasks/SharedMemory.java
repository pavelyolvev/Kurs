package com.example.ktasks;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.LongBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class SharedMemory {
    private static final int BUFFER_SIZE = 1024; // Размер буфера
    private static final long FILE_SIZE = 4096; // Размер файла для отображения в память

    int threadsCount;
    MappedByteBuffer buffer;
    FileChannel fileChannel;
    LongBuffer longBuffer;
    public void openSharedMemory() throws IOException {
        File appPath = new File(TaskManager.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        File file = new File(appPath.getParent() + "/shared_memory.bin");
        fileChannel = FileChannel.open( file.toPath(), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE );

        buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 4096);
        longBuffer = buffer.asLongBuffer();
    }
    public ArrayList<ProcessInfo> getSharedPID() throws IOException {
        ArrayList<ProcessInfo> procs = new ArrayList<>();

        System.out.println(longBuffer.get(0));

        int bufSize = (int) longBuffer.get(0);
        for(int i = 1; i <= bufSize; i++)
        {
            long gotpid = longBuffer.get(i);
            if(ProcessHandle.of(gotpid).isPresent())
                procs.add(new ProcessInfo(longBuffer.get(i)));
        }
        fileChannel.close();
        return procs;
    }

    public int getThreadsCount() {
        return threadsCount;
    }
}
