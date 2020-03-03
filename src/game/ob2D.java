package game;

import base.ResourceManager;
import base.Shape;
import base.Texture2D;
import game.components.Component;
import game.components.ComponentHandler;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ob2D {
	public Shape sh;//Remove due to redundance.

	public Vector2f pos;

	public Vector2f size;

	public String id;

	public String name;

	public Texture2D tex;

	private HashMap<String,Component> componentList;

	public ob2D(Shape sh, Vector2f pos, Vector2f size, String name) {
		this.sh = sh;
		this.pos = pos;
		this.size = size;
		this.name = name;
		this.id = genID();
		componentList=new HashMap<String,Component>();
		this.tex=ResourceManager.basicTex;
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

	public <T> T getComponent(Class<T> c) {
		String className = c.getName();
		return (T)componentList.get(className);
	}
	public <T> void removeComponent(Class<T> c)
	{
		String className=c.getName();
		Component x=componentList.remove(className);

		//TODO: Optimize
		ComponentHandler.database.get(className).remove(x);
	}
	public ArrayList<Component> getAllComponents()
	{
		ArrayList<Component> ret=new ArrayList<Component>();
		for(Map.Entry<String,Component> i:componentList.entrySet())
			ret.add(i.getValue());
		return ret;
	}
	public void delete()
	{
		new Thread(()->{
			for(Map.Entry<String,Component> c:componentList.entrySet())
			{
				String componentName=c.getKey();
				Component component=c.getValue();
				ComponentHandler.database.get(componentName).remove(component);
			}
		}).start();
	}

}
