package net.sldt_team.animationMaker.format;

import java.awt.*;

public class TextureFrame {

    public Color color;
    public float scale;
    public String path;
    public String id;

    public final float uCoord;
    public final float vCoord;
    public final float u1Coord;
    public final float v1Coord;

    public TextureFrame(Color c, float s, String p, String n){
        color = c;
        scale = s;
        path = p;
        id = n;

        uCoord = -1;
        u1Coord = -1;
        vCoord = -1;
        v1Coord = -1;
    }

    public TextureFrame(Color c, float s, String p, String n, float u, float v, float u1, float v1){
        color = c;
        scale = s;
        path = p;
        id = n;

        uCoord = u;
        u1Coord = u1;
        vCoord = v;
        v1Coord = v1;
    }

    public String toString(){
        return id;
    }
}
