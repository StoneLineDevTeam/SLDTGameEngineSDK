package net.sldt_team.assetsManager.utils;

import net.sldt_team.assetsManager.Main;
import net.sldt_team.assetsManager.compiler.ZipFileUtilities;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectManager {

    public static Map<String, String> getProjectInfos(String file) {
        File f = new File(file);
        if (f.exists()) {
            Map<String, String> prjMap = new HashMap<String, String>();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(f));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] command = line.split(">");
                    System.out.println(line);
                    if (command.length == 2) {
                        prjMap.put(command[0], command[1]);
                    }
                }
                reader.close();
                return prjMap;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void saveProjectInfos(String file, Map<String, String> prjMap) {
        File f = new File(file);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            for (Map.Entry entry : prjMap.entrySet()) {
                String k = (String) entry.getKey();
                String v = (String) entry.getValue();

                writer.write(k + ">" + v);
                writer.newLine();
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String[][] folders = {{"backgrounds", "mainBG.png", "sldtBG.png"}, {"buttons", "next.png", "normal.png", "round.png", "stop.png"}, {"components", "consoleScreen_scrollDown.png", "consoleScreen_scrollUp.png", "progressBar.png"}, {"fonts", "normal.png"}, {"message", "button.png", "confirmLogo.png", "criticalLogo.png", "dialog.png", "errorLogo.png", "infoLogo.png", "warningLogo.png"}, {"renderEngine", "missingTex.png"}, {"renderEngine/gradients", "left.png", "right.png", "top.png", "bottom.png"}};
    public static void genereateProjectContentFolder(String prjDir){
        File f20000000 = new File(prjDir + File.separator + "materials" + File.separator);
        if (!f20000000.exists()){
            f20000000.mkdirs();
        }
        for (String[] files : folders) {
            String folderName = files[0];
            for (int i = 1 ; i < files.length; i++) {
                String fileName = files[i];
                String assetInArchive = "defaultAssets/" + folderName + "/" + fileName;
                String asset = folderName.replace("/", File.separator) + File.separator + fileName;
                String assetDir = folderName + File.separator;
                try {
                    File f = new File(prjDir + File.separator + assetDir);
                    if (!f.exists()){
                        if (!f.mkdirs()) {
                            Main.log.warning("Folder creation failed !");
                            continue;
                        }
                    }
                    File f1 = new File(prjDir + File.separator + "materials" + File.separator + asset);
                    writeAppResourceToFile(assetInArchive, f1);
                } catch (IOException e) {
                    Main.log.warning("Unable to create default asset " + assetInArchive + " : " + e.getMessage());
                }
            }
        }
    }

    private static void writeAppResourceToFile(String name, File dir) throws IOException {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
        FileOutputStream out = new FileOutputStream(dir);
        byte[] buffer = new byte[2048];
        while((in.read(buffer)) != -1){
            out.write(buffer);
        }
        in.close();
        out.close();
    }

    /**
     * Copy the file/directory "from" and paste it in "to"
     */
    public static boolean copyFile(File from, File to) {
        if (!from.exists())
            return false;

        if (from.isDirectory()) {
            if (!to.exists()) {
                to.mkdirs();
            }
            File[] files = from.listFiles();
            List<Boolean> bools = new ArrayList<Boolean>();
            for (File f : files) {
                if (f.isDirectory()) {
                    bools.add(copyFile(f, new File(to + File.separator + f.getName())));
                } else {
                    bools.add(copySingleFile(f, new File(to + File.separator + f.getName())));
                }
            }
            for (Boolean b : bools) {
                if (!b) {
                    return false;
                }
            }
            return true;
        } else {
            return copySingleFile(from, to);
        }
    }

    private static boolean copySingleFile(File theFile, File newFile) {
        try {
            FileInputStream in = new FileInputStream(theFile);
            FileOutputStream out = new FileOutputStream(newFile);

            byte[] b = new byte[1024];
            int length;
            while ((length = in.read(b)) > 0) {
                out.write(b, 0, length);
            }

            in.close();
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }
}
