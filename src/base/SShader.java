package base;

import game.DirectionalLight;
import game.EnvironmentLight;
import game.PointLight;

import java.util.ArrayList;

//Scene shader
public class SShader extends BShader {

    public SShader(String vertexPath,String fragmentPath)
    {
        super(vertexPath,fragmentPath);
    }
    public void addPointLights(ArrayList<PointLight> pointLights)
    {
        int nlights= pointLights.size();
        int curnlight=getInt("point_light_count");
        if(curnlight+nlights<256)
        {
            for(int i=0;i<nlights;i++) {
                setStructArray("ptLights", i, "pos", pointLights.get(i).pos);
                setStructArray("ptLights", i, "color", pointLights.get(i).col);
                setStructArray("ptLights", i, "max_intensity", pointLights.get(i).max_intensity);
                setStructArray("ptLights", i, "falloff", pointLights.get(i).falloff);
            }
            setInt("point_light_count",curnlight+nlights);
        }
    }
    public void updateEnvironmentLight(EnvironmentLight elight)
    {
        setStruct("envLight","intensity",elight.intensity);
        setStruct("envLight","color",elight.color);
    }
    public void addDirectionalLights(ArrayList<DirectionalLight> directionalLights)
    {

    }
    public void removeLight(int index)
    {

    }
}
