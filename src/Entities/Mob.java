
package Entities;

import game.Game;
import game.Player;
import java.awt.Color;
import java.awt.Graphics2D;

public class Mob {
    private boolean Selected,visible;
    private int health, dir;
    private int currRow,currColumn;
    private Color color;
    private static final int RIGHT = 1; 
    private static final int LEFT = 3; 
    private static final int UP = 0; 
    private static final int DOWN = 2; 
    
    public Mob(Color color){
        health = 2;
        Selected = false;
        dir=1;
        visible = true;
        this.color = color;
    }

    public void tick(){
        if(health<=0){
            visible = false;
            currRow=0;
            currColumn=0;
        }
    }
    public boolean isSelected() {
        return Selected;
    }

    public void setSelected(boolean Selected) {
        this.Selected = Selected;
    }

    public int getCurrRow() {
        return currRow;
    }

    public void setCurrRow(int currRow) {
        this.currRow = currRow;
    }

    public int getCurrColumn() {
        return currColumn;
    }

    public void setCurrColumn(int currColumn) {
        this.currColumn = currColumn;
    }
    public void setColor(Color color)
    {
        this.color = color;
    }
    public Color getColor()
    {
        return(color);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDir() {
        return dir;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }
    public void draw(Graphics2D g,int xpos, int ypos,int width, int height)
    {
        if(visible){
        g.setColor(color);
        g.fillOval(xpos,ypos,width,height);
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    public void shoot(Player enemy){
        for(int i=0;i<Game.numMobs;i++)
        {
            if(dir==RIGHT)
            {
                if(enemy.mobs[i].getCurrRow()== currRow && currColumn < enemy.mobs[i].getCurrColumn()){
                    enemy.mobs[i].setHealth(enemy.mobs[i].getHealth()-1);
                    System.out.println("Zing");
                }
            }
            if(dir==LEFT)
            {
                if(enemy.mobs[i].getCurrRow()== currRow && currColumn > enemy.mobs[i].getCurrColumn()){
                    enemy.mobs[i].setHealth(enemy.mobs[i].getHealth()-1);
                    System.out.println("Zing");
                }
            }
            if(dir==UP)
            {
                if(enemy.mobs[i].getCurrColumn()== currColumn && currRow > enemy.mobs[i].getCurrRow()){
                    enemy.mobs[i].setHealth(enemy.mobs[i].getHealth()-1);
                    System.out.println("Zing");
                }
            }
            if(dir==DOWN)
            {
                if(enemy.mobs[i].getCurrColumn()== currColumn && currRow < enemy.mobs[i].getCurrRow()){
                    enemy.mobs[i].setHealth(enemy.mobs[i].getHealth()-1);
                    System.out.println("Zing");
                }
            }
        }
        System.out.println("Shoot");
    }
}
