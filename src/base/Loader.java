package base;

import game.World;
import game.components.BoundingBox;
import game.components.Component;
import game.components.LoopAnimation;
import game.components.PlayerMovement;
import game.ob2D;
import org.joml.Vector2f;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Loader {

    ResourceManager rs;
    World w;

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
            w.modelObpair.put(m,new ArrayList<ob2D>());
        }
    }

    private void getComponents(ob2D b, JSONArray ja) throws JSONException {

        JSONObject jo;
        for (int i = 0; i < ja.length(); i++) {
            jo = ja.getJSONObject(i);
            String type = jo.getString("type");
            Component c;
            switch (type) {
                case "LoopAnimation":
                    int frameRate = jo.getInt("frameRate");
                    String[] texIDs = getStringArrFromJSON(jo.getJSONArray("textures"));
                    Texture2D[] textures = new Texture2D[texIDs.length];

                    for (int j = 0; j < texIDs.length; j++)
                        textures[j] = rs.textures.get(texIDs[j]);

                    c = new LoopAnimation(textures, frameRate);
                    b.addComponent(c);
                    break;
                case "PlayerMovement":
                    float walkSpeed = (float) jo.getDouble("walkSpeed");
                    c = new PlayerMovement(walkSpeed);
                    b.addComponent(c);
                    break;
                case "BoundingBox":
                	String bType=jo.getString("bType");
                	if(bType.equals("basic")) {
                        c=new BoundingBox();
						b.addComponent(c);
					}
                	break;
            }
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
                b.tex = Game.defaultTex;
            else {
                String tid = jo.getString("texture");
                b.tex = rs.textures.get(tid);
            }

            getComponents(b, jo.getJSONArray("components"));

            obs.add(b);
            var objectList=w.modelObpair.get(m);

            objectList.add(b);
        }
        w.ob2Ds = obs;
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
            // getPlayer(jj.getJSONObject("player"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
