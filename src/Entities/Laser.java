package Entities;

import static game.Game.SOLID;
import static game.Game.board;
import static game.Game.numColumns;
import static game.Game.numRows;
import java.awt.Color;
import java.awt.Graphics2D;

public class Laser {
    int xpos,ypos;
    int orgxpos,orgypos;
    int lastxpos,lastypos;
    boolean visible;
    int xspeed,yspeed;
    int dir;
    int currCol, currRow;
    public static final int XBORDER = 20;
    public static final int YBORDER = 20;
    public static final int YTITLE = 30;
    public static final int WINDOW_BORDER = 8;
    public static final int WINDOW_WIDTH = 2*(WINDOW_BORDER + XBORDER) + (495 * 2);
    public static final int WINDOW_HEIGHT = YTITLE + WINDOW_BORDER + 2 * YBORDER + (460 * 2);
    int xsize = -1;
    int ysize = -1;
    public Laser(int xpos, int ypos, boolean visible, int dir,int col, int row) {
        
        this.orgxpos = xpos;
        this.orgypos = ypos;
        this.visible = visible;
        if(dir==Mob.UP)
        this.yspeed = -1;
        if(dir==Mob.DOWN)
        this.yspeed = 1;
        if(dir==Mob.RIGHT)
        this.xspeed = 1;
        if(dir==Mob.LEFT)
        this.xspeed = -1;
        
        currCol = col;
        currRow = row;
        
        this.xpos = orgxpos;
        this.ypos = orgypos;
        lastxpos = orgxpos + 26;
        lastypos = orgypos + 26;
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
        
    //        
    //        if(xpos == orgxpos + 26)
    //            currCol+=1;
    //        else if(xpos == orgxpos - 26)
    //            currCol-=1;
    //        else if(ypos == orgypos + 26)
    //            currRow+=1;
    //        else if(ypos == orgypos - 26)
    //            currRow-=1;
    //        
    //        if(xpos == lastxpos + 52)
    //        {
    //            currRow+=1;
    //            lastxpos+=52;
    //        }
    //        else if(xpos == lastxpos - 52)
    //        {
    //            currRow-=1;
    //            lastxpos-=52;
    //        }
    //        else if(ypos == lastypos + 52)
    //        {
    //            currCol+=1;
    //            lastypos+=52;
    //        }
    //        else if(ypos == lastypos - 52)
    //        {
    //            currCol-=1;
    //            lastypos-=52;
    //        }
        
//        if(xpos <= getX(0))
//            visible = false;
//        else if(xpos >= getWidth2())
//            visible = false;
//        else if(ypos <= getYNormal(0))
//            visible = false;
//        else if(ypos >= getHeight2())
//            visible = false;
//        
        
        //currRow = xpos/10;
        //currRow = xpos/10;
        if(visible){
        xpos+=xspeed*30;
        ypos+=yspeed*30;
        }
        
        if (visible && game.Game.board[currRow][currCol] == game.Game.SOLID)
        visible = false;
        
//        if(currCol<0)
//        {
//        currCol=0;
//        visible = false;
//        }
//        if(currRow<0)
//        {
//        currRow = 0;
//        visible = false;
//        }
//        if(currCol > game.Game.numColumns-1)
//        {
//        currCol=0;
//        visible = false;
//        }
//        if(currRow > game.Game.numRows -1)
//        {
//        currRow = 0;
//        visible = false;
//        }

//        System.out.println(xpos+"  "+ypos);
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
       public int getX(int x) {
        return (x + XBORDER + WINDOW_BORDER);
    }

    public int getY(int y) {
        return (y + YBORDER + YTITLE );
    }

    public int getYNormal(int y) {
        return (-y + YBORDER + YTITLE + getHeight2());
    }
    
    public int getWidth2() {
        return (xsize - 2 * (XBORDER + WINDOW_BORDER));
    }

    public int getHeight2() {
        return (ysize - 2 * YBORDER - WINDOW_BORDER - YTITLE);
    }
    
}
 