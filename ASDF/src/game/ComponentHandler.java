package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class ComponentHandler {
	/*
	 * public static HashMap<String, ComponentContainer> entityComponentPairs;
	 * 
	 * public static <T> void add(String id, T comp) {
	 * switch(comp.getClass().toString()) { case entityComponentPairs.get(id).; } }
	 */
	// public static HashMap<>
	public static HashMap<String, HashMap<String, Component>> cmp;

	static {
		cmp = new HashMap<String, HashMap<String, Component>>();
	}

	public static <T> ArrayList<T> getAll(Class<T> c) {
		String className = c.getName();

		HashMap<String, Component> req = cmp.get(className);

		ArrayList<T> ret = new ArrayList<T>();

		for (Entry<String, Component> e : req.entrySet())
			ret.add(c.cast(e.getValue()));
		return ret;
	}
}
