package base;

import game.World;
import game.ob2D;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Map.Entry;

import static org.lwjgl.opengl.GL30.*;

public class Renderer {

	public static void render(ob2D b, BShader bs, Camera2D c) {
		Model m = b.m;

		m.vao.bind();

		m.vao.activateVPointers();

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m.eboID);

		glActiveTexture(GL_TEXTURE0);
		b.tex.bind();

		bs.use();

		Matrix4f tmat = MatrixMath.get2DTMat(b.pos, b.size);
		Matrix4f cmat = MatrixMath.get2DTMat(new Vector2f(-c.pos.x, -c.pos.y), c.scale);

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
	public static void render(World w,Camera2D c,BShader bs)
	{
        for(Entry<Model, ArrayList<ob2D>> et:w.modelObpair.entrySet()) {
            Model m = et.getKey();
            m.vao.bind();
            m.vao.activateVPointers();

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m.eboID);

            glActiveTexture(GL_TEXTURE0);

            for (ob2D b : et.getValue()) {

                b.tex.bind();

                bs.use();

                Matrix4f tmat = MatrixMath.get2DTMat(b.pos, b.size);
                Matrix4f cmat = MatrixMath.get2DTMat(new Vector2f(-c.pos.x, -c.pos.y), c.scale);

                bs.setMatrix("tmat", tmat);
                bs.setMatrix("cmat", cmat);

                bs.setInt("texSamp", 0);

                glDrawElements(GL_TRIANGLES, m.ic, GL_UNSIGNED_INT, 0);

                bs.stop();

                b.tex.unbind();
            }

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
            m.vao.deactivateVPointers();
            m.vao.unbind();
        }
	}
}
