package UI;

import base.Color;
import base.Texture2D;

public class Button extends UIElement {
    static Texture2D unclickedDefault,clickedDefault;
    Texture2D unclicked,clicked;
    static
    {
        unclickedDefault=new Texture2D(Color.BLUE);
        clickedDefault=new Texture2D(Color.GREEN);
    }
    private Runnable onlicklambda;

    public Button(Texture2D unclicked, Texture2D clicked)
    {
        this.unclicked=unclicked;
        this.clicked=clicked;
    }
    public Button()
    {
        unclicked=unclickedDefault;
        clicked=clickedDefault;
    }
    public Button onClick(Runnable lambda)
    {
        onlicklambda=lambda;
        return this;
    }

    public void update()
    {

    }
    public void render() {

    }
}
