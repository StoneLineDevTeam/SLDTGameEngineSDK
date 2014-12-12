package net.sldt_team.doc;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.io.IOException;

public class Display extends JFrame {

    public Display(){
        super("Engine Documentation");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());
        setSize(1024, 768);

        final JEditorPane tp = new JEditorPane();
        try {
            tp.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
            tp.setEditable(false);
            tp.setEnabled(true);
            tp.setPage("http://engine2d.sldt-team.net/docs/");
            tp.addHyperlinkListener(new HyperlinkListener() {
                public void hyperlinkUpdate(HyperlinkEvent e) {
                    if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                        try {
                            tp.setPage(e.getURL());
                        } catch (IOException e1) {
                            Main.log.warning("Unable to redirect !");
                        }
                    }
                }
            });
        } catch (IOException e) {
            Main.log.warning("Unable to redirect !");
        }
        JScrollPane pane = new JScrollPane(tp);

        add(pane, BorderLayout.CENTER);

        setVisible(true);
    }

}
