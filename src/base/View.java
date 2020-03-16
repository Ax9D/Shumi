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
        if(ar>1) {
            ar_correction_matrix.setOrtho2D(-ar * scale, ar * scale, -scale, scale);
            camxExtent = ar * scale;
            camyExtent=scale;
        }
        else {
            ar_correction_matrix.setOrtho2D(-scale, scale, - scale/ar, scale/ar);
            camyExtent=scale/ar;
            camxExtent=scale;
        }
        this.ar = ar;
    }
}
