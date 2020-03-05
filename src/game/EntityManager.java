package game;

import game.components.ComponentHandler;

public class EntityManager {
    public static int entityIx;
    static
    {
        entityIx=-1;
    }
    public static int addEntity()
    {
        return ++entityIx;
    }
}
