package base;

import game.*;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.util.HashMap;
import java.util.Map.Entry;

import static org.lwjgl.opengl.GL30.*;

public class Renderer {

    private Camera2D c;
    private Matrix4f cmat;

    public Renderer(Camera2D c) {
        this.c = c;
    }

    public void setState() {
        cmat = MatrixMath.get2DTMat(new Vector2f(-c.pos.x, -c.pos.y), c.scale);
    }

    public void render(ob2D b, BShader bs) {
        Model m = b.m;

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

    public void renderWorld(World w, BShader bs) {

        glEnable(GL_BLEND);

        GMap gm = w.gm;

        Matrix4f tmat;

        renderGMap(gm, bs);

        bs.use();

        for (Entry<Model, HashMap<ob2D, Boolean>> et : w.modelObpair.entrySet()) {
            Model m = et.getKey();
            m.vao.bind();
            m.vao.activateVPointers();

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m.eboID);

            glActiveTexture(GL_TEXTURE0);


            for (Entry<ob2D, Boolean> be : et.getValue().entrySet()) {
                ob2D b = be.getKey();
                b.tex.bind();

                tmat = MatrixMath.get2DTMat(b.pos, new Vector2f(b.size.x*Game.aspect_ratio,b.size.y));

                bs.setMatrix("tmat", tmat);
                bs.setMatrix("cmat", cmat);

                glDrawElements(GL_TRIANGLES, m.ic, GL_UNSIGNED_INT, 0);

                // b.tex.unbind();
            }


            //glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
            m.vao.deactivateVPointers();
            // m.vao.unbind();
        }
        //bs.stop();
        glDisable(GL_BLEND);
    }

    public void renderGMaptoTexture(GMap map, BShader bs) {

        FBO fbo = map.mapTexFBO;

        glViewport(0,0,fbo.width,fbo.height);
        fbo.bind();

        Model m = ResourceManager.basicQuad;

        m.vao.bind();
        m.vao.activateVPointers();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m.eboID);


        BShader ms = map.ts;

        ms.use();



        Vector2f origin = new Vector2f(0);
        //System.out.println(map.size);
        Vector2f gzerozero = new Vector2f(origin.x - 1 + map.tileSize, origin.y + 1 - map.tileSize);

        Matrix4f tmat = MatrixMath.get2DTMat(origin, 1);

        ms.setMatrix("tmat", tmat);
        map.biomeTex.bind();

        glDrawElements(GL_TRIANGLES, m.ic, GL_UNSIGNED_INT, 0);

        bs.use();

        bs.setMatrix("cmat", MatrixMath.get2DTMat(origin, 1));

        float tileSkip = map.tileSize * 2;

        glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);

        for(Tile t:map.tiles)
        {
            t.tex.bind();
            Vector2f pos=new Vector2f(gzerozero.x+t.gridPos.x*tileSkip,gzerozero.y-t.gridPos.y*tileSkip);
            tmat=MatrixMath.get2DTMat(pos,map.tileSize);
            bs.setMatrix("tmat",tmat);
            glDrawElements(GL_TRIANGLES,m.ic,GL_UNSIGNED_INT,0);
        }

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        m.vao.deactivateVPointers();

        fbo.unbind();

        glViewport(0,0, Main.WIDTH, Main.HEIGHT);
        //m.vao.unbind();

        glDisable(GL_BLEND);
    }

    public void renderGMap(GMap map, BShader bs) {

        glDisable( GL_BLEND );

        Model m = ResourceManager.basicQuad;
        m.vao.bind();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m.eboID);

        m.vao.activateVPointers();

        bs.use();

        map.mapTexFBO.tex.bind();

        Matrix4f tmat = MatrixMath.get2DTMat(map.pos, new Vector2f(map.size*Game.aspect_ratio,map.size));

        bs.setMatrix("tmat", tmat);
        bs.setMatrix("cmat", cmat);

        glDrawElements(GL_TRIANGLES, m.ic, GL_UNSIGNED_INT, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        m.vao.deactivateVPointers();

    }

    public void renderTOFBO(World w, BShader bs, FBO fbo, Model screenQuad, BShader screenShader) {
        fbo.bind();

        //Clear world
        glClearColor(1f, 0.64f, 0.64f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        setState();
        renderWorld(w, bs);

        fbo.unbind();

        glClearColor(1f, 1f, 1f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        screenShader.use();

        screenQuad.vao.bind();

        screenQuad.vao.activateVPointers();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, screenQuad.eboID);

        fbo.tex.bind();

        glDrawElements(GL_TRIANGLES, screenQuad.ic, GL_UNSIGNED_INT, 0);

        screenQuad.vao.deactivateVPointers();
    }
}
