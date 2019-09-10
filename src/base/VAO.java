package base;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GL30.*;

public class VAO {

	ArrayList<Integer> vboIDs;
	int vao;
	public VAO()
	{
		vao=GL30.glGenVertexArrays();
		System.out.println("Created VAO:"+vao);
		vboIDs=new ArrayList<Integer>();
	}
	public void loadToVBO(float[] data,int size)
	{
		bind();

		int vboID=GL30.glGenBuffers();
		System.out.println("Created VBO:"+vboID);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboID);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, Common.toFloatBuffer(data) , GL30.GL_STATIC_DRAW);
		GL30.glVertexAttribPointer(vboIDs.size(), size,GL11.GL_FLOAT, false, 0, 0);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
		unbind();

		vboIDs.add(vboID);
	}
	public void activateVPointers()
	{
		for(int i=0;i<vboIDs.size();i++)
			GL20.glEnableVertexAttribArray(i);
	}
	public void deactivateVPointers()
	{
		for(int i=0;i<vboIDs.size();i++)
			GL20.glEnableVertexAttribArray(i);
	}
	public void bind()
	{
		GL30.glBindVertexArray(vao);
	}
	public void unbind()
	{
		GL30.glBindVertexArray(0);
	}
	public void delete()
	{

	}
}
