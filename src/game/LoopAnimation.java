package game;

import base.Texture2D;

public class LoopAnimation extends Component {
	int nframes;
	int curFrame;
	Texture2D[] textures;

	float frameTime;
	long lastTime;

	public LoopAnimation(Texture2D[] textures, int frameRate) {
		this.textures = textures;
		nframes = textures.length;
		curFrame = 0;

		frameTime = ((float) 1 / frameRate) * 1000;
		lastTime = System.currentTimeMillis();
	}

	public void setParent(ob2D b) {
		super.setParent(b);
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
