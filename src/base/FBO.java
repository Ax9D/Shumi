package base;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL33.*;

public class FBO {
    private int fboID;
    public Texture2D tex;

    public int width,height;
    public FBO(int width,int height)
    {
        this.width=width;
        this.height=height;
        tex=new Texture2D(width,height);
        fboID=glGenFramebuffers();
        bind();
        glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,tex.getID(),0);
        unbind();
    }
    public void saveToDisk(String name)
    {
        bind();
        ByteBuffer buffer= BufferUtils.createByteBuffer(width*height*4);
        glReadPixels(0,0,width,height,GL_RGBA,GL_UNSIGNED_BYTE,buffer);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                int i = (x + (width * y)) * 4;
                int r = buffer.get(i) & 0xFF;
                int g = buffer.get(i + 1) & 0xFF;
                int b = buffer.get(i + 2) & 0xFF;
                image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
            }
        }

        try {
            ImageIO.write(image,"PNG",new File(name));
        } catch (IOException e) { e.printStackTrace(); }
        unbind();
    }
    public void bind()
    {
        glBindFramebuffer(GL_FRAMEBUFFER,fboID);
    }
    public void unbind()
    {
        glBindFramebuffer(GL_FRAMEBUFFER,0);
    }
}
