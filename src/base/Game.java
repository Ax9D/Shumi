package base;

import game.World;
import game.components.CameraController;
import game.components.ComponentHandler;
import game.ob2D;
import org.joml.Vector2f;

import java.util.HashMap;
import java.util.HashSet;

import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.opengl.GL11.glViewport;

public class Game {

    static float[] quadVerts;
    static float[] quadUV={0.0f, 1.0f,
                           1.0f, 1.0f,
                           1.0f, 0.0f,
                           0.0f, 0.0f};
    static int[] quadInds={0, 3, 1, 1, 3, 2};

    static float aspect_ratio=600.0f/800;
    static {
		quadVerts = new float[]{-1.0f , 1.0f,
				1.0f  , 1.0f,
				1.0f  , -1.0f,
				-1.0f  , -1.0f};
	}
	/*
	 * BShader bs; ob2D test; Camera2D c; static Model quad; Player p; static
	 * Texture2D genericTex;
	 *
	 * static float[] quadVerts = { -1f, 1f, 1f, 1f, 1f, -1f, -1f, -1f }; static
	 * int[] quadInds = { 0, 3, 1, 1, 3, 2 };
	 *
	 * static float[] quadtCoords = { 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f
	 * };
	 *
	 * static { quad = new Model(quadVerts, quadInds, quadtCoords); genericTex = new
	 * Texture2D("test.png"); quad.setTexture(genericTex); }
	 */
	public World w;
	public Loader l;
	public Camera2D c;

	public Renderer r;

	FBO screenFBO;

	BShader screenShader;
	BShader simpleShader;

	Shape screenQuad;

	private long prevTime;

	public static long frameTimeMillis;

	public static float tDelta;

	float averageframeTime;

	public Game() {


		glfwSetWindowSizeCallback(WindowInfo.window,(w,width,height)->{

			glViewport(0,0,width,height);
			WindowInfo.WIDTH=width;
			WindowInfo.HEIGHT=height;
			r.setAspectRatio((float)width/height);
			FBO oldscreenFBO=screenFBO;
			oldscreenFBO.delete();
			screenFBO=new FBO(WindowInfo.WIDTH,WindowInfo.HEIGHT);
		});


		prevTime=System.currentTimeMillis();

		w = new World();

        ResourceManager.basicQuad=new Shape(quadVerts,quadInds,quadUV);
        w.modelObpair.put(ResourceManager.basicQuad,new HashSet<ob2D>());

        screenQuad=new Shape(new float[]{
                -1f, 1f,
                1f, 1f,
                1f, -1f,
                -1f, -1f
        },quadInds,quadUV);
/*

        screenRect=new Model(new float[]{
                -1f, 1f,
                1f, 1f,
                1f, -1f,
                -1f, -1f
        },quadInds,quadUV);*/

		l = new Loader(w, "resources.json", "gamedata.json");
		c = new Camera2D(new Vector2f(), new Vector2f(1, 1), 1f);

		r=new Renderer(c);
		r.adjustScale(2);
        r.setAspectRatio((float)WindowInfo.WIDTH/WindowInfo.HEIGHT);

		screenFBO=new FBO(WindowInfo.WIDTH,WindowInfo.HEIGHT);
		screenShader=new BShader("src/screenV.glsl","src/screenF.glsl");
		simpleShader=new BShader("src/simplevertex.glsl","src/simplefragment.glsl");


		System.out.println("Initialized.\n"+
							"Loaded resources and world in "+(System.currentTimeMillis()-prevTime)+"ms");

		//Future me, please refactor this
		((CameraController)ComponentHandler.getAllByComponent(CameraController.class).get(0)).setCamera(c);

		screenShader.use();
		screenShader.setInt("texSamp",0);
	}
	public void update() {
		w.update();
	}

	public void draw(){

		r.renderGame(w,screenFBO,screenQuad,screenShader);

		long curTime=System.currentTimeMillis();
		frameTimeMillis=curTime-prevTime;
		tDelta=frameTimeMillis/1000.0f;
		prevTime=curTime;

	}
	public void run() {

		update();
		draw();

	}

	public void exit() {
		ResourceManager.delete();
	}

}
