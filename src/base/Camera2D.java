package base;

import org.joml.Vector2f;

public class Camera2D {
	public Vector2f pos;
	public Vector2f scale;

	float cameraSpeed;
	public Camera2D(Vector2f pos,Vector2f scale,float cameraSpeed)
	{
		this.pos=pos;
		this.scale=scale;
		this.cameraSpeed=cameraSpeed;
	}
	public void moveLeft()
	{
		pos.x-=cameraSpeed*Game.tDelta;
	}
	public void moveRight()
	{
		pos.x+=cameraSpeed*Game.tDelta;
	}
	public void moveUp()
	{
		pos.y+=cameraSpeed*Game.tDelta;
	}
	public void moveDown()
	{
		pos.y-=cameraSpeed*Game.tDelta;
	}
}
