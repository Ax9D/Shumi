package game;

import input_handling.KeyboardHandler;
import base.Model;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends ob2D {

	float walkSpeed = 0.01f;

	public Player(Model m, Vector2f pos, Vector2f size) {
		super(m, pos, size, "player");
	}

	public void update() {
		/*
		if (KeyboardHandler.isPressed(GLFW_KEY_LEFT))
			pos.x -= walkSpeed;
		else if (KeyboardHandler.isPressed(GLFW_KEY_RIGHT))
			pos.x += walkSpeed;
		else if (KeyboardHandler.isPressed(GLFW_KEY_UP))
			pos.y += walkSpeed;
		else if (KeyboardHandler.isPressed(GLFW_KEY_DOWN))
			pos.y -= walkSpeed;
		 */
	}
}
