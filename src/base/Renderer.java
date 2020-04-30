package base;

import game.GMap;
import game.World;
import game.components.BoundingBox;
import game.ob2D;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import static base.GSystem.debug;
import static base.GSystem.rsmanager;
import static org.lwjgl.opengl.GL30.*;

public class Renderer {
    public Matrix4f cmat;
    private Matrix4f ar_correction_matrix;
    public FBO[] pingpongGaussian;

    private BShader gaussianShader;
    private BShader screenShader;
    private BShader bloomShader;
    private BShader plainShader;

    private Texture2D boundingBoxDebugColor;
    private Texture2D shapeDebugColor;

    private Matrix4f tmat;

    public Renderer() {
        pingpongGaussian = new FBO[2];
        pingpongGaussian[0] = new FBO(WindowInfo.WIDTH, WindowInfo.HEIGHT, 1);
        pingpongGaussian[1] = new FBO(WindowInfo.WIDTH, WindowInfo.HEIGHT, 1);

        screenShader = new BShader("src/screenV.glsl", "src/screenF.glsl");
        gaussianShader = new BShader("src/screenV.glsl", "src/gaussian.glsl");
        bloomShader = new BShader("src/screenV.glsl", "src/bloom.glsl");
        plainShader = new BShader("src/simplevertex.glsl", "src/simplefragment.glsl");

        tmat = new Matrix4f();
        cmat = new Matrix4f();

        boundingBoxDebugColor = new Texture2D(Color.BLUE);
        shapeDebugColor = new Texture2D(Color.WHITE);

        GSystem.view.onChange(() -> {
            GSystem.renderer.pingpongGaussian[0].delete();
            GSystem.renderer.pingpongGaussian[0] = new FBO(WindowInfo.WIDTH, WindowInfo.HEIGHT, 1);
            GSystem.renderer.pingpongGaussian[1].delete();
            GSystem.renderer.pingpongGaussian[1] = new FBO(WindowInfo.WIDTH, WindowInfo.HEIGHT, 1);
        });
    }

    public void setState() {
        Camera2D c = GSystem.view.camera2D;

        MatrixMath.get2DTMat(new Vector2f(-c.pos.x, -c.pos.y), 1, cmat);
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

        renderGMap(gm);

        ss.use();

        glActiveTexture(GL_TEXTURE0);

        ss.setMatrix("ratio_mat", ar_correction_matrix);


        int prev = -1;
        for (ob2D x : w.visible) {
            if (x.sh.vao.vao != prev) {
                x.sh.load();
                prev = x.sh.vao.vao;
            }
            x.tex.bind();
            ss.textureColorCheck(x.tex);
            MatrixMath.get2DTMat(x.pos, x.size, tmat);
            ss.setMatrix("tmat", tmat);

            glDrawElements(GL_TRIANGLES, rsmanager.basicQuad.ic, GL_UNSIGNED_INT, 0);
        }
        glDisable(GL_BLEND);
    }


    public void renderGMap(GMap map) {
        SShader ms = map.ts;

        ms.use();

        glActiveTexture(GL_TEXTURE0);

        ms.setMatrix("cmat", cmat);
        ms.setMatrix("ratio_mat", ar_correction_matrix);

        MatrixMath.get2DTMat(map.pos, map.size, tmat);

        ms.setMatrix("tmat", tmat);

        map.biomeTex.bind();

        ms.textureColorCheck(map.biomeTex);

        renderQuad();

        //End of biome rendering
    }

    public void postProcess(FBO fbo) {
        //Draw brightness buffer to pp buffer 0
        screenShader.use();
        pingpongGaussian[0].bind();
        fbo.tex[0].bind();

        renderQuad();


        int horizontal = 0;

        gaussianShader.use();

        //~horizontal&1 flips the value of horizontal
        for (int i = 0; i < 10; i++, horizontal = ~horizontal & 1) {
            pingpongGaussian[horizontal].tex[0].bind();
            pingpongGaussian[~horizontal & 1].bind();

            gaussianShader.setInt("horizontal", horizontal);
            renderQuad();
        }
        pingpongGaussian[~horizontal & 1].unbind();
    }

    public void debugInfo() {

        World w = GSystem.world;

        SShader ss = w.sceneShader;
        //Draw bounding box rect end

        BoundingBox b;

        int prev = -1;
        for (ob2D x : w.visible) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
            plainShader.use();

            MatrixMath.get2DTMat(x.pos, x.size, tmat);
            plainShader.setMatrix("tmat", tmat);
            plainShader.setMatrix("ratio_mat", ar_correction_matrix);

            shapeDebugColor.bind();
            plainShader.textureColorCheck(shapeDebugColor);

            glDrawArrays(GL_POLYGON, 0, 4);

            boundingBoxDebugColor.bind();
            plainShader.textureColorCheck(boundingBoxDebugColor);

            b = x.getComponent(BoundingBox.class);

            MatrixMath.get2DTMat(x.pos.x + b.xoffset * x.size.x, x.pos.y + b.yoffset * x.size.y, x.size.x * b.xratio, x.size.y * b.yratio, tmat);

            plainShader.setMatrix("tmat", tmat);

            glDrawArrays(GL_POLYGON, 0, 4);

            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        }
    }

    public void renderQuad() {
        rsmanager.basicQuad.load();

        glDrawElements(GL_TRIANGLES, rsmanager.basicQuad.ic, GL_UNSIGNED_INT, 0);

        rsmanager.basicQuad.unload();
    }

    public void renderQuadLines() {
        rsmanager.basicQuad.vao.bind();

        rsmanager.basicQuad.vao.activateVPointers();

        glDrawArrays(GL_LINES, 0, 4);

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

        glClearColor(1f, 1f, 1f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        bloomShader.use();

        bloomShader.setInt("blurTexture", 0);
        bloomShader.setInt("colorTexture", 1);

        glActiveTexture(GL_TEXTURE0);
        pingpongGaussian[0].tex[0].bind();

        glActiveTexture(GL_TEXTURE1);
        fbo.tex[1].bind();
        //fbo.tex[1].bind();

        //screenShader.use();
        // fbo.tex[0].bind();
        //pingpongGaussian[0].tex[0].bind();
        renderQuad();

        if (debug)
            debugInfo();
    }
}
