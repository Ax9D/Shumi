package game.components;

public class BoundingBox extends Component {
	public float left, right, top,bottom;
	public float xratio,yratio;
	public float xoffset,yoffset;


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
		left=parent.pos.x+xoffset-parent.size.x*xratio;
		right=parent.pos.x+xoffset+parent.size.x*xratio;
		top=parent.pos.y+yoffset+parent.size.y*yratio;
		bottom=parent.pos.y+yoffset-parent.size.y*yratio;

		float t;
		if(top<bottom) {
			t=bottom;
			bottom = top;
			top=t;
		}
		if(right<left) {
			t=left;
			left = right;
			right=t;
		}
	}
}
