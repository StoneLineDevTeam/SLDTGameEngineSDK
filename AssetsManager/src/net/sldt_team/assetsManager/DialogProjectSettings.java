package net.sldt_team.assetsManager;

import net.sldt_team.assetsManager.utils.ProjectManager;
import net.sldt_team.assetsManager.utils.Utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DialogProjectSettings extends JDialog{

    private Display display;

    public DialogProjectSettings(Display parent){
        super(parent);
        display = parent;
        setLocationRelativeTo(parent);
        setLayout(new FlowLayout());
        setTitle("Editing project settings...");

        setPreferredSize(new Dimension(512, 178));
        setMinimumSize(new Dimension(512, 178));

        final JLabel prjNameLabel = new JLabel("Project name : ");
        add(prjNameLabel);
        final JTextField name = new JTextField(35);
        name.setText(parent.prjName);
        add(name);

        final JLabel prjGameLabel = new JLabel("Game name : ");
        add(prjGameLabel);
        final JTextField gameName = new JTextField(35);
        gameName.setText(parent.prjGameName);
        add(gameName);

        final JLabel prjCompLabel = new JLabel("Project compile dir : ");
        add(prjCompLabel);
        final JTextField compDir = new JTextField(30);
        compDir.setText(parent.prjCompileDir);
        add(compDir);

        final JButton setCompileDirectory = new JButton("...");
        setCompileDirectory.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FolderFilter());
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnValue = fileChooser.showOpenDialog(DialogProjectSettings.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    if (Utilities.getFileExtention(selectedFile) != null) {
                        JOptionPane.showMessageDialog(DialogProjectSettings.this, "You must choose the directory where to compile the assets file. \nOnly directories are allowed...", "SLDT's AssetsManager", JOptionPane.ERROR_MESSAGE);
                    }

                    compDir.setText(selectedFile.toString());
                }
            }
        });
        add(setCompileDirectory);

        JButton ok = new JButton("  Ok  ");
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (display.projectFile != null && display.projectFile.exists()) {
                    display.prjName = name.getText();
                    display.prjCompileDir = compDir.getText();
                    File newRacine = new File(display.projectFile.getParentFile().getParentFile() + File.separator + display.prjName + File.separator);
                    File newPrjFile = new File(newRacine + File.separator + "project.assets.prj");

                    boolean b = display.projectFile.getParentFile().renameTo(newRacine);
                    Main.log.warning("User requested rename project directory ; exit code is : " + b);

                    Map<String, String> prjInfos = new HashMap<String, String>();
                    prjInfos.put("NAME", display.prjName);
                    prjInfos.put("DIR", newRacine + File.separator + "content");
                    prjInfos.put("COMPILE_DIR", compDir.getText());
                    prjInfos.put("GAME_NAME", gameName.getText());
                    ProjectManager.saveProjectInfos(newPrjFile.toString(), prjInfos);
                    display.onProjectOppened(prjInfos);
                    display.resetTree(new File(display.prjDir), display.prjName);
                }
                dispose();
            }
        });
        add(ok);

        setResizable(false);
    }
}
