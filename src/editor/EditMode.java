package editor;

import base.Game;
import input_handling.MouseHandler;
import game.ob2D;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.HashSet;

import static org.lwjgl.glfw.GLFW.*;

public class EditMode {

    private boolean hasPicked;

    private ob2D pickedObject;
    private PickedType pickedType;

    Game game;
    private Vector2f mouseWorldPos;

    private Vector2f lastPickedObjectPos;

    boolean enabled;
    public EditMode(Game game, Vector2f mouseWorldPos)
    {
        this.game=game;
        this.mouseWorldPos=mouseWorldPos;
        hasPicked=false;
        enabled=false;

        MouseHandler.addButtonEventListener((button,action)->{
            if(button==GLFW_MOUSE_BUTTON_LEFT && action==GLFW_PRESS && enabled)
            {
                if(hasPicked)
                {
                    System.out.println("Objekt dropped");

                    //Update to new mouse drop coordinates
                    lastPickedObjectPos.x = mouseWorldPos.x;
                    lastPickedObjectPos.y = mouseWorldPos.y;

                    //Reassign vector object
                    pickedObject.pos = lastPickedObjectPos;
                    hasPicked=false;
                }
                else {
                    pickedObject = findPickedObject();
                    if (pickedObject != null)
                    {
                        hasPicked=true;
                        pickedType=PickedType.ob2D;

                        lastPickedObjectPos=pickedObject.pos;
                        pickedObject.pos=mouseWorldPos;
                        System.out.println("Objekt picked");

                    }
                }
            }
        });

    }

    public void reset()
    {
        //TODO:Eliminate code duplication
        if(hasPicked) {
            System.out.println("Objekt dropped");

            //Update to new mouse drop coordinates
            lastPickedObjectPos.x = mouseWorldPos.x;
            lastPickedObjectPos.y = mouseWorldPos.y;

            //Reassign vector object
            pickedObject.pos = lastPickedObjectPos;
            hasPicked = false;
        }
    }
    private ob2D findPickedObject()
    {
        ArrayList<ob2D> obList=game.r.visible;

        ob2D b2D;
        for(int i=obList.size()-1;i>=0;i--)
        {
            b2D=obList.get(i);
            if(!( (mouseWorldPos.x<b2D.pos.x-b2D.size.x || mouseWorldPos.x>b2D.pos.x+b2D.size.x) || (mouseWorldPos.y<b2D.pos.y-b2D.size.y || mouseWorldPos.y>b2D.pos.y+b2D.size.y) ))
                return b2D;
        }
        return null;
    }
    public void update()
    {
    }
}
