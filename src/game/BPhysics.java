package game;

import org.joml.Vector2f;

public class BPhysics {

	// Assuming B is the fixed object
	public void handleROCollision(ob2D A, ob2D B) {
		// BoundingBox bA=A.getBoundingBox();
		// BoundingBox bB=B.getBoundingBox();

		// if()
		A.getComponent(BoundingBox.class);
		B.getComponent(BoundingBox.class);

	}
}
