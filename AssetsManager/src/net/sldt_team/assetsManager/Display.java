package net.sldt_team.assetsManager;

import net.sldt_team.assetsManager.compiler.ZipFileCompiler;
import net.sldt_team.assetsManager.compiler.ZipFileUtilities;
import net.sldt_team.assetsManager.utils.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Display extends JFrame {

    private JTree theTree;
    private FileSystemModel treeModel;
    private JScrollPane thePane;
    private TreeImageDisplayPanel thePanel;

    public String prjName;
    public String prjDir;
    public String prjCompileDir;
    public String prjGameName;
    public File projectFile;

    public Display(File project, boolean console){
        super("No files opened - SLDT's AssetsManager");
        setSize(1024, 768);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(true);
        setLayout(new BorderLayout());

        resetTree(null, null);

        JMenuBar bar = new JMenuBar();
        bar.add(getFileMenu());
        bar.add(getProjectMenu());
        bar.add(getViewMenu());
        bar.add(getHelpMenu());
        setJMenuBar(bar);

        thePanel = getMainPanel();
        theTree = new JTree(treeModel);
        theTree.setEditable(false);
        theTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        theTree.setShowsRootHandles(true);
        theTree.addTreeSelectionListener(thePanel);
        JScrollPane pane = new JScrollPane(theTree);
        pane.setSize(new Dimension(256, getHeight() - 70));
        pane.setMaximumSize(new Dimension(256, getHeight() - 70));
        pane.setMinimumSize(new Dimension(256, getHeight() - 70));
        pane.setPreferredSize(new Dimension(256, getHeight() - 70));
        pane.setLocation(10, 10);
        add(pane, BorderLayout.LINE_START);
        thePane = pane;

        add(thePanel, BorderLayout.CENTER);

        setVisible(true);

        if (project != null){
            Map<String, String> infos = ProjectManager.getProjectInfos(project.toString());
            if (infos != null){
                onProjectOppened(infos);
                projectFile = project;
                resetTree(new File(infos.get("DIR")), infos.get("NAME"));
            }
        }
        if (console){
            new DialogDevelopperConsole(this).setVisible(true);
        }
    }

    public void onProjectOppened(Map<String, String> prjInfos){
        if (prjInfos == null){
            prjName = null;
            prjDir = null;
            prjCompileDir = null;
            prjGameName = null;
            setTitle("No files opened - SLDT's AssetsManager");
            return;
        }

        String name = prjInfos.get("NAME");
        String dir = prjInfos.get("DIR");
        String compDir = prjInfos.get("COMPILE_DIR");
        String gName = prjInfos.get("GAME_NAME");

        setTitle(name + " - SLDT's AssetsManager");
        prjName = name;
        prjDir = dir;
        prjCompileDir = compDir;
        prjGameName = gName;
    }

    public void resetTree(File directoryToDisplay, String name){
        if (theTree == null) {
            treeModel = new FileSystemModel(directoryToDisplay, name);
        } else {
            remove(thePane);
            thePane = null;
            theTree = null;
            treeModel = new FileSystemModel(directoryToDisplay, name);

            theTree = new JTree(treeModel);
            theTree.setEditable(false);
            theTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            theTree.setShowsRootHandles(true);
            theTree.addTreeSelectionListener(thePanel);
            JScrollPane pane = new JScrollPane(theTree);
            pane.setSize(new Dimension(256, getHeight() - 70));
            pane.setMaximumSize(new Dimension(256, getHeight() - 70));
            pane.setMinimumSize(new Dimension(256, getHeight() - 70));
            pane.setPreferredSize(new Dimension(256, getHeight() - 70));
            pane.setLocation(10, 10);
            add(pane, BorderLayout.LINE_START);
            thePane = pane;

            setVisible(false);
            setVisible(true);
        }
    }

    public TreeImageDisplayPanel getMainPanel(){
        thePanel = new TreeImageDisplayPanel(this);
        thePanel.setSize(712, getHeight() - 70);
        thePanel.setLayout(new FlowLayout());
        thePanel.setLocation(300, 10);
        thePanel.setBackground(Color.GRAY);

        return thePanel;
    }

    public JMenu getProjectMenu(){
        JMenu project = new JMenu("Project");
        project.setMnemonic('P');

        JMenuItem settings = new JMenuItem("Project settings");
        settings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new DialogProjectSettings(Display.this).setVisible(true);
            }
        });
        project.add(settings);

        JMenuItem compile = new JMenuItem("Export as ZIP");
        compile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (prjDir != null && prjName != null && projectFile != null) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File(prjDir).getParentFile());
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("ZIP archive (.zip)", "zip", "Exported archive");
                    fileChooser.setFileFilter(filter);
                    fileChooser.setAcceptAllFileFilterUsed(false);
                    int returnValue = fileChooser.showSaveDialog(Display.this);
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        File content = new File(prjDir);
                        File[] files = content.listFiles();
                        assert files != null;
                        List<File> preparedFiles = new ArrayList<File>();
                        for (File f : files) {
                            System.out.println(f.toString());
                            if (!f.isDirectory()) {
                                preparedFiles.add(f);
                            } else {
                                addDirectoryToCompileList(f, preparedFiles);
                            }
                        }
                        File[] finalFiles = new File[preparedFiles.size()];
                        for (int i = 0 ; i < preparedFiles.size() ; i++){
                            finalFiles[i] = preparedFiles.get(i);
                        }
                        try {
                            ZipFileCompiler.craeteZip(finalFiles, selectedFile);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });
        project.add(compile);

        JMenu assetMenu = new JMenu("Assets");
        JMenuItem texImport = new JMenuItem("Import Texture File");
        texImport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (theTree.getSelectionPath() == null) {
                    return;
                }
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(prjDir).getParentFile());
                FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG File (.png)", "png", "Texture file");
                fileChooser.setFileFilter(filter);
                fileChooser.setAcceptAllFileFilterUsed(false);
                int returnValue = fileChooser.showOpenDialog(Display.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedTexture = fileChooser.getSelectedFile();

                    String s = theTree.getSelectionPath().toString();
                    s = s.replace(", ", File.separator);
                    s = s.replace("[", File.separator);
                    s = s.replace("]", File.separator);
                    s = s.replace(prjName, File.separator);

                    File path = new File(prjDir + File.separator + s);

                    ProjectManager.copyFile(selectedTexture, path);
                    //selectedTexture
                }
            }
        });
        JMenuItem remove = new JMenuItem("Remove Selected File");
        remove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (theTree.getSelectionPath() == null) {
                    return;
                }
                int result = JOptionPane.showConfirmDialog(Display.this, "Are you sure you want to remove that file ?", "Remove selected file...", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                Main.log.info("User requested file remove");
                if (result == 0) {
                    String s = theTree.getSelectionPath().toString();
                    s = s.replace(", ", File.separator);
                    s = s.replace("[", File.separator);
                    s = s.replace("]", File.separator);
                    s = s.replace(prjName, File.separator);

                    File theFile = new File(prjDir + File.separator + s);
                    if (!theFile.exists()) {
                        return;
                    }

                    if (theFile.isDirectory()){
                        try {
                            Utilities.delete(theFile);
                        } catch (IOException e1) {
                            Main.log.warning("An error has occured while trying to delete file...");
                            return;
                        }
                    } else if (theFile.isFile()){
                        if (!theFile.delete()) {
                            Main.log.warning("An error has occured while trying to delete file...");
                            return;
                        }
                    }
                    Main.log.info("File removed !");
                    resetTree(new File(prjDir), prjName);
                } else {
                    Main.log.info("User canceled operation");
                }

            }
        });
        JMenuItem rename = new JMenuItem("Rename Selected File");
        rename.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (theTree.getSelectionPath() == null) {
                    return;
                }
                DialogStringRequest dialog = new DialogStringRequest(Display.this, "Rename selected file...", "Enter the new name for the current selected file.");
                Main.log.info("User requested file rename");
                dialog.showDialog();
                if (dialog.getExitCode() == 0){
                    String s = theTree.getSelectionPath().toString();
                    s = s.replace(", ", File.separator);
                    s = s.replace("[", File.separator);
                    s = s.replace("]", File.separator);
                    s = s.replace(prjName, File.separator);

                    File theFile = new File(prjDir + File.separator + s);
                    if (!theFile.exists()){
                        return;
                    }
                    File newFile = null;
                    if (theFile.isFile()) {
                        newFile = new File(theFile.getParentFile() + File.separator + dialog.getExitValue() + ".png");
                    } else if (theFile.isDirectory()){
                        newFile = new File(theFile.getParentFile() + File.separator + dialog.getExitValue());
                    }
                    assert newFile != null;
                    assert !newFile.exists();
                    theFile.renameTo(newFile);

                    Main.log.info("File renamed !");
                    resetTree(new File(prjDir), prjName);
                } else if (dialog.getExitCode() == 1){
                    Main.log.info("User canceled operation");
                }

            }
        });
        assetMenu.add(texImport);
        assetMenu.add(remove);
        assetMenu.add(rename);
        project.add(assetMenu);

        JMenu compileMenu = new JMenu("Compile As");
        JMenuItem zipCompile = new JMenuItem("ZIP Archive");
        zipCompile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ZipFileCompiler.compileZipAssetsFile(prjCompileDir, prjGameName, prjDir);
            }
        });
        JMenuItem jarCompile = new JMenuItem("JAR Archive");
        jarCompile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ZipFileCompiler.compileJarAssetsFile(prjCompileDir, prjGameName, prjDir);
            }
        });
        JMenuItem gafCompile = new JMenuItem("GAF Archive (GameAssetsFile)");
        gafCompile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ZipFileCompiler.compileGafAssetsFile(prjCompileDir, prjGameName, prjDir);
            }
        });
        compileMenu.add(zipCompile);
        compileMenu.add(jarCompile);
        compileMenu.add(gafCompile);
        project.add(compileMenu);

        return project;
    }

    private void addDirectoryToCompileList(File dir, List<File> list){
        File[] files = dir.listFiles();

        assert files != null;
        for (File f : files){
            if (!f.isDirectory()){
                list.add(f);
            } else {
                addDirectoryToCompileList(f, list);
            }
        }
    }

    public JMenu getFileMenu(){
        JMenu file = new JMenu("File");
        file.setMnemonic('F');

        JMenuItem open = new JMenuItem("Open project");
        open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Assets project file (.assets.prj)", "prj", "Assets project file");
                fileChooser.setFileFilter(filter);
                fileChooser.setAcceptAllFileFilterUsed(false);
                int returnValue = fileChooser.showOpenDialog(Display.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    Map<String, String> infos = ProjectManager.getProjectInfos(selectedFile.toString());
                    if (infos != null){
                        onProjectOppened(infos);
                        projectFile = selectedFile;
                        resetTree(new File(infos.get("DIR")), infos.get("NAME"));
                    }
                }
            }
        });
        file.add(open);

        JMenuItem newPrj = new JMenuItem("New project");
        newPrj.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FolderFilter());
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnValue = fileChooser.showOpenDialog(Display.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    if (Utilities.getFileExtention(selectedFile) != null) {
                        JOptionPane.showMessageDialog(Display.this, "You must choose a folder for your new assets project. \nOnly directories are allowed in project saving...", "SLDT's AssetsManager", JOptionPane.ERROR_MESSAGE);
                    }
                    File contentFolder = new File(selectedFile, "content");
                    if (!contentFolder.exists()) {
                        contentFolder.mkdirs();
                    }
                    ProjectManager.genereateProjectContentFolder(contentFolder.toString());
                    Map<String, String> content = new HashMap<String, String>();
                    content.put("DIR", contentFolder.toString());
                    content.put("NAME", "NewProject");
                    content.put("COMPILE_DIR", "NOT_DEFINED");
                    content.put("GAME_NAME", "NOT_DEFINED");
                    ProjectManager.saveProjectInfos(selectedFile + File.separator + "project.assets.prj", content);
                    projectFile = selectedFile;
                    onProjectOppened(content);
                    resetTree(contentFolder, "NewProject");
                }
            }
        });
        file.add(newPrj);

        JMenuItem close = new JMenuItem("Close project");
        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                projectFile = null;
                thePanel.oldX = 0;
                thePanel.oldY = 0;
                thePanel.imagePath = null;
                onProjectOppened(null);
                resetTree(null, null);
                repaint();
                thePanel.repaint();
            }
        });
        file.add(close);

        return file;
    }

    public JMenu getHelpMenu(){
        JMenu help = new JMenu("?");
        help.setMnemonic('?');

        JMenuItem about = new JMenuItem("About");
        about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(Display.this, "SLDT's GameEngine - AssetsManager \n Program developped by SLDT, as an utility for compiling assets files used by SLDT's GameEngine.", "SLDT's AssetsManager", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        help.add(about);

        JMenuItem devConsole = new JMenuItem("Developper Console");
        devConsole.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!(Main.developperConsole == null)) {
                    Main.developperConsole.setFocusableWindowState(true);
                    Main.developperConsole.requestFocus();
                    return;
                }
                new DialogDevelopperConsole(Display.this).setVisible(true);
            }
        });
        help.add(devConsole);

        return help;
    }

    public JMenu getViewMenu(){
        JMenu window = new JMenu("Window");
        window.setMnemonic('W');

        JMenuItem reset = new JMenuItem("Reset view");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (thePanel != null){
                    thePanel.oldX = 0;
                    thePanel.oldY = 0;
                    thePanel.repaint();
                }
            }
        });
        window.add(reset);

        JMenuItem refresh = new JMenuItem("Refresh window");
        refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (thePanel != null){
                    invalidate();
                    repaint();
                    thePanel.repaint();
                    theTree.repaint();
                    thePane.repaint();
                    setVisible(false);
                    setVisible(true);
                    validate();
                }
                resetTree(new File(prjDir), prjName);
            }
        });
        window.add(refresh);

        return window;
    }
}

class FolderFilter extends javax.swing.filechooser.FileFilter {
    public boolean accept( File file ) {
        return file.isDirectory();
    }

    public String getDescription() {
        return "We only take directories";
    }
}
