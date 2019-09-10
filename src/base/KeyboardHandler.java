package base;

import org.lwjgl.glfw.*;
import static org.lwjgl.glfw.GLFW.*;


public class KeyboardHandler {
	 static long window;
	 public static boolean isPressed(int key)
	 {
		 return glfwGetKey(window, key)==GLFW_PRESS;
	 }
}
