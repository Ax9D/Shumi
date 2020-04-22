package UI;

import base.ImageUtils;
import base.Texture2D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class GFont {
    Glyph[] glyphs;
    private boolean antialias;
    public int fontHeight;
    public int spaceWidth;
    public GFont(String fontName,boolean antialias)
    {
        glyphs=new Glyph[256];
        Font f=new Font(fontName,Font.PLAIN,48);
        this.antialias=antialias;
        for(char c=32;c<127;c++)
            glyphs[c]=loadGlyph(f,c);

        spaceWidth=glyphs[' '].width;
    }
    public int getWidth(String s)
    {
        char c;
        int ret=0;
        for(int i=0;i<s.length();i++)
        {
            c=s.charAt(i);
            if(glyphs[c]!=null)
            ret+=glyphs[c].width;
        }
        return ret;
    }
    private Glyph loadGlyph(Font f,char c)
    {
        BufferedImage img=new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2D=img.createGraphics();
        g2D.setFont(f);

        if(antialias)
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        FontMetrics fmetric=g2D.getFontMetrics();

        int glyphWidth=fmetric.charWidth(c);

        if(fontHeight==0)
            fontHeight=fmetric.getHeight();

        g2D.dispose();

        img=new BufferedImage(glyphWidth,fontHeight,BufferedImage.TYPE_INT_ARGB);
        g2D=img.createGraphics();
        g2D.setFont(f);

        if(antialias)
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        g2D.setPaint(Color.WHITE);
        g2D.drawString(String.valueOf(c),0,fmetric.getAscent());
        g2D.dispose();

        ByteBuffer imgBuff= ImageUtils.toByteBuffer(img,glyphWidth,fontHeight);

        Texture2D tex=new Texture2D(imgBuff,glyphWidth,fontHeight);
        tex.setNearest();

        if(!antialias)
            tex.setNearest();
        else
            tex.setLinear();

        Glyph ret=new Glyph(tex,glyphWidth,fontHeight);

        return ret;
    }

}
