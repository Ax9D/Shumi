package base;

import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyState {
    int keycode;
    /*enum eventType{
        PRESS,
        RELEASE
    }
    eventType et;


     */
    int action;
    long time;
    public KeyState(int keycode)
    {
        this.keycode=keycode;
        action= GLFW_RELEASE;
        time=0;
    }
    public void updateKeyState(int action,long time)
    {
        this.action=action;
        this.time=time;
    }
}
