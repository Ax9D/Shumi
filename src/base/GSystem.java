package base;

import game.World;
import game.components.ComponentHandler;

public class GSystem {
    public static World world;
    public static Renderer renderer;
    public static ResourceManager rsmanager;
    public static Loader loader;
    public static ComponentHandler componentHandler;

    public static void setRenderer(Renderer r)
    {
        renderer=r;
    }
    public static void setWorld(World w)
    {
        world=w;
    }
    public static void setRsManager(ResourceManager r)
    {
        rsmanager=r;
    }
    public static void setLoader(Loader l)
    {
        loader=l;
    }
    public static void setcomponentHandler(ComponentHandler ch)
    {
        componentHandler=ch;
    }
}
