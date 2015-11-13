/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import Entities.Laser;
import Entities.Mob;
import GUI.CharacterOneSelect;
import GUI.CharacterTwoSelect;
import GUI.SettingsMenu;
import GUI.Menu;
import static game.Game.WINDOW_BORDER;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game extends JFrame implements Runnable {
    static final int XBORDER = 20;
    public static final int YBORDER = 20;
    public static final int YTITLE = 30;
    static final int WINDOW_BORDER = 8;
    static final int WINDOW_WIDTH = 2*(WINDOW_BORDER + XBORDER) + (495 * 2);
    static final int WINDOW_HEIGHT = YTITLE + WINDOW_BORDER + 2 * YBORDER + (460 * 2);
    boolean animateFirstTime = true;
    int xsize = -1;
    int ysize = -1;
    Image image;
    Image character, character2;
    Image Floor, darkness; 
    Image WallY, WallX, Wall;
    
    Image dice1side, dice2side,dice3side,dice4side,dice5side, dice6side;
    
    Laser laser;
    
    
        int changeDiceNumber = 1;
        boolean keepRollingDice = true;
        boolean rollDiceOver = false;
        boolean okToSwitchPlayer = false;
    
    Graphics2D g;
    
    // the gray immage is 0 and the blue image is 1
    public static  int charonetype = 0;
    public static  int chartwotype = 0;
    public static final int numRows = 20;
    public static final int numColumns = 20;

    public static int board[][];
    public static final int BOX2 = 4;
    public static final int BOX1 = 3;
    public static final int MOB = 2;
    public static final int SOLID = 1;
    public static final int EMPTY = 0;
    
    public static int numMobs = 3;
    Player playerOne, playerTwo;

    int currentRow;
    int currentColumn;

    int columnDir;
    int rowDir;
    
    boolean gameOver, playerOneTurn;
    
    
    enum WinState{
        playerOne, playerTwo
    }
    WinState Winstate;
    
    
    int timeCount;
    int timeSpeedVal;
    
    
    public static Game frame1;
    public static Menu gui;
    public static SettingsMenu settings;
    public static CharacterOneSelect charone;
    public static CharacterTwoSelect chartwo;
    
//    public int xdelta = getWidth2()/numColumns ,ydelta = getHeight2()/numRows;
    
    public static void main(String[] args) {
        frame1 = new Game();
        frame1.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setVisible(false);
        frame1.setResizable(false);
        
        gui = new Menu();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setVisible(true);
       
        settings = new SettingsMenu();
        settings.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        settings.setVisible(false);
        
        charone = new CharacterOneSelect();
        charone.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        charone.setVisible(false);
        charone.setResizable(false);
        
        chartwo = new CharacterTwoSelect();
        chartwo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chartwo.setVisible(false);
        chartwo.setResizable(false);
    }

    public Game() {

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.BUTTON1 == e.getButton()) {
                    //left button
                    int xpos = e.getX() - getX(0);
                    int ypos = e.getY() - getY(0);
                    if (xpos < 0 || ypos < 0 || xpos > getWidth2() || ypos > getHeight2())
                        return;
                    
                    int xdelta = getWidth2()/numColumns;
                    int ydelta = getHeight2()/numRows;
                    
                    currentColumn = xpos/xdelta;
                    currentRow = ypos/ydelta;
                    if(playerOneTurn)
                    for(int i = 0; i < numMobs;i++)
                    {
                        playerOne.mobs[i].setSelected(false);
                        if(playerOne.mobs[i].getCurrColumn() == currentColumn && playerOne.mobs[i].getCurrRow() == currentRow)
                        {
                            playerOne.mobs[i].setSelected(true);
                        }
                    }
                    else
                    for(int i = 0; i < numMobs;i++)
                    {
                        playerTwo.mobs[i].setSelected(false);
                        if(playerTwo.mobs[i].getCurrColumn() == currentColumn && playerTwo.mobs[i].getCurrRow() == currentRow)
                        {
                            playerTwo.mobs[i].setSelected(true);
                        }
                    } 
                    if(currentColumn == 19 && currentRow == 0)
                    {
                        keepRollingDice = false;
                    }
                }
                if (e.BUTTON3 == e.getButton()) {
                     //right button
                
                    reset();
                }
                repaint();
            }
        });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        repaint();
      }
    });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseMoved(MouseEvent e) {
        repaint();
      }
    });

        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if(laser.isVisible())
                    return;
                if (e.VK_D == e.getKeyCode())
                {
                    if(playerOneTurn)
                    for(int i = 0; i < numMobs;i++)
                    {
                      if(playerOne.mobs[i].isSelected())
                      {
                            playerOne.mobs[i].setDir(1);
                      }
                    }
                    else
                    for(int i = 0; i < numMobs;i++)
                    {
                      if(playerTwo.mobs[i].isSelected())
                      {
                            playerTwo.mobs[i].setDir(1);
                      }
                    }
                }
                if (e.VK_W == e.getKeyCode())
                {
                    if(playerOneTurn)
                    for(int i = 0; i < numMobs;i++)
                    {
                      if(playerOne.mobs[i].isSelected())
                      {
                            playerOne.mobs[i].setDir(0);
                      }
                    }
                    else
                    for(int i = 0; i < numMobs;i++)
                    {
                      if(playerTwo.mobs[i].isSelected())
                      {
                            playerTwo.mobs[i].setDir(0);
                      }
                    }
                }
                if (e.VK_S == e.getKeyCode())
                {
                    if(playerOneTurn)
                    for(int i = 0; i < numMobs;i++)
                    {
                      if(playerOne.mobs[i].isSelected())
                      {
                            playerOne.mobs[i].setDir(2);
                      }
                    }
                    else
                    for(int i = 0; i < numMobs;i++)
                    {
                      if(playerTwo.mobs[i].isSelected())
                      {
                            playerTwo.mobs[i].setDir(2);
                      }
                    }
                }
                if (e.VK_RIGHT == e.getKeyCode())
                {
                    if(playerOneTurn)
                    for(int i = 0; i < numMobs;i++)
                    {
                      if(playerOne.mobs[i].isSelected())
                      {
                          if(playerOne.getNumTurns() >0 && board[playerOne.mobs[i].getCurrRow()][playerOne.mobs[i].getCurrColumn()+1] != SOLID
                                   && board[playerOne.mobs[i].getCurrRow()][playerOne.mobs[i].getCurrColumn()+1] != MOB)
                          {
                            board[playerOne.mobs[i].getCurrRow()][playerOne.mobs[i].getCurrColumn()]=EMPTY;
                            board[playerOne.mobs[i].getCurrRow()][playerOne.mobs[i].getCurrColumn()+1] = MOB;
                            playerOne.mobs[i].setCurrColumn(playerOne.mobs[i].getCurrColumn()+1);
                            playerOne.setNumTurns(playerOne.getNumTurns()-1);
                          }
                      }
                    }
                    else
                    for(int i = 0; i < numMobs;i++)
                    {
                      if(playerTwo.mobs[i].isSelected())
                      {
                          if(playerTwo.getNumTurns() >0 && board[playerTwo.mobs[i].getCurrRow()][playerTwo.mobs[i].getCurrColumn()+1] != SOLID 
                                  && board[playerTwo.mobs[i].getCurrRow()][playerTwo.mobs[i].getCurrColumn()+1] != MOB)
                          {
                            board[playerTwo.mobs[i].getCurrRow()][playerTwo.mobs[i].getCurrColumn()]=EMPTY;
                            board[playerTwo.mobs[i].getCurrRow()][playerTwo.mobs[i].getCurrColumn()+1] = MOB;
                            playerTwo.mobs[i].setCurrColumn(playerTwo.mobs[i].getCurrColumn()+1);
                            playerTwo.setNumTurns(playerTwo.getNumTurns()-1);
                          }
                      }
                    }
                }
                  if (e.VK_A == e.getKeyCode())
                {
                    if(playerOneTurn)
                    for(int i = 0; i < numMobs;i++)
                    {
                      if(playerOne.mobs[i].isSelected())
                      {
                            playerOne.mobs[i].setDir(3);
                      }
                    }
                    else
                    for(int i = 0; i < numMobs;i++)
                    {
                      if(playerTwo.mobs[i].isSelected())
                      {
                            playerTwo.mobs[i].setDir(3);
                      }
                    }
                }
                if (e.VK_LEFT == e.getKeyCode())
                {
                    if(playerOneTurn)
                        for(int i = 0; i < numMobs;i++)
                        {
                          if(playerOne.mobs[i].isSelected())
                          {
                              if(playerOne.getNumTurns() >0 && board[playerOne.mobs[i].getCurrRow()][playerOne.mobs[i].getCurrColumn()-1] != SOLID &&
                                     board[playerOne.mobs[i].getCurrRow()][playerOne.mobs[i].getCurrColumn()-1] != MOB)
                                {
                                   board[playerOne.mobs[i].getCurrRow()][playerOne.mobs[i].getCurrColumn()]=EMPTY;
                                   board[playerOne.mobs[i].getCurrRow()][playerOne.mobs[i].getCurrColumn()-1]=MOB;
                                   playerOne.mobs[i].setCurrColumn(playerOne.mobs[i].getCurrColumn()-1);
                                   playerOne.setNumTurns(playerOne.getNumTurns()-1);
                                }
                          }
                        }
                    else
                        for(int i = 0; i < numMobs;i++)
                        {
                          if(playerTwo.mobs[i].isSelected())
                          {
                              if(playerTwo.getNumTurns() >0 && board[playerTwo.mobs[i].getCurrRow()][playerTwo.mobs[i].getCurrColumn()-1] != SOLID && 
                                      board[playerTwo.mobs[i].getCurrRow()][playerTwo.mobs[i].getCurrColumn()-1] != MOB)
                                {
                                   board[playerTwo.mobs[i].getCurrRow()][playerTwo.mobs[i].getCurrColumn()]=EMPTY;
                                   board[playerTwo.mobs[i].getCurrRow()][playerTwo.mobs[i].getCurrColumn()-1] = MOB;
                                   playerTwo.mobs[i].setCurrColumn(playerTwo.mobs[i].getCurrColumn()-1);
                                   playerTwo.setNumTurns(playerTwo.getNumTurns()-1);
                                }
                          }
                        }
                }
                if (e.VK_UP == e.getKeyCode())
                {
                    if(playerOneTurn) 
                    for(int i = 0; i < numMobs;i++)
                        {
                          if(playerOne.mobs[i].isSelected())
                          {
                              if(playerOne.getNumTurns() >0 && board[playerOne.mobs[i].getCurrRow()-1][playerOne.mobs[i].getCurrColumn()] != SOLID &&
                                      board[playerOne.mobs[i].getCurrRow()-1][playerOne.mobs[i].getCurrColumn()] != MOB)
                                {
                                    board[playerOne.mobs[i].getCurrRow()][playerOne.mobs[i].getCurrColumn()] = EMPTY;
                                    board[playerOne.mobs[i].getCurrRow()-1][playerOne.mobs[i].getCurrColumn()] = MOB;
                                    playerOne.mobs[i].setCurrRow(playerOne.mobs[i].getCurrRow()-1);
                                    playerOne.setNumTurns(playerOne.getNumTurns()-1);
                                }
                          }
                        }
                    else 
                        for(int i = 0; i < numMobs;i++)
                        {
                          if(playerTwo.mobs[i].isSelected())
                          {
                              if(playerTwo.getNumTurns() >0 && board[playerTwo.mobs[i].getCurrRow()-1][playerTwo.mobs[i].getCurrColumn()] != SOLID &&
                                      board[playerTwo.mobs[i].getCurrRow()-1][playerTwo.mobs[i].getCurrColumn()] != MOB)
                                {
                                    board[playerTwo.mobs[i].getCurrRow()][playerTwo.mobs[i].getCurrColumn()]   = EMPTY;
                                    board[playerTwo.mobs[i].getCurrRow()-1][playerTwo.mobs[i].getCurrColumn()] = MOB;
                                    playerTwo.mobs[i].setCurrRow(playerTwo.mobs[i].getCurrRow()-1);
                                    playerTwo.setNumTurns(playerTwo.getNumTurns()-1);
                                }
                          }
                        }
                }
                if (e.VK_DOWN == e.getKeyCode())
                {
                    if(playerOneTurn)
                     for(int i = 0; i < numMobs;i++)
                        {
                          if(playerOne.mobs[i].isSelected())
                          {
                              if(playerOne.getNumTurns() >0 && board[playerOne.mobs[i].getCurrRow()+1][playerOne.mobs[i].getCurrColumn()] != SOLID &&
                                      board[playerOne.mobs[i].getCurrRow()+1][playerOne.mobs[i].getCurrColumn()] != MOB)
                                {
                                    board[playerOne.mobs[i].getCurrRow()][playerOne.mobs[i].getCurrColumn()] = EMPTY;
                                    board[playerOne.mobs[i].getCurrRow()+1][playerOne.mobs[i].getCurrColumn()] = MOB;
                                    playerOne.mobs[i].setCurrRow(playerOne.mobs[i].getCurrRow()+1);
                                    playerOne.setNumTurns(playerOne.getNumTurns()-1);
                                }
                          }
                        }
                    else
                     for(int i = 0; i < numMobs;i++)
                        {
                          if(playerTwo.mobs[i].isSelected())
                          {
                              if(playerTwo.getNumTurns() >0 && board[playerTwo.mobs[i].getCurrRow()+1][playerTwo.mobs[i].getCurrColumn()] != SOLID &&
                                      board[playerTwo.mobs[i].getCurrRow()+1][playerTwo.mobs[i].getCurrColumn()] != MOB)
                                {
                                    board[playerTwo.mobs[i].getCurrRow()][playerTwo.mobs[i].getCurrColumn()]=EMPTY;
                                    board[playerTwo.mobs[i].getCurrRow()+1][playerTwo.mobs[i].getCurrColumn()] = MOB;
                                    playerTwo.mobs[i].setCurrRow(playerTwo.mobs[i].getCurrRow()+1);
                                    playerTwo.setNumTurns(playerTwo.getNumTurns()-1);
                                }
                          }
                        }
                }
                if (e.VK_ESCAPE == e.getKeyCode())
                {
                    frame1.setVisible(false);
                    gui.setVisible(true);
                    
                }
                if(e.VK_SPACE == e.getKeyCode())
                {
                    if(playerOneTurn)
                     for(int i = 0; i < numMobs;i++)
                        {
                          if(playerOne.mobs[i].isSelected())
                          {
                              if(playerOne.getNumTurns() >5)
                                {
                                    playerOne.mobs[i].shoot(playerTwo);
                                    playerOne.setNumTurns(playerOne.getNumTurns()-6);
                                    laser = null;
                                    laser = new Laser((getX(0) + playerOne.mobs[i].getCurrColumn()*getWidth2()/numColumns + (getWidth2()/numColumns)/2)
                                                       ,(getY(0)+ playerOne.mobs[i].getCurrRow()*getHeight2()/numRows) + (getHeight2()/numRows)/2,
                                                             true, playerOne.mobs[i].getDir(),playerOne.mobs[i].getCurrColumn() ,playerOne.mobs[i].getCurrRow());
                                }
                          }
                        }
                    else
                     for(int i = 0; i < numMobs;i++)
                        {
                          if(playerTwo.mobs[i].isSelected())
                          {
                              if(playerTwo.getNumTurns() >5)
                                {
                                    playerTwo.mobs[i].shoot(playerOne);
                                    playerTwo.setNumTurns(playerTwo.getNumTurns()-6);
                                    laser = null;
                                    laser = new Laser((getX(0) + playerTwo.mobs[i].getCurrColumn()*getWidth2()/numColumns+ (getWidth2()/numColumns)/2 )
                                                      ,(getY(0)+ playerTwo.mobs[i].getCurrRow()*getHeight2()/numRows) + (getHeight2()/numRows)/2,
                                                      true, playerTwo.mobs[i].getDir(), playerTwo.mobs[i].getCurrColumn() ,playerTwo.mobs[i].getCurrRow() );
                                }
                          }
                        }
                }

                repaint();
            }
        });
        init();
        start();
    }




    Thread relaxer;
