package game;

import base.BShader;
import base.Model;
import base.Texture2D;
import org.joml.Vector2f;

public class GMap {
    public Texture2D grass;
    public Texture2D dirt;
    public Texture2D map;

    public Vector2f pos,size;

    public BShader ts;

    public ob2D[] vertPath;
    public ob2D[] horPath;

    public GMap(Vector2f pos,Vector2f scale,Texture2D grass, Texture2D dirt,BShader ts)
    {
        this.grass=grass;
        this.dirt=dirt;

        this.pos=pos;
        this.size=scale;

        this.ts=ts;
    }
    public void addhertPath(Vector2f pos,int ntiles)
    {

    }
    public void addverPath(Vector2f pos,int ntiles)
    {

    }
}
