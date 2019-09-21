package game;

import base.Model;
import game.components.CameraController;
import game.components.ComponentHandler;
import game.components.LoopAnimation;
import game.components.PlayerMovement;

import java.util.ArrayList;
import java.util.HashMap;

public class World {
    public ArrayList<ob2D> ob2Ds;

    public HashMap<Model, HashMap<ob2D,Boolean>> modelObpair;

    public GMap gm;

    public World() {
        modelObpair = new HashMap<Model, HashMap<ob2D,Boolean>>();
    }
    public void deleteOb2D(ob2D b)
    {
        b.delete();
        modelObpair.get(b.m).remove(b);
    }
    public void update() {

        for (LoopAnimation ba : ComponentHandler.getAllByComponent(LoopAnimation.class))
            ba.update();

        PlayerMovement pm = ComponentHandler.getAllByComponent(PlayerMovement.class).get(0);
        pm.update();

        ob2D p =pm.parent;

        for (int i = 1; i < ob2Ds.size(); i++)
            BPhysics.handlePlayerFOCollision(p, ob2Ds.get(i));

        ComponentHandler.getAllByComponent(CameraController.class).get(0).update();

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
