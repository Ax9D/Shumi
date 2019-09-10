package game;

public class BoundingBox extends Component {
	float left, right, up, down;

	public BoundingBox(float left, float right, float up, float down) {
		this.left = left;
		this.right = right;
		this.up = up;
		this.down = down;
	}
}
