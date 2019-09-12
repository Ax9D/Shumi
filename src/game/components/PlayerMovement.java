package game.components;

import base.Game;
import base.KeyboardHandler;

import static org.lwjgl.glfw.GLFW.*;

public class PlayerMovement extends Component {

	public enum Direction {
		UP, DOWN, LEFT, RIGHT
	}

    public Direction walkDirection;
    public float walkSpeed;

    public PlayerMovement(float walkSpeed) {
        this.walkSpeed = walkSpeed;
    }

    public void init() {
        walkDirection = Direction.RIGHT;
    }

    public void update() {
        if (KeyboardHandler.isPressed(GLFW_KEY_W)) {
            parent.pos.y += walkSpeed * Game.tDelta;
            walkDirection = Direction.UP;
        } else if (KeyboardHandler.isPressed(GLFW_KEY_S)) {
            parent.pos.y -= walkSpeed * Game.tDelta;
            walkDirection = Direction.DOWN;
        } else if (KeyboardHandler.isPressed(GLFW_KEY_A)) {
            parent.pos.x -= walkSpeed * Game.tDelta;
            walkDirection = Direction.LEFT;
        } else if (KeyboardHandler.isPressed(GLFW_KEY_D)) {
            parent.pos.x += walkSpeed * Game.tDelta;
            walkDirection = Direction.RIGHT;
        }

    }
}
