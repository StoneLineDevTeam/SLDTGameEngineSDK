package net.sldt_team.animationMaker.gameEngineToAWT;

import net.sldt_team.animationMaker.format.AWTAnimationLoader;
import net.sldt_team.animationMaker.format.TextureFrame;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class TextureFrameListSelectionHandler implements ListSelectionListener {

    private PropertyTableModel tableModel;
    private JList parentList;

    public TextureFrameListSelectionHandler(JList l, PropertyTableModel p){
        tableModel = p;
        parentList = l;
    }

    public void valueChanged(ListSelectionEvent e) {
        int i = parentList.getSelectedIndex();
        TextureFrame frame = AWTAnimationLoader.currentOpenedAnimation.textures.get(i);
        if (frame != null){
            tableModel.removeProperty("Color");
            tableModel.removeProperty("Alpha");
            tableModel.removeProperty("Scale");
            tableModel.removeProperty("Texture Path");
            tableModel.removeProperty("ID");
            if (AWTAnimationLoader.currentOpenedAnimation.isUsingUV) {
                tableModel.removeProperty("UV");
            }

            tableModel.addProperty("Color", "{" + frame.color.getRed() + "," + frame.color.getGreen() + "," + frame.color.getBlue() + "}");
            tableModel.addProperty("Alpha", frame.color.getAlpha());
            tableModel.addProperty("Scale", frame.scale);
            tableModel.addProperty("Texture Path", frame.path);
            tableModel.addProperty("ID", frame.id);
            if (AWTAnimationLoader.currentOpenedAnimation.isUsingUV) {
                tableModel.addProperty("UV", "{" + frame.uCoord + "," + frame.vCoord + "," + frame.u1Coord + "," + frame.v1Coord + "}");
            }
        }
    }
}
