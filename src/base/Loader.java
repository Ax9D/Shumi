package base;

import game.*;
import game.components.*;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashSet;

public class Loader {
    World w;
    static Texture2D defaultTex;
    private static final String assetsDirPath="assets";
    private static final String textureDirPath=Paths.get(assetsDirPath,"textures").toString();

    static{
        defaultTex=new Texture2D("assets/textures/test.png");
    }

    public Loader(World w, String rspath, String gmpath) {
        this.w = w;
        loadGame(rspath, gmpath);
    }
    private void getTextures(JSONArray ja) throws JSONException {
        for (int i = 0; i < ja.length(); i++) {
            JSONObject jo = ja.getJSONObject(i);
            String path = jo.getString("path");
            //Get only file name without the extension
            String id=path.substring(0,path.lastIndexOf('.'));
            //Convert / to . --> player/walk/up becomes player.walk.up
            id=id.replace('/','.');
            Texture2D tex;
            GSystem.rsmanager.addTexture2D(tex=new Texture2D(textureDirPath+"/"+path),id);
            if(jo.has("interpolation"))
            {
                switch(jo.getString("interpolation"))
                {
                    case "linear":
                        break;
                    case "nearest":
                }
            }

            tex.setNearest();
        }
    }

    private void getModels(JSONArray ja) throws JSONException {

        for (int i = 0; i < ja.length(); i++) {
            JSONObject jo = ja.getJSONObject(i);
            String mid = jo.getString("id");
            // String tid = jo.getString("texture");

            float[] vertices = Common.getFloatArrFromJSON(jo.getJSONArray("vertices"));
            int[] indices = Common.getIntArrFromJSON(jo.getJSONArray("indices"));
            float[] texCoords = Common.getFloatArrFromJSON(jo.getJSONArray("textureCoords"));

            Shape m = new Shape(vertices, indices, texCoords);
            // m.setTexture(rs.textures.get(tid));

            GSystem.rsmanager.addModel(m, mid);
        }
    }

    private void getComponents(ob2D b, JSONArray ja) throws JSONException {

        JSONObject jo;
        for (int i = 0; i < ja.length(); i++) {
            jo = ja.getJSONObject(i);
            Component c=null;
            String type=jo.getString("type");
            switch (type) {
                case "LoopAnimation":
                    int frameRate = jo.getInt("frameRate");
                    String[] texIDs = Common.getStringArrFromJSON(jo.getJSONArray("textures"));
                    Texture2D[] textures = new Texture2D[texIDs.length];

                    for (int j = 0; j < texIDs.length; j++)
                        textures[j] = GSystem.rsmanager.getTexture(texIDs[j]);

                    c = new LoopAnimation(textures, frameRate);
                    break;


                case "PlayerMovement":
                    float walkSpeed = (float) jo.getDouble("walkSpeed");
                    c = new PlayerMovement(walkSpeed);

                    break;



                case "BoundingBox":
                	String bType=jo.getString("bType");
                	if(bType.equals("basic")) {
                        c=new BoundingBox();
					}
                	break;




                case "CameraController":
                    float minHor=(float)jo.getDouble("minHor");
                    float maxHor=(float)jo.getDouble("maxHor");
                    float minVer=(float)jo.getDouble("minVer");
                    float maxVer=(float)jo.getDouble("maxVer");

                    c=new CameraController(minHor,maxHor,minVer,maxVer);

                    break;
                default:
                    throw new JSONException("No matching components found: "+type);
            }

            b.addComponent(c);
        }
    }

    private void getOb2Ds(JSONArray ja) throws JSONException {
        for (int i = 0; i < ja.length(); i++) {
            JSONObject jo = ja.getJSONObject(i);
            String bid = jo.getString("id");
            String mid = jo.getString("model");

            float[] posArr = Common.getFloatArrFromJSON(jo.getJSONArray("pos"));
            float[] sizeArr = Common.getFloatArrFromJSON(jo.getJSONArray("size"));

            Vector2f pos = new Vector2f(posArr[0], posArr[1]);
            Vector2f size = new Vector2f(sizeArr[0], sizeArr[1]);

            Shape m=GSystem.rsmanager.getModel(mid);
            ob2D b = new ob2D(m, pos, size, bid);

            if (!jo.has("texture"))
                b.tex = defaultTex;
            else {
                String tid = jo.getString("texture");
                b.tex = GSystem.rsmanager.getTexture(tid);
            }

            getComponents(b, jo.getJSONArray("components"));
            if(!bid.equals("player"))
                w.addOb2D(b);
            else
                {
                    w.p=b;
                    w.addOb2D(w.p);
                }
        }
    }
    public void getGMaps(JSONArray ja) throws JSONException
    {
        JSONObject jo=ja.getJSONObject(0);
        float[] posF=Common.getFloatArrFromJSON(jo.getJSONArray("pos"));
        float sizeF=(float)jo.getDouble("size");

        int tileCount=jo.getInt("tileCount");

        var ts=new SShader("src/vertex.glsl","src/mapF.glsl");

        w.gm=new GMap(new Vector2f(posF[0],posF[1]),sizeF,ts,tileCount);

        w.gm.biomeTex=GSystem.rsmanager.getTexture("grass");

        ts.use();

        ts.setInt("tileCount",tileCount);
        ts.setInt("biomeTex",0);

        ts.stop();
    }
    /*
    private void getPlayer(JSONObject jo) throws JSONException {
        float[] posA = getFloatArrFromJSON(jo.getJSONArray("pos"));
        float[] sizeA = getFloatArrFromJSON(jo.getJSONArray("size"));
        String mid = jo.getString("model");

        w.p = new Player(ResourceManager.models.get(mid), new Vector2f(posA[0], posA[1]), new Vector2f(sizeA[0], sizeA[1]));
    }
    */
    public void loadGame(String rspath, String gmpath) {
        loadrsData(rspath);
        loadGameData(gmpath);
        w.init();
    }

    public void loadrsData(String path) {
        String fullJSONtxt = Common.getText(path);
        try {
            JSONObject jj = new JSONObject(fullJSONtxt);

            getTextures(jj.getJSONArray("textures"));
            getModels(jj.getJSONArray("models"));

            // getOb2Ds(jj.getJSONArray("ob2Ds"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void loadGameData(String path) {
        String fullJSONtxt = Common.getText(path);

        try {
            JSONObject jj = new JSONObject(fullJSONtxt);

            getOb2Ds(jj.getJSONArray("ob2Ds"));
            getGMaps(jj.getJSONArray("GMaps"));
            // getPlayer(jj.getJSONObject("player"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public Component loadComponent(JSONObject jo) throws JSONException {
        String name=jo.getString("type");
        Component retComp=null;
        try {
            //Potentially unsafe code
            System.out.println("Loading component: "+name);

            //retComp;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return retComp;
    }
}
