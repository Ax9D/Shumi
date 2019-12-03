package base;

import org.joml.Vector4f;
import static org.lwjgl.glfw.GLFW.*;

public class MouseHandler {

    public static float mouseX, mouseY;

    public static Vector4f getGLScreenPos() {
        float glPosX = (mouseX / WindowInfo.WIDTH) * 2 - 1;
        float glPosY = -(mouseY / WindowInfo.HEIGHT) * 2 + 1;

        return new Vector4f(glPosX, glPosY, 0, 1);
    }

    public static boolean mouseLeftClicked()
    {
        return glfwGetMouseButton(WindowInfo.window,GLFW_MOUSE_BUTTON_LEFT)==GLFW_PRESS;
    }
}
