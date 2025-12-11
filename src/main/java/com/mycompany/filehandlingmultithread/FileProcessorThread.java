/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.filehandlingmultithread;

import java.io.File;

/**
 *
 * @author hamed_2
 */
public class FileProcessorThread  implements Runnable{
    private File[] files;
    private ManipulatingWithDirectory manipulator;

    public FileProcessorThread(File[] files, ManipulatingWithDirectory manipulator) {
        this.files = files;
        this.manipulator = manipulator;
    }

    @Override
public void run() {
    for (File file : files) {
        if (!file.isFile()) continue;

        String[] wordsArray = manipulator.readFileToWords(file);

        String result = String.format(
            "Thread %s processed file: %s\n" +
            "Total words: %d\n" +
            "Shortest word: %s\n" +
            "Longest word: %s\n" +
            "Count of 'are': %d\n" +
            "Count of 'you': %d\n" +
            "Count of 'is': %d\n",
            Thread.currentThread().getName(),
            file.getName(),
            ManipulatingWithDirectory.numberOfWords(wordsArray),
            ManipulatingWithDirectory.ShortestWord(wordsArray),
            ManipulatingWithDirectory.LongestWord(wordsArray),
            ManipulatingWithDirectory.NumberOfSpecificWord(wordsArray, "are"),
            ManipulatingWithDirectory.NumberOfSpecificWord(wordsArray, "you"),
            ManipulatingWithDirectory.NumberOfSpecificWord(wordsArray, "is")
        );

        System.out.println(result); 
    }
}


    public static void processFilesWithThreads(CreateDirectoryWithFiles create) {
        ManipulatingWithDirectory manipulator = new ManipulatingWithDirectory(create);

        File directory = create.directory;
        if (directory == null || !directory.exists()) {
            System.out.println("Directory does not exist.");
            return;
        }

        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("No files to process.");
            return;
        }

        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("Available cores: " + cores);

        int threadCount = Math.min(cores, files.length); 
        File[][] filesPerThread = new File[threadCount][]; 

        int base = files.length / threadCount;
        int rem = files.length % threadCount;
        int index = 0;
        for (int i = 0; i < threadCount; i++)
        {
            int size = base + (i < rem ? 1 : 0);
            filesPerThread[i] = new File[size];
            for (int j = 0; j < size; j++) 
            {
                filesPerThread[i][j] = files[index++];
            }
        }
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++)
        {
            threads[i] = new Thread(new FileProcessorThread(filesPerThread[i], manipulator), "Thread-" + (i+1));
            threads[i].start();
        }

        for (Thread t : threads)
        {
            try 
            {
                t.join();
            } 
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        System.out.println("\nAll files processed using multithreading.");
    }
}
