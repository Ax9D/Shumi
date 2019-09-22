package game;

import base.Game;
import base.KeyboardHandler;
import editor.Editor;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {
	static final int WIDTH=800;
	static final int HEIGHT=600;

	static long window;
	public static void init()
	{
		if(!glfwInit())
		{
			fail("failed to initialize GLFW");
		}
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		window=glfwCreateWindow(WIDTH, HEIGHT, "", 0, 0);
		if(window==0)
		{
			fail("Failed to crete Window");
		}
		glfwMakeContextCurrent(window);
		GL.createCapabilities();

		glfwSwapInterval(1);

		glfwSetKeyCallback(window, (w,key,scancode,action,mods)->{
			if(key==GLFW_KEY_ESCAPE && action==GLFW_RELEASE)
				glfwSetWindowShouldClose(window, true);
		});

		System.out.println(GL11.glGetString(GL11.GL_VERSION));
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable( GL_BLEND );
	}
	public static void main(String[] args)
	{
		init();

		if(args.length>0 && args[0].equals("Editor"))
		{
			Editor e=new Editor();

		}
		else {
			glfwShowWindow(window);

			Game g = new Game();
			KeyboardHandler.window = window;


			//glClearColor(0.64f,0.64f,0.64f,1.0f);
			while (!glfwWindowShouldClose(window)) {
				glfwSetWindowTitle(window, "FPS: " + Math.round(1 / Game.tDelta));

				g.run();

				glfwPollEvents();
				glfwSwapBuffers(window);
			}
			g.exit();
		}

	}
	public static void fail(String error)
	{
		System.out.println(error);
		System.exit(-1);
	}
}