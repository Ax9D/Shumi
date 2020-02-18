package game;

import base.Model;
import base.Texture2D;
import game.components.Component;
import game.components.ComponentHandler;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.HashMap;

public class ob2D {
	public Model m;//Remove due to redundance.

	public Vector2f pos;

	public Vector2f size;

	public String id;

	public String name;

	public Texture2D tex;

	private HashMap<String,Component> componentList;

	public ob2D(Model m, Vector2f pos, Vector2f size, String name) {
		this.m = m;
		this.pos = pos;
		this.size = size;
		this.name = name;
		this.id = genID();
		componentList=new HashMap<String,Component>();
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


		ArrayList<Component> cmpList = ComponentHandler.database.get(className);

		if (cmpList == null)
			ComponentHandler.database.put(className, cmpList = new ArrayList<Component>());

		cmpList.add(c);

		componentList.put(className,c);

		c.setParent(this);
	}

	public <T> Component getComponent(Class<T> c) {
		String className = c.getName();
		/*
		 * Object req = comps.get(className); return c.cast(req);*/
		return componentList.get(className);
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
