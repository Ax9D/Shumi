package UI;

import org.joml.Vector2f;

public class TextBox {
    private String str;
    Vector2f topLeft;
    float width,height;
    public TextBox(String str, Vector2f topleft, float width, float height)
    {
        this.str=str;
        this.topLeft=topleft;
        this.width=width;
        this.height=height;
    }
}
