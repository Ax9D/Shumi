package game;

import base.GSystem;
import base.Shape;
import base.Texture2D;
import game.components.Component;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ob2D {
    public Shape sh;//Remove due to redundance.

    public Vector2f pos;

    public Vector2f size;

    public String name;

    public Texture2D tex;

    public HashMap<String, Component> compList;

    public ob2D(Shape sh, Vector2f pos, Vector2f size, String name) {
        this.sh = sh;
        this.pos = pos;
        this.size = size;
        this.name = name;
        this.tex = GSystem.rsmanager.basicTex;
        compList = new HashMap<String, Component>();
    }

    public <T extends Component> void addComponent(T c) {
        String className = c.getClass().getName();
        System.out.println("Adding " + className);

        ArrayList<Component> cmpList = GSystem.componentHandler.database.get(className);

        if (cmpList == null)
            GSystem.componentHandler.database.put(className, cmpList = new ArrayList<Component>());

        cmpList.add(c);
        compList.putIfAbsent(className, c);

        c.setParent(this);
    }

    public <T> T getComponent(Class<T> c) {
        String className = c.getName();
        return (T) compList.get(className);
    }

    public <T> void removeComponent(Class<T> c) {
        String className = c.getName();
        Component x = compList.remove(className);
        GSystem.componentHandler.database.get(className).remove(x);
    }

    public ArrayList<Component> getAllComponents() {
        ArrayList<Component> ret = new ArrayList<Component>();
        Component comp;
        for (Map.Entry<String, Component> i : compList.entrySet()) {
            comp = i.getValue();
            ret.add(comp);
        }
        return ret;
    }

    public void delete() {
        for (Map.Entry<String, Component> x : compList.entrySet()) {
            Component c = x.getValue();
            GSystem.componentHandler.database.get(x.getKey()).remove(c);
        }

    }

}
