package base;

import org.joml.Matrix4f;
import org.joml.Vector2f;

public class MatrixMath {

	public static Matrix4f get2DTMat(Vector2f t,Vector2f s)
	{
		Matrix4f ret=new Matrix4f();
		ret.identity();
		ret.translate(t.x,t.y,0.0f);
		ret.scale(s.x,s.y,1);
		return ret;
	}
	public static Matrix4f get2DTMat(float x,float y,float sx,float sy)
	{
		Matrix4f ret=new Matrix4f();
		ret.identity();
		ret.translate(x,y,0.0f);
		ret.scale(sx,sy,1);
		return ret;
	}
	public static Matrix4f get2DTMat(Vector2f t,Vector2f s,float r)
	{
		Matrix4f ret=new Matrix4f();
		ret.identity();
		ret.translate(t.x,t.y,0.0f);
		ret.scale(s.x,s.y,1);
		ret.rotateZ(r);
		return ret;
	}
	public static Matrix4f get2DTMat(Vector2f t,float s)
	{
		Matrix4f ret=new Matrix4f();
		ret.identity();
		ret.translate(t.x,t.y,0.0f);
		ret.scale(s,s,1);

		return ret;
	}
	public static Matrix4f get2DTMat(Vector2f t,float s,float r)
	{
		Matrix4f ret=new Matrix4f();
		ret.identity();
		ret.translate(t.x,t.y,0.0f);
		ret.scale(s,s,1f);
		ret.rotateZ(r);
		return ret;
	}

}
