package base;

import java.util.HashMap;
import java.util.Map;

public class ResourceManager {
    private static HashMap<String, Shape> models;
    private static HashMap<String, Texture2D> textures;
    private static HashMap<String, BShader> shaders;

    public static Shape basicQuad;

    static {
        models = new HashMap<String, Shape>();
        textures = new HashMap<String, Texture2D>();
        shaders = new HashMap<String, BShader>();
    }


    public static void addModel(Shape m, String id) {
        models.put(id, m);
    }

    public static void addTexture2D(Texture2D t, String id) {
        textures.put(id, t);
    }

    public static void addShader(BShader s, String id) {
        shaders.put(id, s);

    }
    public static Shape getModel(String id)
    {
        if(id.equals("generic_quad"))
            return basicQuad;
        else
            return models.get(id);
    }
    public static Texture2D getTexture(String id)
    {
        return textures.get(id);
    }
    public static void delete() {
        for (Map.Entry<String, Shape> entry : models.entrySet())
            entry.getValue().delete();
        for (Map.Entry<String, Texture2D> entry : textures.entrySet())
            entry.getValue().delete();
        for (Map.Entry<String, BShader> entry : shaders.entrySet())
            entry.getValue().delete();
    }
}
