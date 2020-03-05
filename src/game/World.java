package game;

import base.ResourceManager;
import base.SShader;
import base.Shape;
import game.components.BoundingBox;
import game.components.Component;
import game.components.ComponentHandler;
import game.components.PlayerMovement;
import input_handling.KeyboardHandler;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class World {
    public HashSet<ob2D> ob2Ds;
    public ArrayList<PointLight> pointLights;
    public EnvironmentLight envLight;

    public GMap gm;

    int time;

    public SShader sceneShader;

    public ArrayList<ob2D> visible;

    public ob2D p;

    public World() {
        sceneShader=new SShader("src/vertex.glsl","src/fragment.glsl");
        pointLights=new ArrayList<PointLight>();
        ob2Ds=new HashSet<ob2D>();
        visible=new ArrayList<ob2D>();
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

            for(int i=0;i<100;i++)
            {
                ob2D x;
                addOb2D(x=new ob2D(ResourceManager.basicQuad,new Vector2f((float)Math.random()*16-8,(float)Math.random()*16-8),new Vector2f((float)(Math.random()*0.125f)),"asdf"));
                x.addComponent(new BoundingBox());
            }
            System.out.println(ComponentHandler.database.toString());
    }
    public void addOb2D(ob2D b)
    {
        ob2Ds.add(b);
    }
    public void deleteOb2D(ob2D b)
    {
        b.delete();
        ob2Ds.remove(b);
    }
    public void update() {

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
       for (ob2D x:ob2Ds) {
            BPhysics.handlePlayerFOCollision(p, x);
       }

       time+=1;
    }

}
