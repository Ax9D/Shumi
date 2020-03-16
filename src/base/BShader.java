package base;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

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
	public void setVector3f(String vname, Vector3f vec) {
		int loc = glGetUniformLocation(program, vname);

		glUniform3f(loc,vec.x,vec.y,vec.z);
	}
	public int getInt(String vname)
	{
		int loc=glGetUniformLocation(program,vname);
		return glGetUniformi(program,loc);
	}
	public void setStructArray(String arrName,int index,String fieldName,float fieldValue)
	{
		String queryString=arrName+"["+index+"]."+fieldName;
		int loc=glGetUniformLocation(program,queryString);
		glUniform1f(loc,fieldValue);
	}
	public void setStructArray(String arrName, int index, String fieldName, Vector2f fieldValue)
	{
		String queryString=arrName+"["+index+"]."+fieldName;
		int loc=glGetUniformLocation(program,queryString);
		glUniform2f(loc,fieldValue.x,fieldValue.y);
	}
	public void setStruct(String structName,String fieldName,Vector3f fieldValue)
    {
        String queryString=structName+"."+fieldName;
        int loc=glGetUniformLocation(program,queryString);
        glUniform3f(loc,fieldValue.x,fieldValue.y,fieldValue.z);
    }
    public void setStruct(String structName,String fieldName,float fieldValue)
    {
        String queryString=structName+"."+fieldName;
        int loc=glGetUniformLocation(program,queryString);
        glUniform1f(loc,fieldValue);
    }
	public void setStructArray(String arrName, int index, String fieldName, Vector3f fieldValue)
	{
		String queryString=arrName+"["+index+"]."+fieldName;
		int loc=glGetUniformLocation(program,queryString);
		glUniform3f(loc,fieldValue.x,fieldValue.y,fieldValue.z);
	}
	public void setInt(String vname, int x) {
		int loc = glGetUniformLocation(program, vname);
		glUniform1i(loc, x);
	}
	public void setFloat(String vname, float x) {
		int loc = glGetUniformLocation(program, vname);
		glUniform1f(loc, x);
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
