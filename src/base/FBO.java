package base;

import static org.lwjgl.opengl.GL33.*;

public class FBO {
    private int fboID;
    public Texture2D tex;
    public FBO(Texture2D t)
    {
        tex=t;
        fboID=glGenFramebuffers();
        bind();
        glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,tex.getID(),0);
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
