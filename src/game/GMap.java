package game;

import base.BShader;
import base.FBO;
import base.Texture2D;
import org.joml.Vector2f;

public class GMap {

    public Texture2D biomeTex;

    public Vector2f pos;

    public float size;

    public BShader ts;

    public Path[] paths;

    public float tileSize;

   public FBO mapTexFBO;

    public GMap(Vector2f pos,float size,BShader ts,int tileCount,Path[] paths)
    {
        this.pos=pos;
        this.size=size;

        this.ts=ts;
        this.paths=paths;
        this.tileSize=size/tileCount;
        //this.zerozero=new Vector2f(this.pos.x-size.x+tileSize,this.pos.y+size.y-tileSize);
       // System.out.println(zerozero);

        //Bodgy.. refactor later

        int WIDTH=Main.WIDTH*(int)size;
        int HEIGHT=Main.HEIGHT*(int)size;
        mapTexFBO=new FBO(WIDTH,HEIGHT);
    }
    /*public void addhertPath(Vector2f pos,int ntiles)
    {

    }
    public void addverPath(Vector2f pos,int ntiles)
    {

    }*/
}
