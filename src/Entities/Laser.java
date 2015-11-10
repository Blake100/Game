package Entities;

import static game.Game.numRows;
import java.awt.Color;
import java.awt.Graphics2D;

public class Laser {
    int xpos,ypos;
    boolean visible;
    int xspeed,yspeed;
    int dir;
    int currCol, currRow;

    public Laser(int xpos, int ypos, boolean visible, int dir,int col, int row) {
        this.xpos = xpos;
        this.ypos = ypos;
        this.visible = visible;
        if(dir==Mob.UP)
        this.yspeed = -45;
        if(dir==Mob.DOWN)
        this.yspeed = 45;
        if(dir==Mob.RIGHT)
        this.xspeed = 45;
        if(dir==Mob.LEFT)
        this.xspeed = -45;
        
        currCol = col;
        currRow = row;
        
    }

    public int getXpos() {
        return xpos;
    }

    public void setXpos(int xpos) {
        this.xpos = xpos;
    }

    public int getYpos() {
        return ypos;
    }

    public void setYpos(int ypos) {
        this.ypos = ypos;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getXSpeed() {
        return xspeed;
    }
    public int getYSpeed() {
        return yspeed;
    }

    public void setXSpeed(int speed) {
        this.xspeed = speed;
    }
    public void setYSpeed(int speed) {
        this.yspeed = speed;
    }
    public void tick(){
        if(visible){
        xpos+=xspeed;
        ypos+=yspeed;
        }
        if(yspeed>xspeed && yspeed !=0)
        currRow = ((ypos-75)/(game.Game.numRows)/2);
        if(yspeed<xspeed && yspeed !=0)
        currRow = ((ypos-150)/(game.Game.numRows)/2);
        if(yspeed>xspeed && xspeed !=0)
        currCol = ((xpos-150)/(game.Game.numColumns)/2);
        if(yspeed<xspeed && xspeed !=0)
        currCol = ((xpos-150)/(game.Game.numColumns)/2);
        
        //currCol = xpos/990;
        //currRow = (ypos)/(920);
//        
//        if(currCol<0)
//        currCol=0;
//        if(currRow<0)
//        currRow = 0;
//        if(currCol > game.Game.numColumns-1)
//        currCol=0;
//        if(currRow > game.Game.numRows -1)
//        currRow = 0;
        if (visible && (game.Game.board[currRow][currCol] == game.Game.SOLID))
            visible = false;
        System.out.println(currRow+"  "+currCol);
    }
    public void render(Graphics2D g)
    {
        if(yspeed !=0)
        {
            g.setColor(Color.red);
           g.fillRect(xpos, ypos, 2, 20);
        }
        if(xspeed != 0) //(dir==Mob.RIGHT || dir==Mob.LEFT)
        {
            g.setColor(Color.blue);
            g.fillRect(xpos, ypos, 20, 2);
        }
    }
    
}
