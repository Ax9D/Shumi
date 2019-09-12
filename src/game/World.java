package game;

import base.Model;
import game.components.ComponentHandler;
import game.components.LoopAnimation;
import game.components.PlayerMovement;

import java.util.ArrayList;
import java.util.HashMap;

public class World {
	public ArrayList<ob2D> ob2Ds;

	public HashMap<Model, ArrayList<ob2D>> modelObpair;

	public ob2D player;

	public World()
	{
		modelObpair=new HashMap<Model,ArrayList<ob2D>>();
	}
	public void update() {

		for (LoopAnimation ba : ComponentHandler.getAll(LoopAnimation.class))
			ba.update();

		ComponentHandler.getAll(PlayerMovement.class).get(0).update();

		BPhysics.handlePlayerFOCollision(ob2Ds.get(0),ob2Ds.get(1));

		// for (ob2D b : ob2Ds)
		// runComponents(b);

		// Test code --> Remove
		// b.getComponent(BasicAnimation.class).run();

		// p.update();
	}
	/*
	 * public void runComponents(ob2D b) { for (Entry<String, Component> en :
	 * b.comps.entrySet()) { en.getValue().run(); } }
	 */
}
