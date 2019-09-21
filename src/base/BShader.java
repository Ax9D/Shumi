package base;

import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL30.*;

public class BShader {
	private int program;

	public BShader(String vertexPath, String fragmentPath) {
		String vertexCode = Common.getText(vertexPath);
		String fragmentCode = Common.getText(fragmentPath);

		int vertexShader = compile(vertexCode, GL_VERTEX_SHADER);
		int fragmentShader = compile(fragmentCode, GL_FRAGMENT_SHADER);

		program = glCreateProgram();

		glAttachShader(program, vertexShader);
		glAttachShader(program, fragmentShader);

		glLinkProgram(program);

		String linkLog = glGetProgramInfoLog(program);
		if (linkLog.length()==0)
			System.out.println(linkLog);

		glDetachShader(program, vertexShader);
		glDetachShader(program, fragmentShader);

		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);

	}

	public void use() {
		glUseProgram(program);
	}

	public void stop() {
		glUseProgram(0);
	}

	public void setMatrix(String vname, Matrix4f mat) {
		int loc = glGetUniformLocation(program, vname);

		float[] asFloat = new float[16];
		mat.get(asFloat);

		glUniformMatrix4fv(loc, false, asFloat);
	}

	public void setInt(String vname, int x) {
		int loc = glGetUniformLocation(program, vname);
		glUniform1i(loc, x);
	}

	public void delete() {
		glDeleteProgram(program);
	}

	private int compile(String code, int type) {
		int shader = glCreateShader(type);
		glShaderSource(shader, code);
		glCompileShader(shader);
		String infoLog = glGetShaderInfoLog(shader);
		System.out.println(infoLog);
		return shader;
	}
}
