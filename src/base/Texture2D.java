package base;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL33.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class Texture2D {
	private int tex;

	public Texture2D(String path) {
		int[] w = new int[1];
		int[] h = new int[1];
		int[] ch = new int[1];

		stbi_set_flip_vertically_on_load(true);
		var imageBuffer = stbi_load(path, w, h, ch, 4);

		if (imageBuffer == null)
			throw new RuntimeException("Failed to Load texture: " + path + "\n" + stbi_failure_reason());

		tex = glGenTextures();

		bind();


		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, w[0], h[0], 0, GL_RGBA, GL_UNSIGNED_BYTE, imageBuffer);

		glGenerateMipmap(GL_TEXTURE_2D);

		unbind();

		stbi_image_free(imageBuffer);

		System.out.println("Created Texture:"+tex);

	}
	public Texture2D setNearest()
	{
		bind();
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST);
		unbind();

		return this;
	}
	public Texture2D(int w,int h)
	{
		tex = glGenTextures();

		bind();


		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE,0);

		glGenerateMipmap(GL_TEXTURE_2D);

		unbind();

		System.out.println("Created Texture:"+tex);
	}
	public int getID()
	{
		return tex;
	}
	public void delete() {
		glDeleteTextures(tex);
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, tex);
	}

	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}
