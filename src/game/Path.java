package game;

import base.ResourceManager;
import base.Texture2D;
import org.joml.Vector2f;
import org.joml.Vector2i;

public class Path  {
    public enum PathType
    {
        Stone,Dirt;
    }
    public enum PathDirection
    {
        Vertical,Horizontal;
    }

    public PathDirection dir;
    public Texture2D tex;
    public Vector2i gridPos;

    public int ntiles;
    public Path(PathType t, Vector2i gridPos,int ntiles,PathDirection direction)
    {

        dir=direction;

        this.ntiles=ntiles;

        this.gridPos=gridPos;

        switch(t)
        {
            case Stone:
                tex= ResourceManager.getTexture("stone");
                break;
            case Dirt:
                tex= ResourceManager.getTexture("dirt");
                break;
            default:
                throw new RuntimeException("Invalid path type: "+t);
        }
    }
}
