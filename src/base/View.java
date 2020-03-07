package base;

import org.joml.Matrix4f;

public class View {
    public Camera2D camera2D;
    public float ar;
    public float scale;

    public Matrix4f ar_correction_matrix;
    public float camxExtent;
    public float camyExtent;

    public View(Camera2D camera2D, float ar, float scale)
    {
        this.camera2D = camera2D;
        this.scale=scale;
        camyExtent=scale;
        ar_correction_matrix=new Matrix4f();
        adjustAspectRatio(ar);
    }
    public void adjustScale(float scale)
    {
            this.scale = scale;
            adjustAspectRatio(ar);
            camyExtent = scale;
    }
    public void adjustAspectRatio(float ar)
    {
        ar_correction_matrix.setOrtho2D(-ar * scale, ar * scale, -scale, scale);
        this.ar = ar;
        camxExtent = ar * scale;
    }
}
