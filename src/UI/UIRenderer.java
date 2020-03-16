package UI;

import base.*;
import org.joml.Matrix4f;
import org.joml.Vector2f;

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

        float xpos=tbox.topLeft.x*WindowInfo.WIDTH;
        float ypos=tbox.topLeft.y*WindowInfo.HEIGHT;


        float xMax=(tbox.topLeft.x+tbox.size.x)*WindowInfo.WIDTH;
        float yMax=(tbox.topLeft.y-tbox.size.y)*WindowInfo.HEIGHT;

        float w;

        Glyph c;
        for(int i=0;i<tbox.words.length && ypos+fontHeight>yMax;i++)
        {
            if(xpos+tbox.wordWidths[i]*tbox.textSize>xMax) {
                ypos -= fontHeight*2;//Proceed to next Line
                xpos=tbox.topLeft.x*WindowInfo.WIDTH;
            }
            //Draw word
            for(int j=0;j<tbox.words[i].length();j++)
            {
                c=tbox.font.glyphs[tbox.words[i].charAt(j)];
                c.tex.bind();

                w=c.width*tbox.textSize;

                pos.x=xpos+w;
                pos.y=ypos-fontHeight;

                size.x=w;
                size.y=fontHeight;

                textShader.setMatrix("tmat", MatrixMath.get2DTMat(pos,size));

                glDrawElements(GL_TRIANGLES,sh.ic,GL_UNSIGNED_INT,0);

                xpos+=2*w;
            }
                if(i!=tbox.words.length-1) {
                    //Draw a space after word
                    c = tbox.font.glyphs[' '];
                    c.tex.bind();

                    w = c.width * tbox.textSize;

                    pos.x = xpos + w;
                    pos.y = ypos - fontHeight;

                    size.x = w;
                    size.y = fontHeight;

                    textShader.setMatrix("tmat", MatrixMath.get2DTMat(pos, size));

                    glDrawElements(GL_TRIANGLES, sh.ic, GL_UNSIGNED_INT, 0);

                    xpos += 2 * w;
                }
        }
        glDisable(GL_BLEND);
    }
}
