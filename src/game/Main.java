package game;

import base.Game;
import base.KeyboardHandler;
import base.MouseHandler;
import base.WindowInfo;
import editor.Editor;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.windows.WINDOWPLACEMENT;

import java.awt.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {
	public static boolean doEditor=true;

	public static void init()
	{
		if(!glfwInit())
		{
			fail("failed to initialize GLFW");
		}
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		WindowInfo.window=glfwCreateWindow(WindowInfo.WIDTH, WindowInfo.HEIGHT, "", 0, 0);
		if(WindowInfo.window==0)
		{
			fail("Failed to crete Window");
		}
		glfwMakeContextCurrent(WindowInfo.window);
		GL.createCapabilities();

		glfwSwapInterval(1);

		glfwSetKeyCallback(WindowInfo.window, (w, key, scancode, action, mods)->{
			if(key==GLFW_KEY_ESCAPE && action==GLFW_RELEASE)
				glfwSetWindowShouldClose(WindowInfo.window, true);

			//KeyboardHandler.updateKeyState(key,action);
			KeyboardHandler.handleKeyEvents(key,action);
		});
		glfwSetCursorPosCallback(WindowInfo.window,(w,xpos,ypos)->{
			MouseHandler.mouseX=(float)xpos;
			MouseHandler.mouseY=(float)ypos;
		});
		glfwSetMouseButtonCallback(WindowInfo.window,(w,button,action,mods)->
		{
			MouseHandler.handleButtonEvents(button,action);
		});


		System.out.println(GL11.glGetString(GL11.GL_VERSION));

		KeyboardHandler.addEventListener((key,action)->{if(key==GLFW_KEY_TAB && action==GLFW_RELEASE)System.out.println("TAB DESU YO");});

		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);
	}
	public static void main(String[] args)
	{
		init();


		Game g = new Game();
		glfwShowWindow(WindowInfo.window);
		if(doEditor)
		{
			Editor e=new Editor(g);
			while (!glfwWindowShouldClose(WindowInfo.window)) {
				glfwSetWindowTitle(WindowInfo.window, "FPS: " + Math.round(1 / Game.tDelta));

				e.run();

				glfwPollEvents();
				glfwSwapBuffers(WindowInfo.window);
			}
		}
		else {

			//glClearColor(0.64f,0.64f,0.64f,1.0f);
			while (!glfwWindowShouldClose(WindowInfo.window)) {
				glfwSetWindowTitle(WindowInfo.window, "FPS: " + Math.round(1 / Game.tDelta));

				g.run();

				glfwPollEvents();
				glfwSwapBuffers(WindowInfo.window);
			}
		}
		g.exit();
	}
	public static void fail(String error)
	{
		System.out.println(error);
		System.exit(-1);
	}
}