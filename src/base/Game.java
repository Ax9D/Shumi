package base;

import game.Main;
import game.World;
import game.components.CameraController;
import game.components.ComponentHandler;
import game.ob2D;
import org.joml.Vector2f;

import java.util.HashMap;

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
	World w;
	Loader l;
	Camera2D c;
	BShader bs;
	Renderer r;

	FBO screenFBO;

	BShader screenShader;

	Model screenQuad;

	private long prevTime;

	public static float tDelta;

	public Game() {
		/*
		 *
		 * test = new ob2D(quad, new Vector2f(0.25f, 0), new Vector2f(0.5f, 0.5f));
		 *
		 * c = new Camera2D(new Vector2f(0.25f, 0), new Vector2f(1, 1), 0.01f);
		 *
		 * p = new Player(quad, new Vector2f(0.25f, 0), new Vector2f(0.25f, 0.25f));
		 */
		prevTime=System.currentTimeMillis();

		w = new World();

        ResourceManager.basicQuad=new Model(quadVerts,quadInds,quadUV);
        w.modelObpair.put(ResourceManager.basicQuad,new HashMap<ob2D,Boolean>());

        screenQuad=new Model(new float[]{
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

		bs = new BShader("src/vertex", "src/fragment");
		l = new Loader(w, "resources.json", "gamedata.json");
		c = new Camera2D(new Vector2f(), new Vector2f(1, 1), 1f);

		r=new Renderer(c);



		screenFBO=new FBO(800,600);
		screenShader=new BShader("src/screenV","src/screenF");


		// w.ob2Ds.get(0).addComponent(new BasicAnimation(new Texture2D[] { new
		// Texture2D("an1.png"),
		// new Texture2D("an2.png"), new Texture2D("an3.png"), new Texture2D("an4.png")
		// }, 10));

		System.out.println("Initialized.\n"+
							"Loaded resources and world in "+(System.currentTimeMillis()-prevTime)+"ms");

		//Future me, please refactor this
		ComponentHandler.getAllByComponent(CameraController.class).get(0).setCamera(c);

		bs.use();
		bs.setInt("texSamp", 0);


		screenShader.use();
		screenShader.setInt("texSamp",0);

		r.renderGMaptoTexture(w.gm,bs);
		w.gm.mapTexFBO.saveToDisk("asdf.png");
	}

	public void update() {
		w.update();
	}

	public void draw(){
		//Renderer.render(w,c,bs);
		r.renderTOFBO(w,bs,screenFBO,screenQuad,screenShader);
	}
	public void run() {
		long curTime=System.currentTimeMillis();

		tDelta=(curTime-prevTime)/1000.0f;

		update();
		draw();
		prevTime=curTime;
	}

	public void exit() {
		ResourceManager.delete();
	}

}
