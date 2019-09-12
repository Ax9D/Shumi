package game.components;

import game.ob2D;

public abstract class Component {
	protected ob2D parent;

	public void setParent(ob2D parent) {
		this.parent = parent;
		init();
	}
	public abstract void init();
}
