package game.components;

import base.Texture2D;
import input_handling.KeyboardHandler;
public class PlayerAnimation extends Component {
    /*enum AnimationStates {
        standing,walkingLeft,walkingRight,walkingUp,walkingDown
    }*/
    public int currentAnimationState;
    private int previousAnimationState;

    public static final int standing=0;
    public static final int walkingLeft=1;
    public static final int walkingRight=2;
    public static final int walkingUp=3;
    public static final int walkingDown=4;



    private Texture2D[] walkLeftFrames;
    private Texture2D[] walkRightFrames;
    private Texture2D[] walkUpFrames;
    private Texture2D[] walkDownFrames;

    private Texture2D[][] frames;

    private float frameTime;

    private int frameIx;
    private long lastTime;

    public PlayerAnimation(float frameRate,Texture2D[] walkLeftFrames,Texture2D[] walkRightFrames,Texture2D[] walkUpFrames,Texture2D[] walkDownFrames,Texture2D standingFrame)
    {
        currentAnimationState = standing;
        previousAnimationState = standing;
        frameTime=Math.round((1/frameRate)*1000);
        this.walkLeftFrames=walkLeftFrames;
        this.walkRightFrames=walkRightFrames;
        this.walkUpFrames=walkUpFrames;
        this.walkDownFrames=walkDownFrames;

        frames=new Texture2D[5][];

        frames[0]=new Texture2D[]{standingFrame};
        frames[1]=walkLeftFrames;
        frames[2]=walkRightFrames;
        frames[3]=walkUpFrames;
        frames[4]=walkDownFrames;
    }
    public void init()
    {
    }

    public  void update()
    {
        long now=System.currentTimeMillis();
        if(currentAnimationState!=previousAnimationState)
        {
            frameIx=0;
            lastTime=now;
            this.parent.tex=frames[currentAnimationState][0];
        }
        else
        {
            if(now-lastTime>=frameTime) {
                lastTime=now;
                frameIx++;
                this.parent.tex=frames[currentAnimationState][frameIx%=frames[currentAnimationState].length];
            }

        }
        previousAnimationState=currentAnimationState;

    }
}
