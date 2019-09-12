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
}
