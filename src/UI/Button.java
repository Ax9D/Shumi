package UI;

import base.*;
import input_handling.MouseHandler;
import org.joml.Matrix4f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Button extends UIElement {
    static Texture2D unclickedDefault,clickedDefault;
    static Runnable nothing;

    Texture2D unclicked,clicked;
    Texture2D tex;

    TextBox btnText;
    static
    {
        unclickedDefault=new Texture2D(Color.BLUE);
        clickedDefault=new Texture2D(Color.GREEN);
        nothing=()->{};
    }
    private Runnable onclicklambda;

    public Button(String text,Texture2D unclicked, Texture2D clicked)
    {
        super();
        this.unclicked=unclicked;
        this.clicked=clicked;

        onClick(nothing);

        MouseHandler.addButtonEventListener((button,action)->{
            if(button== GLFW_MOUSE_BUTTON_1)
            {
                if(action==GLFW_PRESS) {
                    tex = clicked;
                    onclicklambda.run();
                }
                else if(action==GLFW_RELEASE)
                    tex=unclicked;
            }
        });
        btnText=new TextBox(text,topLeft,size.x,size.y,WindowInfo.WIDTH*parent.width*size.x);
    }
    public Button(String text)
    {
        this(text,unclickedDefault,clickedDefault);
    }
    public void render()
    {
        Matrix4f tmat=new Matrix4f();
        compute();
        MatrixMath.get2DTMat(topLeft_a,size_a,tmat);


        BShader s=GSystem.uirenderer.generalShader;
        tex.bind();
        s.textureColorCheck(tex);

        s.setMatrix("tmat",tmat);

        glDrawElements(GL_TRIANGLES,GSystem.rsmanager.basicQuad.ic,GL_UNSIGNED_INT,0);
    }
    public Button onClick(Runnable lambda)
    {
        onclicklambda=lambda;
        return this;
    }
}
