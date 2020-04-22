package game.components;

import game.ob2D;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class Component {
    public ob2D parent;
    public boolean enabled;

    public void setParent(ob2D parent) {
        this.parent = parent;
        init();
        enable();
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public static Component loadComponent(JSONObject jo) throws JSONException {
        System.out.println("Loaded component");
        return null;
    }

    public abstract void init();

    public void update() {

    }
}

