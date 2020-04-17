package base;

import UI.Frame;
import org.joml.Vector2f;

public class WindowInfo {
    public static long window;
    public static int WIDTH=1920;
    public static int HEIGHT=1080;

    public static Frame windowFrame=new Frame(new Vector2f(0,0),1,1);
    public static void updateWindow(int WIDTH,int HEIGHT)
    {
        WindowInfo.WIDTH=WIDTH;
        WindowInfo.HEIGHT=HEIGHT;
    }
}
