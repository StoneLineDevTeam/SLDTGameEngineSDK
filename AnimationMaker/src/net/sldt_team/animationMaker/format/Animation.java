package net.sldt_team.animationMaker.format;

import net.sldt_team.animationMaker.Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Animation {

    public List<TextureFrame> textures;
    public int interval;
    private int currentTexture = 0;
    private boolean canIncrase = true;
    public EnumAnimationType animationType;
    public boolean isUsingUV;

    private int ticks;

    //AWT Adaption Start
    private Map<String, Image> imgMap = new HashMap<String, Image>();
    //AWT Adaption End

    public Animation(int i, EnumAnimationType type, TextureFrame[] frames, boolean uv){
        interval = i;
        animationType = type;
        textures = new ArrayList<TextureFrame>();
        Collections.addAll(textures, frames);
        isUsingUV = uv;
    }

    public EnumAnimationType getType(){
        return animationType;
    }

    /**
     * Returns the interval of time between frame change
     */
    public int getInterval(){
        return interval;
    }

    /**
     * Sets the frame to render (work only for STATE_RENDERED types)
     */
    public void setCurrentFrameID(String id){
        int i = 0;
        for (TextureFrame tex : textures){
            if (tex.id.equals(id)){
                break;
            }
            i++;
        }
        if (animationType == EnumAnimationType.STATE_RENDERED && i < textures.size()){
            TextureFrame frame = textures.get(i);
            if (frame != null) {
                currentTexture = i;
            }
        }
    }

    private void nextTexture(){
        if (animationType == EnumAnimationType.ONEWAY_RENDERED) {
            if ((currentTexture + 1) < textures.size()) {
                currentTexture++;
            } else {
                currentTexture = 0;
            }
        } else if (animationType == EnumAnimationType.TWOWAY_RENDERED){
            if (currentTexture <= 0 || !(currentTexture >= (textures.size() - 1)) && canIncrase) {
                currentTexture++;
            } else {
                canIncrase = false;
                currentTexture--;
                if (currentTexture <= 0) {
                    canIncrase = true;
                }
            }
        } else if (animationType == EnumAnimationType.FONT_RENDERED){
            if ((currentTexture + 1) < textures.size()) {
                currentTexture++;
            } else {
                currentTexture = 0;
            }
        }
    }

    //AWT Adaption Start
    /**
     * Returns the current texture that is being drawn (used by animated fonts)
     */
    public Image getCurrentTexture(){
        if (imgMap.containsKey(getCurrentFrame().path)){
            return imgMap.get(getCurrentFrame().path);
        }
        try {
            Image img = ImageIO.read(new File(getCurrentFrame().path));
            imgMap.put(getCurrentFrame().path, img);
            return img;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    //AWT Adaption End

    protected TextureFrame getCurrentFrame(){
        return textures.get(currentTexture);
    }

    /**
     * Updates this animation
     */
    public void update(){
        if (animationType == EnumAnimationType.STATE_RENDERED){
            return;
        }
        ticks++;
        if (ticks >= getInterval()){
            nextTexture();
            ticks = 0;
        }
    }

    //AWT Adaption Start
    /**
     * Renders this animation (args : Instance of RenderEngine, X-Coord, Y-Coord, Width, Height)
     */
    public void render(Graphics g, int x, int y, int width, int height){
        TextureFrame frame = getCurrentFrame();
        Image i = getCurrentTexture();
        if (i == null){
            Main.log.warning("Unable to render format : 'Unexpected Error Calling renderEngine.loadTexture' !");
            return;
        }
        g.setColor(frame.color);
        g.drawImage(i, x, y, width, height, null);
    }
    //AWT Adaption End
}
