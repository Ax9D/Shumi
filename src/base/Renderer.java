package base;

import game.*;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import static org.lwjgl.opengl.GL30.*;

public class Renderer {
    public Matrix4f cmat;
    private Matrix4f ar_correction_matrix;
    public Renderer() {
    }
    public void setState() {
        Camera2D c=GSystem.view.camera2D;
        cmat = MatrixMath.get2DTMat(new Vector2f(-c.pos.x, -c.pos.y), 1);
        GSystem.world.sceneShader.use();
        GSystem.world.sceneShader.setMatrix("cmat",cmat);
        GSystem.world.sceneShader.stop();
        ar_correction_matrix=GSystem.view.ar_correction_matrix;
    }
    public void render(ob2D b, BShader bs) {
        Shape m = b.sh;

        m.vao.bind();

        m.vao.activateVPointers();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m.eboID);

        glActiveTexture(GL_TEXTURE0);
        b.tex.bind();

        bs.use();

        Matrix4f tmat = MatrixMath.get2DTMat(b.pos, b.size);

        bs.setMatrix("tmat", tmat);
        bs.setMatrix("cmat", cmat);

        bs.setInt("texSamp", 0);

        glDrawElements(GL_TRIANGLES, m.ic, GL_UNSIGNED_INT, 0);

        bs.stop();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        b.tex.unbind();

        m.vao.deactivateVPointers();

        m.vao.unbind();
    }
    public void renderWorld() {

        World w=GSystem.world;

        SShader ss=w.sceneShader;
        GMap gm = w.gm;

        Matrix4f tmat;

        renderGMap(gm);

        ss.use();



        w.p.sh.load();
        glActiveTexture(GL_TEXTURE0);
        w.p.tex.bind();

        tmat = MatrixMath.get2DTMat(w.p.pos, w.p.size);

        ss.setMatrix("tmat", tmat);
        ss.setMatrix("ratio_mat",ar_correction_matrix);


        glDrawElements(GL_TRIANGLES, w.p.sh.ic, GL_UNSIGNED_INT, 0);

          int prev=-1;
            for(ob2D x:w.visible)
            {
                if(x.sh.vao.vao!=prev) {
                    x.sh.load();
                    prev=x.sh.vao.vao;
                }
                glActiveTexture(GL_TEXTURE0);
                x.tex.bind();
                tmat=MatrixMath.get2DTMat(x.pos,x.size);
                ss.setMatrix("tmat",tmat);
                glDrawElements(GL_TRIANGLES,x.sh.ic,GL_UNSIGNED_INT,0);

            }


    }

    public void renderGMap(GMap map) {

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        Shape m = GSystem.rsmanager.basicQuad;

        SShader ms = map.ts;

        m.load();

        ms.use();

        ms.setMatrix("cmat", cmat);
        ms.setMatrix("ratio_mat", ar_correction_matrix);

        Matrix4f tmat = MatrixMath.get2DTMat(map.pos, map.size);

        ms.setMatrix("tmat", tmat);

        map.biomeTex.bind();

        glDrawElements(GL_TRIANGLES, m.ic, GL_UNSIGNED_INT, 0);

        //End of biome rendering
    }

    public void renderGame(FBO fbo, Shape screenQuad, BShader screenShader) {
        fbo.bind();

        //Clear world
        glClearColor(0.516f, 0.734f, 0.809f, 1.0f);
        //glClearColor(0f, 0f, 0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        setState();
        renderWorld();

        fbo.unbind();

        glDisable(GL_BLEND);
        glClearColor(1f, 1f, 1f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        screenShader.use();

        screenQuad.load();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, screenQuad.eboID);

        fbo.tex.bind();

        glDrawElements(GL_TRIANGLES, screenQuad.ic, GL_UNSIGNED_INT, 0);

        screenQuad.unload();
    }
}
