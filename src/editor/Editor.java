package editor;

import base.*;
import game.components.CameraController;
import game.components.ComponentHandler;
import game.ob2D;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;

public class Editor {
	Game game;
	Vector2f mouseWorldPos;

	private Vector2f lastClickMouseWorldPos;
	private Matrix4f arMat;

	private EditorState editorState;

	private boolean edit;
	private boolean isDrag;
	private EditMode emode;
	public Editor(Game game)
	{
		this.game=game;
		mouseWorldPos=new Vector2f();
		lastClickMouseWorldPos=new Vector2f();
		arMat=game.r.getARMat();
		edit=false;
		isDrag=false;

		emode=new EditMode(game,mouseWorldPos);
		//Disable camera follow
		ComponentHandler.getAllByComponent(CameraController.class).get(0).disable();


		KeyboardHandler.addEventListener((key,action)->{
			if(key== GLFW_KEY_TAB && action==GLFW_RELEASE)
				edit=!edit;
		});
	}
	public void updateMousePos()
	{
		Matrix4f mousePosMatrix;

		Camera2D gameCam=game.c;
		Matrix4f cameraMat= MatrixMath.get2DTMat(new Vector2f(-gameCam.pos.x,-gameCam.pos.y),1);

		mousePosMatrix=new Matrix4f(arMat);

		mousePosMatrix.mul(cameraMat);

		mousePosMatrix.invert();

		Vector4f mouseWorldPosV4= MouseHandler.getGLScreenPos().mul(mousePosMatrix);

		mouseWorldPos.x=mouseWorldPosV4.x;
		mouseWorldPos.y=mouseWorldPosV4.y;
	}
	public void updateEditorCamera()
	{
		Vector2f diff=new Vector2f(lastClickMouseWorldPos.x-mouseWorldPos.x,lastClickMouseWorldPos.y-mouseWorldPos.y);
		game.c.pos.add(diff);
	}
	public void update()
	{
		updateMousePos();

		if(edit)
			emode.update();
		else {
			game.update();

		//Handle click drag camera pan
		if (!isDrag) {
			if (MouseHandler.mouseLeftClicked()) {
				lastClickMouseWorldPos.x = mouseWorldPos.x;
				lastClickMouseWorldPos.y = mouseWorldPos.y;

				isDrag = true;
			}
		} else if (MouseHandler.mouseLeftUnClicked())
			isDrag = false;
		else
			updateEditorCamera();

		}
	}


	public void run()
	{
		update();
		game.draw();
	}
}
