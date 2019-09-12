package game;

import game.components.BoundingBox;
import game.components.PlayerMovement;

public class BPhysics {

	// Assuming B is the fixed object
	public static void handlePlayerFOCollision(ob2D A, ob2D B) {
		// BoundingBox bA=A.getBoundingBox();
		// BoundingBox bB=B.getBoundingBox();

		// if()
		BoundingBox bA=A.getComponent(BoundingBox.class);
		BoundingBox bB=B.getComponent(BoundingBox.class);

		bA.compute();
		bB.compute();

		//Left wall of B
		if(isCollision(bA,bB))
		{

		    PlayerMovement pm=A.getComponent(PlayerMovement.class);

			var walkDir=pm.walkDirection;

			switch(walkDir)
			{
				case LEFT:
					A.pos.x+=bB.right-bA.left;
					break;
				case RIGHT:
					A.pos.x-=bA.right-bB.left;
					break;
				case UP:
					A.pos.y-=bA.top-bB.bottom;
					break;
				case DOWN:
					A.pos.y+=bB.top-bA.bottom;
					break;
			}

		}
	}
	public static boolean isCollision(BoundingBox bA,BoundingBox bB)
	{
			return !(bA.right<=bB.left || bA.left>=bB.right || bA.bottom>=bB.top || bA.top<=bB.bottom);
	}
}
