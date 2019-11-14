package game;

import base.Model;
import base.SShader;
import game.components.*;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class World {
    public ArrayList<ob2D> ob2Ds;
    public ArrayList<PointLight> pointLights;

    public HashMap<Model, HashMap<ob2D,Boolean>> modelObpair;

    public GMap gm;

    public SShader sceneShader;

    public World() {
        modelObpair = new HashMap<Model, HashMap<ob2D,Boolean>>();
        sceneShader=new SShader("src/vertex.glsl","src/fragment.glsl");
        pointLights=new ArrayList<PointLight>();
    }
    public void init()
    {
            float k=0.5f;
            pointLights.add(new PointLight(new Vector2f(-k,0f),new Vector3f(0.9257f,0.8945f,0.6835f),0.7f,2f));
            pointLights.add(new PointLight(new Vector2f(k,0f),new Vector3f(0.9257f,0.8945f,0.6835f),0.7f,2f));
            pointLights.add(new PointLight(new Vector2f(0f,k),new Vector3f(0.9257f,0.8945f,0.6835f),0.7f,2f));
            sceneShader.use();
            sceneShader.addLights(pointLights);

            gm.ts.use();
            gm.ts.addLights(pointLights);
    }
    public void deleteOb2D(ob2D b)
    {
        b.delete();
        modelObpair.get(b.m).remove(b);
    }
    public void update() {

        /*
        for (LoopAnimation ba : ComponentHandler.getAllByComponent(LoopAnimation.class))
            ba.update();



        ComponentHandler.getAllByComponent(CameraController.class).get(0).update();
        */
        PlayerMovement pm = (PlayerMovement)ComponentHandler.getAllByComponent(PlayerMovement.class).get(0);
       // pm.update();

        ob2D p =pm.parent;


         for(Map.Entry<String,HashMap<String,Component>> x:ComponentHandler.cmp.entrySet())
         {
             for(Map.Entry<String,Component> c:x.getValue().entrySet())
             {
                 //JAVA IS WEIRD
                 c.getValue().update();
             }
         }

        for (int i = 1; i < ob2Ds.size(); i++)
            BPhysics.handlePlayerFOCollision(p, ob2Ds.get(i));
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
