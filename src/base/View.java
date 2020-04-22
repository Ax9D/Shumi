package base;

import org.joml.Matrix4f;

import java.util.ArrayList;

public class View {
    public Camera2D camera2D;
    public float ar;
    public float scale;

    public Matrix4f ar_correction_matrix;
    public float camxExtent;
    public float camyExtent;

    private ArrayList<Runnable> onChangeLambdas;

    public View(Camera2D camera2D, float ar, float scale)
    {
        this.camera2D = camera2D;
        this.scale=scale;
        camyExtent=scale;
        ar_correction_matrix=new Matrix4f();
        onChangeLambdas=new ArrayList<Runnable>();
        adjustAspectRatio(ar);
    }
    public void adjustScale(float scale)
    {
            this.scale = scale;
            adjustAspectRatio(ar);
            camyExtent = scale;
    }
    public void onChange(Runnable r)
    {
        onChangeLambdas.add(r);
    }
    public void processChange()
    {
        for(Runnable r:onChangeLambdas)
            r.run();
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
