package base;

import org.joml.Vector4f;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class MouseHandler {

    static ArrayList<MButtonEventFn> mButtonEventFns;

    public static float mouseX, mouseY;

    static{
        mButtonEventFns=new ArrayList<MButtonEventFn>();
    }


    public static Vector4f getGLScreenPos() {
        float glPosX = (mouseX / WindowInfo.WIDTH) * 2 - 1;
        float glPosY = -(mouseY / WindowInfo.HEIGHT) * 2 + 1;

        return new Vector4f(glPosX, glPosY, 0, 1);
    }
    public static void addButtonEventListener(MButtonEventFn fn)
    {
        mButtonEventFns.add(fn);
    }
    public static void handleButtonEvents(int button,int action)
    {
        for(MButtonEventFn fn:mButtonEventFns)
            fn.execute(button,action);
    }
    public static boolean mouseLeftClicked()
    {
        return glfwGetMouseButton(WindowInfo.window,GLFW_MOUSE_BUTTON_LEFT)==GLFW_PRESS;
    }
    public static boolean mouseLeftUnClicked()
    {
        return glfwGetMouseButton(WindowInfo.window,GLFW_MOUSE_BUTTON_LEFT)==GLFW_RELEASE;
    }
}
