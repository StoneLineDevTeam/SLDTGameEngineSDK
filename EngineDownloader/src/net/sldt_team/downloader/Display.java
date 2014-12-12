package net.sldt_team.downloader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Display extends JFrame {

    public Display() {
        super("Engine Downloader");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());
        setSize(512, 256);
        setResizable(false);

        final JList list = new JList();
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        List<String> releaseData = null;
        try {
            releaseData = SLDTGameEngineDownloader.getGameEngineVersionList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert releaseData != null;
        list.setListData(releaseData.toArray());
        JScrollPane pane = new JScrollPane(list);

        JButton b = new JButton("Download Selected Version");
        b.setMnemonic('D');
        final List<String> finalReleaseData = releaseData;
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int id = list.getSelectedIndex();
                String obj = finalReleaseData.get(id);

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FolderFilter());
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnValue = fileChooser.showOpenDialog(Display.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    try {
                        SLDTGameEngineDownloader.downloadGameEngineFromGitHub(obj, fileChooser.getSelectedFile());
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }
        });

        add(pane, BorderLayout.CENTER);
        add(b, BorderLayout.PAGE_END);

        setVisible(true);
    }

}

class FolderFilter extends javax.swing.filechooser.FileFilter {
    public boolean accept(File file) {
        return file.isDirectory();
    }

    public String getDescription() {
        return "We only take directories";
    }
}