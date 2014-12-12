package net.sldt_team.animationMaker.format;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.sldt_team.animationMaker.Main;

import java.awt.*;
import java.io.File;
import java.io.FileReader;

public class AnimationFile {

    private File fileToLoad;

    protected AnimationFile(String file){
        fileToLoad = new File(file + ".anim");
        if (!fileToLoad.exists()){
            Main.log.severe("Failed to load animation " + file + " : no such file.");
            fileToLoad = null;
        }
    }

    protected Animation loadFile(){
        try {
            Main.log.info("Loading animation...");
            Gson gson = new GsonBuilder().create();
            AbstractAnimationFile file = gson.fromJson(new FileReader(fileToLoad), AbstractAnimationFile.class);
            TextureFrame[] frames = new TextureFrame[file.frames.length];
            if (file.frames.length == 0){
                Main.log.severe("Failed to load animation : Animation frames must have, at least, 1 entry !");
                return null;
            }
            for (int i = 0 ; i < file.frames.length ; i++){
                AbstractFrameColor c = file.frames[i].color;
                if (file.usingUV) {
                    AbstractFrameUV uvCoords = file.frames[i].uv;
                    frames[i] = new TextureFrame(new Color(c.r, c.g, c.b, c.a), file.frames[i].scale, file.frames[i].path, file.frames[i].id, uvCoords.u, uvCoords.v, uvCoords.u1, uvCoords.v1);
                } else {
                    frames[i] = new TextureFrame(new Color(c.r, c.g, c.b, c.a), file.frames[i].scale, file.frames[i].path, file.frames[i].id);
                }
                Main.log.info("Frame found : " + frames[i].toString());
            }
            Animation anim =  new Animation(file.interval, file.type, frames, file.usingUV);
            Main.log.info("Interval is " + anim.getInterval());
            Main.log.info("Animation successfully loaded !");
            return anim;
        } catch (Exception e) {
            Main.log.severe("Failed to load animation : " + e.getClass().getName() + "_" + e.getMessage());
        }
        return null;
    }
}
