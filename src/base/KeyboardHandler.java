package base;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;


public class KeyboardHandler {

	static ArrayList<KeyEventFn> keyEventFns;
	static
	{
        keyEventFns=new ArrayList<KeyEventFn>();
	}

	 public static boolean isPressed(int key)
	 {
         return glfwGetKey(WindowInfo.window, key)==GLFW_PRESS;
	 }
	public static boolean isReleased(int key)
	{
        return glfwGetKey(WindowInfo.window, key)==GLFW_RELEASE;
	}
    public static void addEventListener(KeyEventFn fn)
    {
        keyEventFns.add(fn);
    }
    public static void handleKeyEvents(int key,int action)
    {
        for(KeyEventFn fn:keyEventFns) {
            fn.execute(key,action);
        }

    }
}
