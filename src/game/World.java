package game;

import base.ResourceManager;
import base.Shape;
import base.SShader;
import game.components.BoundingBox;
import game.components.Component;
import game.components.ComponentHandler;
import game.components.PlayerMovement;
import input_handling.KeyboardHandler;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public class World {
    public HashSet<ob2D> ob2Ds;
    public ArrayList<PointLight> pointLights;
    public EnvironmentLight envLight;

    public HashMap<Shape, HashSet<ob2D>> modelObpair;

    public GMap gm;

    int time;

    public SShader sceneShader;

    public ob2D p;
    LinkedList<ob2D> test;

    public World() {
        modelObpair = new HashMap<Shape, HashSet<ob2D>>();
        sceneShader=new SShader("src/vertex.glsl","src/fragment.glsl");
        pointLights=new ArrayList<PointLight>();
        ob2Ds=new HashSet<ob2D>();
        test=new LinkedList<ob2D>();
        time=0;
    }
    public void init()
    {
            float k=1f;
            pointLights.add(new PointLight(new Vector2f(-k,0f),new Vector3f(1),1f,50f));
            pointLights.add(new PointLight(new Vector2f(k,0f),new Vector3f(.906f,0.676f,0.473f),1f,10f));
            pointLights.add(new PointLight(new Vector2f(0f,k),new Vector3f(1),1f,50f));
            pointLights.add(new PointLight(new Vector2f(0f,-k),new Vector3f(1),1f,50f));
            envLight=new EnvironmentLight(new Vector3f(1),1f);

            sceneShader.use();
            sceneShader.addPointLights(pointLights);
            sceneShader.updateEnvironmentLight(envLight);

            gm.ts.use();
            gm.ts.addPointLights(pointLights);
            gm.ts.updateEnvironmentLight(envLight);

            for(int i=0;i<10000;i++)
            {
                ob2D x;
                addOb2D(x=new ob2D(ResourceManager.basicQuad,new Vector2f((float)Math.random()*16-8,(float)Math.random()*16-8),new Vector2f((float)(Math.random()*0.125f)),"asdf"));
                x.addComponent(new BoundingBox());
            }
        KeyboardHandler.addEventListener((key,action)->{
           /*if(key== GLFW.GLFW_KEY_F && action==GLFW.GLFW_RELEASE) {
                deleteOb2D(test.removeLast());
                System.out.println("Deleted");
            }*/
        });
    }
    public void addOb2D(ob2D b)
    {
        ob2Ds.add(b);
        test.add(b);
        modelObpair.get(b.sh).add(b);
    }
    public void deleteOb2D(ob2D b)
    {
        b.delete();
        ob2Ds.remove(b);
        modelObpair.get(b.sh).remove(b);
    }
    public void update() {

        /*
        for (LoopAnimation ba : ComponentHandler.getAllByComponent(LoopAnimation.class))
            ba.update();



        ComponentHandler.getAllByComponent(CameraController.class).get(0).update();
        */
        //time+=1;
        envLight.intensity=0.2f;
        gm.ts.use();
        gm.ts.updateEnvironmentLight(envLight);
        gm.ts.stop();

        sceneShader.use();
        sceneShader.updateEnvironmentLight(envLight);
        sceneShader.stop();

        for(Map.Entry<String,ArrayList<Component>> et:ComponentHandler.database.entrySet())
        {
            for(Component c:et.getValue())
            {
                if(c.enabled)
                    c.update();
            }
        }

        ob2D[] obArray=new ob2D[ob2Ds.size()];
        ob2Ds.toArray(obArray);
       for (ob2D x:ob2Ds) {
            BPhysics.handlePlayerFOCollision(p, x);
       }

       time+=1;
    }

}
