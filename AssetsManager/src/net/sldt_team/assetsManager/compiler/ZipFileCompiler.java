package net.sldt_team.assetsManager.compiler;

import net.sldt_team.assetsManager.Main;
import net.sldt_team.assetsManager.utils.Utilities;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFileCompiler {

    public static void craeteZip(File[] files, File zipArchive) throws IOException {
        // Compiled zip file :
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipArchive));

        for (File file : files) {
            if (file != null) {
                FileInputStream in = new FileInputStream(file);

                String s = file.toString();
                String dir = s.split("content")[1];
                dir = dir.substring(1);
                dir = dir.replace(File.separatorChar, '/');
                out.putNextEntry(new ZipEntry(dir));

                byte[] b = new byte[1024];
                int count;

                while ((count = in.read(b)) > 0) {
                    System.out.println();
                    out.write(b, 0, count);
                }
                in.close();
            }
        }

        out.close();
    }

    private static void addDirectoryToCompileList(File dir, List<File> list) {
        File[] files = dir.listFiles();

        assert files != null;
        for (File f : files) {
            if (!f.isDirectory()) {
                int code = checkAndAddFile(list, f);
                switch(code) {
                    case 0:
                        Main.log.severe("The compiler has returned error code 0 : unknown_error");
                        return;

                    case 1:
                        Main.log.severe("The compiler has returned error code 1 : file_invalid_or_corrupted");
                        return;
                }
            } else {
                addDirectoryToCompileList(f, list);
            }
        }
    }

    private static boolean isPowerOfTwo(int number) {
        if (number <= 0) {
            throw new IllegalArgumentException("number: " + number);
        }
        return (number & -number) == number;
    }

    private static int checkAndAddFile(List<File> list, File f) {
        if (Utilities.getFileExtention(f) != null) {
            try {
                FileInputStream input = new FileInputStream(f);
                input.close();
                list.add(f);
                return -1;
            } catch (IOException e) {
                return 1;
            }
        } else {
            return 0;
        }
    }

    public static void compileZipAssetsFile(String prjCompileDir, String prjGameName, String prjDir) {
        File selectedFile = new File(prjCompileDir + File.separator + prjGameName.toLowerCase() + ".assets.zip");
        File content = new File(prjDir);
        File[] files = content.listFiles();
        assert files != null;
        Main.log.warning("Starting ZipAssetsCompiler for " + selectedFile.toString());
        List<File> preparedFiles = new ArrayList<File>();
        for (File f : files) {
            if (!f.isDirectory()) {
                int code = checkAndAddFile(preparedFiles, f);
                switch(code) {
                    case 0:
                        Main.log.severe("The compiler has returned error code 0 : unknown_error");
                        return;

                    case 1:
                        Main.log.severe("The compiler has returned error code 1 : file_invalid_or_corrupted");
                        return;
                }
            } else {
                addDirectoryToCompileList(f, preparedFiles);
            }
        }
        File[] finalFiles = new File[preparedFiles.size()];
        for (int i = 0; i < preparedFiles.size(); i++) {
            finalFiles[i] = preparedFiles.get(i);
        }
        try {
            craeteZip(finalFiles, selectedFile);
        } catch (IOException e1) {
            Main.log.warning("ZipAssetsCompiler failure");
            Main.log.severe("Compiler has stopped running, closing...");
            return;
        }

        Main.log.fine("ZipAssetsCompiler succeeded !");
    }

    public static void compileJarAssetsFile(String prjCompileDir, String prjGameName, String prjDir) {
        File selectedFile = new File(prjCompileDir + File.separator + prjGameName.toLowerCase() + ".assets.jar");
        File content = new File(prjDir);
        File[] files = content.listFiles();
        assert files != null;
        Main.log.warning("Starting JarAssetsCompiler for " + selectedFile.toString());
        List<File> preparedFiles = new ArrayList<File>();
        for (File f : files) {
            if (!f.isDirectory()) {
                int code = checkAndAddFile(preparedFiles, f);
                switch(code) {
                    case 0:
                        Main.log.severe("The compiler has returned error code 0 : unknown_error");
                        return;

                    case 1:
                        Main.log.severe("The compiler has returned error code 1 : file_invalid_or_corrupted");
                        return;
                }
            } else {
                addDirectoryToCompileList(f, preparedFiles);
            }
        }
        File[] finalFiles = new File[preparedFiles.size()];
        for (int i = 0; i < preparedFiles.size(); i++) {
            finalFiles[i] = preparedFiles.get(i);
        }
        try {
            craeteZip(finalFiles, selectedFile);
        } catch (IOException e1) {
            Main.log.warning("JarAssetsCompiler failure");
            Main.log.severe("Compiler has stopped running, closing...");
            return;
        }

        Main.log.fine("JarAssetsCompiler succeeded !");
    }

    public static void compileGafAssetsFile(String prjCompileDir, String prjGameName, String prjDir) {
        File selectedFile = new File(prjCompileDir + File.separator + prjGameName.toLowerCase() + ".assets.cache.jar");
        File content = new File(prjDir);
        File[] files = content.listFiles();
        assert files != null;
        Main.log.warning("Starting GafAssetsCompiler for " + selectedFile.toString());
        List<File> preparedFiles = new ArrayList<File>();
        for (File f : files) {
            if (!f.isDirectory()) {
                int code = checkAndAddFile(preparedFiles, f);
                switch(code) {
                    case 0:
                        Main.log.severe("The compiler has returned error code 0 : unknown_error");
                        return;

                    case 1:
                        Main.log.severe("The compiler has returned error code 1 : file_invalid_or_corrupted");
                        return;
                }
            } else {
                addDirectoryToCompileList(f, preparedFiles);
            }
        }
        File[] finalFiles = new File[preparedFiles.size()];
        for (int i = 0; i < preparedFiles.size(); i++) {
            finalFiles[i] = preparedFiles.get(i);
        }
        try {
            craeteZip(finalFiles, selectedFile);
            File finalFile = new File(prjCompileDir + File.separator + prjGameName.toLowerCase() + ".assets.gaf");
            GAFCompiler.compileAssetsFileFromZip(selectedFile, finalFile);
            selectedFile.delete();
        } catch (IOException e1) {
            Main.log.warning("GafAssetsCompiler failure");
            Main.log.severe("Compiler has stopped running, closing...");
            return;
        }

        Main.log.fine("GafAssetsCompiler succeeded !");
    }
}