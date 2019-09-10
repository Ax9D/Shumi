package game;

import base.KeyboardHandler;
import static org.lwjgl.glfw.GLFW.*;

public class PlayerMovement extends Component {

	float walkSpeed;

	public PlayerMovement(float walkSpeed) {
		this.walkSpeed = walkSpeed;
	}

	public void update() {
		if (KeyboardHandler.isPressed(GLFW_KEY_W))
			parent.pos.y += walkSpeed;
		else if (KeyboardHandler.isPressed(GLFW_KEY_S))
			parent.pos.y -= walkSpeed;
		else if (KeyboardHandler.isPressed(GLFW_KEY_A))
			parent.pos.x -= walkSpeed;
		else if (KeyboardHandler.isPressed(GLFW_KEY_D))
			parent.pos.x += walkSpeed;

	}
}
