package base;

import UI.TextBox;
import UI.UI;
import UI.UIRenderer;
import game.World;
import game.components.CameraController;
import game.components.ComponentHandler;
import input_handling.KeyboardHandler;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL30C.GL_FRAMEBUFFER_SRGB;

public class Game {

    float[] fpses;
    int fpsesIx;
    static float[] quadVerts = new float[]{-1.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, -1.0f,
            -1.0f, -1.0f};
    static float[] quadUV = {0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f};
    static int[] quadInds = {0, 3, 1, 1, 3, 2};


    Camera2D c;

    FBO screenFBO;


    private long prevTime;

    public static long frameTimeMillis;

    public static float tDelta;

    UIRenderer ur;
    TextBox fps;
    TextBox test;

    public Game() {


        fpsesIx = 0;

        fpses = new float[10];

        prevTime = System.currentTimeMillis();
        ResourceManager rm = new ResourceManager();

        rm.basicQuad = new Shape(quadVerts, quadInds, quadUV);

        GSystem.rsmanager = rm;

        GSystem.componentHandler = new ComponentHandler();

        Camera2D c = new Camera2D(new Vector2f(), 1f);


        GSystem.view = new View(c, (float) WindowInfo.WIDTH / WindowInfo.HEIGHT, 10);


        GSystem.view.onChange(() -> {
            FBO oldscreenFBO = screenFBO;
            oldscreenFBO.delete();
            screenFBO = new FBO(WindowInfo.WIDTH, WindowInfo.HEIGHT, 2);
        });

        GSystem.world = new World();

        GSystem.loader = new Loader(GSystem.world, "resources.json", "gamedata.json");

        GSystem.renderer = new Renderer();

        GSystem.ui = new UI();

        screenFBO = new FBO(WindowInfo.WIDTH, WindowInfo.HEIGHT, 2);


        System.out.println("Initialized.\n" +
                "Loaded resources and world in " + (System.currentTimeMillis() - prevTime) + "ms");

        //Future me, please refactor this
        ((CameraController) GSystem.componentHandler.getAllByComponent(CameraController.class).get(0)).setCamera(c);


        ur = new UIRenderer();
        fps = new TextBox("", new Vector2f(.90f, 0.95f), 0.2f, 0.1f, 9f);
        fps.setFont("Tahoma");
        fps.setColor(new Vector4f(1,1,1,0.5f));

        GSystem.uirenderer = ur;

        test = new TextBox("", new Vector2f(0f, 1f), 1f, 1f, 10f);


        glfwSetWindowSizeCallback(WindowInfo.window, (w, width, height) -> {

            glViewport(0, 0, width, height);
            WindowInfo.updateWindow(width, height);

            float ar = (float) width / height;

            GSystem.view.adjustAspectRatio(ar);
            GSystem.view.processChange();
        });

        KeyboardHandler.addEventListener((key, action) -> {

            if (key == GLFW_KEY_F2 && action == GLFW_PRESS)
                GSystem.debug = !GSystem.debug;

        });
        glEnable(GL_FRAMEBUFFER_SRGB);
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

    public static float average(float[] x) {
        float ret = 0;
        for (int i = 0; i < x.length; i++)
            ret += x[i];
        return ret / x.length;
    }

    public void draw() {


        GSystem.renderer.renderGame(screenFBO);

        if (fpsesIx > fpses.length - 1)
            fpsesIx = 0;

        fpses[fpsesIx++] = 1 / tDelta;

        fps.setString("FPS: " + Math.round(average(fpses)));

        ur.renderText(fps);
        ur.renderText(test);

        long curTime = System.currentTimeMillis();
        frameTimeMillis = curTime - prevTime;
        tDelta = frameTimeMillis / 1000.0f;
        prevTime = curTime;

    }

    public void run() {

        update();
        draw();
    }

    public void exit() {

        GSystem.rsmanager.delete();
    }

}
