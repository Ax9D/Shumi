package game;

import base.*;
import game.components.BoundingBox;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.*;

public class World {
    public HashSet<ob2D> ob2Ds;
    public ArrayList<PointLight> pointLights;
    public EnvironmentLight envLight;

    public GMap gm;

    int time;

    public SShader sceneShader;

    public ArrayList<ob2D> visible;

    public ob2D p;

    private static Comparator<ob2D> spriteCompare=(ob2D a,ob2D b)-> {
        BoundingBox ba = a.getComponent(BoundingBox.class);
        BoundingBox bb = b.getComponent(BoundingBox.class);

        return ba.top > bb.top ? 1 : -1;
    };


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
            pointLights.add(new PointLight(new Vector2f(-k,0f),new Vector3f(1),0.5f,50f));
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
                addOb2D(x=new ob2D(GSystem.rsmanager.basicQuad,new Vector2f((float)Math.random()*16-8,(float)Math.random()*16-8),new Vector2f((float)(Math.random()*0.125f)),"asdf"));
                x.addComponent(new BoundingBox());
            }
    }
    public void addOb2D(ob2D b)
    {
        ob2Ds.add(b);
    }
    public void deleteOb2D(ob2D b)
    {
        ob2Ds.remove(b);
        b.delete();
    }

    private void pruneVisible()
    {
        View v=GSystem.view;
        Camera2D c=v.camera2D;
        float left=c.pos.x-v.camxExtent;
        float right=c.pos.x+v.camxExtent;
        float top=c.pos.y+v.camyExtent;
        float bottom=c.pos.y-v.camyExtent;

        visible.clear();

        float bposx,bposy,bleft,bright,btop,bbottom,bsizex,bsizey;
        for(ob2D b:ob2Ds) {
            bposx=b.pos.x;
            bposy=b.pos.y;
            bsizex=b.size.x;
            bsizey=b.size.y;
            bleft = bposx - bsizex;
            bright = bposx + bsizex;
            btop = bposy + bsizey;
            bbottom = bposy - bsizey;
            if (BPhysics.isCollision(left,right,bleft,bright,bottom,top,bbottom,btop))
                visible.add(b);
        }
        Collections.sort(visible,spriteCompare);


    }
    public void update() {


        envLight.intensity=0.7f;
        gm.ts.use();
        gm.ts.updateEnvironmentLight(envLight);
        gm.ts.stop();

        sceneShader.use();
        sceneShader.updateEnvironmentLight(envLight);
        sceneShader.stop();

       for (ob2D x:ob2Ds) {
            BPhysics.handlePlayerFOCollision(p, x);
       }

       pruneVisible();
       time+=1;
    }

}
