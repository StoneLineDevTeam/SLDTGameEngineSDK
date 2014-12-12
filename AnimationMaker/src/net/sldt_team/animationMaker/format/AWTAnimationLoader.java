package net.sldt_team.animationMaker.format;

import java.io.File;

public class AWTAnimationLoader {

    public static Animation currentOpenedAnimation = null;

    public static Animation loadAnimationFile(File file){
        String str = file.toString();
        str = str.substring(0, str.lastIndexOf('.'));
        AnimationFile f = new AnimationFile(str);
        return f.loadFile();
    }

    public static String generateAnimationScript(){
        String finalScript = "{\n";
        String interval = "    interval: " + currentOpenedAnimation.getInterval() + ",\n";
        finalScript += interval;
        String type = "    type: " + currentOpenedAnimation.getType() + ",\n";
        finalScript += type;
        finalScript += "    frames: [\n";

        for (TextureFrame frame : currentOpenedAnimation.textures){
            String curFrame = "";
            if (currentOpenedAnimation.isUsingUV){
                curFrame = "         {scale: " + frame.scale + ", color: {r: " + frame.color.getRed() + ", g: " + frame.color.getGreen() + ", b: " + frame.color.getBlue() + ", a: " + frame.color.getAlpha() + "}, path: \"" + frame.path + "\", id: \"" + frame.id + "\", uv: {u: " + frame.uCoord + ", v: " + frame.vCoord + ", u1: " + frame.u1Coord + ", v1: " + frame.v1Coord + "}},";
            } else {
                curFrame = "         {scale: " + frame.scale + ", color: {r: " + frame.color.getRed() + ", g: " + frame.color.getGreen() + ", b: " + frame.color.getBlue() + ", a: " + frame.color.getAlpha() + "}, path: \"" + frame.path + "\", id: \"" + frame.id + "\"},";
            }
            finalScript += curFrame;
            finalScript += "\n";
        }
        finalScript = finalScript.substring(0, finalScript.length() - 3);
        finalScript += "}";
        finalScript += "\n";

        finalScript += "    ]\n";
        finalScript += "}";

        return finalScript;
    }
}
