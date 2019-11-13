package base;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

public class Model {
	public VAO vao;
	public int eboID;
	public int ic;

	public Model(VAO vao, int eboID, int ic) {
		this.vao = vao;
		this.eboID = eboID;
		this.ic = ic;
	}

	public Model(float[] verts, int[] inds, float[] tCoords) {
		vao = new VAO();
		vao.loadToVBO(verts, 2);
		vao.loadToVBO(tCoords, 2);

		// Create EBO
		vao.bind();
		eboID = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, eboID);
		GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, Common.toIntBuffer(inds), GL30.GL_STATIC_DRAW);
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, 0);
		vao.unbind();

		ic = inds.length;
	}

	/*
	 * public void setTexture(Texture2D tex) { this.tex = tex; }
	 */
	public void load()
	{
		vao.bind();
		vao.activateVPointers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
	}
	public void unload()
	{
		vao.deactivateVPointers();
	}
	public void delete() {
		vao.delete();
		GL20.glDeleteBuffers(eboID);
	}
}
