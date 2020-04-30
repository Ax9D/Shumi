package UI;

import base.Color;
import base.Texture2D;
import org.joml.Vector2f;

import java.util.ArrayList;

public class Frame {

    Vector2f topLeft;
    //Width and height range from 0 to 1
    public float width, height;
    ArrayList<UIElement> elements;
    Texture2D tex;

    static Texture2D defaultTexture;

    static{
        defaultTexture= new Texture2D(Color.GREY);
    }

    public Frame(Vector2f topLeft, float width, float height) {
        this.topLeft = topLeft;
        this.width = width;
        this.height = height;
        tex=defaultTexture;
    }
    public Frame setTexture(Texture2D t)
    {
        tex=t;
        return this;
    }

    public Frame moveTo(float x, float y) {
        topLeft.x = x;
        topLeft.y = y;
        return this;
    }


}
