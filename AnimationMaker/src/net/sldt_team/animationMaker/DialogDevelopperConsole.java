package net.sldt_team.animationMaker;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DialogDevelopperConsole extends JDialog {

    private JTextPane thePane;

    public DialogDevelopperConsole(Display parent) {
        super(parent);
        if (Main.developperConsole == null){
            Main.developperConsole = this;
        }

        setLocationRelativeTo(parent);
        setLayout(null);
        setTitle("View development console...");

        setPreferredSize(new Dimension(532, 532));
        setMinimumSize(new Dimension(532, 532));

        setResizable(false);


        JTextPane area = new JTextPane();
        area.setSize(500, 500);
        area.setLocation(12 , 12);
        area.setEditable(false);

        updateDialog(area);
        thePane = area;

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Main.developperConsole = null;
                dispose();
                Runtime.getRuntime().gc();
            }
        });

        JScrollPane pane = new JScrollPane(area);
        pane.setSize(500, 480);
        pane.setLocation(12, 12);
        add(pane);
    }

    private void updateDialog(JTextPane area){
        for (String s : Main.programLogs){
            if (s.contains("#RED#")) {
                s = s.replace("#RED#", "");
                appendToPane(area, s + "\n", Color.RED);
            } else if (s.contains("#BLUE#")) {
                s = s.replace("#BLUE#", "");
                appendToPane(area, s + "\n", Color.BLUE);
            } else if (s.contains("#YELLOW#")) {
                s = s.replace("#YELLOW#", "");
                appendToPane(area, s + "\n", Color.ORANGE);
            } else if (s.contains("#BLACK#")) {
                s = s.replace("#BLACK#", "");
                appendToPane(area, s + "\n", Color.BLACK);
            }
        }
    }

    public void addLogLine(String s){
        if (s.contains("#RED#")) {
            s = s.replace("#RED#", "");
            appendToPane(thePane, s + "\n", Color.RED);
        } else if (s.contains("#BLUE#")) {
            s = s.replace("#BLUE#", "");
            appendToPane(thePane, s + "\n", Color.BLUE);
        } else if (s.contains("#YELLOW#")) {
            s = s.replace("#YELLOW#", "");
            appendToPane(thePane, s + "\n", Color.ORANGE);
        } else if (s.contains("#BLACK#")) {
            s = s.replace("#BLACK#", "");
            appendToPane(thePane, s + "\n", Color.BLACK);
        }
    }

    private void appendToPane(JTextPane tp, String msg, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

         StyledDocument doc = tp.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), msg, aset);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
