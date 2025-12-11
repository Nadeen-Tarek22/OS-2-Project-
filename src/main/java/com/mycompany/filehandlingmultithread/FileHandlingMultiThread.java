/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.filehandlingmultithread;

import java.io.File;
import java.util.Scanner;

/**
 *
 * @author hamed_2
 */
public class FileHandlingMultiThread {

    public static void main(String[] args) {
       Scanner sc = new Scanner(System.in);
        CreateDirectoryWithFiles obj = new CreateDirectoryWithFiles();

        File directory = obj.directory;

     
        if (directory.exists()) {
            File[] existingFiles = directory.listFiles();
            if (existingFiles != null && existingFiles.length > 0) {
                System.out.println("Existing files in folder:");
                for (File f : existingFiles) {
                    if (f.isFile()) {
                        System.out.println(" - " + f.getName());
                    }
                }
                System.out.println("Total files: " + existingFiles.length);
            } else {
                System.out.println("Folder exists but has no files.");
            }
        } else {
            System.out.println("Folder does not exist yet. It will be created when you add files.");
        }


        int n = -1;
        while (n < 0) {
            System.out.print("Enter number of files you want to create: ");
            n = sc.nextInt();
            sc.nextLine();
            if (n < 0) {
                System.out.println("Please enter a non-negative number.");
            }
        }

       
        if (n > 0) {
            String[] userFiles = new String[n];
            for (int i = 0; i < n; i++) {
                System.out.print("Enter file name " + (i + 1) + ": ");
                userFiles[i] = sc.nextLine();
            }
            obj.createFiles(userFiles);
        }

        int choice = -1;

   
        while (true) {
            System.out.println("\n===== MENU =====");
            if (n > 0) { 
                System.out.println("1 - Add file");
                System.out.println("2 - Delete file");
                System.out.println("3 - Count files");
                System.out.println("0 - Exit");
            } else { 
                System.out.println("1 - Delete file");
                System.out.println("2 - Count files");
                System.out.println("0 - Exit");
            }

            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();

        
            if (choice < 0) {
                System.out.println("Invalid choice! Please enter a valid number.");
                continue;
            }

            if (n > 0) { 
                switch (choice) {
                    case 1:
                        System.out.print("Enter file name to ADD: ");
                        String newFile = sc.nextLine();
                        obj.addFile(newFile);
                        break;
                    case 2:
                        System.out.print("Enter file name to DELETE: ");
                        String deleteName = sc.nextLine();
                        obj.deleteFile(deleteName);
                        break;
                    case 3:
                        System.out.println("Files in folder: " + obj.fileCount());
                        break;
                    case 0:
                        System.out.println("Exiting menu...");
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
            } else {
                switch (choice) {
                    case 1:
                        System.out.print("Enter file name to DELETE: ");
                        String deleteName = sc.nextLine();
                        obj.deleteFile(deleteName);
                        break;
                    case 2:
                        System.out.println("Files in folder: " + obj.fileCount());
                        break;
                    case 0:
                        System.out.println("Exiting menu...");
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
            }

            if (choice == 0) {
                break; 
            }
        }


        System.out.println("\n===== PROCESSING ALL FILES USING MULTITHREADING =====");
        System.out.println("\n===== PROCESSING ALL FILES USING MULTITHREADING =====");
        FileProcessorThread.processFilesWithThreads(obj);
    }
}


