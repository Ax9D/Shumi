package game.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ComponentHandler {

	public HashMap<String,ArrayList<Component>> database;

	//public static HashMap<>


	public ComponentHandler()
	{
		database = new HashMap<String,ArrayList<Component>>();
	}
	public  <T extends Component> ArrayList<Component> getAllByComponent(Class<T> c) {
		String className = c.getName();

		ArrayList<Component> ret = database.get(className);

		return ret;
	}
	public void updateComponents()
	{

		for(Map.Entry<String,ArrayList<Component>> et:database.entrySet())
		{
			for(Component c:et.getValue())
			{
				if(c.enabled)
					c.update();
			}
		}
	}
}
