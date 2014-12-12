package net.sldt_team.assetsManager.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utilities {
    public static String getFileExtention(File f){
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

    public static void delete(File file)
            throws IOException {

        if(file.isDirectory()){
            System.out.println("Deleting directory " + file.getAbsolutePath());
            //directory is empty, then delete it
            if(file.list().length==0){
                if (file.delete())
                    System.out.println("Directory is deleted : " + file.getAbsolutePath());
            }else{
                //list all the directory contents
                String files[] = file.list();

                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);

                    //recursive delete
                    delete(fileDelete);
                }

                //check the directory again, if empty then delete it
                if(file.list().length==0){
                    if (file.delete())
                        System.out.println("Directory is deleted : " + file.getAbsolutePath());
                }
            }
        }else{
            //if file, then delete it
            if (file.delete())
                System.out.println("File is deleted : " + file.getAbsolutePath());
        }
    }

    public static List<File> getValidFiles(File f){
        File[] files = f.listFiles();
        List<File> fileList = new ArrayList<File>();
        if (files != null){
            for (File file : files){
                if (file.isDirectory()){
                    fileList.add(file);
                    continue;
                }
                if (getFileExtention(file) == null){
                    continue;
                }
                if (getFileExtention(file).equalsIgnoreCase("PNG")){
                    fileList.add(file);
                }
            }
        }
        return fileList;
    }
}
