package editor;

import base.*;
import game.ob2D;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.awt.*;

import static org.lwjgl.glfw.GLFW.*;

public class Editor {
	Game game;
	Vector2f mouseWorldPos;
	private Matrix4f arMat;

	private boolean hasPicked;

	private Object pickedObject2D;
	private PickedType pickedType;

	public Editor(Game game)
	{
		this.game=game;
		mouseWorldPos=new Vector2f();
		arMat=game.getRenderer().getARMat();
		pickedObject2D=null;
		hasPicked=false;

	}
	public void updateMousePos()
	{
		Matrix4f mousePosMatrix;

		Camera2D gameCam=game.getCamera();
		Matrix4f cameraMat= MatrixMath.get2DTMat(new Vector2f(-gameCam.pos.x,-gameCam.pos.y),1);

		mousePosMatrix=new Matrix4f(arMat);

		mousePosMatrix.mul(cameraMat);

		mousePosMatrix.invert();

		Vector4f mouseWorldPosV4= MouseHandler.getGLScreenPos().mul(mousePosMatrix);

		mouseWorldPos.x=mouseWorldPosV4.x;
		mouseWorldPos.y=mouseWorldPosV4.y;
	}
	public void update()
	{
		updateMousePos();
		boolean hasLeftClicked=MouseHandler.mouseLeftClicked();

		if(hasLeftClicked) {
			;
		}
	}
	public void run()
	{
		update();
		game.run();
	}
}