////////////////////////////////////////////////////////////////////////////
    public void init() {
        requestFocus();
    }
////////////////////////////////////////////////////////////////////////////
    public void destroy() {
    }
////////////////////////////////////////////////////////////////////////////
    public void paint(Graphics gOld) {
        if (image == null || xsize != getSize().width || ysize != getSize().height) {
            xsize = getSize().width;
            ysize = getSize().height;
            image = createImage(xsize, ysize);
            g = (Graphics2D) image.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }

//fill background
        g.setColor(Color.cyan);

        g.fillRect(0, 0, xsize, ysize);

        int x[] = {getX(0), getX(getWidth2()), getX(getWidth2()), getX(0), getX(0)};
        int y[] = {getY(0), getY(0), getY(getHeight2()), getY(getHeight2()), getY(0)};
//fill border
        g.setColor(Color.white);
        g.fillPolygon(x, y, 4);
// draw border
        g.setColor(Color.red);
        g.drawPolyline(x, y, 5);

        if (animateFirstTime) {
            gOld.drawImage(image, 0, 0, null);
            return;
        }

        g.setColor(Color.black);
//horizontal lines
        for (int zi=1;zi<numRows;zi++)
        {
            g.drawLine(getX(0) ,getY(0)+zi*getHeight2()/numRows ,
            getX(getWidth2()) ,getY(0)+zi*getHeight2()/numRows );
        }
//vertical lines
        for (int zi=1;zi<numColumns;zi++)
        {
            g.drawLine(getX(0)+zi*getWidth2()/numColumns ,getY(0) ,
            getX(0)+zi*getWidth2()/numColumns,getY(getHeight2())  );
        }

                
                 g.setFont(new Font("Impact",Font.PLAIN,20));
                 g.drawString("Player 1 Turns Left : " + playerOne.getNumTurns(), 32, 50);  
                 g.drawString("Player 2 Turns Left : " + playerTwo.getNumTurns(), 332, 50);  
                 
                 if(playerOneTurn)
                 g.drawString("Player One Turn", 532, 50);  
                 else
                 g.drawString("Player Two Turn", 532, 50);  

                 
                 for(int i = 0; i<numRows;i++){
                    for(int u = 0; u< numColumns;u++){
                        drawTile(Floor,(getX(0)+u*getWidth2()/(numColumns))+ ((getWidth2()/numColumns)/2),(getY(0)+i*getHeight2()/(numRows)) + (getHeight2()/numRows)/2,0.0,1.6,1.5);
                        
                        if(board[i][u]!=SOLID && board[i][u]!=MOB && board[i][u]!= BOX1 
                        || board[i][u]!=SOLID && board[i][u]!=MOB && board[i][u]!= BOX2)
                        {
                           drawTile(darkness,(getX(0)+u*getWidth2()/(numColumns))+ ((getWidth2()/numColumns)/2),(getY(0)+i*getHeight2()/(numRows)) + (getHeight2()/numRows)/2,0.0,1.6,1.5); 
                        }
                    }
                }
                                      for(int i = 0; i<numRows;i++){
                    for(int u = 0; u< numColumns;u++){
                        if((board[i][u] == BOX1 && playerOneTurn) ||  (board[i][u] == BOX2 && !playerOneTurn))
                        drawTile(Floor,(getX(0)+u*getWidth2()/(numColumns))+ ((getWidth2()/numColumns)/2),(getY(0)+i*getHeight2()/(numRows)) + (getHeight2()/numRows)/2,0.0,1.6,1.5);
                    }
                }
                
                                      
                  
                 
                for(int i = 0; i < numMobs;i++)
                {
                    int playerOneMobDir = playerOne.mobs[i].getDir();
                    int playerTwoMobDir = playerTwo.mobs[i].getDir();
                        if((playerOne.mobs[i].isVisible() && playerOneTurn) ||  
                         (!playerOneTurn && board[playerOne.mobs[i].getCurrRow()][playerOne.mobs[i].getCurrColumn()] == BOX2 && playerOne.mobs[i].isVisible()))
                        {
                            if(Game.charonetype == 0)
                                drawCharacter(character,(getX(0) + playerOne.mobs[i].getCurrColumn()*getWidth2()/numColumns + (getWidth2()/numColumns)/2)
                                     , (getY(0)+ playerOne.mobs[i].getCurrRow()*getHeight2()/numRows) + (getHeight2()/numRows)/2,90*playerOneMobDir,1,1);
                            else if(Game.charonetype == 1)
                                drawCharacter(character2,(getX(0) + playerOne.mobs[i].getCurrColumn()*getWidth2()/numColumns + (getWidth2()/numColumns)/2)
                                     , (getY(0)+ playerOne.mobs[i].getCurrRow()*getHeight2()/numRows) + (getHeight2()/numRows)/2,90*playerOneMobDir,1,1);
                        }
                       if((playerTwo.mobs[i].isVisible() && !playerOneTurn) ||  
                         (playerOneTurn && board[playerTwo.mobs[i].getCurrRow()][playerTwo.mobs[i].getCurrColumn()] == BOX1 && playerTwo.mobs[i].isVisible()))
                        {
                            if(Game.chartwotype == 0)
                                drawCharacter(character,(getX(0) + playerTwo.mobs[i].getCurrColumn()*getWidth2()/numColumns + (getWidth2()/numColumns)/2)
                                     , (getY(0)+ playerTwo.mobs[i].getCurrRow()*getHeight2()/numRows) + (getHeight2()/numRows)/2,90*playerTwoMobDir,1,1);
                            else if(Game.chartwotype == 1)
                                drawCharacter(character2,(getX(0) + playerTwo.mobs[i].getCurrColumn()*getWidth2()/numColumns + (getWidth2()/numColumns)/2)
                                     , (getY(0)+ playerTwo.mobs[i].getCurrRow()*getHeight2()/numRows) + (getHeight2()/numRows)/2,90*playerTwoMobDir,1,1);
                        }
                }    
                 
                 for(int i = 0; i<numRows;i++){
                    for(int u = 0; u< numColumns;u++){
                        boolean hasHorizontal = false;
                        boolean hasVertical = false;
                        
                        if(i-1 >= 0 && board[i-1][u]==SOLID)
                            hasHorizontal = true;
                        if(i+1 <numRows && board[i+1][u]==SOLID)
                            hasHorizontal = true;
                        
                        if(u-1 >= 0 && board[i][u-1]==SOLID)
                            hasVertical = true;
                        if(u+1 <numRows && board[i][u+1]==SOLID)
                            hasVertical = true;
                        
                        if(board[i][u]==SOLID)
                        {
                            if(hasHorizontal&&hasVertical)
                            drawTile(Wall,(getX(0)+u*getWidth2()/(numColumns))+ ((getWidth2()/numColumns)/2),(getY(0)+i*getHeight2()/(numRows)) + (getHeight2()/numRows)/2,0.0,1.6,1.5);

                            else if(hasHorizontal)
                                drawTile(WallY,(getX(0)+u*getWidth2()/(numColumns))+ ((getWidth2()/numColumns)/2),(getY(0)+i*getHeight2()/(numRows)) + (getHeight2()/numRows)/2,0.0,1.6,1.5);
                            else if(hasVertical)
                                drawTile(WallX,(getX(0)+u*getWidth2()/(numColumns))+ ((getWidth2()/numColumns)/2),(getY(0)+i*getHeight2()/(numRows)) + (getHeight2()/numRows)/2,0.0,1.6,1.5);
                        }
                    }
                }

            
                 if(laser.isVisible())
                 {
                     laser.render(g);
                 }
                  
                 
        // drawing the dice and testing to see whether it should stay on the image or keep looping through the others  
                 if(changeDiceNumber == 1)
                 {
                    drawTile(dice1side,(getX(0) + 19*getWidth2()/numColumns + (getWidth2()/numColumns)/2)
                    ,(getY(0)+ 0*getHeight2()/numRows) + (getHeight2()/numRows)/2,0,.5,.5);
                    if(playerOneTurn && !keepRollingDice && !rollDiceOver)
                    {
                        playerOne.setNumTurns(3);
                        rollDiceOver = true;
                        okToSwitchPlayer = true;
                    }
                    else if(!playerOneTurn && !keepRollingDice && !rollDiceOver)
                    {
                         playerTwo.setNumTurns(3);
                         rollDiceOver = true;
                         okToSwitchPlayer = true;
                    }
                 }
                 else if(changeDiceNumber == 2)
                 {
                    drawTile(dice2side,(getX(0) + 19*getWidth2()/numColumns + (getWidth2()/numColumns)/2)
                  ,(getY(0)+ 0*getHeight2()/numRows) + (getHeight2()/numRows)/2,0,.5,.5);
                    if(playerOneTurn && !keepRollingDice && !rollDiceOver)
                    {
                        playerOne.setNumTurns(6);
                        rollDiceOver = true;
                        okToSwitchPlayer = true;
                    }
                    else if(!playerOneTurn && !keepRollingDice && !rollDiceOver)
                    {
                         playerTwo.setNumTurns(6);
                         rollDiceOver = true;
                         okToSwitchPlayer = true;
                    }
                 }
                 else if(changeDiceNumber == 3)
                 {
                    drawTile(dice3side,(getX(0) + 19*getWidth2()/numColumns + (getWidth2()/numColumns)/2)
                  ,(getY(0)+ 0*getHeight2()/numRows) + (getHeight2()/numRows)/2,0,.5,.5);
                    if(playerOneTurn && !keepRollingDice && !rollDiceOver)
                    {
                        playerOne.setNumTurns(9);
                        rollDiceOver = true;
                        okToSwitchPlayer = true;
                    }
                    else if(!playerOneTurn && !keepRollingDice && !rollDiceOver)
                    {
                         playerTwo.setNumTurns(9);
                         rollDiceOver = true;
                         okToSwitchPlayer = true;
                    }
                 }
                 else if(changeDiceNumber == 4)
                 {
                    drawTile(dice4side,(getX(0) + 19*getWidth2()/numColumns + (getWidth2()/numColumns)/2)
                  ,(getY(0)+ 0*getHeight2()/numRows) + (getHeight2()/numRows)/2,0,.5,.5);
                    if(playerOneTurn && !keepRollingDice && !rollDiceOver)
                    {
                        playerOne.setNumTurns(12);
                        rollDiceOver = true;
                        okToSwitchPlayer = true;
                    }
                    else if(!playerOneTurn && !keepRollingDice && !rollDiceOver)
                    {
                         playerTwo.setNumTurns(12);
                         rollDiceOver = true;
                         okToSwitchPlayer = true;
                    }
                 }
                 else if(changeDiceNumber == 5)
                 {
                    drawTile(dice5side,(getX(0) + 19*getWidth2()/numColumns + (getWidth2()/numColumns)/2)
                  ,(getY(0)+ 0*getHeight2()/numRows) + (getHeight2()/numRows)/2,0,.5,.5);
                    if(playerOneTurn && !keepRollingDice && !rollDiceOver)
                    {
                        playerOne.setNumTurns(15);
                        rollDiceOver = true;
                        okToSwitchPlayer = true;
                    }
                    else if(!playerOneTurn && !keepRollingDice && !rollDiceOver)
                    {
                         playerTwo.setNumTurns(15);
                         rollDiceOver = true;
                         okToSwitchPlayer = true;
                    }
                         
                 }
                 else if(changeDiceNumber == 6)
                 {
                    drawTile(dice6side,(getX(0) + 19*getWidth2()/numColumns + (getWidth2()/numColumns)/2)
                  ,(getY(0)+ 0*getHeight2()/numRows) + (getHeight2()/numRows)/2,0,.5,.5);
                    if(playerOneTurn && !keepRollingDice && !rollDiceOver)
                    {
                        playerOne.setNumTurns(18);
                        rollDiceOver = true;
                        okToSwitchPlayer = true;
                    }
                    else if(!playerOneTurn && !keepRollingDice && !rollDiceOver)
                    {
                         playerTwo.setNumTurns(18);
                         rollDiceOver = true;
                         okToSwitchPlayer = true;
                    }
                 }    
                 
                 
                 
                                        for(int i = 0; i<numRows;i++){
                    for(int u = 0; u< numColumns;u++){
                        if(board[i][u] == BOX1  && playerOne.getNumTurns() == 0  && rollDiceOver  || board[i][u] == BOX2 && playerTwo.getNumTurns() == 0 && rollDiceOver)
                            board[i][u] = EMPTY;
                    }
                }
        if (Winstate == WinState.playerOne)
        {
            g.setColor(Color.BLUE);
            g.setFont(new Font("Impact",Font.BOLD,60));
            g.drawString("P1 WINS", 140, 350);
        }
        if (Winstate == WinState.playerTwo)
        {
            g.setColor(Color.BLUE);
            g.setFont(new Font("Impact",Font.BOLD,60));
            g.drawString("P2 WINS", 140, 350);
        }
 
      gOld.drawImage(image, 0, 0, null);
    }
    public void drawCharacter(Image image,int xpos,int ypos,double rot,double xscale,
        double yscale) {
        int width = image.getWidth(this);
        int height = image.getHeight(this);
        g.translate(xpos,ypos);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );

        g.drawImage(image,-width/2,-height/2,
        width,height,this);

        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xpos,-ypos);
    }
