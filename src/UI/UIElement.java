package UI;

import org.joml.Vector2f;

public abstract class UIElement {
    public Frame parent;
    public Vector2f topLeft;
    public Vector2f size;
    public abstract void update();
    public abstract void render();
}
