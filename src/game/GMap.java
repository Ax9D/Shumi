package game;

import base.SShader;
import base.Texture2D;
import org.joml.Vector2f;

public class GMap {

    public Texture2D biomeTex;

    public Vector2f pos;

    public float size;

    public SShader ts;

    public float tileSize;

    public GMap(Vector2f pos, float size, SShader ts, int tileCount)
    {
        this.pos=pos;
        this.size=size;

        this.ts=ts;
        this.tileSize=size/tileCount;
    }
}