////////////////////////////////////////////////////////////////////////////
// needed for     implement runnable
    public void run() {
        while (true) {
            animate();
            repaint();
            double seconds = .05;    //time that 1 frame takes.
            int miliseconds = (int) (1000.0 * seconds);
            try {
                Thread.sleep(miliseconds);
            } catch (InterruptedException e) {
            }
        }
    }
/////////////////////////////////////////////////////////////////////////
    public void reset() {
        board = new int[numColumns][numRows];
        playerOne = new Player();
        playerTwo = new Player();
        playerOneTurn = true;
        playerOne.setNumTurns(0);

        changeDiceNumber = 1;
        keepRollingDice = true;
        rollDiceOver = false;
        okToSwitchPlayer = false;
        
        laser = new Laser(55,55,false, 0, 0 ,0);
        
        for(int i =0; i<numMobs;i++){
        playerOne.mobs[i] = new Mob(Color.black,2);
        playerTwo.mobs[i] = new Mob(Color.blue,0);
        }
       
        for(int i =0; i<numMobs;i++){
        playerOne.mobs[i].setCurrColumn(6+(i*3));
        playerTwo.mobs[i].setCurrColumn(6+(i*3));
        playerOne.mobs[i].setCurrRow(1);
        playerTwo.mobs[i].setCurrRow(numRows-2);
        }
        for(int i = 0; i<numRows;i++){
            for(int u = 0; u< numColumns;u++){
                board[u][i] = EMPTY;
            }
        }
        
        for(int i = 0; i<numRows;i++){
        board[i][0]=SOLID;
        board[i][numRows-1]= SOLID;
        }
        for(int i = 0; i<numColumns;i++){
        board[0][i]=SOLID;
        board[numColumns-1][i]= SOLID;
        }
        
        ////LEFT SIDE
        for(int i = 0; i<numColumns-15;i++){
        board[6][i]=SOLID;
        board[numColumns-1][i]= SOLID;
        }
        for(int i = 0; i<numColumns-15;i++){
        board[13][i]=SOLID;
        board[numColumns-1][i]= SOLID;
        }
        for(int i = 0; i<numRows-12;i++){
        board[i+6][5]=SOLID;
        }
        for(int i = 0; i<numRows-13;i++){
        board[i][4]=SOLID;
        board[i+13][4]=SOLID;
        }
        
        
        ////RIGHT SIDE

        for(int i = 0; i<numRows-13;i++){
        board[i][15]=SOLID;
        board[i+13][15]=SOLID;
        }
        for(int i = 0; i<numRows-12;i++){
        board[i+6][14]=SOLID;
        }
        for(int i = 0; i<numColumns-15;i++){
        board[6][i+15]=SOLID;
        board[numRows-7][i+15]= SOLID;
        }
        
        ////MIDDLE HALL
        for(int i = 0; i<numColumns-12;i++){
        board[8][i+6]=SOLID;
        board[numRows-9][i+6]= SOLID;
        }
        
        //DOORS
        board[17][4] = EMPTY;
        board[17][16] = EMPTY;
        board[2][4] = EMPTY;
        board[2][16] = EMPTY;
        
        board[6][2] = EMPTY;
        board[6][17] = EMPTY;
        board[13][2] = EMPTY;
        board[13][17] = EMPTY;
        
        board[17][15] = EMPTY;
        board[2][15] = EMPTY;
        
        board[9][5] = EMPTY;
        board[10][5] = EMPTY;
        board[9][14] = EMPTY;
        board[10][14] = EMPTY;
        
        board[8][7] = EMPTY;
        board[8][8] = EMPTY;
        board[11][11] = EMPTY;
        board[11][12] = EMPTY;
        
        playerTwo.setNumTurns(0);
        
      
    }
