package UI;

import base.*;
import base.Shape;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

public class UIRenderer {

    private BShader textShader;
    public Matrix4f ar_correction_matrix;
    private Vector2f pos,size;
    public UIRenderer()
    {
        textShader=new BShader("src/textV.glsl","src/textF.glsl");
        ar_correction_matrix=new Matrix4f();
        ar_correction_matrix.identity();
        ar_correction_matrix.setOrtho2D(0,WindowInfo.WIDTH,0,WindowInfo.HEIGHT);
        pos=new Vector2f();
        size=new Vector2f();

        GSystem.view.onChange(()->{
            GSystem.uirenderer.ar_correction_matrix.setOrtho2D(0,WindowInfo.WIDTH,0,WindowInfo.HEIGHT);
        });
    }
    public void renderFrame(Frame frame)
    {

    }
    public void renderText(TextBox tbox)
    {
        Shape sh=GSystem.rsmanager.basicQuad;

        glEnable(GL_BLEND);
        sh.vao.bind();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, sh.eboID);

        textShader.use();

        textShader.setVector3f("textColor",tbox.textColor);
        textShader.setMatrix("ratio_mat",ar_correction_matrix);

        glActiveTexture(GL_TEXTURE0);

        float fontHeight=tbox.font.fontHeight*tbox.textSize;

        Frame parentFrame=tbox.parent;


        float xpos=(parentFrame.topLeft.x+tbox.topLeft.x*parentFrame.width)*WindowInfo.WIDTH;
        float ypos=(parentFrame.topLeft.y+tbox.topLeft.y*parentFrame.height)*WindowInfo.HEIGHT;


        float xMax=xpos+tbox.size.x*WindowInfo.WIDTH;
        float yMax=ypos-tbox.size.y*WindowInfo.HEIGHT;

        float w;

        char c;
        Glyph g;
        float xpos_,ypos_;
        xpos_=xpos;
        ypos_=ypos;
        for(int i=0;i<tbox.words.length && ypos_+fontHeight>yMax;i++)
        {
            if(xpos_+tbox.wordWidths[i]*tbox.textSize>xMax) {
                ypos_ -= fontHeight*2;//Proceed to next Line
                xpos_=xpos;
            }
            //Draw word
            for(int j=0;j<tbox.words[i].length();j++)
            {
                c=tbox.words[i].charAt(j);
                if(c=='\n')
                {
                    //Move to next line
                    ypos_-=fontHeight*2;
                    xpos_=xpos;
                }
                else {
                    g = tbox.font.glyphs[c];
                    g.tex.bind();

                    w = g.width * tbox.textSize;

                    pos.x = xpos_ + w;
                    pos.y = ypos_ - fontHeight;

                    size.x = w;
                    size.y = fontHeight;

                    textShader.setMatrix("tmat", MatrixMath.get2DTMat(pos, size));

                    glDrawElements(GL_TRIANGLES, sh.ic, GL_UNSIGNED_INT, 0);

                    xpos_ += 2 * w;
                }
            }
                //If not last word
                if(i!=tbox.words.length-1) {
                    //Draw a space after word
                    g = tbox.font.glyphs[' '];
                    g.tex.bind();

                    w = g.width * tbox.textSize;

                    pos.x = xpos_ + w;
                    pos.y = ypos_ - fontHeight;

                    size.x = w;
                    size.y = fontHeight;

                    textShader.setMatrix("tmat", MatrixMath.get2DTMat(pos, size));

                    glDrawElements(GL_TRIANGLES, sh.ic, GL_UNSIGNED_INT, 0);

                    xpos_ += 2 * w;
                }
        }
        glDisable(GL_BLEND);
    }
}
