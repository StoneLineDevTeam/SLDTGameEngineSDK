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

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        final JLabel prjNameLabel = new JLabel("Project name : ");
        p.add(prjNameLabel);
        final JTextField name = new JTextField(35);
        name.setText(parent.prjName);
        p.add(name);

        main.add(p);

        JPanel p1 = new JPanel();
        p1.setLayout(new FlowLayout());
        final JLabel prjGameLabel = new JLabel("Game name : ");
        p1.add(prjGameLabel);
        final JTextField gameName = new JTextField(35);
        gameName.setText(parent.prjGameName);
        p1.add(gameName);

        main.add(p1);

        JPanel p2 = new JPanel();
        p2.setLayout(new FlowLayout());
        final JLabel prjCompLabel = new JLabel("Project compile dir : ");
        p2.add(prjCompLabel);
        final JTextField compDir = new JTextField(30);
        compDir.setText(parent.prjCompileDir);
        p2.add(compDir);
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
        p2.add(setCompileDirectory);

        main.add(p2);

        JPanel p3 = new JPanel();
        p3.setLayout(new FlowLayout());
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

                    if (newPrjFile.exists()){
                        newPrjFile.delete();
                    }

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
        p3.add(ok);
        JButton cancel = new JButton("  Cancel  ");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        p3.add(cancel);

        main.add(p3);

        add(main);

        setResizable(false);
    }
}
