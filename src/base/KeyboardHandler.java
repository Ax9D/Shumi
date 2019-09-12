package base;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;


public class KeyboardHandler {
	 static long window;
	 public static boolean isPressed(int key)
	 {
		 return glfwGetKey(window, key)==GLFW_PRESS;
	 }
}
