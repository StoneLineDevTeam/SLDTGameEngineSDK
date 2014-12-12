package net.sldt_team.assetsManager.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DialogStringRequest extends JDialog {

    private int exitCode;
    private String choosedString;

    private JFrame theParent;

    public DialogStringRequest(final JFrame parent, String title, String content){
        super(parent, title, true);
        theParent = parent;
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(parent);
        setLayout(new FlowLayout());
        setResizable(false);

        setPreferredSize(new Dimension(512, 178));
        setMinimumSize(new Dimension(512, 178));

        JLabel contentLabel = new JLabel(content);
        add(contentLabel);
        final JTextField field = new JTextField(49);
        add(field);

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exitCode = 1;
                choosedString = null;
                parent.setEnabled(true);
                dispose();
            }
        });
        add(cancel);

        JButton ok = new JButton("  Ok  ");
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exitCode = 0;
                choosedString = field.getText();
                parent.setEnabled(true);
                dispose();
            }
        });
        add(ok);
    }

    public void showDialog(){
        theParent.setEnabled(false);
        setVisible(true);
    }

    public int getExitCode(){
        return exitCode;
    }

    public String getExitValue(){
        return choosedString;
    }
}
