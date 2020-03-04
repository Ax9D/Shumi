package base;

import game.GMap;
import game.Tile;
import game.World;
import game.components.BoundingBox;
import game.ob2D;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.util.*;
import java.util.Map.Entry;

import static org.lwjgl.opengl.GL30.*;

public class Renderer {

    private Camera2D c;
    private Matrix4f cmat;
    private Matrix4f ar_correction_matrix;

    private float scale;
    private float ar;

    private float camxExtent;
    private float camyExtent;

    public ArrayList<ob2D> visible;

    private static Comparator<ob2D> spriteCompare=(ob2D a,ob2D b)-> {
        BoundingBox ba = a.getComponent(BoundingBox.class);
        BoundingBox bb = b.getComponent(BoundingBox.class);

        return ba.top > bb.top ? 1 : -1;
    };

    public Renderer(Camera2D c) {
        this.c = c;
        ar_correction_matrix=new Matrix4f();
        visible=new ArrayList<ob2D>();
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

    public void setAspectRatio(float ar)
    {
        ar_correction_matrix.setOrtho2D(-ar*scale,ar*scale,-scale,scale);
        this.ar=ar;
        camxExtent=ar*scale;
    }
    public void adjustScale(float scale)
    {
        this.scale=scale;
        setAspectRatio(ar);
        camyExtent=scale;
    }
    public float getScale()
    {
        return scale;
    }
    private void pruneVisible(World w)
    {
        float left=c.pos.x-camxExtent;
        float right=c.pos.x+camxExtent;
        float top=c.pos.y+camyExtent;
        float bottom=c.pos.y-camyExtent;

        visible.clear();

        float bposx,bposy,bleft,bright,btop,bbottom,bsizex,bsizey;
        for(ob2D b:w.ob2Ds) {
            bposx=b.pos.x;
            bposy=b.pos.y;
            bsizex=b.size.x;
            bsizey=b.size.y;
            bleft = bposx - bsizex;
            bright = bposx + bsizex;
            btop = bposy + bsizey;
            bbottom = bposy - bsizey;
            if (!(bright<=left || bleft>=right || bbottom>=top || btop<=bottom))
                visible.add(b);
        }
        Collections.sort(visible,spriteCompare);
    }
    public void renderWorld(World w) {

        pruneVisible(w);

        SShader ss=w.sceneShader;
        GMap gm = w.gm;

        Matrix4f tmat;

        renderGMap(gm, ss);

        ss.use();



        w.p.sh.load();
        glActiveTexture(GL_TEXTURE0);
        w.p.tex.bind();

        tmat = MatrixMath.get2DTMat(w.p.pos, w.p.size);

        ss.setMatrix("tmat", tmat);
        ss.setMatrix("ratio_mat",ar_correction_matrix);


        glDrawElements(GL_TRIANGLES, w.p.sh.ic, GL_UNSIGNED_INT, 0);

        /*

        for (Entry<Shape, HashSet<ob2D>> et : w.modelObpair.entrySet()) {
            Shape sh = et.getKey();
            sh.load();

            glActiveTexture(GL_TEXTURE0);


            for (ob2D b : et.getValue() ){
                b.tex.bind();

                tmat = MatrixMath.get2DTMat(b.pos, b.size);

                ss.setMatrix("tmat", tmat);


                glDrawElements(GL_TRIANGLES, sh.ic, GL_UNSIGNED_INT, 0);

                // b.tex.unbind();
            }
*/          int prev=-1;
            for(ob2D x:visible)
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

    public void renderGMap(GMap map, SShader ss) {

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        Shape m= ResourceManager.basicQuad;

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

    public void renderGame(World w, FBO fbo, Shape screenQuad, BShader screenShader) {
        fbo.bind();

        //Clear world
        glClearColor(0.516f, 0.734f, 0.809f, 1.0f);
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
