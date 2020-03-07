package base;

import game.World;
import game.components.CameraController;
import game.components.ComponentHandler;
import game.ob2D;
import org.joml.Vector2f;

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


	Camera2D c;

	FBO screenFBO;

	BShader screenShader;
	BShader simpleShader;

	Shape screenQuad;

	private long prevTime;

	public static long frameTimeMillis;

	public static float tDelta;


	public Game() {


		glfwSetWindowSizeCallback(WindowInfo.window,(w,width,height)->{

			glViewport(0,0,width,height);
			WindowInfo.WIDTH=width;
			WindowInfo.HEIGHT=height;
			GSystem.renderer.setAspectRatio((float)width/height);
			FBO oldscreenFBO=screenFBO;
			oldscreenFBO.delete();
			screenFBO=new FBO(WindowInfo.WIDTH,WindowInfo.HEIGHT);
		});


		prevTime=System.currentTimeMillis();

		World w=new World();
		GSystem.setWorld(w);

        ResourceManager rm=new ResourceManager();

        rm.basicQuad=new Shape(quadVerts,quadInds,quadUV);
        GSystem.setRsManager(rm);

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

		GSystem.setcomponentHandler(new ComponentHandler());

		Loader l = new Loader(w, "resources.json", "gamedata.json");
		Camera2D c = new Camera2D(new Vector2f(), new Vector2f(1, 1), 1f);

		Renderer r=new Renderer(c);


		GSystem.setLoader(l);
		GSystem.setRenderer(r);

		r.adjustScale(2);
        r.setAspectRatio((float)WindowInfo.WIDTH/WindowInfo.HEIGHT);

		screenFBO=new FBO(WindowInfo.WIDTH,WindowInfo.HEIGHT);
		screenShader=new BShader("src/screenV.glsl","src/screenF.glsl");
		simpleShader=new BShader("src/simplevertex.glsl","src/simplefragment.glsl");


		System.out.println("Initialized.\n"+
							"Loaded resources and world in "+(System.currentTimeMillis()-prevTime)+"ms");

		//Future me, please refactor this
		((CameraController)GSystem.componentHandler.getAllByComponent(CameraController.class).get(0)).setCamera(c);



		screenShader.use();
		screenShader.setInt("texSamp",0);
	}
	public void update() {
		GSystem.componentHandler.updateComponents();
		GSystem.world.update();
	}

	public void draw(){

		GSystem.renderer.renderGame(screenFBO,screenQuad,screenShader);

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

		GSystem.rsmanager.delete();
	}

}
