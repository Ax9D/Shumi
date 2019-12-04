package game;

import base.KeyboardHandler;
import base.Model;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends ob2D {

	float walkSpeed = 0.01f;

	public Player(Model m, Vector2f pos, Vector2f size) {
		super(m, pos, size, "player");



		//Key Handling setup
		KeyboardHandler.addEventListener((key,action)->{

			boolean pressed_or_repeated=action==GLFW_PRESS || action==GLFW_REPEAT;

			if(pressed_or_repeated) {
				if (key==GLFW_KEY_LEFT)
					pos.x -= walkSpeed;
				else if (key==GLFW_KEY_RIGHT)
					pos.x += walkSpeed;
				else if (key==GLFW_KEY_UP)
					pos.y += walkSpeed;
				else if (key==GLFW_KEY_DOWN)
					pos.y -= walkSpeed;
			}
		});
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
