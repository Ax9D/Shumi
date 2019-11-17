package base;

import game.Main;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class WindowInfo {
    public static final int WIDTH=800;
    public static final int HEIGHT=600;
    public static float mouseX,mouseY;

    public static Vector4f getGLScreenPos()
    {
        float glPosX=(mouseX/ WIDTH)*2 - 1.0f;
        float glPosY=-(mouseY/HEIGHT)*2+1.0f;

        return new Vector4f(glPosX,glPosY,0,1);
    }
}
