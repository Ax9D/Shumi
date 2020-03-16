package base;

import UI.UI;
import UI.UIRenderer;
import UI.TextBox;
import UI.*;
import game.World;
import game.components.CameraController;
import game.components.ComponentHandler;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.opengl.GL11.glViewport;

public class Game {

    static float[] quadVerts;
    static float[] quadUV={0.0f, 1.0f,
                           1.0f, 1.0f,
                           1.0f, 0.0f,
                           0.0f, 0.0f};
    static int[] quadInds={0, 3, 1, 1, 3, 2};

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

	UIRenderer ur;
	TextBox fps;
	public Game() {


		glfwSetWindowSizeCallback(WindowInfo.window,(w,width,height)->{

			glViewport(0,0,width,height);
			WindowInfo.WIDTH=width;
			WindowInfo.HEIGHT=height;
			float ar=(float)width/height;
			GSystem.view.adjustAspectRatio(ar);
			//GSystem.uirenderer.ar_correction_matrix.setOrtho2D(-ar,ar,-1,1);
			GSystem.uirenderer.ar_correction_matrix.setOrtho2D(0,WindowInfo.WIDTH,0,WindowInfo.HEIGHT);
			FBO oldscreenFBO=screenFBO;
			oldscreenFBO.delete();
			screenFBO=new FBO(WindowInfo.WIDTH,WindowInfo.HEIGHT);

		});


		prevTime=System.currentTimeMillis();

		World w=new World();
		GSystem.world=w;

        ResourceManager rm=new ResourceManager();

        rm.basicQuad=new Shape(quadVerts,quadInds,quadUV);
        GSystem.rsmanager=rm;

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

		GSystem.componentHandler=new ComponentHandler();

		Loader l = new Loader(w, "resources.json", "gamedata.json");
		Camera2D c = new Camera2D(new Vector2f(), new Vector2f(1, 1), 1f);

		Renderer r=new Renderer();


		GSystem.loader=l;
		GSystem.renderer=r;


        GSystem.view=new View(c,(float)WindowInfo.WIDTH/WindowInfo.HEIGHT,2);
        GSystem.ui=new UI();

		screenFBO=new FBO(WindowInfo.WIDTH,WindowInfo.HEIGHT);
		screenShader=new BShader("src/screenV.glsl","src/screenF.glsl");
		simpleShader=new BShader("src/simplevertex.glsl","src/simplefragment.glsl");


		System.out.println("Initialized.\n"+
							"Loaded resources and world in "+(System.currentTimeMillis()-prevTime)+"ms");

		//Future me, please refactor this
		((CameraController)GSystem.componentHandler.getAllByComponent(CameraController.class).get(0)).setCamera(c);


		ur=new UIRenderer();
		fps=new TextBox("",new Vector2f(.90f,1f),0.01f,0.01f,0.2f);
		fps.setFont("Arial");
		fps.setColor(Color.GREEN);

		GSystem.uirenderer=ur;
		screenShader.use();
		screenShader.setInt("texSamp",0);

	}
	public void update() {
		GSystem.componentHandler.updateComponents();
		GSystem.world.update();
		GSystem.ui.update();
	}

	public void draw(){

		GSystem.renderer.renderGame(screenFBO,screenQuad,screenShader);

		fps.setString("FPS:"+Math.round(1/tDelta));
		ur.renderText(fps);

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
