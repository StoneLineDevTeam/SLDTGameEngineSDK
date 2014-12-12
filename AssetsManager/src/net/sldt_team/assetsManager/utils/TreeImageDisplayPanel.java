package net.sldt_team.assetsManager.utils;

import net.sldt_team.assetsManager.Display;
import net.sldt_team.assetsManager.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;

public class TreeImageDisplayPanel extends JPanel implements TreeSelectionListener, MouseListener, MouseMotionListener {

    private Display display;

    public Image imagePath;

    private int x = 0;
    private int y = 0;

    public int oldX;
    public int oldY;

    private int ticks = 0;

    public TreeImageDisplayPanel(Display disp){
        display = disp;
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void valueChanged(TreeSelectionEvent e) {
        String s = e.getPath().toString();
        s = s.replace(", ", File.separator);
        s = s.replace("[", File.separator);
        s = s.replace("]", File.separator);
        s = s.replace(display.prjName, File.separator);

        File theFile = new File(display.prjDir + File.separator + s);

        if (!theFile.isDirectory()){
            try {
                imagePath = ImageIO.read(theFile);
                repaint();
            } catch (IOException e1) {
                Main.log.warning("Unable to read image file...");
            }
        } else {
            imagePath = null;
            repaint();
        }
    }

    public void paintComponent(Graphics g){
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (imagePath != null) {
            g.drawImage(imagePath, oldX, oldY, Color.GRAY, null);
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e)
    {
        x = e.getX();
        y = e.getY();
    }

    public void mouseReleased(MouseEvent e) {}

    public void mouseDragged(MouseEvent e)
    {
        if (imagePath == null){
            return;
        }

        ticks++;
        if (ticks >= 10) {
            if (contains(e.getX(), e.getY())) {
                int tmpX = oldX + e.getX() - x;
                int tmpY = oldY + e.getY() - y;

                oldX = tmpX;
                oldY = tmpY;
                repaint();
            }
            ticks = 0;
        }
    }

    public void mouseMoved(MouseEvent e) {

    }
}
