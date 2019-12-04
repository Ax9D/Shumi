package game;

import base.Model;
import base.SShader;
import game.components.Component;
import game.components.ComponentHandler;
import game.components.PlayerMovement;
import org.ietf.jgss.GSSCredential;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class World {
    public ArrayList<ob2D> ob2Ds;
    public ArrayList<PointLight> pointLights;
    public EnvironmentLight envLight;

    public HashMap<Model, HashMap<ob2D,Boolean>> modelObpair;

    public GMap gm;

    int time;

    public SShader sceneShader;

    public World() {
        modelObpair = new HashMap<Model, HashMap<ob2D,Boolean>>();
        sceneShader=new SShader("src/vertex.glsl","src/fragment.glsl");
        pointLights=new ArrayList<PointLight>();
        time=0;
    }
    public void init()
    {
            float k=1f;
            pointLights.add(new PointLight(new Vector2f(-k,0f),new Vector3f(1),1f,2f));
            pointLights.add(new PointLight(new Vector2f(k,0f),new Vector3f(1),1f,5f));
            pointLights.add(new PointLight(new Vector2f(0f,k),new Vector3f(1),1f,5f));
            pointLights.add(new PointLight(new Vector2f(0f,-k),new Vector3f(1),1f,5f));
            envLight=new EnvironmentLight(new Vector3f(1),1f);

            sceneShader.use();
            sceneShader.addPointLights(pointLights);
            sceneShader.updateEnvironmentLight(envLight);

            gm.ts.use();
            gm.ts.addPointLights(pointLights);
            gm.ts.updateEnvironmentLight(envLight);
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
        //time+=1;
        envLight.intensity=1*Math.abs((float)Math.sin(time*0.001f));
        gm.ts.use();
        gm.ts.updateEnvironmentLight(envLight);
        gm.ts.stop();

        sceneShader.use();
        sceneShader.updateEnvironmentLight(envLight);
        sceneShader.stop();

        PlayerMovement pm = (PlayerMovement)ComponentHandler.getAllByComponent(PlayerMovement.class).get(0);
       // pm.update();

        ob2D p =pm.parent;


         for(Map.Entry<String,HashMap<String,Component>> x:ComponentHandler.cmp.entrySet())
         {
             for(Map.Entry<String,Component> c:x.getValue().entrySet())
             {
                 //JAVA IS WEIRD
                 Component c_=c.getValue();
                 if(c_.enabled)
                    c_.update();
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
