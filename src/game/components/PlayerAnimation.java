package game.components;

import base.Texture2D;

public class PlayerAnimation {
    enum AnimationStates {
        standing,walkingLeft,walkingRight,walkingUp,walkingDown
    }
    private AnimationStates currentAnimationState;

    private Texture2D[] walkLeftFrames;
    private Texture2D[] walkRightFrames;
    private Texture2D[] walkUpFrames;
    private Texture2D[] walkDownFrames;

    private int frameRate;

    public PlayerAnimation()
    {
        currentAnimationState= AnimationStates.standing;
    }

    public  void update()
    {

    }
}
