package game;

import base.Game;
import base.WindowInfo;
import editor.Editor;
import input_handling.KeyboardHandler;
import input_handling.MouseHandler;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {
    public static boolean doEditor = true;

    public static void init() {
        if (!glfwInit()) {
            fail("failed to initialize GLFW");
        }
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        WindowInfo.window = glfwCreateWindow(WindowInfo.WIDTH, WindowInfo.HEIGHT, "", 0, 0);
        if (WindowInfo.window == 0) {
            fail("Failed to crete Window");
        }
        glfwMakeContextCurrent(WindowInfo.window);
        GL.createCapabilities();

        glfwSwapInterval(1);

        glfwSetKeyCallback(WindowInfo.window, (w, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(WindowInfo.window, true);

            //KeyboardHandler.updateKeyState(key,action);
            KeyboardHandler.handleKeyEvents(key, action);
        });
        glfwSetCursorPosCallback(WindowInfo.window, (w, xpos, ypos) -> {
            MouseHandler.mouseX = (float) xpos;
            MouseHandler.mouseY = (float) ypos;

            //Edit.
        });
        glfwSetMouseButtonCallback(WindowInfo.window, (w, button, action, mods) ->
        {
            MouseHandler.handleButtonEvents(button, action);
        });
        glfwSetScrollCallback(WindowInfo.window, (w, xoff, yoff) -> {
            MouseHandler.handleScrollEvents(yoff);
        });


        System.out.println(GL11.glGetString(GL11.GL_VERSION));

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        //glEnable(GL_FRAMEBUFFER_SRGB);
    }

    public static void main(String[] args) {
        init();


        Game g = new Game();
        glfwShowWindow(WindowInfo.window);
        glViewport(0, 0, WindowInfo.WIDTH, WindowInfo.HEIGHT);
        if (doEditor) {
            Editor e = new Editor(g);
            while (!glfwWindowShouldClose(WindowInfo.window)) {
                //glfwSetWindowTitle(WindowInfo.window, "FPS: " + Math.round(1 / Game.tDelta));

                e.run();

                glfwPollEvents();
                glfwSwapBuffers(WindowInfo.window);
            }
        } else {

            //glClearColor(0.64f,0.64f,0.64f,1.0f);
            while (!glfwWindowShouldClose(WindowInfo.window)) {
                //glfwSetWindowTitle(WindowInfo.window, "FPS: " + Math.round(1 / Game.tDelta));

                g.run();

                glfwPollEvents();
                glfwSwapBuffers(WindowInfo.window);
            }
        }
        g.exit();
    }

    public static void fail(String error) {
        System.out.println(error);
        System.exit(-1);
    }
}