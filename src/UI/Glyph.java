package UI;

import base.Texture2D;

public class Glyph {
    public Texture2D tex;
    public int width, height;

    public Glyph(Texture2D tex, int width, int height) {
        this.tex = tex;
        this.width = width;
        this.height = height;
    }
}
