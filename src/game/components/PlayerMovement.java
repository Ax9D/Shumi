package game.components;

import base.Game;
import input_handling.KeyboardHandler;
import org.json.JSONException;
import org.json.JSONObject;

import static org.lwjgl.glfw.GLFW.*;

public class PlayerMovement extends Component {

	public enum Direction {
		UP, DOWN, LEFT, RIGHT
	}

    public static Component loadComponent(JSONObject jo) throws JSONException {
        float walkSpeed = (float) jo.getDouble("walkSpeed");
        Component c = new PlayerMovement(walkSpeed);
        return c;
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
        PlayerAnimation aniCompoment=parent.getComponent(PlayerAnimation.class);

        if (KeyboardHandler.isPressed(GLFW_KEY_W)) {
            parent.pos.y += walkSpeed * Game.tDelta;
            walkDirection = Direction.UP;
            aniCompoment.currentAnimationState= PlayerAnimation.walkingUp;
        } else if (KeyboardHandler.isPressed(GLFW_KEY_S)) {
            parent.pos.y -= walkSpeed * Game.tDelta;
            walkDirection = Direction.DOWN;
            aniCompoment.currentAnimationState= PlayerAnimation.walkingDown;
        } else if (KeyboardHandler.isPressed(GLFW_KEY_A)) {
            parent.pos.x -= walkSpeed * Game.tDelta;
            walkDirection = Direction.LEFT;
            aniCompoment.currentAnimationState= PlayerAnimation.walkingLeft;
        } else if (KeyboardHandler.isPressed(GLFW_KEY_D)) {
            parent.pos.x += walkSpeed * Game.tDelta;
            walkDirection = Direction.RIGHT;
            aniCompoment.currentAnimationState= PlayerAnimation.walkingRight;
        }
        else
            aniCompoment.currentAnimationState= PlayerAnimation.standing;

    }
}
