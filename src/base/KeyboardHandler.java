package base;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;


public class KeyboardHandler {
	 public static boolean isPressed(int key)
	 {
		 return glfwGetKey(WindowInfo.window, key)==GLFW_PRESS;
	 }
}
