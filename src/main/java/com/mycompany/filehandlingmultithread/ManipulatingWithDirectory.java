/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.filehandlingmultithread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author hamed_2
 */
public class ManipulatingWithDirectory {
    
    CreateDirectoryWithFiles Create;

    public ManipulatingWithDirectory(CreateDirectoryWithFiles Create) {
        this.Create = Create;
    }

    public static String ShortestWord(String[] s)
    {
        
        String shortest = s[0];
        for (int i = 0; i < s.length; i++) {
            
            if (s[i].length() <= shortest.length()) 
            {
                shortest = s[i];
            }
        }
       
        return shortest;
    }

    public static String LongestWord(String[] s) 
    {
        String shortest = s[0];
        for (int i = 0; i < s.length; i++) {
            if (s[i].length() >= shortest.length())
            {
                shortest = s[i];
            }
        }
        return shortest;
    }

    public static int NumberOfSpecificWord(String[] s, String word) {
        int count = 0;
        for (int i = 0; i < s.length; i++) 
        {
            if (s[i].equalsIgnoreCase(word))
            {
                count++;
            }
        }
        return count;
    }

   public static int numberOfWords(String[] wordsArray) {
    return wordsArray == null ? 0 : wordsArray.length;
}
// process to run without GUI
/*
    public void processAllFiles() 
    {
        File directory = Create.directory;
        if (directory == null || !directory.exists()) {
            System.out.println("Directory does not exist.");
            return;
        }

        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("No files in directory.");
            return;
        }

        for (File file : files) {
            if (!file.isFile()) 
                continue;
            System.out.println("\nFile: " + file.getName());
            String[] wordsArray = readFileToWords(file);
            System.out.println("Total words: " + numberOfWords(wordsArray));
            System.out.println("Shortest word: " + ShortestWord(wordsArray));
            System.out.println("Longest word: " + LongestWord(wordsArray));
            System.out.println("Count of 'are': " + NumberOfSpecificWord(wordsArray, "are"));
            System.out.println("Count of 'you': " + NumberOfSpecificWord(wordsArray, "you"));
            System.out.println("Count of 'is': " + NumberOfSpecificWord(wordsArray, "is"));
        }
    }

 */

    public String[] readFileToWords(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder allText = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                allText.append(line).append(" ");
            }

            String content = allText.toString().trim();
            if (content.isEmpty()) {
                return new String[0]; 
            }

            return content.split("\\s+"); 
        } catch (IOException e) {
            System.out.println("Error reading file: " + file.getName());
            return new String[0];
        }
    }

}


 
