package game;

import base.GSystem;

public class SaveSystem {
    private String savePath;
    public SaveSystem(String savePath)
    {
        this.savePath=savePath;
    }
    public void saveWorld()
    {
        World w=GSystem.world;

    }
    public void loadWorld()
    {

    }
}
