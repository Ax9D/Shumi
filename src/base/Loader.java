package base;

import game.*;
import game.components.*;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Loader {
    World w;
    static Texture2D defaultTex;

    static{
        defaultTex=new Texture2D("test.png");
    }

    public Loader(World w, String rspath, String gmpath) {
        this.w = w;
        loadGame(rspath, gmpath);
    }
    private void getTextures(JSONArray ja) throws JSONException {
        for (int i = 0; i < ja.length(); i++) {
            JSONObject jo = ja.getJSONObject(i);
            String tid = jo.getString("id");
            String path = jo.getString("path");
            ResourceManager.addTexture2D(new Texture2D(path), tid);
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

            Model m = new Model(vertices, indices, texCoords);
            // m.setTexture(rs.textures.get(tid));

            ResourceManager.addModel(m, mid);
            w.modelObpair.put(m,new HashMap<ob2D,Boolean>());
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
                        textures[j] = ResourceManager.getTexture(texIDs[j]);

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
        ArrayList<ob2D> obs = new ArrayList<ob2D>();
        for (int i = 0; i < ja.length(); i++) {
            JSONObject jo = ja.getJSONObject(i);
            String bid = jo.getString("id");
            String mid = jo.getString("model");

            float[] posArr = Common.getFloatArrFromJSON(jo.getJSONArray("pos"));
            float[] sizeArr = Common.getFloatArrFromJSON(jo.getJSONArray("size"));

            Vector2f pos = new Vector2f(posArr[0], posArr[1]);
            Vector2f size = new Vector2f(sizeArr[0], sizeArr[1]);

            Model m=ResourceManager.getModel(mid);
            ob2D b = new ob2D(m, pos, size, bid);

            if (!jo.has("texture"))
                b.tex = defaultTex;
            else {
                String tid = jo.getString("texture");
                b.tex = ResourceManager.getTexture(tid);
            }

            getComponents(b, jo.getJSONArray("components"));

            obs.add(b);
            var objectList=w.modelObpair.get(m);

            objectList.put(b,true);
        }
        w.ob2Ds = obs;
    }
    public Path[] getPaths(JSONArray ja) throws JSONException {
        int n=ja.length();
        Path[] ret=new Path[n];

        int[] gridPos;
        int ntiles;
        Path.PathType type;
        Path.PathDirection dir;

        for(int i=0;i<n;i++)
        {
            JSONObject jo=ja.getJSONObject(i);
            type=Path.PathType.valueOf(jo.getString("type"));
            gridPos=Common.getIntArrFromJSON(jo.getJSONArray("gridPos"));
            ntiles=jo.getInt("ntiles");
            dir=Path.PathDirection.valueOf(jo.getString("direction"));

            ret[i]=new Path(type,new Vector2i(gridPos[0],gridPos[1]),ntiles,dir);
        }


        return ret;
    }

    public Tile[] getTiles(JSONArray ja) throws JSONException {
        int n=ja.length();
        Tile[] ret=new Tile[n];

        int[] gridPos;
        String texID;

        for(int i=0;i<n;i++)
        {
            JSONObject jo=ja.getJSONObject(i);
            gridPos=Common.getIntArrFromJSON(jo.getJSONArray("gridPos"));
            texID=jo.getString("texture");

            ret[i]=new Tile(new Vector2f(gridPos[0],gridPos[1]),ResourceManager.getTexture(texID));
        }


        return ret;
    }
    public void getGMaps(JSONArray ja) throws JSONException
    {
        JSONObject jo=ja.getJSONObject(0);
        float[] posF=Common.getFloatArrFromJSON(jo.getJSONArray("pos"));
        float sizeF=(float)jo.getDouble("size");

        Tile[] tiles=getTiles(jo.getJSONArray("tiles"));

        int tileCount=jo.getInt("tileCount");

        var ts=new SShader("src/vertex.glsl","src/mapF.glsl");

        w.gm=new GMap(new Vector2f(posF[0],posF[1]),sizeF,ts,tileCount,tiles);

        w.gm.biomeTex=ResourceManager.getTexture("grass");

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
