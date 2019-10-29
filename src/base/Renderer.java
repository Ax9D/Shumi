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

                tmat = MatrixMath.get2DTMat(b.pos, b.size);

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

        bs.setMatrix("cmat", MatrixMath.get2DTMat(new Vector2f(0), new Vector2f(1,1)));

        float tileSkip = map.tileSize * 2;

        glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);

        /*
        for (Path p : map.paths) {
            p.tex.bind();
            if (p.dir == Path.PathDirection.Horizontal) {
                for (int i = 0; i < p.ntiles; i++) {
                    Vector2f pos = new Vector2f(gzerozero.x + (p.gridPos.x + i) * tileSkip, gzerozero.y - p.gridPos.y * tileSkip);

                    tmat = MatrixMath.get2DTMat(pos, new Vector2f(map.tileSize), 0);
                    bs.setMatrix("tmat", tmat);
                    glDrawElements(GL_TRIANGLES, m.ic, GL_UNSIGNED_INT, 0);
                }
            } else if (p.dir == Path.PathDirection.Vertical) {
                for (int i = 0; i < p.ntiles; i++) {
                    Vector2f pos = new Vector2f(gzerozero.x + p.gridPos.x * tileSkip, gzerozero.y - (p.gridPos.y + i) * tileSkip);

                    tmat = MatrixMath.get2DTMat(pos, new Vector2f(map.tileSize), (float) Math.PI / 2);
                    bs.setMatrix("tmat", tmat);
                    glDrawElements(GL_TRIANGLES, m.ic, GL_UNSIGNED_INT, 0);
                }
            }
        }
        // ms.stop();
        */
        for(Tile t:map.tiles)
        {
            t.tex.bind();
            Vector2f pos=new Vector2f(gzerozero.x+t.gridPos.x*tileSkip,gzerozero.y-t.gridPos.y*tileSkip);
            tmat=MatrixMath.get2DTMat(pos,new Vector2f(map.tileSize),0);
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
        /*Model m = ResourceManager.basicQuad;

        m.vao.bind();
        m.vao.activateVPointers();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m.eboID);

        Matrix4f tmat;

        BShader ms=map.ts;

        ms.use();

        tmat = MatrixMath.get2DTMat(map.pos, map.size);

        ms.setMatrix("cmat", cmat);
        ms.setMatrix("tmat", tmat);

        map.biomeTex.bind();

        glDrawElements(GL_TRIANGLES, m.ic, GL_UNSIGNED_INT, 0);

        bs.use();
        bs.setMatrix("cmat", cmat);

        float tileSkip=map.tileSize*2;

        for (Path p : map.paths) {
            p.tex.bind();
            if (p.dir == Path.PathDirection.Horizontal) {
                for (int i = 0; i <p.ntiles; i++) {
                    Vector2f pos = new Vector2f(map.zerozero.x + (p.gridPos.x + i) * tileSkip, map.zerozero.y - p.gridPos.y * tileSkip);

                    tmat = MatrixMath.get2DTMat(pos, new Vector2f(map.tileSize), 0);
                    bs.setMatrix("tmat", tmat);
                    glDrawElements(GL_TRIANGLES, m.ic, GL_UNSIGNED_INT, 0);
                }
            } else if (p.dir == Path.PathDirection.Vertical) {
                for (int i = 0; i <p.ntiles; i++) {
                    Vector2f pos = new Vector2f(map.zerozero.x + p.gridPos.x * tileSkip, map.zerozero.y - (p.gridPos.y + i) * tileSkip);

                    tmat = MatrixMath.get2DTMat(pos, new Vector2f(map.tileSize), (float)Math.PI/2);
                    bs.setMatrix("tmat", tmat);
                    glDrawElements(GL_TRIANGLES, m.ic, GL_UNSIGNED_INT, 0);
                }
            }
        }
        // ms.stop();
        m.vao.deactivateVPointers();
        //m.vao.unbind();
         */
        //glClear(GL_COLOR_BUFFER_BIT);

        glDisable( GL_BLEND );

        Model m = ResourceManager.basicQuad;
        m.vao.bind();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m.eboID);
        m.vao.activateVPointers();

        bs.use();

        map.mapTexFBO.tex.bind();

        Matrix4f tmat = MatrixMath.get2DTMat(map.pos, map.size);

        bs.setMatrix("tmat", tmat);
        bs.setMatrix("cmat", cmat);


        glDrawElements(GL_TRIANGLES, m.ic, GL_UNSIGNED_INT, 0);

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