/////////////////////////////////////////////////////////////////////////
    public void animate() {

        if (animateFirstTime) {
            animateFirstTime = false;
            if (xsize != getSize().width || ysize != getSize().height) {
                xsize = getSize().width;
                ysize = getSize().height;
            }
            character = Toolkit.getDefaultToolkit().getImage("./resources/charone.png");
            character2 = Toolkit.getDefaultToolkit().getImage("./resources/chartwo.png");
          
            Floor = Toolkit.getDefaultToolkit().getImage("./resources/Tiles/Floor1.png");
            WallX = Toolkit.getDefaultToolkit().getImage("./resources/Tiles/WallHorizontal.png");
            WallY = Toolkit.getDefaultToolkit().getImage("./resources/Tiles/WallVertical.png");
            Wall = Toolkit.getDefaultToolkit().getImage("./resources/Tiles/Wall.png");
            darkness = Toolkit.getDefaultToolkit().getImage("./resources/Tiles/darkness.GIF");
            
            dice1side = Toolkit.getDefaultToolkit().getImage("./resources/Dice/diceoneside.GIF");
            dice2side = Toolkit.getDefaultToolkit().getImage("./resources/Dice/dicetwoside.GIF");
            dice3side = Toolkit.getDefaultToolkit().getImage("./resources/Dice/dicethreeside.GIF");
            dice4side = Toolkit.getDefaultToolkit().getImage("./resources/Dice/dicefourside.GIF");
            dice5side = Toolkit.getDefaultToolkit().getImage("./resources/Dice/dicefiveside.GIF");
            dice6side = Toolkit.getDefaultToolkit().getImage("./resources/Dice/dicesixside.GIF");
            reset();
           
        }
        if(Winstate == null)
        {
            for(int i = 0; i<numRows;i++){
                        for(int u = 0; u< numColumns;u++){
                            if(board[i][u] == BOX1 || board[i][u] == BOX2)
                            {
                                board[i][u] = EMPTY;
                            }
                        }
                        }
            
                for(int i =0; i<numMobs;i++){
                    if(playerOneTurn)
                   board[playerOne.mobs[i].getCurrRow()][playerOne.mobs[i].getCurrColumn()]=MOB;
                    else 
                   board[playerOne.mobs[i].getCurrRow()][playerOne.mobs[i].getCurrColumn()]=EMPTY;
                   
                    if(!playerOneTurn)
                   board[playerTwo.mobs[i].getCurrRow()][playerTwo.mobs[i].getCurrColumn()]=MOB;
                    else
                   board[playerTwo.mobs[i].getCurrRow()][playerTwo.mobs[i].getCurrColumn()]=EMPTY;
                  
                    
                     if((playerTwo.mobs[i].isVisible() && !playerOneTurn) ||  
                         (playerOneTurn && board[playerTwo.mobs[i].getCurrRow()][playerTwo.mobs[i].getCurrColumn()] == BOX1 && playerTwo.mobs[i].isVisible()))
                         board[playerTwo.mobs[i].getCurrRow()][playerTwo.mobs[i].getCurrColumn()] = MOB;
                    
                    
                       //PLAYER ONE
                   if(playerOneTurn)
                   {
                   if(playerOne.mobs[i].getDir() == 2)
                   {
                       
                        if(board[playerOne.mobs[i].getCurrRow()+1][playerOne.mobs[i].getCurrColumn()-1]!= SOLID)
                        board[playerOne.mobs[i].getCurrRow()+1][playerOne.mobs[i].getCurrColumn()-1]=BOX1;
                        if(board[playerOne.mobs[i].getCurrRow()+1][playerOne.mobs[i].getCurrColumn()]!=SOLID)
                        board[playerOne.mobs[i].getCurrRow()+1][playerOne.mobs[i].getCurrColumn()]=BOX1;
                        if(board[playerOne.mobs[i].getCurrRow()+1][playerOne.mobs[i].getCurrColumn()+1]!=SOLID)
                        board[playerOne.mobs[i].getCurrRow()+1][playerOne.mobs[i].getCurrColumn()+1]=BOX1;
                        boolean doit = true;
                        for(int x = 1; x < 8; x++)
                        {
                            if(playerOne.mobs[i].getCurrRow()+x >numRows-1 || board[playerOne.mobs[i].getCurrRow()+x][playerOne.mobs[i].getCurrColumn()]==SOLID)
                                doit = false;
                            if(doit)
                            {
                            if(board[playerOne.mobs[i].getCurrRow()+x][playerOne.mobs[i].getCurrColumn()-1]!=SOLID)
                            board[playerOne.mobs[i].getCurrRow()+x][playerOne.mobs[i].getCurrColumn()-1]=BOX1;
//                            if(board[playerOne.mobs[i].getCurrRow()+2][playerOne.mobs[i].getCurrColumn()-2]!=SOLID)
//                            board[playerOne.mobs[i].getCurrRow()+2][playerOne.mobs[i].getCurrColumn()-2]=BOX;
//                            if(board[playerOne.mobs[i].getCurrRow()+3][playerOne.mobs[i].getCurrColumn()-2]!=SOLID)
//                            board[playerOne.mobs[i].getCurrRow()+3][playerOne.mobs[i].getCurrColumn()-2]=BOX;
                            if(board[playerOne.mobs[i].getCurrRow()+x][playerOne.mobs[i].getCurrColumn()]!=SOLID)
                            board[playerOne.mobs[i].getCurrRow()+x][playerOne.mobs[i].getCurrColumn()]=BOX1;
                            if(board[playerOne.mobs[i].getCurrRow()+x][playerOne.mobs[i].getCurrColumn()+1]!=SOLID)
                            board[playerOne.mobs[i].getCurrRow()+x][playerOne.mobs[i].getCurrColumn()+1]=BOX1;
                            }
//                            if(board[playerOne.mobs[i].getCurrRow()+2][playerOne.mobs[i].getCurrColumn()+2]!=SOLID)
//                            board[playerOne.mobs[i].getCurrRow()+2][playerOne.mobs[i].getCurrColumn()+2]=BOX;
//                            if(board[playerOne.mobs[i].getCurrRow()+3][playerOne.mobs[i].getCurrColumn()+2]!=SOLID)
//                            board[playerOne.mobs[i].getCurrRow()+3][playerOne.mobs[i].getCurrColumn()+2]=BOX;
                        }
                   }
                   if(playerOne.mobs[i].getDir() == 1)
                   {
                       
                        if(board[playerOne.mobs[i].getCurrRow()][playerOne.mobs[i].getCurrColumn()+1]!=SOLID)
                        board[playerOne.mobs[i].getCurrRow()][playerOne.mobs[i].getCurrColumn()+1]=BOX1;
                        if(board[playerOne.mobs[i].getCurrRow()+1][playerOne.mobs[i].getCurrColumn()+1]!=SOLID)
                        board[playerOne.mobs[i].getCurrRow()+1][playerOne.mobs[i].getCurrColumn()+1]=BOX1;
                        if(board[playerOne.mobs[i].getCurrRow()-1][playerOne.mobs[i].getCurrColumn()+1]!=SOLID)
                        board[playerOne.mobs[i].getCurrRow()-1][playerOne.mobs[i].getCurrColumn()+1]=BOX1;
                        boolean doit = true;
                         for(int x = 1; x < 8; x++)
                        {
                            if(playerOne.mobs[i].getCurrColumn()+x >numColumns-1 || board[playerOne.mobs[i].getCurrRow()][playerOne.mobs[i].getCurrColumn()+x]==SOLID)
                                doit = false;
                            if(doit)
                            {
                            if(board[playerOne.mobs[i].getCurrRow()-1][playerOne.mobs[i].getCurrColumn()+x]!=SOLID)
                            board[playerOne.mobs[i].getCurrRow()-1][playerOne.mobs[i].getCurrColumn()+x]=BOX1;
//                            if(board[playerOne.mobs[i].getCurrRow()-2][playerOne.mobs[i].getCurrColumn()+2]!=SOLID)
//                            board[playerOne.mobs[i].getCurrRow()-2][playerOne.mobs[i].getCurrColumn()+2]=BOX;
//                            if(board[playerOne.mobs[i].getCurrRow()-2][playerOne.mobs[i].getCurrColumn()+3]!=SOLID)
//                            board[playerOne.mobs[i].getCurrRow()-2][playerOne.mobs[i].getCurrColumn()+3]=BOX;
                            if(board[playerOne.mobs[i].getCurrRow()][playerOne.mobs[i].getCurrColumn()+x]!=SOLID)
                            board[playerOne.mobs[i].getCurrRow()][playerOne.mobs[i].getCurrColumn()+x]=BOX1;
                            if(board[playerOne.mobs[i].getCurrRow()+1][playerOne.mobs[i].getCurrColumn()+x]!=SOLID)
                            board[playerOne.mobs[i].getCurrRow()+1][playerOne.mobs[i].getCurrColumn()+x]=BOX1;
                            }
//                            if(board[playerOne.mobs[i].getCurrRow()+2][playerOne.mobs[i].getCurrColumn()+2]!=SOLID)
//                            board[playerOne.mobs[i].getCurrRow()+2][playerOne.mobs[i].getCurrColumn()+2]=BOX;
//                            if(board[playerOne.mobs[i].getCurrRow()+2][playerOne.mobs[i].getCurrColumn()+3]!=SOLID)
//                            board[playerOne.mobs[i].getCurrRow()+2][playerOne.mobs[i].getCurrColumn()+3]=BOX;
                        }
                   }
                   if(playerOne.mobs[i].getDir() == 0)
                   {
                    
                        if(board[playerOne.mobs[i].getCurrRow()-1][playerOne.mobs[i].getCurrColumn()-1]!= SOLID)
                        board[playerOne.mobs[i].getCurrRow()-1][playerOne.mobs[i].getCurrColumn()-1]=BOX1;
                        if(board[playerOne.mobs[i].getCurrRow()-1][playerOne.mobs[i].getCurrColumn()]!=SOLID)
                        board[playerOne.mobs[i].getCurrRow()-1][playerOne.mobs[i].getCurrColumn()]=BOX1;
                        if(board[playerOne.mobs[i].getCurrRow()-1][playerOne.mobs[i].getCurrColumn()+1]!=SOLID)
                        board[playerOne.mobs[i].getCurrRow()-1][playerOne.mobs[i].getCurrColumn()+1]=BOX1;
                        boolean doit = true;
                        for(int x = 1; x < 8; x++)
                        {
                            if(playerOne.mobs[i].getCurrRow()-x <0 || board[playerOne.mobs[i].getCurrRow()-x][playerOne.mobs[i].getCurrColumn()]==SOLID)
                                doit = false;
                            if(doit)
                            {
                            if(board[playerOne.mobs[i].getCurrRow()-x][playerOne.mobs[i].getCurrColumn()-1]!=SOLID)
                            board[playerOne.mobs[i].getCurrRow()-x][playerOne.mobs[i].getCurrColumn()-1]=BOX1;
//                            if(board[playerOne.mobs[i].getCurrRow()-2][playerOne.mobs[i].getCurrColumn()-2]!=SOLID)
//                            board[playerOne.mobs[i].getCurrRow()-2][playerOne.mobs[i].getCurrColumn()-2]=BOX;
//                            if(board[playerOne.mobs[i].getCurrRow()-3][playerOne.mobs[i].getCurrColumn()-2]!=SOLID)
//                            board[playerOne.mobs[i].getCurrRow()-3][playerOne.mobs[i].getCurrColumn()-2]=BOX;
                            if(board[playerOne.mobs[i].getCurrRow()-x][playerOne.mobs[i].getCurrColumn()]!=SOLID)
                            board[playerOne.mobs[i].getCurrRow()-x][playerOne.mobs[i].getCurrColumn()]=BOX1;
                            if(board[playerOne.mobs[i].getCurrRow()-x][playerOne.mobs[i].getCurrColumn()+1]!=SOLID)
                            board[playerOne.mobs[i].getCurrRow()-x][playerOne.mobs[i].getCurrColumn()+1]=BOX1;
                            }
//                            if(board[playerOne.mobs[i].getCurrRow()-2][playerOne.mobs[i].getCurrColumn()+2]!=SOLID)
//                            board[playerOne.mobs[i].getCurrRow()-2][playerOne.mobs[i].getCurrColumn()+2]=BOX;
//                            if(board[playerOne.mobs[i].getCurrRow()-3][playerOne.mobs[i].getCurrColumn()+2]!=SOLID)
//                            board[playerOne.mobs[i].getCurrRow()-3][playerOne.mobs[i].getCurrColumn()+2]=BOX;
                        }
                   }
                   if(playerOne.mobs[i].getDir() == 3)
                   {
                    if(board[playerOne.mobs[i].getCurrRow()][playerOne.mobs[i].getCurrColumn()-1]!=SOLID)
                    board[playerOne.mobs[i].getCurrRow()][playerOne.mobs[i].getCurrColumn()-1]=BOX1;
                    if(board[playerOne.mobs[i].getCurrRow()+1][playerOne.mobs[i].getCurrColumn()-1]!=SOLID)
                    board[playerOne.mobs[i].getCurrRow()+1][playerOne.mobs[i].getCurrColumn()-1]=BOX1;
                    if(board[playerOne.mobs[i].getCurrRow()-1][playerOne.mobs[i].getCurrColumn()-1]!=SOLID)
                    board[playerOne.mobs[i].getCurrRow()-1][playerOne.mobs[i].getCurrColumn()-1]=BOX1;
                    
                    boolean doit = true;
                     for(int x = 1; x < 8; x++)
                        {
                            if(playerOne.mobs[i].getCurrColumn()-x <0 || board[playerOne.mobs[i].getCurrRow()][playerOne.mobs[i].getCurrColumn()-x]==SOLID)
                                doit=false;;
                                if(doit)
                                {
                            if(board[playerOne.mobs[i].getCurrRow()-1][playerOne.mobs[i].getCurrColumn()-x]!=SOLID && playerOne.mobs[i].getCurrRow()-1 < numRows-1)
                            board[playerOne.mobs[i].getCurrRow()-1][playerOne.mobs[i].getCurrColumn()-x]=BOX1;
//                            if(board[playerOne.mobs[i].getCurrRow()-2][playerOne.mobs[i].getCurrColumn()-2]!=SOLID)
//                            board[playerOne.mobs[i].getCurrRow()-2][playerOne.mobs[i].getCurrColumn()-2]=BOX;
//                            if(board[playerOne.mobs[i].getCurrRow()-2][playerOne.mobs[i].getCurrColumn()-3]!=SOLID)
//                            board[playerOne.mobs[i].getCurrRow()-2][playerOne.mobs[i].getCurrColumn()-3]=BOX;
                            if(board[playerOne.mobs[i].getCurrRow()][playerOne.mobs[i].getCurrColumn()-x]!=SOLID && playerOne.mobs[i].getCurrRow() < numRows-1)
                            board[playerOne.mobs[i].getCurrRow()][playerOne.mobs[i].getCurrColumn()-x]=BOX1;
                            if(board[playerOne.mobs[i].getCurrRow()+1][playerOne.mobs[i].getCurrColumn()-x]!=SOLID && playerOne.mobs[i].getCurrRow()+1 < numRows-1)
                            board[playerOne.mobs[i].getCurrRow()+1][playerOne.mobs[i].getCurrColumn()-x]=BOX1;
                                }
//                            if(board[playerOne.mobs[i].getCurrRow()+2][playerOne.mobs[i].getCurrColumn()-2]!=SOLID)
//                            board[playerOne.mobs[i].getCurrRow()+2][playerOne.mobs[i].getCurrColumn()-2]=BOX;
//                            if(board[playerOne.mobs[i].getCurrRow()+2][playerOne.mobs[i].getCurrColumn()-3]!=SOLID)
//                            board[playerOne.mobs[i].getCurrRow()+2][playerOne.mobs[i].getCurrColumn()-3]=BOX;
                        }
                   }
                   }
                          
                   
                   
                   
                   
                      //PLAYER TWO
                   if(!playerOneTurn)
                   {
                   if(playerTwo.mobs[i].getDir() == 2)
                   {
                       
                        if(board[playerTwo.mobs[i].getCurrRow()+1][playerTwo.mobs[i].getCurrColumn()-1]!= SOLID)
                        board[playerTwo.mobs[i].getCurrRow()+1][playerTwo.mobs[i].getCurrColumn()-1]=BOX2;
                        if(board[playerTwo.mobs[i].getCurrRow()+1][playerTwo.mobs[i].getCurrColumn()]!=SOLID)
                        board[playerTwo.mobs[i].getCurrRow()+1][playerTwo.mobs[i].getCurrColumn()]=BOX2;
                        if(board[playerTwo.mobs[i].getCurrRow()+1][playerTwo.mobs[i].getCurrColumn()+1]!=SOLID)
                        board[playerTwo.mobs[i].getCurrRow()+1][playerTwo.mobs[i].getCurrColumn()+1]=BOX2;
                        boolean doit = true;
                        for(int x = 1; x < 8; x++)
                        {
                            if(playerTwo.mobs[i].getCurrRow()+x >numRows-1 || board[playerTwo.mobs[i].getCurrRow()+x][playerTwo.mobs[i].getCurrColumn()]==SOLID)
                                doit = false;
                            if(doit)
                            {
                            if(board[playerTwo.mobs[i].getCurrRow()+x][playerTwo.mobs[i].getCurrColumn()-1]!=SOLID)
                            board[playerTwo.mobs[i].getCurrRow()+x][playerTwo.mobs[i].getCurrColumn()-1]=BOX2;
//                            if(board[playerOne.mobs[i].getCurrRow()+2][playerOne.mobs[i].getCurrColumn()-2]!=SOLID)
//                            board[playerOne.mobs[i].getCurrRow()+2][playerOne.mobs[i].getCurrColumn()-2]=BOX;
//                            if(board[playerOne.mobs[i].getCurrRow()+3][playerOne.mobs[i].getCurrColumn()-2]!=SOLID)
//                            board[playerOne.mobs[i].getCurrRow()+3][playerOne.mobs[i].getCurrColumn()-2]=BOX;
                            if(board[playerTwo.mobs[i].getCurrRow()+x][playerTwo.mobs[i].getCurrColumn()]!=SOLID)
                            board[playerTwo.mobs[i].getCurrRow()+x][playerTwo.mobs[i].getCurrColumn()]=BOX2;
                            if(board[playerTwo.mobs[i].getCurrRow()+x][playerTwo.mobs[i].getCurrColumn()+1]!=SOLID)
                            board[playerTwo.mobs[i].getCurrRow()+x][playerTwo.mobs[i].getCurrColumn()+1]=BOX2;
                            }
//                            if(board[playerOne.mobs[i].getCurrRow()+2][playerOne.mobs[i].getCurrColumn()+2]!=SOLID)
//                            board[playerOne.mobs[i].getCurrRow()+2][playerOne.mobs[i].getCurrColumn()+2]=BOX;
//                            if(board[playerOne.mobs[i].getCurrRow()+3][playerOne.mobs[i].getCurrColumn()+2]!=SOLID)
//                            board[playerOne.mobs[i].getCurrRow()+3][playerOne.mobs[i].getCurrColumn()+2]=BOX;
                        }
                   }
                   if(playerTwo.mobs[i].getDir() == 1)
                   {
                       
                        if(board[playerTwo.mobs[i].getCurrRow()][playerTwo.mobs[i].getCurrColumn()+1]!=SOLID)
                        board[playerTwo.mobs[i].getCurrRow()][playerTwo.mobs[i].getCurrColumn()+1]=BOX2;
                        if(board[playerTwo.mobs[i].getCurrRow()+1][playerTwo.mobs[i].getCurrColumn()+1]!=SOLID)
                        board[playerTwo.mobs[i].getCurrRow()+1][playerTwo.mobs[i].getCurrColumn()+1]=BOX2;
                        if(board[playerTwo.mobs[i].getCurrRow()-1][playerTwo.mobs[i].getCurrColumn()+1]!=SOLID)
                        board[playerTwo.mobs[i].getCurrRow()-1][playerTwo.mobs[i].getCurrColumn()+1]=BOX2;
                        boolean doit = true;
                         for(int x = 1; x < 8; x++)
                        {
                            if(playerTwo.mobs[i].getCurrColumn()+x >numColumns-1 || board[playerOne.mobs[i].getCurrRow()][playerOne.mobs[i].getCurrColumn()+x]==SOLID)
                                doit = false;
                            if(doit)
                            {
                            if(board[playerTwo.mobs[i].getCurrRow()-1][playerTwo.mobs[i].getCurrColumn()+x]!=SOLID)
                            board[playerTwo.mobs[i].getCurrRow()-1][playerTwo.mobs[i].getCurrColumn()+x]=BOX2;
//                            if(board[playerOne.mobs[i].getCurrRow()-2][playerOne.mobs[i].getCurrColumn()+2]!=SOLID)
//                            board[playerOne.mobs[i].getCurrRow()-2][playerOne.mobs[i].getCurrColumn()+2]=BOX;
//                            if(board[playerOne.mobs[i].getCurrRow()-2][playerOne.mobs[i].getCurrColumn()+3]!=SOLID)
//                            board[playerOne.mobs[i].getCurrRow()-2][playerOne.mobs[i].getCurrColumn()+3]=BOX;
                            if(board[playerTwo.mobs[i].getCurrRow()][playerTwo.mobs[i].getCurrColumn()+x]!=SOLID)
                            board[playerTwo.mobs[i].getCurrRow()][playerTwo.mobs[i].getCurrColumn()+x]=BOX2;
                            if(board[playerTwo.mobs[i].getCurrRow()+1][playerTwo.mobs[i].getCurrColumn()+x]!=SOLID)
                            board[playerTwo.mobs[i].getCurrRow()+1][playerTwo.mobs[i].getCurrColumn()+x]=BOX2;
                            }
//                            if(board[playerOne.mobs[i].getCurrRow()+2][playerOne.mobs[i].getCurrColumn()+2]!=SOLID)
//                            board[playerOne.mobs[i].getCurrRow()+2][playerOne.mobs[i].getCurrColumn()+2]=BOX;
//                            if(board[playerOne.mobs[i].getCurrRow()+2][playerOne.mobs[i].getCurrColumn()+3]!=SOLID)
//                            board[playerOne.mobs[i].getCurrRow()+2][playerOne.mobs[i].getCurrColumn()+3]=BOX;
                        }
                   }
                   if(playerTwo.mobs[i].getDir() == 0)
                   {
                    
                        if(board[playerTwo.mobs[i].getCurrRow()-1][playerTwo.mobs[i].getCurrColumn()-1]!= SOLID)
                        board[playerTwo.mobs[i].getCurrRow()-1][playerTwo.mobs[i].getCurrColumn()-1]=BOX2;
                        if(board[playerTwo.mobs[i].getCurrRow()-1][playerTwo.mobs[i].getCurrColumn()]!=SOLID)
                        board[playerTwo.mobs[i].getCurrRow()-1][playerTwo.mobs[i].getCurrColumn()]=BOX2;
                        if(board[playerTwo.mobs[i].getCurrRow()-1][playerTwo.mobs[i].getCurrColumn()+1]!=SOLID)
                        board[playerTwo.mobs[i].getCurrRow()-1][playerTwo.mobs[i].getCurrColumn()+1]=BOX2;
                        boolean doit = true;
                        for(int x = 1; x < 8; x++)
                        {
                            if(playerTwo.mobs[i].getCurrRow()-x <0 || board[playerTwo.mobs[i].getCurrRow()-x][playerTwo.mobs[i].getCurrColumn()]==SOLID)
                                doit = false;
                            if(doit)
                            {
                            if(board[playerTwo.mobs[i].getCurrRow()-x][playerTwo.mobs[i].getCurrColumn()-1]!=SOLID)
                            board[playerTwo.mobs[i].getCurrRow()-x][playerTwo.mobs[i].getCurrColumn()-1]=BOX2;
//                            if(board[playerOne.mobs[i].getCurrRow()-2][playerOne.mobs[i].getCurrColumn()-2]!=SOLID)
//                            board[playerOne.mobs[i].getCurrRow()-2][playerOne.mobs[i].getCurrColumn()-2]=BOX;
//                            if(board[playerOne.mobs[i].getCurrRow()-3][playerOne.mobs[i].getCurrColumn()-2]!=SOLID)
//                            board[playerOne.mobs[i].getCurrRow()-3][playerOne.mobs[i].getCurrColumn()-2]=BOX;
                            if(board[playerTwo.mobs[i].getCurrRow()-x][playerTwo.mobs[i].getCurrColumn()]!=SOLID)
                            board[playerTwo.mobs[i].getCurrRow()-x][playerTwo.mobs[i].getCurrColumn()]=BOX2;
                            if(board[playerTwo.mobs[i].getCurrRow()-x][playerTwo.mobs[i].getCurrColumn()+1]!=SOLID)
                            board[playerTwo.mobs[i].getCurrRow()-x][playerTwo.mobs[i].getCurrColumn()+1]=BOX2;
                            }
//                            if(board[playerOne.mobs[i].getCurrRow()-2][playerOne.mobs[i].getCurrColumn()+2]!=SOLID)
//                            board[playerOne.mobs[i].getCurrRow()-2][playerOne.mobs[i].getCurrColumn()+2]=BOX;
//                            if(board[playerOne.mobs[i].getCurrRow()-3][playerOne.mobs[i].getCurrColumn()+2]!=SOLID)
//                            board[playerOne.mobs[i].getCurrRow()-3][playerOne.mobs[i].getCurrColumn()+2]=BOX;
                        }
                   }
                   if(playerTwo.mobs[i].getDir() == 3)
                   {
                    if(board[playerTwo.mobs[i].getCurrRow()][playerTwo.mobs[i].getCurrColumn()-1]!=SOLID)
                    board[playerTwo.mobs[i].getCurrRow()][playerTwo.mobs[i].getCurrColumn()-1]=BOX2;
                    if(board[playerTwo.mobs[i].getCurrRow()+1][playerTwo.mobs[i].getCurrColumn()-1]!=SOLID)
                    board[playerTwo.mobs[i].getCurrRow()+1][playerTwo.mobs[i].getCurrColumn()-1]=BOX2;
                    if(board[playerTwo.mobs[i].getCurrRow()-1][playerTwo.mobs[i].getCurrColumn()-1]!=SOLID)
                    board[playerTwo.mobs[i].getCurrRow()-1][playerTwo.mobs[i].getCurrColumn()-1]=BOX2;
                    
                    boolean doit = true;
                     for(int x = 1; x < 8; x++)
                        {
                            if(playerTwo.mobs[i].getCurrColumn()-x <0 || board[playerTwo.mobs[i].getCurrRow()][playerTwo.mobs[i].getCurrColumn()-x]==SOLID)
                                doit = false;
                            if(doit)
                            {
                            if(board[playerTwo.mobs[i].getCurrRow()-1][playerTwo.mobs[i].getCurrColumn()-x]!=SOLID && playerTwo.mobs[i].getCurrRow()-1 < numRows-1)
                            board[playerTwo.mobs[i].getCurrRow()-1][playerTwo.mobs[i].getCurrColumn()-x]=BOX2;
//                            if(board[playerOne.mobs[i].getCurrRow()-2][playerOne.mobs[i].getCurrColumn()-2]!=SOLID)
//                            board[playerOne.mobs[i].getCurrRow()-2][playerOne.mobs[i].getCurrColumn()-2]=BOX;
//                            if(board[playerOne.mobs[i].getCurrRow()-2][playerOne.mobs[i].getCurrColumn()-3]!=SOLID)
//                            board[playerOne.mobs[i].getCurrRow()-2][playerOne.mobs[i].getCurrColumn()-3]=BOX;
                            if(board[playerTwo.mobs[i].getCurrRow()][playerTwo.mobs[i].getCurrColumn()-x]!=SOLID && playerTwo.mobs[i].getCurrRow() < numRows-1)
                            board[playerTwo.mobs[i].getCurrRow()][playerTwo.mobs[i].getCurrColumn()-x]=BOX2;
                            if(board[playerTwo.mobs[i].getCurrRow()+1][playerTwo.mobs[i].getCurrColumn()-x]!=SOLID && playerTwo.mobs[i].getCurrRow()+1 < numRows-1)
                            board[playerTwo.mobs[i].getCurrRow()+1][playerTwo.mobs[i].getCurrColumn()-x]=BOX2;
                            }
//                            if(board[playerOne.mobs[i].getCurrRow()+2][playerOne.mobs[i].getCurrColumn()-2]!=SOLID)
//                            board[playerOne.mobs[i].getCurrRow()+2][playerOne.mobs[i].getCurrColumn()-2]=BOX;
//                            if(board[playerOne.mobs[i].getCurrRow()+2][playerOne.mobs[i].getCurrColumn()-3]!=SOLID)
//                            board[playerOne.mobs[i].getCurrRow()+2][playerOne.mobs[i].getCurrColumn()-3]=BOX;
                        }
                   }
                            
                    }
                }
                    
                   
                
            if(playerOneTurn)
            {
                if(!keepRollingDice && playerOne.getNumTurns() <= 0 && okToSwitchPlayer)
                {
                    keepRollingDice = true;
                    playerOneTurn = false;
//                        for(int i = 0; i<numRows;i++){
//                        for(int u = 0; u< numColumns;u++){
//                            if(board[i][u] == BOX)
//                            {
//                                board[i][u] = EMPTY;
//                            }
//                        }
//                        }
                }
                for(int i = 0; i < numMobs;i++)
                 {
                    playerTwo.mobs[i].setSelected(false);
                 } 
            }
            else if(!playerOneTurn)
            {
                if(!keepRollingDice && playerTwo.getNumTurns() <= 0 && okToSwitchPlayer)
                {
                    keepRollingDice = true;    
                    playerOneTurn = true;
//                     for(int i = 0; i<numRows;i++){
//                        for(int u = 0; u< numColumns;u++){
//                            if(board[i][u] == BOX)
//                            {
//                                board[i][u] = EMPTY;
//                            }
//                        }
//                        }
                }
                for(int i = 0; i < numMobs;i++)
                 {
                    playerOne.mobs[i].setSelected(false);
                 }
            }
            // every time it goes through it changes the face of the dice
            if(timeCount % 2 == 1)
            {
                if(keepRollingDice)
                {
                    okToSwitchPlayer = false;
                    rollDiceOver = false;
                    changeDiceNumber = (int) (Math.random()*6+1);
                }
            }
            {
            boolean gameover = true;
                for(int i = 0; i< numMobs;i++)
                {
                    if(playerOne.mobs[i].isVisible())
                        gameover = false;
                }
                if(gameover)
                    Winstate = WinState.playerTwo;
            }
            {
            boolean gameover = true;
                for(int i = 0; i< numMobs;i++)
                {
                    if(playerTwo.mobs[i].isVisible())
                        gameover = false;
                }
                if(gameover)
                        Winstate = WinState.playerOne;
            }
            
            if(laser.getXSpeed() > 0 && board[laser.getYpos()/ (getHeight2()/numRows) -1][laser.getXpos() / (getWidth2()/numColumns)]==SOLID )
                laser.setVisible(false);
            else if(laser.getXSpeed() < 0 && board[laser.getYpos()/ (getHeight2()/numRows) -1][laser.getXpos() / (getWidth2()/numColumns)-1]==SOLID )
                laser.setVisible(false);
            else if(laser.getYSpeed() > 0 && board[laser.getYpos()/ (getHeight2()/numRows) ][laser.getXpos() / (getWidth2()/numColumns)-1]==SOLID )
                laser.setVisible(false);
            else if(laser.getYSpeed() < 0 && board[laser.getYpos()/ (getHeight2()/numRows)-1][laser.getXpos() / (getWidth2()/numColumns)-1]==SOLID )
                laser.setVisible(false);
            
            laser.tick();
            playerOne.tick();
            playerTwo.tick();
            timeCount++;  
        }
    }
////////////////////////////////////////////////////////////////////////////
    public void drawTile(Image Tile, int xpos, int ypos, double rot, double xscale,double yscale)
    {
        int width = Tile.getWidth(this);
        int height = Tile.getWidth(this);
        
        g.translate(xpos,ypos);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );
        
        g.drawImage(Tile,-width/2,-height/2,width,height,this);
        
        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xpos,-ypos);
    }
////////////////////////////////////////////////////////////////////////////
    public void start() {
        if (relaxer == null) {
            relaxer = new Thread(this);
            relaxer.start();
        }
    }
////////////////////////////////////////////////////////////////////////////
    public void stop() {
        if (relaxer.isAlive()) {
            relaxer.stop();
        }
        relaxer = null;
    }
/////////////////////////////////////////////////////////////////////////
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