package base;

import game.GMap;
import game.Tile;
import game.World;
import game.ob2D;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.util.HashMap;
import java.util.Map.Entry;

import static org.lwjgl.opengl.GL30.*;

public class Renderer {

    private Camera2D c;
    private Matrix4f cmat;
    private Matrix4f ar_correction_matrix;


    public Renderer(Camera2D c) {
        this.c = c;
        ar_correction_matrix=new Matrix4f();
    }
    public Matrix4f getARMat()
    {
        return ar_correction_matrix;
    }
    public void setState(World w) {
        cmat = MatrixMath.get2DTMat(new Vector2f(-c.pos.x, -c.pos.y), 1);
        w.sceneShader.use();
        w.sceneShader.setMatrix("cmat",cmat);
        w.sceneShader.stop();
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

    public void setAspectRatio(float ar,float scale)
    {
        ar*=scale;
        ar_correction_matrix.setOrtho2D(-ar,ar,-scale,scale);
    }
    public void renderWorld(World w) {

        SShader ss=w.sceneShader;
        GMap gm = w.gm;

        Matrix4f tmat;

        renderGMap(gm, ss);

        ss.use();

        for (Entry<Model, HashMap<ob2D, Boolean>> et : w.modelObpair.entrySet()) {
            Model m = et.getKey();
            m.load();

            glActiveTexture(GL_TEXTURE0);


            for (Entry<ob2D, Boolean> be : et.getValue().entrySet()) {
                ob2D b = be.getKey();
                b.tex.bind();

                tmat = MatrixMath.get2DTMat(b.pos, b.size);

                ss.setMatrix("tmat", tmat);
                ss.setMatrix("ratio_mat",ar_correction_matrix);


                glDrawElements(GL_TRIANGLES, m.ic, GL_UNSIGNED_INT, 0);

                // b.tex.unbind();
            }


            //glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
            m.unload();
            // m.vao.unbind();
        }
        //bs.stop();


    }

   /* public void renderGMaptoTexture(GMap map, BShader bs) {


        FBO fbo = map.mapTexFBO;

        glViewport(0,0,fbo.width,fbo.height);
        fbo.bind();

        Model m = ResourceManager.basicQuad;

        m.load();

        BShader ms = map.ts;

        ms.use();



        Vector2f origin = new Vector2f(0);
        //System.out.println(map.size);
        Vector2f gzerozero = new Vector2f(origin.x - 1 + map.tileSize, origin.y + 1 - map.tileSize);

        Matrix4f tmat = MatrixMath.get2DTMat(origin, 1);

        ms.setMatrix("tmat", tmat);
        map.biomeTex.bind();

        glDrawElements(GL_TRIANGLES, m.ic, GL_UNSIGNED_INT, 0);

        //End of biome rendering


        bs.use();

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

        m.unload();

        fbo.unbind();

        glViewport(0,0, Main.WIDTH, Main.HEIGHT);
        //m.vao.unbind();

        glDisable(GL_BLEND);


    }
*/
    public void renderGMap(GMap map, SShader ss) {

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        Model m= ResourceManager.basicQuad;

        SShader ms = map.ts;

        m.load();

        ms.use();

        ms.setMatrix("cmat", cmat);
        ms.setMatrix("ratio_mat",ar_correction_matrix);

        Matrix4f tmat = MatrixMath.get2DTMat(map.pos, map.size);

        ms.setMatrix("tmat", tmat);

        map.biomeTex.bind();

        glDrawElements(GL_TRIANGLES, m.ic, GL_UNSIGNED_INT, 0);

        //End of biome rendering

        ss.use();
        ss.setMatrix("ratio_mat",ar_correction_matrix);

        float tileSkip = map.tileSize * 2;


        //System.out.println(map.size);
        Vector2f gzerozero = new Vector2f(map.pos.x - map.size + map.tileSize, map.pos.y + map.size - map.tileSize);

        for(Tile t:map.tiles)
        {
            t.tex.bind();
            Vector2f pos=new Vector2f(gzerozero.x+t.gridPos.x*tileSkip,gzerozero.y-t.gridPos.y*tileSkip);
            tmat=MatrixMath.get2DTMat(pos,map.tileSize);
            ss.setMatrix("tmat",tmat);
            glDrawElements(GL_TRIANGLES,m.ic,GL_UNSIGNED_INT,0);
        }

        m.unload();
    }

    public void renderGame(World w, FBO fbo, Model screenQuad, BShader screenShader) {
        fbo.bind();

        //Clear world
        glClearColor(1f, 0.64f, 0.64f, 1.0f);
        //glClearColor(0f, 0f, 0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        setState(w);
        renderWorld(w);

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
