package base;
import game.SaveSystem;
import game.World;
import game.components.ComponentHandler;
import UI.*;

public class GSystem {
    public static World world;
    public static Renderer renderer;
    public static ResourceManager rsmanager;
    public static Loader loader;
    public static ComponentHandler componentHandler;
    public static View view;
    public static UI ui;
    public static UIRenderer uirenderer;
    public static SaveSystem saveSystem;
    public static boolean debug=true;
}
