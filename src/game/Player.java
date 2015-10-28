package game;

public class Player {
    private int numTurns;
    private int maxTurns = 16;
    public Mob[] mobs = new Mob[game.Game.numMobs];
    
    Player()
    {
     numTurns = (int) (Math.random() * maxTurns + 1);    
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
