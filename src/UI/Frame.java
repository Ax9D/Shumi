package UI;

import org.joml.Vector2f;

import java.util.ArrayList;

public class Frame {

    Vector2f topLeft;
    //Width and height range from 0 to 1
    public float width, height;
    ArrayList<UIElement> elements;

    public Frame(Vector2f topLeft, float width, float height) {
        this.topLeft = topLeft;
        this.width = width;
        this.height = height;
    }

    public void moveTo(float x, float y) {
        topLeft.x = x;
        topLeft.y = y;
    }

}
