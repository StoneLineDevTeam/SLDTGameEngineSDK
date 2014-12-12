package net.sldt_team.animationMaker;

import net.sldt_team.animationMaker.format.AWTAnimationLoader;
import net.sldt_team.animationMaker.format.Animation;
import net.sldt_team.animationMaker.gameEngineToAWT.PropertyTableListener;
import net.sldt_team.animationMaker.gameEngineToAWT.PropertyTableModel;
import net.sldt_team.animationMaker.gameEngineToAWT.TextureFrameListSelectionHandler;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Display extends JFrame {

    public PropertyTableModel tableModel;
    public PropertyTableModel mainTableModel;
    private JTextArea scriptArea;
    public JList theList;

    public Display(){
        super("SLDT's AnimationMaker");
        setLayout(new BorderLayout());

        JMenuBar bar = new JMenuBar();
        bar.add(getFileMenu());
        bar.add(getHelpMenu());
        setJMenuBar(bar);

        setSize(1024, 768);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(true);

        JPanel tblPanel = new JPanel();
        tblPanel.setLayout(new BorderLayout());

        JTable tbl = getAnimationFramesPropertyTable();

        JList list = getAnimationFramesTable();
        theList = list;
        list.addListSelectionListener(new TextureFrameListSelectionHandler(theList, tableModel));
        JScrollPane pane = new JScrollPane(list);
        tblPanel.add(pane, BorderLayout.LINE_END);

        JScrollPane pane1 = new JScrollPane(tbl);
        pane1.setPreferredSize(new Dimension(256, 200));
        tblPanel.add(pane1, BorderLayout.PAGE_END);

        add(tblPanel, BorderLayout.LINE_END);

        add(getMainTabPanel(), BorderLayout.CENTER);

        setVisible(true);
    }

    public void openAnimationFile(Animation anim){
        AWTAnimationLoader.currentOpenedAnimation = anim;
        theList.setListData(anim.textures.toArray());

        mainTableModel.removeProperty("Interval");
        mainTableModel.removeProperty("Type");
        mainTableModel.removeProperty("Using UV");

        mainTableModel.addProperty("Interval", anim.getInterval());
        mainTableModel.addProperty("Type", anim.getType().toString());
        mainTableModel.addProperty("Using UV", anim.isUsingUV);
    }

    public void regenScript(){
        scriptArea.setText(AWTAnimationLoader.generateAnimationScript());
    }

    private JMenu getFileMenu(){
        JMenu file = new JMenu("File");
        file.setMnemonic('F');

        JMenuItem open = new JMenuItem("Open File");
        open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Animation file (.anim)", "anim", "SLDT's GameEngine animation file");
                fileChooser.setFileFilter(filter);
                fileChooser.setAcceptAllFileFilterUsed(false);
                int returnValue = fileChooser.showOpenDialog(Display.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    Animation anim = AWTAnimationLoader.loadAnimationFile(selectedFile);

                    openAnimationFile(anim);
                }
            }
        });

        file.add(open);

        return file;
    }

    private JMenu getHelpMenu(){
        JMenu help = new JMenu("?");
        help.setMnemonic('?');

        JMenuItem about = new JMenuItem("About");
        about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(Display.this, "SLDT's GameEngine - AnimationMaker \n Program developped by SLDT, as an utility for easly make format files used by SLDT's GameEngine.", "SLDT's AnimationMaker", JOptionPane.INFORMATION_MESSAGE);
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

    private JList getAnimationFramesTable(){
        JList list = new JList();
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return list;
    }

    private JTable getAnimationFramesPropertyTable(){
        JTable table = new JTable();
        PropertyTableModel model = new PropertyTableModel();
        tableModel = model;
        table.setModel(model);
        table.getModel().addTableModelListener(new PropertyTableListener(this));
        return table;
    }

    private JTabbedPane getMainTabPanel(){
        JTabbedPane pane = new JTabbedPane();

        JScrollPane pane1 = new JScrollPane(getAnimationPropertyTable());
        pane.add("Animation Properties", pane1);

        JScrollPane pane2 = new JScrollPane(getScriptArea());
        pane.add("Animation Script", pane2);

        return pane;
    }

    private JTable getAnimationPropertyTable(){
        JTable table = new JTable();
        PropertyTableModel model = new PropertyTableModel();
        mainTableModel = model;
        table.setModel(model);
        table.getModel().addTableModelListener(new PropertyTableListener(this));
        return table;
    }

    private JTextArea getScriptArea(){
        JTextArea area = new JTextArea();
        scriptArea = area;
        return area;
    }
}
