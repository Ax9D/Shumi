package base;

import game.*;
import game.components.BoundingBox;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import static base.GSystem.*;
import static org.lwjgl.opengl.GL30.*;

public class Renderer {
    public Matrix4f cmat;
    private Matrix4f ar_correction_matrix;
    public FBO[] pingpongGaussian;

    private BShader gaussianShader;
    private BShader screenShader;
    private BShader bloomShader;
    private BShader plainShader;
    private Texture2D blue;
    private Texture2D white;

    public Renderer() {
        pingpongGaussian = new FBO[2];
        pingpongGaussian[0] = new FBO(WindowInfo.WIDTH, WindowInfo.HEIGHT, 1);
        pingpongGaussian[1] = new FBO(WindowInfo.WIDTH, WindowInfo.HEIGHT, 1);

        screenShader = new BShader("src/screenV.glsl", "src/screenF.glsl");
        gaussianShader = new BShader("src/screenV.glsl", "src/gaussian.glsl");
        bloomShader = new BShader("src/screenV.glsl", "src/bloom.glsl");
        plainShader = new BShader("src/simplevertex.glsl", "src/simplefragment.glsl");

        blue = new Texture2D(Color.BLUE_A);
        white = new Texture2D(Color.WHITE_A);

        GSystem.view.onChange(() -> {
            GSystem.renderer.pingpongGaussian[0].delete();
            GSystem.renderer.pingpongGaussian[0] = new FBO(WindowInfo.WIDTH, WindowInfo.HEIGHT, 1);
            GSystem.renderer.pingpongGaussian[1].delete();
            GSystem.renderer.pingpongGaussian[1] = new FBO(WindowInfo.WIDTH, WindowInfo.HEIGHT, 1);
        });
    }

    public void setState() {
        Camera2D c = GSystem.view.camera2D;
        cmat = MatrixMath.get2DTMat(new Vector2f(-c.pos.x, -c.pos.y), 1);
        GSystem.world.sceneShader.use();
        GSystem.world.sceneShader.setMatrix("cmat", cmat);
        plainShader.use();
        plainShader.setMatrix("cmat", cmat);
        ar_correction_matrix = GSystem.view.ar_correction_matrix;
    }

    public void renderWorld() {

        glEnable(GL_BLEND);
        World w = GSystem.world;

        SShader ss = w.sceneShader;
        GMap gm = w.gm;

        Matrix4f tmat;

        renderGMap(gm);

        ss.use();
        w.p.sh.load();

        w.p.tex.bind();
        ss.textureColorCheck(w.p.tex);

        glActiveTexture(GL_TEXTURE0);

        tmat = MatrixMath.get2DTMat(w.p.pos, w.p.size);

        ss.setMatrix("tmat", tmat);
        ss.setMatrix("ratio_mat", ar_correction_matrix);


        w.p.sh.load();
        glDrawElements(GL_TRIANGLES, rsmanager.basicQuad.ic, GL_UNSIGNED_INT, 0);

        int prev = -1;
        for (ob2D x : w.visible) {
            if (x.sh.vao.vao != prev) {
                x.sh.load();
                prev = x.sh.vao.vao;
            }
            glActiveTexture(GL_TEXTURE0);
            x.tex.bind();
            ss.textureColorCheck(x.tex);

            tmat = MatrixMath.get2DTMat(x.pos, x.size);
            ss.setMatrix("tmat", tmat);

            glDrawElements(GL_TRIANGLES, rsmanager.basicQuad.ic, GL_UNSIGNED_INT, 0);
        }
    }



