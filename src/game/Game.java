/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import Entities.Mob;
import GUI.SettingsMenu;
import GUI.Menu;
import static game.Game.WINDOW_BORDER;
import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;

public class Game extends JFrame implements Runnable {
    static final int XBORDER = 20;
    static final int YBORDER = 20;
    static final int YTITLE = 30;
    static final int WINDOW_BORDER = 8;
    static final int WINDOW_WIDTH = 2*(WINDOW_BORDER + XBORDER) + (495 * 2);
    static final int WINDOW_HEIGHT = YTITLE + WINDOW_BORDER + 2 * YBORDER + (460 * 2);
    boolean animateFirstTime = true;
    int xsize = -1;
    int ysize = -1;
    Image image;
    Image dice1side,dice2side,dice3side,dice4side,dice5side,dice6side;
    Image character, character2;
    Image Floor; 
    Image WallY, WallX, Wall;
    
    boolean keepRollingDice;
    boolean rollDiceOver;
    int changeDiceNumber;
    
    Graphics2D g;
    sound bgSound = null;
    
    final int numRows = 20;
    final int numColumns = 20;

    public static int board[][];
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
    
    
    int timeCount;
    int timeSpeedVal;
    
    public static Game frame1;
    public static Menu gui;
    public static SettingsMenu settings;
    public static void main(String[] args) {
        frame1 = new Game();
        frame1.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setVisible(false);
        
        gui = new Menu();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setVisible(true);
       
        settings = new SettingsMenu();
        settings.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        settings.setVisible(false);
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
                    int ydelta = getHeight2()/numRows;
                    int xdelta = getWidth2()/numColumns;
                    
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

                
               
                 g.drawString("Player 1 Turns Left : " + playerOne.getNumTurns(), 32, 50);  
                 g.drawString("Player 2 Turns Left : " + playerTwo.getNumTurns(), 332, 50);  
                 
                 if(playerOneTurn)
                 g.drawString("Player One Turn", 532, 50);  
                 else
                 g.drawString("Player Two Turn", 532, 50);  

                 for(int i = 0; i<numRows;i++){
                    for(int u = 0; u< numColumns;u++){
                        drawTile(Floor,(getX(0)+u*getWidth2()/(numColumns))+ ((getWidth2()/numColumns)/2),(getY(0)+i*getHeight2()/(numRows)) + (getHeight2()/numRows)/2,0.0,1.6,1.5);
                    }
                }
                 
                 for(int i = 0; i < numMobs;i++)
                {
                    int playerOneMobDir = playerOne.mobs[i].getDir();
                    int playerTwoMobDir = playerTwo.mobs[i].getDir();
                    if(playerOne.mobs[i].isVisible())
                    drawCharacter(character,(getX(0) + playerOne.mobs[i].getCurrColumn()*getWidth2()/numColumns + (getWidth2()/numColumns)/2)
                                 , (getY(0)+ playerOne.mobs[i].getCurrRow()*getHeight2()/numRows) + (getHeight2()/numRows)/2,90*playerOneMobDir,1,1);
                    if(playerTwo.mobs[i].isVisible())
                    drawCharacter(character2,(getX(0) + playerTwo.mobs[i].getCurrColumn()*getWidth2()/numColumns + (getWidth2()/numColumns)/2)
                                 , (getY(0)+ playerTwo.mobs[i].getCurrRow()*getHeight2()/numRows) + (getHeight2()/numRows)/2,90*playerTwoMobDir,1,1);
                    
                     
                }
                 
                 
                  for(int i = 0; i<numRows;i++){
                    for(int u = 0; u< numColumns;u++){
                        drawTile(WallX,(getX(0)+u*getWidth2()/(numColumns))+ ((getWidth2()/numColumns)/2),
                                    (getY(0)+0*getHeight2()/(numRows)) + (getHeight2()/numRows)/2,0.0,1.6,1.5);
                        drawTile(WallX,(getX(0)+u*getWidth2()/(numColumns))+ ((getWidth2()/numColumns)/2),
                                    (getY(0)+19*getHeight2()/(numRows)) + (getHeight2()/numRows)/2,0.0,1.6,1.5);
                        drawTile(WallY,(getX(0)+0*getWidth2()/(numColumns))+ ((getWidth2()/numColumns)/2),
                                    (getY(0)+i*getHeight2()/(numRows)) + (getHeight2()/numRows)/2,0.0,1.6,1.5);
                        drawTile(WallY,(getX(0)+19*getWidth2()/(numColumns))+ ((getWidth2()/numColumns)/2),
                                    (getY(0)+i*getHeight2()/(numRows)) + (getHeight2()/numRows)/2,0.0,1.6,1.5);
                        
                       drawTile(Wall,(getX(0)+0*getWidth2()/(numColumns))+ ((getWidth2()/numColumns)/2),
                                    (getY(0)+0*getHeight2()/(numRows)) + (getHeight2()/numRows)/2,0.0,1.6,1.5); 
                       drawTile(Wall,(getX(0)+19*getWidth2()/(numColumns))+ ((getWidth2()/numColumns)/2),
                                    (getY(0)+19*getHeight2()/(numRows)) + (getHeight2()/numRows)/2,0.0,1.6,1.5); 
                       drawTile(Wall,(getX(0)+0*getWidth2()/(numColumns))+ ((getWidth2()/numColumns)/2),
                                    (getY(0)+19*getHeight2()/(numRows)) + (getHeight2()/numRows)/2,0.0,1.6,1.5); 
                       drawTile(Wall,(getX(0)+19*getWidth2()/(numColumns))+ ((getWidth2()/numColumns)/2),
                                    (getY(0)+0*getHeight2()/(numRows)) + (getHeight2()/numRows)/2,0.0,1.6,1.5); 
                    }
                }
               
                 
                 
                 
                 if(changeDiceNumber == 1)
                 {
                    drawDice(dice1side,(getX(0) + 19*getWidth2()/numColumns + (getWidth2()/numColumns)/2)
                    ,(getY(0)+ 0*getHeight2()/numRows) + (getHeight2()/numRows)/2,0,.5,.5);
                    if(playerOneTurn && !keepRollingDice && !rollDiceOver)
                    {
                        playerOne.setNumTurns(3);
                        rollDiceOver = true;
                    }
                    else if(!playerOneTurn && !keepRollingDice && !rollDiceOver)
                    {
                         playerTwo.setNumTurns(3);
                         rollDiceOver = true;
                    }
                 }
                 else if(changeDiceNumber == 2)
                 {
                    drawDice(dice2side,(getX(0) + 19*getWidth2()/numColumns + (getWidth2()/numColumns)/2)
                  ,(getY(0)+ 0*getHeight2()/numRows) + (getHeight2()/numRows)/2,0,.5,.5);
                    if(playerOneTurn && !keepRollingDice && !rollDiceOver)
                    {
                        playerOne.setNumTurns(6);
                        rollDiceOver = true;
                    }
                    else if(!playerOneTurn && !keepRollingDice && !rollDiceOver)
                    {
                         playerTwo.setNumTurns(6);
                         rollDiceOver = true;
                    }
                 }
                 else if(changeDiceNumber == 3)
                 {
                    drawDice(dice3side,(getX(0) + 19*getWidth2()/numColumns + (getWidth2()/numColumns)/2)
                  ,(getY(0)+ 0*getHeight2()/numRows) + (getHeight2()/numRows)/2,0,.5,.5);
                    if(playerOneTurn && !keepRollingDice && !rollDiceOver)
                    {
                        playerOne.setNumTurns(9);
                        rollDiceOver = true;
                    }
                    else if(!playerOneTurn && !keepRollingDice && !rollDiceOver)
                    {
                         playerTwo.setNumTurns(9);
                         rollDiceOver = true;
                    }
                 }
                 else if(changeDiceNumber == 4)
                 {
                    drawDice(dice4side,(getX(0) + 19*getWidth2()/numColumns + (getWidth2()/numColumns)/2)
                  ,(getY(0)+ 0*getHeight2()/numRows) + (getHeight2()/numRows)/2,0,.5,.5);
                    if(playerOneTurn && !keepRollingDice && !rollDiceOver)
                    {
                        playerOne.setNumTurns(12);
                        rollDiceOver = true;
                    }
                    else if(!playerOneTurn && !keepRollingDice && !rollDiceOver)
                    {
                         playerTwo.setNumTurns(12);
                         rollDiceOver = true;
                    }
                 }
                 else if(changeDiceNumber == 5)
                 {
                    drawDice(dice5side,(getX(0) + 19*getWidth2()/numColumns + (getWidth2()/numColumns)/2)
                  ,(getY(0)+ 0*getHeight2()/numRows) + (getHeight2()/numRows)/2,0,.5,.5);
                    if(playerOneTurn && !keepRollingDice && !rollDiceOver)
                    {
                        playerOne.setNumTurns(15);
                        rollDiceOver = true;
                    }
                    else if(!playerOneTurn && !keepRollingDice && !rollDiceOver)
                    {
                         playerTwo.setNumTurns(15);
                         rollDiceOver = true;
                    }
                         
                 }
                 else if(changeDiceNumber == 6)
                 {
                    drawDice(dice6side,(getX(0) + 19*getWidth2()/numColumns + (getWidth2()/numColumns)/2)
                  ,(getY(0)+ 0*getHeight2()/numRows) + (getHeight2()/numRows)/2,0,.5,.5);
                    changeDiceNumber = 1;
                    if(playerOneTurn && !keepRollingDice && !rollDiceOver)
                    {
                        playerOne.setNumTurns(18);
                        rollDiceOver = true;
                    }
                    else if(!playerOneTurn && !keepRollingDice && !rollDiceOver)
                    {
                         playerTwo.setNumTurns(18);
                         rollDiceOver = true;
                    }
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
    public void drawDice(Image image,int xpos,int ypos,double rot,double xscale,
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
        changeDiceNumber = 1;
        board = new int[numColumns][numRows];
        playerOne = new Player();
        playerTwo = new Player();
        playerOneTurn = true;
        keepRollingDice = true;
        rollDiceOver = false;
        timeCount = 0;
        playerOne.setNumTurns(0);
        playerTwo.setNumTurns(0);
       

        for(int i =0; i<numMobs;i++){
            playerOne.mobs[i] = new Mob(Color.black);
            playerTwo.mobs[i] = new Mob(Color.blue);
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

        
    }
/////////////////////////////////////////////////////////////////////////
    public void animate() {

        if (animateFirstTime) {
            animateFirstTime = false;
            if (xsize != getSize().width || ysize != getSize().height) {
                xsize = getSize().width;
                ysize = getSize().height;
            }
            character = Toolkit.getDefaultToolkit().getImage("./resources/char.png");
            character2 = Toolkit.getDefaultToolkit().getImage("./resources/char2.png");
            Floor = Toolkit.getDefaultToolkit().getImage("./resources/Tiles/Floor1.png");
            WallX = Toolkit.getDefaultToolkit().getImage("./resources/Tiles/WallHorizontal.png");
            WallY = Toolkit.getDefaultToolkit().getImage("./resources/Tiles/WallVertical.png");
            Wall = Toolkit.getDefaultToolkit().getImage("./resources/Tiles/Wall.png");
            
            dice1side = Toolkit.getDefaultToolkit().getImage("./resources/Dice/diceoneside.GIF");
            dice2side = Toolkit.getDefaultToolkit().getImage("./resources/Dice/dicetwoside.GIF");
            dice3side = Toolkit.getDefaultToolkit().getImage("./resources/Dice/dicethreeside.GIF");
            dice4side = Toolkit.getDefaultToolkit().getImage("./resources/Dice/dicefourside.GIF");
            dice5side = Toolkit.getDefaultToolkit().getImage("./resources/Dice/dicefiveside.GIF");
            dice6side = Toolkit.getDefaultToolkit().getImage("./resources/Dice/dicesixside.GIF");
            reset();
           bgSound = new sound("./starwars.wav"); 
        }
       if(bgSound.donePlaying)
       {
           bgSound = new sound("./starwars.wav"); 
       }
        if(playerOneTurn)
        {
            if(playerOne.getNumTurns() <= 0 && !keepRollingDice)
            {
            playerOneTurn = false;
            keepRollingDice = true;
            }
            for(int i = 0; i < numMobs;i++)
             {
                playerTwo.mobs[i].setSelected(false);
             } 
        }
        else if(!playerOneTurn)
        {
            if(playerTwo.getNumTurns() <= 0 && !keepRollingDice)
            {
            playerOneTurn = true;
            keepRollingDice = true;
            }
            for(int i = 0; i < numMobs;i++)
             {
                playerOne.mobs[i].setSelected(false);
             }
        }
        
        if(timeCount % 2 == 1)
        {
            if(keepRollingDice)
            {
            changeDiceNumber = (int) (Math.random()*6+1);
            rollDiceOver = false;
            }
        }
        
      playerOne.tick();
      playerTwo.tick();
      timeCount++;  
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
class sound implements Runnable {
    Thread myThread;
    File soundFile;
    public boolean donePlaying = false;
    sound(String _name)
    {
        soundFile = new File(_name);
        myThread = new Thread(this);
        myThread.start();
    }
    public void run()
    {
        try {
        AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);
        AudioFormat format = ais.getFormat();
    //    System.out.println("Format: " + format);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        SourceDataLine source = (SourceDataLine) AudioSystem.getLine(info);
        source.open(format);
        source.start();
        int read = 0;
        byte[] audioData = new byte[16384];
        while (read > -1){
            read = ais.read(audioData,0,audioData.length);
            if (read >= 0) {
                source.write(audioData,0,read);
            }
        }
        donePlaying = true;

        source.drain();
        source.close();
        }
        catch (Exception exc) {
            System.out.println("error: " + exc.getMessage());
            exc.printStackTrace();
        }
    }

}

