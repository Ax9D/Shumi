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

	float[] fpses;
	int fpsesIx;
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
	TextBox test;
	public Game() {

		fpsesIx=0;
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

		fpses=new float[20];

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
		fps.setColor(Color.BLACK);

		GSystem.uirenderer=ur;
		screenShader.use();
		screenShader.setInt("texSamp",0);

		test=new TextBox("",new Vector2f(0f,1f),1f,1f,0.2f);
		/*test.setString("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi sagittis, tellus sed sollicitudin vulputate, urna ipsum placerat lectus, at maximus turpis nibh quis orci. Curabitur non sapien risus. Maecenas ut augue efficitur, feugiat turpis in, maximus nisl. Etiam nulla justo, porta a risus eget, mattis interdum augue. Proin suscipit libero ex, sit amet viverra justo dapibus vitae. Phasellus pretium mi sed ex malesuada vehicula aliquam pharetra diam. Aenean maximus aliquet bibendum. Etiam maximus viverra sodales. Mauris feugiat urna ornare mauris mattis accumsan. Sed sit amet neque et est vehicula pulvinar at lacinia mi. Sed ultricies ante nec ultrices ullamcorper.\n" +
				"\n" +
				"Pellentesque euismod sem et est gravida blandit. Curabitur vehicula eget nibh et aliquet. Duis a tellus ligula. Aliquam dignissim ac nibh sit amet dapibus. Praesent ac condimentum leo. Nullam quis dictum purus. Fusce auctor consectetur ultricies. Morbi sed diam sit amet diam porttitor accumsan ac vel nisi. Proin lectus nibh, mollis eu commodo vitae, semper ac urna. Aenean nibh ante, hendrerit quis lectus id, finibus cursus odio. In eget pulvinar leo. Aenean a interdum mauris, vel efficitur leo. Phasellus non libero tempor orci dignissim dictum ac id lectus. Pellentesque pretium, purus et fermentum ultrices, est nibh volutpat enim, at ullamcorper felis turpis id magna.\n" +
				"\n" +
				"Praesent at posuere odio, nec lobortis sem. Curabitur fringilla ex a lorem pellentesque, a consectetur leo auctor. Aenean aliquet varius ipsum, sit amet sagittis est fringilla sed. Vivamus in commodo orci. Curabitur justo enim, fermentum a nunc eget, vehicula accumsan dui. Etiam nec ex dictum, placerat nibh in, euismod velit. Mauris dapibus urna vitae orci efficitur, in scelerisque metus tristique. Vestibulum consequat porta ipsum id sollicitudin. Etiam viverra arcu sit amet enim facilisis, vestibulum dictum orci consequat. Morbi pretium fringilla justo, eu eleifend lectus suscipit non.\n" +
				"\n" +
				"Integer a suscipit sapien. Etiam iaculis ornare arcu bibendum mollis. Integer sagittis nisl erat. Maecenas eget urna at ipsum scelerisque sagittis. Ut mattis porta tellus rutrum ultrices. Duis cursus semper ullamcorper. Etiam et nulla quis lorem accumsan ornare quis sed lorem. Donec feugiat, neque ut tempus mollis, nibh lorem feugiat lorem, et accumsan magna augue ut turpis. In pulvinar rutrum purus, non sollicitudin neque gravida vel. Integer condimentum facilisis ligula, ac molestie arcu vehicula eget. Aliquam elit arcu, rutrum vel euismod pellentesque, sodales id quam.");
*/
	}
	public void update() {
		GSystem.componentHandler.updateComponents();
		GSystem.world.update();
		GSystem.ui.update();
	}
	public static float average(float[] x)
	{
		float ret=0;
		for(int i=0;i<x.length;i++)
			ret+=x[i];
		return ret/x.length;
	}
	public void draw(){


		GSystem.renderer.renderGame(screenFBO,screenQuad,screenShader);

		if(fpsesIx>fpses.length-1)
			fpsesIx=0;

		fpses[fpsesIx++]=1/tDelta;
		fps.setString("FPS:"+Math.round(average(fpses)));

		ur.renderText(fps);
		ur.renderText(test);

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
