package base;

import UI.GFont;

import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;

public class ResourceManager {
    HashMap<String, Shape> models;
    HashMap<String, Texture2D> textures;
    HashMap<String, BShader> shaders;
    HashMap<String, GFont> fonts;

    GFont defaultFont;

    public Shape basicQuad;
    public Texture2D basicTex;

    public ResourceManager()
    {
        models = new HashMap<String, Shape>();
        textures = new HashMap<String, Texture2D>();
        shaders = new HashMap<String, BShader>();
        fonts=new HashMap<String,GFont>();
        basicTex = new Texture2D("test.png");
        defaultFont=new GFont("Arial",true);
    }

    public void addModel(Shape m, String id) {
        models.put(id, m);
    }

    public  void addTexture2D(Texture2D t, String id) {
        textures.put(id, t);
    }

    public  void addShader(BShader s, String id) {
        shaders.put(id, s);

    }
    public void addFont(GFont f,String fontName)
    {
        fonts.put(fontName,f);
    }
    public  Shape getModel(String id)
    {
        if(id.equals("generic_quad"))
            return basicQuad;
        else
            return models.get(id);
    }
    public  GFont getFont(String fontName)
    {
        return fonts.getOrDefault(fontName,defaultFont);
    }
    public  Texture2D getTexture(String id)
    {
        return textures.get(id);
    }
    public  void delete() {
        for (Map.Entry<String, Shape> entry : models.entrySet())
            entry.getValue().delete();
        for (Map.Entry<String, Texture2D> entry : textures.entrySet())
            entry.getValue().delete();
        for (Map.Entry<String, BShader> entry : shaders.entrySet())
            entry.getValue().delete();
    }
}
