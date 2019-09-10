package game;

import base.KeyboardHandler;
import static org.lwjgl.glfw.GLFW.*;
enum Direction
{
		UP,DOWN,LEFT,RIGHT;
};
public class PlayerMovement extends Component {

	public Direction walkDirection;
	public float walkSpeed;
	public PlayerMovement(float walkSpeed) {
		this.walkSpeed = walkSpeed;
		walkDirection=null;
	}

	public void update() {
		if (KeyboardHandler.isPressed(GLFW_KEY_W)) {
			parent.pos.y += walkSpeed;
			walkDirection=Direction.UP;
		}
		else if (KeyboardHandler.isPressed(GLFW_KEY_S)) {
			parent.pos.y -= walkSpeed;
			walkDirection=Direction.DOWN;
		}
		else if (KeyboardHandler.isPressed(GLFW_KEY_A)) {
			parent.pos.x -= walkSpeed;
			walkDirection=Direction.LEFT;
		} else if (KeyboardHandler.isPressed(GLFW_KEY_D)) {
			parent.pos.x += walkSpeed;
			walkDirection=Direction.RIGHT;
		}
		else
			walkDirection=null;

	}
}
