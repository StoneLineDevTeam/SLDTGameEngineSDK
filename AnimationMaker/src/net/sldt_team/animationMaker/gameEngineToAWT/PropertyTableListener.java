package net.sldt_team.animationMaker.gameEngineToAWT;

import net.sldt_team.animationMaker.Display;
import net.sldt_team.animationMaker.format.AWTAnimationLoader;
import net.sldt_team.animationMaker.format.EnumAnimationType;
import net.sldt_team.animationMaker.format.TextureFrame;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;

public class PropertyTableListener implements TableModelListener{

    private Display theDisplay;
    public PropertyTableListener(Display d){
        theDisplay = d;
    }

    private int previousSelectedIndex = -1;

    public void tableChanged(TableModelEvent e) {
        if (e.getType() == TableModelEvent.UPDATE){
            if (e.getSource() == theDisplay.mainTableModel){
                if (theDisplay.mainTableModel.getPropertyValue("Interval") != null && theDisplay.mainTableModel.getPropertyValue("Interval") instanceof String) {
                    String interval = (String) theDisplay.mainTableModel.getPropertyValue("Interval");
                    AWTAnimationLoader.currentOpenedAnimation.interval = Integer.parseInt(interval);
                }

                if (theDisplay.mainTableModel.getPropertyValue("Using UV") != null && theDisplay.mainTableModel.getPropertyValue("Using UV") instanceof String) {
                    String b = (String) theDisplay.mainTableModel.getPropertyValue("Using UV");
                    AWTAnimationLoader.currentOpenedAnimation.isUsingUV = Boolean.parseBoolean(b);
                }

                if (theDisplay.mainTableModel.getPropertyValue("Type") != null) {
                    String type = (String) theDisplay.mainTableModel.getPropertyValue("Type");
                    if (type.equals("ONEWAY_RENDERED")) {
                        AWTAnimationLoader.currentOpenedAnimation.animationType = EnumAnimationType.ONEWAY_RENDERED;
                    } else if (type.equals("TWOWAY_RENDERED")) {
                        AWTAnimationLoader.currentOpenedAnimation.animationType = EnumAnimationType.TWOWAY_RENDERED;
                    } else if (type.equals("STATE_RENDERED")) {
                        AWTAnimationLoader.currentOpenedAnimation.animationType = EnumAnimationType.STATE_RENDERED;
                    } else if (type.equals("FONT_RENDERED")) {
                        AWTAnimationLoader.currentOpenedAnimation.animationType = EnumAnimationType.FONT_RENDERED;
                    }
                }
            } else if (e.getSource() == theDisplay.tableModel) {
                int i = theDisplay.theList.getSelectedIndex();
                if (previousSelectedIndex != i) {
                    TextureFrame frame = AWTAnimationLoader.currentOpenedAnimation.textures.get(i);
                    if (frame == null) {
                        return;
                    }

                    if (theDisplay.tableModel.getPropertyValue("Color") != null && theDisplay.tableModel.getPropertyValue("Color") instanceof String) {
                        String color = (String) theDisplay.tableModel.getPropertyValue("Color");

                        color = color.substring(1, color.length() - 1);
                        String[] rgb = color.split(",");
                        int a = frame.color.getAlpha();
                        frame.color = new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]), a);
                    }
                    if (theDisplay.tableModel.getPropertyValue("Alpha") != null && theDisplay.tableModel.getPropertyValue("Alpha") instanceof String) {
                        String alpha = (String) theDisplay.tableModel.getPropertyValue("Alpha");

                        int r = frame.color.getRed();
                        int g = frame.color.getGreen();
                        int b = frame.color.getBlue();
                        frame.color = new Color(r, g, b, Integer.parseInt(alpha));
                    }
                    if (theDisplay.tableModel.getPropertyValue("Scale") != null && theDisplay.tableModel.getPropertyValue("Scale") instanceof String) {
                        String scale = (String) theDisplay.tableModel.getPropertyValue("Scale");

                        frame.scale = Float.parseFloat(scale);
                    }
                    if (theDisplay.tableModel.getPropertyValue("Texture Path") != null && theDisplay.tableModel.getPropertyValue("Texture Path") instanceof String) {
                        String s = (String) theDisplay.tableModel.getPropertyValue("Texture Path");

                        frame.path = s;
                    }
                    if (theDisplay.tableModel.getPropertyValue("ID") != null && theDisplay.tableModel.getPropertyValue("ID") instanceof String) {
                        String s = (String) theDisplay.tableModel.getPropertyValue("ID");

                        frame.id = s;
                    }
                    if (AWTAnimationLoader.currentOpenedAnimation.isUsingUV) {
                        if (theDisplay.tableModel.getPropertyValue("UV") != null && theDisplay.tableModel.getPropertyValue("UV") instanceof String) {
                            String color = (String) theDisplay.tableModel.getPropertyValue("UV");

                            color = color.substring(1, color.length() - 1);
                            String[] rgb = color.split(",");
                            int a = frame.color.getAlpha();
                            frame.color = new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]), a);
                        }
                    }

                    previousSelectedIndex = i;
                }
            }
            theDisplay.regenScript();
        }
    }

}
