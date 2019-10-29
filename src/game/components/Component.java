package game.components;

import game.ob2D;

public abstract class Component {
	public ob2D parent;

	public interface loaderLambda{
		void load();
	};

	public static loaderLambda loaderFn;

	public void setParent(ob2D parent) {
		this.parent = parent;
		init();

	}
	public abstract void init();
}
