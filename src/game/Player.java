package game;

import Entities.Mob;

public class Player {
    private int numTurns;
    private int maxTurns = 16;
    public Mob[] mobs = new Mob[game.Game.numMobs];
    
    Player()
    {
     numTurns = 0;    
    }
    public void tick(){
        for(int i = 0; i< mobs.length;i++)
            mobs[i].tick();
    }
    public int getNumTurns()
    {
        return numTurns;
    }
    public void setNumTurns(int numTurns)
    {
        this.numTurns = numTurns;
    }
}
