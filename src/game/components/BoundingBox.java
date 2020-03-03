package game.components;

import org.json.JSONException;
import org.json.JSONObject;

public class BoundingBox extends Component {

    public static Component loadComponent(JSONObject jo) throws JSONException {
        Component c=null;
        String bType=jo.getString("bType");
        if(bType.equals("basic")) {
            c=new BoundingBox();
        }
        return c;
    }

	public float left, right, top,bottom;
	private float xratio,yratio;
	private float xoffset,yoffset;

	public void init()
	{
		this.xratio=1;
		this.yratio=1;
		this.xoffset=this.yoffset=0;
	}
	public void init(float xratio,float yratio)
	{
		this.xratio=xratio;
		this.yratio=yratio;
	}
	public void compute()
	{

		left=parent.pos.x-xoffset-(parent.size.x)*xratio;
		right=parent.pos.x+xoffset+(parent.size.x)*xratio;
		top=parent.pos.y+yoffset+(parent.size.y)*yratio;
		bottom=parent.pos.y-yoffset-(parent.size.y)*yratio;
	}
}
