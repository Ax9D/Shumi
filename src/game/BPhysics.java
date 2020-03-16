package game;

import game.components.BoundingBox;
import game.components.PlayerMovement;

public class BPhysics {
	// Assuming B is the fixed object
	public static void handlePlayerFOCollision(ob2D A, ob2D B) {

		BoundingBox bA=A.getComponent(BoundingBox.class);
		BoundingBox bB=B.getComponent(BoundingBox.class);

		bA.compute();
		bB.compute();

		float xoverlap;
		float yoverlap;

		float rightgap,leftgap,topgap,bottomgap;
		boolean pushLeft;
		boolean pushBottom;
		if(isCollision(bA,bB))
		{
			rightgap=Math.abs(bA.right-bB.left);
			leftgap=Math.abs(bA.left-bB.right);
			topgap=Math.abs(bA.bottom-bB.top);
			bottomgap=Math.abs(bA.top-bB.bottom);


			if(rightgap<leftgap) {
				xoverlap = rightgap;
				pushLeft=true;
			}
			else
			{
				xoverlap=leftgap;
				pushLeft=false;
			}


			if(bottomgap<topgap) {
				yoverlap = bottomgap;
				pushBottom=true;
			}
			else
			{
				yoverlap=topgap;
				pushBottom=false;
			}

			if(xoverlap<yoverlap)
			{
				if(pushLeft)
					A.pos.x-=xoverlap;
				else
					A.pos.x+=xoverlap;
			}
			else
			{
				if(pushBottom)
					A.pos.y-=yoverlap;
				else
					A.pos.y+=yoverlap;
			}
		}
	}
	public static boolean isCollision(BoundingBox bA,BoundingBox bB)
	{
		return !(bA.right<=bB.left || bA.left>=bB.right || bA.bottom>=bB.top || bA.top<=bB.bottom);
	}
	public static boolean isCollision(float aleft,float aright,float bleft,float bright,float abottom,float atop,float bbottom,float btop)
	{
		return !(aright<=bleft || aleft>bright || abottom>=btop || atop<=bbottom);
	}
}
