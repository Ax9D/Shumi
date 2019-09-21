package game;

import base.Model;
import base.Texture2D;
import game.components.Component;
import game.components.ComponentHandler;
import org.joml.Vector2f;

import java.util.HashMap;
import java.util.Map.Entry;

public class ob2D {
	public Model m;

	public Vector2f pos;

	public Vector2f size;

	public String id;

	public String name;

	public Texture2D tex;

	public ob2D(Model m, Vector2f pos, Vector2f size, String name) {
		this.m = m;
		this.pos = pos;
		this.size = size;
		this.name = name;
		this.id = genID();
	}

	private String genID() {
		return name;
	}

	public <T extends Component> void addComponent(T c) {
		/*
		 * comps.put(c.getClass().getName(), c); c.setParent(this);
		 */
		String className = c.getClass().getName();
		System.out.println("Adding " + className);
		HashMap<String,Component> cmpList = ComponentHandler.cmp.get(className);

		if (cmpList == null)
			ComponentHandler.cmp.put(className, cmpList = new HashMap<String,Component>());

		cmpList.put(id,c);

		c.setParent(this);
	}

	public <T extends Component> T getComponent(Class<T> c) {
		String className = c.getName();
		/*
		 * Object req = comps.get(className); return c.cast(req);
		 */
		HashMap<String,Component> ret=ComponentHandler.cmp.get(className);
		return c.cast(ret.get(id));
	}

	/*
	 * public Component[] getRawComponents() { Component[] ret = new
	 * Component[comps.size()]; int c = 0; for (Entry<String, Component> en :
	 * comps.entrySet()) { ret[c++] = en.getValue(); } return ret; }
	 */
	public void removeComponent(Class c) {
		String className = c.getName();
		HashMap<String, Component> cmpList = ComponentHandler.cmp.get(className);
		cmpList.remove(id);

	}
	public void delete()
	{
		/*for(Entry<Class,Boolean> ent:comps.entrySet())
		{
			Class cmpName=ent.getKey();

			ComponentHandler.cmp.get(cmpName).remove(id);
		}*/
	}

}
