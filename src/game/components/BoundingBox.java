package game.components;

public class BoundingBox extends Component {
	public float left, right, top,bottom;
	private float xExtent,yExtent;

	public void init()
	{
		this.xExtent=parent.size.x;
		this.yExtent=parent.size.y;
	}
	public void init(float xExtent,float yExtent)
	{
		this.xExtent=xExtent;
		this.yExtent=yExtent;
	}
	public void compute()
	{
		left=parent.pos.x-xExtent;
		right=parent.pos.x+xExtent;
		top=parent.pos.y+yExtent;
		bottom=parent.pos.y-yExtent;
	}
}
