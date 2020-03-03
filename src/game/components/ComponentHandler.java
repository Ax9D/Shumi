package game.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class ComponentHandler {

	public static HashMap<String,ArrayList<Component>> database;

	//public static HashMap<>

	static {
		database = new HashMap<String,ArrayList<Component>>();
	}

	public static <T extends Component> ArrayList<Component> getAllByComponent(Class<T> c) {
			String className = c.getName();


		ArrayList<Component> ret = database.get(className);

		return ret;
	}
}
