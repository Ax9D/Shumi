package game;

import org.joml.Vector3f;

public class EnvironmentLight {

    public float intensity;
    public Vector3f color;

    public EnvironmentLight(Vector3f color, float intensity) {
        this.color = color;
        this.intensity = intensity;
    }
}
