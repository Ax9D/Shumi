package game;

import org.joml.Vector2f;

public class BPhysics {

	// Assuming B is the fixed object
	public void handlePlayerFOCollision(ob2D A, ob2D B) {
		// BoundingBox bA=A.getBoundingBox();
		// BoundingBox bB=B.getBoundingBox();

		// if()
		BoundingBox bA=A.getComponent(BoundingBox.class);
		BoundingBox bB=B.getComponent(BoundingBox.class);

		//Left wall of B
		if(isCollision(bA,bB))
		{
			Direction walkDir=A.getComponent(PlayerMovement.class).walkDirection;

			switch(walkDir)
			{
				case LEFT:
					A.pos.x+=bB.right-bA.left;
					break;
				case RIGHT:
					A.pos.x-=bA.right-bB.left;
					break;
				case UP:
					A.pos.y-=bB.bottom-bA.top;
					break;
				case DOWN:
					A.pos.y+=bB.top-bA.bottom;
					break;
			}

		}
	}
	public boolean isCollision(BoundingBox bA,BoundingBox bB)
	{
	}
}
