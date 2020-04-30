package UI;

import base.WindowInfo;
import org.joml.Vector2f;

public abstract class UIElement {
    public Frame parent;
    public Vector2f topLeft;
    public Vector2f size;

    Vector2f topLeft_a;
    Vector2f size_a;

    public UIElement() {
        topLeft_a = new Vector2f();
        size_a = new Vector2f();
    }

    public void compute() {
        topLeft_a.x = (parent.topLeft.x + (topLeft.x + size.x) * parent.width) * WindowInfo.WIDTH;
        topLeft_a.y = (parent.topLeft.y - (topLeft.y + size.y) * parent.height) * WindowInfo.HEIGHT;
        size_a.x=parent.width*size.x*WindowInfo.WIDTH;
        size_a.y=parent.height*size.y*WindowInfo.HEIGHT;
    }
}
