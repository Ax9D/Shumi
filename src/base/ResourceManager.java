package base;

import java.util.HashMap;
import java.util.Map;

public class ResourceManager {
    HashMap<String, Model> models;
    HashMap<String, Texture2D> textures;
    HashMap<String, BShader> shaders;

    public ResourceManager() {
        models = new HashMap<String, Model>();
        textures = new HashMap<String, Texture2D>();
        shaders = new HashMap<String, BShader>();
    }


    public void addModel(Model m, String id) {
        models.put(id, m);
    }

    public void addTexture2D(Texture2D t, String id) {
        textures.put(id, t);
    }

    public void addShader(BShader s, String id) {
        shaders.put(id, s);

    }

    public void delete() {
        for (Map.Entry<String, Model> entry : models.entrySet())
            entry.getValue().delete();
        for (Map.Entry<String, Texture2D> entry : textures.entrySet())
            entry.getValue().delete();
        for (Map.Entry<String, BShader> entry : shaders.entrySet())
            entry.getValue().delete();
    }
}
