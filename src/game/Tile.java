package game;

import base.Texture2D;
import org.joml.Vector2f;

public class Tile {
    public Vector2f gridPos;
    public Texture2D tex;

    public Tile(Vector2f gridPos, Texture2D tex) {
        this.gridPos = gridPos;
        this.tex = tex;
    }
}
