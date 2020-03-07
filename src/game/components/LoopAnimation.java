package game.components;

import base.Common;
import base.GSystem;
import base.ResourceManager;
import base.Texture2D;
import game.ob2D;
import org.json.JSONException;
import org.json.JSONObject;

public class LoopAnimation extends Component {
	int nframes;
	int curFrame;
	Texture2D[] textures;

	float frameTime;
	long lastTime;

    public static Component loadComponent(JSONObject jo) throws JSONException {
        int frameRate = 0;
        frameRate = jo.getInt("frameRate");
        String[] texIDs = Common.getStringArrFromJSON(jo.getJSONArray("textures"));
        Texture2D[] textures = new Texture2D[texIDs.length];

        for (int j = 0; j < texIDs.length; j++)
            textures[j] = GSystem.rsmanager.getTexture(texIDs[j]);

        Component c=new LoopAnimation(textures, frameRate);
        return c;
    }
	public LoopAnimation(Texture2D[] textures, int frameRate) {
		this.textures = textures;
		nframes = textures.length;
		curFrame = 0;

		frameTime = ((float) 1 / frameRate) * 1000;
		lastTime = System.currentTimeMillis();



	}

	public void setParent(ob2D b) {
		super.setParent(b);
	}
	public void init()
	{
		parent.tex = textures[0];
	}
	public void update() {
		long curTime = System.currentTimeMillis();

		if (curFrame == nframes - 1)
			curFrame = -1;

		if (curTime - lastTime >= frameTime) {

			parent.tex = textures[++curFrame];
			lastTime = curTime;
		}
	}

	public void run() {
		update();
	}
}
