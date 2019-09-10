package game;

public class BoundingBox extends Component {
	float left, right, top,bottom;

	public BoundingBox(float left, float right, float top, float bottom) {
		this.left = left;
		this.right = right;
		this.bottom = bottom;
		this.top = top;
	}
	public BoundingBox()
	{
		this.left=parent.pos.x-parent.size.x;
		this.right=parent.pos.x+parent.size.x;
		this.bottom=parent.pos.y-parent.size.y;
		this.top=parent.pos.y+parent.size.y;
	}
}
