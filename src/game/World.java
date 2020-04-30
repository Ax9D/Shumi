package game;

import base.*;
import game.components.BoundingBox;
import game.components.LoopAnimation;
import game.components.PlayerAnimation;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class World {
    public HashSet<ob2D> ob2Ds;
    public ArrayList<PointLight> pointLights;
    public EnvironmentLight envLight;

    public GMap gm;

    int time;

    public SShader sceneShader;

    public ArrayList<ob2D> visible;

    public ob2D p;

    private static Comparator<ob2D> spriteCompare = (ob2D a, ob2D b) -> {
        BoundingBox ba = a.getComponent(BoundingBox.class);
        BoundingBox bb = b.getComponent(BoundingBox.class);

        return ba.bottom < bb.bottom ? 1 : -1;
    };


    public World() {
        sceneShader = new SShader("src/vertex.glsl", "src/fragment.glsl");
        pointLights = new ArrayList<PointLight>();
        ob2Ds = new HashSet<ob2D>();
        visible = new ArrayList<ob2D>();
        time = 0;
    }

    public void init() {
        float k = 10f;
        pointLights.add(new PointLight(new Vector2f(-k, 0f), new Vector3f(255,100,100).div(255), 10f, 0.1f));
        envLight = new EnvironmentLight(new Vector3f(255).div(255), 0f);


        sceneShader.use();
        sceneShader.addPointLights(pointLights);
        sceneShader.updateEnvironmentLight(envLight);

        gm.ts.use();
        gm.ts.addPointLights(pointLights);
        gm.ts.updateEnvironmentLight(envLight);

        for (int i = 0; i < 0; i++) {
            ob2D x;
            addOb2D(x = new ob2D(GSystem.rsmanager.basicQuad, new Vector2f((float) Math.random() * 16 - 8, (float) Math.random() * 16 - 8), new Vector2f((float) (Math.random() * 0.125f)), "asdf"));
            x.addComponent(new BoundingBox());
        }
        Texture2D t = new Texture2D("player.png");
        t.setNearest();
        p.tex = t;
        p.getComponent(LoopAnimation.class).disable();

        p.addComponent(new PlayerAnimation(8, GSystem.rsmanager.getTextures("player.walk.left"), GSystem.rsmanager.getTextures("player.walk.right"),
                GSystem.rsmanager.getTextures("player.walk.up"), GSystem.rsmanager.getTextures("player.walk.down"), t));


        BoundingBox bb = p.getComponent(BoundingBox.class);

        bb.xratio = 0.2f;
        bb.yratio = 0.125f / 2;
        //bb.yoffset=-1.6f;

        bb.yoffset = -0.9f;


        bb = find("tree").getComponent(BoundingBox.class);
        bb.xratio = 0.3f;
        bb.yratio = 0.09f;
        bb.xoffset = -0.1f;
        bb.yoffset = -0.55f;

        gm.biomeTex.isColor = false;
        gm.biomeTex.setNearest();
        gm.biomeTex.color = new Vector4f(119, 186, 120, 255).div(255);
        //gm.biomeTex.color=new Vector4f(0.05f,0.05f,0.05f,1);

        gm.size = 200;
        gm.tileSize = 0.125f;
        p.size = new Vector2f(1.8f);
    }

    public void addOb2D(ob2D b) {
        ob2Ds.add(b);
    }

    public void deleteOb2D(ob2D b) {
        ob2Ds.remove(b);
        b.delete();
    }

    public ob2D find(String id) {
        for (ob2D x : ob2Ds)
            if (x.name.equals(id))
                return x;
        return null;
    }

    private void pruneVisible() {
        View v = GSystem.view;
        Camera2D c = v.camera2D;
        float left = c.pos.x - v.camxExtent;
        float right = c.pos.x + v.camxExtent;
        float top = c.pos.y + v.camyExtent;
        float bottom = c.pos.y - v.camyExtent;

        visible.clear();

        float bposx, bposy, bleft, bright, btop, bbottom, bsizex, bsizey;
        for (ob2D b : ob2Ds) {
            bposx = b.pos.x;
            bposy = b.pos.y;
            bsizex = b.size.x;
            bsizey = b.size.y;
            bleft = bposx - bsizex;
            bright = bposx + bsizex;
            btop = bposy + bsizey;
            bbottom = bposy - bsizey;
            if (BPhysics.isCollision(left, right, bleft, bright, bottom, top, bbottom, btop))
                visible.add(b);
        }
        Collections.sort(visible, spriteCompare);


    }

    public void update() {
        gm.ts.use();
        gm.ts.updateEnvironmentLight(envLight);
        gm.ts.stop();

        sceneShader.use();
        sceneShader.updateEnvironmentLight(envLight);
        sceneShader.stop();

        for (ob2D x : ob2Ds) {
            if (x != p)
                BPhysics.handlePlayerFOCollision(p, x);
        }

        pruneVisible();
        time += 1;
    }

}
