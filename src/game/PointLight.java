package game;

import base.BShader;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class PointLight {
    public Vector2f pos;
    public Vector3f col;
    public float max_intensity;
    public float falloff;
    public PointLight(Vector2f pos, Vector3f col, float max_intensity, float falloff)
    {
        this.pos=pos;
        this.col=col;
        this.max_intensity=max_intensity;
        this.falloff=falloff;
    }
}