    public void renderGMap(GMap map) {

        SShader ms = map.ts;

        ms.use();

        ms.setMatrix("cmat", cmat);
        ms.setMatrix("ratio_mat", ar_correction_matrix);

        Matrix4f tmat = MatrixMath.get2DTMat(map.pos, map.size);

        ms.setMatrix("tmat", tmat);

        map.biomeTex.bind();

        ms.textureColorCheck(map.biomeTex);

        renderQuad();

        //End of biome rendering
    }
    public void postProcess(FBO fbo)
    {
        //Draw brightness buffer to pp buffer 0
        screenShader.use();
        pingpongGaussian[0].bind();
        fbo.tex[0].bind();

        renderQuad();




        int horizontal=0;

        gaussianShader.use();

        //~horizontal&1 flips the value of horizontal
        for(int i=0;i<10;i++,horizontal=~horizontal&1)
        {
            pingpongGaussian[horizontal].tex[0].bind();
            pingpongGaussian[~horizontal&1].bind();

            gaussianShader.setInt("horizontal",horizontal);
            renderQuad();
        }
        pingpongGaussian[~horizontal&1].unbind();
    }
    public void debugInfo()
    {

        World w = GSystem.world;

        SShader ss = w.sceneShader;

        Matrix4f tmat=MatrixMath.get2DTMat(w.p.pos,w.p.size);

        glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);

        //Draw texture rect start
        plainShader.use();

        plainShader.setMatrix("tmat",tmat);
        plainShader.setMatrix("ratio_mat",ar_correction_matrix);

        white.bind();
        plainShader.textureColorCheck(white);

        glDrawArrays(GL_POLYGON,0,4);
        //Draw texture rect end

        //Draw bounding box rect start
        blue.bind();
        plainShader.textureColorCheck(blue);

        BoundingBox b=w.p.getComponent(BoundingBox.class);

        tmat=MatrixMath.get2DTMat(w.p.pos.x+b.xoffset,w.p.pos.y+b.yoffset,w.p.size.x*b.xratio,w.p.size.y*b.yratio);
        plainShader.setMatrix("tmat",tmat);

        glDrawArrays(GL_POLYGON,0,4);

        glPolygonMode(GL_FRONT_AND_BACK,GL_FILL);
        //Draw bounding box rect end

        ss.use();

        int prev = -1;
        for (ob2D x : w.visible) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
            plainShader.use();

            tmat=MatrixMath.get2DTMat(x.pos,x.size);
            plainShader.setMatrix("tmat",tmat);
            plainShader.setMatrix("ratio_mat", ar_correction_matrix);

            white.bind();
            plainShader.textureColorCheck(white);

            glDrawArrays(GL_POLYGON, 0, 4);

            blue.bind();
            plainShader.textureColorCheck(blue);

            b = x.getComponent(BoundingBox.class);

            tmat = MatrixMath.get2DTMat(x.pos.x+b.xoffset,x.pos.y+b.yoffset,x.size.x*b.xratio,x.size.y*b.yratio);
            plainShader.setMatrix("tmat", tmat);

            glDrawArrays(GL_POLYGON, 0, 4);

            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        }
    }
    public void renderQuad()
    {
        rsmanager.basicQuad.load();

        glDrawElements(GL_TRIANGLES, rsmanager.basicQuad.ic, GL_UNSIGNED_INT, 0);

        rsmanager.basicQuad.unload();
    }
    public void renderQuadLines()
    {
        rsmanager.basicQuad.vao.bind();

        rsmanager.basicQuad.vao.activateVPointers();

        glDrawArrays(GL_LINES,0,4);

        rsmanager.basicQuad.vao.deactivateVPointers();
        rsmanager.basicQuad.vao.unbind();
    }
    public void renderGame(FBO fbo) {
        fbo.bind();

        glClearColor(0.516f, 0.734f, 0.809f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        setState();
        renderWorld();

        fbo.unbind();

        postProcess(fbo);

        glDisable(GL_BLEND);
        glClearColor(1f, 1f, 1f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        bloomShader.use();

        bloomShader.setInt("blurTexture",0);
        bloomShader.setInt("colorTexture",1);

        glActiveTexture(GL_TEXTURE0);
        pingpongGaussian[0].tex[0].bind();

        glActiveTexture(GL_TEXTURE1);
        fbo.tex[1].bind();
        //fbo.tex[1].bind();

        //screenShader.use();
       // fbo.tex[0].bind();
        //pingpongGaussian[0].tex[0].bind();
        renderQuad();

        if(debug)
            debugInfo();
    }
}
