package base;

import game.GMap;
import game.World;
import game.components.*;
import game.ob2D;
import org.joml.Vector2f;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Loader {

    ResourceManager rs;
    World w;
    static Texture2D defaultTex;

    static{
        defaultTex=new Texture2D("test.png");
    }
    /*
     * public static Texture2D loadIMG(String path) { int[] w=new int[1]; int[]
     * h=new int[1]; int[] ch=new int[1];
     *
     * var imageBuffer=stbi_load(path, w, h, ch,4);
     *
     * if(imageBuffer==null) throw new
     * RuntimeException("Failed to Load texture: "+path+"\n"+stbi_failure_reason());
     *
     * System.out.println(imageBuffer);
     *
     * int tex=glGenTextures(); glBindTexture(GL_TEXTURE_2D, tex);
     *
     * glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
     * glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,
     * GL_LINEAR_MIPMAP_LINEAR); glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S,
     * GL_CLAMP_TO_EDGE); glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T,
     * GL_CLAMP_TO_EDGE);
     *
     * glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, w[0], h[0],0,
     * GL_RGBA,GL_UNSIGNED_BYTE,imageBuffer);
     *
     * glBindTexture(GL_TEXTURE_2D,0);
     *
     * stbi_image_free(imageBuffer);
     *
     * textures.add(tex);
     *
     * A return new Texture2D(tex); } public static void delete() { int[] iAr=new
     * int[textures.size()]; for(int i=0;i<iAr.length;i++) { iAr[i]=textures.get(i);
     * } glDeleteTextures(iAr); } private static ByteBuffer loadBytes(String path) {
     *
     * File f=new File(path);
     *
     * ByteBuffer ret=BufferUtils.createByteBuffer((int)f.length());
     *
     * try { Scanner sc=new Scanner(new FileInputStream(f)); while(sc.hasNextByte())
     * ret.put(sc.nextByte());
     *
     * } catch (FileNotFoundException e) { e.printStackTrace(); } ret.flip(); return
     * ret;
     *
     * }
     */
    public Loader(ResourceManager rs, World w, String rspath, String gmpath) {
        this.rs = rs;
        this.w = w;
        loadGame(rspath, gmpath);
    }

    private String[] getStringArrFromJSON(JSONArray ja) throws JSONException {
        String[] ret;
        ret = new String[ja.length()];
        for (int i = 0; i < ja.length(); i++)
            ret[i] = ja.getString(i);
        return ret;
    }

    private float[] getFloatArrFromJSON(JSONArray ja) throws JSONException {
        float[] ret;
        ret = new float[ja.length()];
        for (int i = 0; i < ja.length(); i++)
            ret[i] = (float) ja.getDouble(i);

        return ret;
    }

    private int[] getIntArrFromJSON(JSONArray ja) throws JSONException {
        int[] ret;
        ret = new int[ja.length()];
        for (int i = 0; i < ja.length(); i++)
            ret[i] = ja.getInt(i);

        return ret;
    }

    private void getTextures(JSONArray ja) throws JSONException {
        for (int i = 0; i < ja.length(); i++) {
            JSONObject jo = ja.getJSONObject(i);
            String tid = jo.getString("id");
            String path = jo.getString("path");
            rs.addTexture2D(new Texture2D(path), tid);
        }
    }

    private void getModels(JSONArray ja) throws JSONException {

        for (int i = 0; i < ja.length(); i++) {
            JSONObject jo = ja.getJSONObject(i);
            String mid = jo.getString("id");
            // String tid = jo.getString("texture");

            float[] vertices = getFloatArrFromJSON(jo.getJSONArray("vertices"));
            int[] indices = getIntArrFromJSON(jo.getJSONArray("indices"));
            float[] texCoords = getFloatArrFromJSON(jo.getJSONArray("textureCoords"));

            Model m = new Model(vertices, indices, texCoords);
            // m.setTexture(rs.textures.get(tid));

            rs.addModel(m, mid);
            w.modelObpair.put(m,new HashMap<ob2D,Boolean>());
        }
    }

    private void getComponents(ob2D b, JSONArray ja) throws JSONException {

        JSONObject jo;
        for (int i = 0; i < ja.length(); i++) {
            jo = ja.getJSONObject(i);
            String type = jo.getString("type");
            Component c=null;
            switch (type) {
                case "LoopAnimation":
                    int frameRate = jo.getInt("frameRate");
                    String[] texIDs = getStringArrFromJSON(jo.getJSONArray("textures"));
                    Texture2D[] textures = new Texture2D[texIDs.length];

                    for (int j = 0; j < texIDs.length; j++)
                        textures[j] = rs.textures.get(texIDs[j]);

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

            float[] posArr = getFloatArrFromJSON(jo.getJSONArray("pos"));
            float[] sizeArr = getFloatArrFromJSON(jo.getJSONArray("size"));

            Vector2f pos = new Vector2f(posArr[0], posArr[1]);
            Vector2f size = new Vector2f(sizeArr[0], sizeArr[1]);

            Model m=rs.models.get(mid);
            ob2D b = new ob2D(m, pos, size, bid);

            if (!jo.has("texture"))
                b.tex = defaultTex;
            else {
                String tid = jo.getString("texture");
                b.tex = rs.textures.get(tid);
            }

            getComponents(b, jo.getJSONArray("components"));

            obs.add(b);
            var objectList=w.modelObpair.get(m);

            objectList.put(b,true);
        }
        w.ob2Ds = obs;
    }
    public void getGMaps(JSONArray ja) throws JSONException
    {
        JSONObject jo=ja.getJSONObject(0);
        Texture2D grass=rs.textures.get(jo.getString("grass"));
        Texture2D dirt=rs.textures.get(jo.getString("dirt"));
        Texture2D map=rs.textures.get(jo.getString("map"));
        float[] posF=getFloatArrFromJSON(jo.getJSONArray("pos"));
        float[] sizeF=getFloatArrFromJSON(jo.getJSONArray("size"));


        var ts=new BShader("src/mapV","src/mapF");
        w.gm=new GMap(new Vector2f(posF[0],posF[1]),new Vector2f(sizeF[0],sizeF[1]),grass,dirt,map,ts);

        ts.use();

        ts.setInt("map",0);
        ts.setInt("grass",1);
        ts.setInt("dirt",2);

        ts.stop();
    }
    /*
    private void getPlayer(JSONObject jo) throws JSONException {
        float[] posA = getFloatArrFromJSON(jo.getJSONArray("pos"));
        float[] sizeA = getFloatArrFromJSON(jo.getJSONArray("size"));
        String mid = jo.getString("model");

        w.p = new Player(rs.models.get(mid), new Vector2f(posA[0], posA[1]), new Vector2f(sizeA[0], sizeA[1]));
    }
    */
    public void loadGame(String rspath, String gmpath) {
        loadrsData(rspath);
        loadGameData(gmpath);
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
}
