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
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TreeFileDisplayPanel extends JPanel implements TreeSelectionListener, MouseListener, MouseMotionListener {

    private Display display;

    public Image imagePath;

    private int x = 0;
    private int y = 0;

    public int oldX;
    public int oldY;

    private int ticks = 0;

    public TreeFileDisplayPanel(Display disp){
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
            boolean flag = false;
            try {
                imagePath = ImageIO.read(theFile);
                if (imagePath == null){
                    flag = true;
                }
                repaint();
            } catch (IOException e1) {
                Main.log.warning("Unable to read image file...");
                flag = true;
            }
            if (flag) {
                try {
                    //Image image = new BufferedImage()
                    List<String> list = new ArrayList<String>();
                    BufferedReader reader = new BufferedReader(new FileReader(theFile));
                    String cur;
                    while ((cur = reader.readLine()) != null){
                        cur = cur.replace("\t", "       ");
                        list.add(cur);
                    }
                    reader.close();
                    imagePath = new BufferedImage(1024, (list.size() + 1) * 16, BufferedImage.TYPE_BYTE_GRAY);
                    Graphics g = imagePath.getGraphics();
                    for (int i = 0 ; i < list.size() ; i++){
                        String s2222222 = list.get(i);
                        g.drawString(s2222222, 0, (i + 1) * 16);
                    }
                } catch (Exception e2) {
                    Main.log.warning("Unable to read file...");
                }
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
