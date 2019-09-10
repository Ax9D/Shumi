package game;

import java.util.ArrayList;

import base.BShader;
import base.Camera2D;
import base.Renderer;

public class World {
	public ArrayList<ob2D> ob2Ds;
	public Player p;

	public void update() {

		for (LoopAnimation ba : ComponentHandler.getAll(LoopAnimation.class))
			ba.update();

		ComponentHandler.getAll(PlayerMovement.class).get(0).update();

		// for (ob2D b : ob2Ds)
		// runComponents(b);

		// Test code --> Remove
		// b.getComponent(BasicAnimation.class).run();

		// p.update();
	}

	public void render(Camera2D c, BShader bs) {
		for (ob2D b : ob2Ds)
			Renderer.render(b, bs, c);
	}
	/*
	 * public void runComponents(ob2D b) { for (Entry<String, Component> en :
	 * b.comps.entrySet()) { en.getValue().run(); } }
	 */
}
