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

    public float tileSize;

   public Tile[] tiles;

    public GMap(Vector2f pos,float size,BShader ts,int tileCount,Tile[] tiles)
    {
        this.pos=pos;
        this.size=size;

        this.ts=ts;
        this.tiles=tiles;
        this.tileSize=size/tileCount;
        //this.zerozero=new Vector2f(this.pos.x-size.x+tileSize,this.pos.y+size.y-tileSize);
       // System.out.println(zerozero);
    }
    /*public void addhertPath(Vector2f pos,int ntiles)
    {

    }
    public void addverPath(Vector2f pos,int ntiles)
    {

    }*/
}
