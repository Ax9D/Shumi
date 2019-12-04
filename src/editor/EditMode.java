package editor;

import base.Game;
import base.KeyboardHandler;
import base.MouseHandler;
import game.ob2D;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class EditMode {

    private boolean hasPicked;

    private Object pickedObject;
    private PickedType pickedType;

    Game game;
    private Vector2f mouseWorldPos;
    private Vector2f oldObjectPos;

    public EditMode(Game game, Vector2f mouseWorldPos)
    {
        this.game=game;
        this.mouseWorldPos=mouseWorldPos;
        this.oldObjectPos=null;
        hasPicked=false;

        MouseHandler.addButtonEventListener((button,action)->{
            if(button==GLFW_MOUSE_BUTTON_LEFT && action==GLFW_PRESS)
            {
                if(hasPicked) {
                    hasPicked = false;
                    System.out.println("Objekt dropped");
                /*
                switch(pickedType)
                {
                    case ob2D:
                        oldObjectPos.x=mouseWorldPos.x;
                        oldObjectPos.y=mouseWorldPos.y;
                        ((ob2D)pickedObject).pos=oldObjectPos;
                }*/
                }
                else {
                    ob2D picked = findPickedObject();
                    if (picked != null)
                    {
                        hasPicked=true;
                        pickedType=PickedType.ob2D;
                        System.out.println("Objekt found");
                    /*
                    oldObjectPos=picked.pos;
                    picked.pos=mouseWorldPos;

                    pickedObject=picked;
*/
                    }
                }
            }
        });

    }

    private ob2D findPickedObject()
    {
        ArrayList<ob2D> obList=game.w.ob2Ds;

        for(ob2D b2D:obList)
        {
            if(!((mouseWorldPos.x<b2D.pos.x-b2D.size.x || mouseWorldPos.x>b2D.pos.x+b2D.size.x) && (mouseWorldPos.y<b2D.pos.y-b2D.size.y || mouseWorldPos.y>b2D.pos.y+b2D.size.y) ))
                return b2D;
        }
        return null;
    }
    public void update()
    {
    }
}
