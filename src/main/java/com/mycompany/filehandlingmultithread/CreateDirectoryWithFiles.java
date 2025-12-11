/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.filehandlingmultithread;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author hamed_2
 */
public class CreateDirectoryWithFiles {
    File directory;
    public CreateDirectoryWithFiles()
    {
        directory = new File("D:/data/myOldFolder");
        if (directory.exists()) 
        {
            System.out.println("Already exists: " + directory.getAbsolutePath());
        }
        else 
        {
         
            boolean ok = directory.mkdir(); 
            System.out.println(ok ? "Created: " + directory.getAbsolutePath() : "Failed to create directory");
        }   
            
        
        
    }
       public void createFiles(String[] fileNames) {

        for (String name : fileNames) {
            File file = new File(directory, name); 

            try 
            {
                if (file.createNewFile())
                {
                    System.out.println("File created: " + file.getAbsolutePath());
                } 
                else
                {
                    System.out.println("File already exists: " + file.getAbsolutePath());
                }
            }
            catch (IOException e)
            {
                System.out.println("Error creating file: " + file.getName() + " -> " + e.getMessage());
            }
        }
    }
         public void addFile(String fileName) {
        File file = new File(directory, fileName);
        try 
        {
            if (file.createNewFile())
            {
                System.out.println("File created: " + file.getAbsolutePath());
            } 
            else
            {
                System.out.println("File already exists: " + file.getAbsolutePath());
            }
        } 
        catch (IOException e)
        {
            System.out.println("Error creating file: " + fileName + " -> " + e.getMessage());
        }
    }

   
    public void deleteFile(String fileName) {
        File file = new File(directory, fileName);
        if (file.exists())
        {
            if (file.delete())
            {
                System.out.println("Deleted: " + fileName);
            }
            else 
            {
                System.out.println("Failed to delete: " + fileName);
            }
        } 
        else 
        {
            System.out.println("File not found: " + fileName);
        }
    }

  
    public int fileCount() {
        String[] files = directory.list();
        return (files == null) ? 0 : files.length;
    }
}
