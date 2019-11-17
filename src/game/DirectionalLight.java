package game;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class DirectionalLight {
    Vector3f color;
    Vector2f dir;
    float intensity;
    float beaminess;
    float length;
    public DirectionalLight(Vector3f color, float intensity, Vector2f dir,float beaminess,float length)
    {
        this.color=color;
        this.dir=dir;
        this.intensity=intensity;
        this.beaminess=beaminess;
        this.length=length;
    }
}
