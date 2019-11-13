package game.components;

import base.Camera2D;
import game.World;
import org.joml.Vector2f;
import org.json.JSONException;
import org.json.JSONObject;

public class CameraController extends Component{

    public static Component loadComponent(JSONObject jo) throws JSONException {

        float minHor=(float)jo.getDouble("minHor");
        float maxHor=(float)jo.getDouble("maxHor");
        float minVer=(float)jo.getDouble("minVer");
        float maxVer=(float)jo.getDouble("maxVer");

        Component c=new CameraController(minHor,maxHor,minVer,maxVer);
        return c;
    }


    private float minHor, maxHor, minVer, maxVer;
    private Camera2D c;



    public CameraController(float minHor, float maxHor, float minVer, float maxVer) {
        this.minHor = minHor;
        this.maxHor = maxHor;
        this.minVer = minVer;
        this.maxVer = maxVer;
    }
    public void setCamera(Camera2D c)
    {
        this.c=c;
    }
    public void init()
    {
    }
    public void update()
    {
        Vector2f screenPos=new Vector2f(parent.pos.x-c.pos.x,parent.pos.y-c.pos.y);

        if(screenPos.x>=maxHor)
            c.moveRight();
        else if(screenPos.x<=minHor)
            c.moveLeft();

        if(screenPos.y>=maxVer)
            c.moveUp();
        else if(screenPos.y<=minVer)
            c.moveDown();
      //  else if(parent.pos.x<)
    }
}
