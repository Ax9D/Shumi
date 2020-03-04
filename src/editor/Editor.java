package editor;

import base.*;
import game.components.CameraController;
import game.components.ComponentHandler;
import input_handling.KeyboardHandler;
import input_handling.MouseHandler;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;

public class Editor {
	Game game;
	Vector2f mouseWorldPos;

	private Vector2f lastClickMouseWorldPos;
	private Matrix4f arMat;

	private boolean isDrag;
	private EditMode emode;

	float scrollSpeed;
	public Editor(Game game)
    {
		this.game=game;
		mouseWorldPos=new Vector2f();
		lastClickMouseWorldPos=new Vector2f();
		arMat=game.r.getARMat();

		isDrag=false;

		emode=new EditMode(game,mouseWorldPos);
		//Disable camera follow
		ComponentHandler.getAllByComponent(CameraController.class).get(0).disable();

		scrollSpeed=0.1f;

		KeyboardHandler.addEventListener((key, action)->{
			if(key== GLFW_KEY_TAB && action==GLFW_RELEASE) {
				emode.enabled = !emode.enabled;
				if(!emode.enabled)
					emode.reset();
			}
		});
		MouseHandler.addScrollEventListener((amt)->{

			System.out.println(amt);
			float new_scale=game.r.getScale()*(float)(1-scrollSpeed*amt);
			game.r.adjustScale(new_scale);

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

		if(emode.enabled)
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
		game.draw();
		update();
	}
}
