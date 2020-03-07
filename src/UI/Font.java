package UI;

import base.Texture2D;

public class Font {
    Texture2D fontTex;
    public Font(String fontPath)
    {
        fontTex=new Texture2D(fontPath);
    }
    public void load()
    {

    }

}
