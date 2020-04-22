package game;

import base.Shape;
import org.joml.Vector2f;

public class Player extends ob2D {

    float walkSpeed = 0.01f;

    public Player(Shape m, Vector2f pos, Vector2f size) {
        super(m, pos, size, "player");
    }

    public void update() {

    }
}
