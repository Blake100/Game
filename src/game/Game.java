/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import static game.Game.WINDOW_BORDER;
import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
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
    Image playerOneMobLeft;
    Image playerOneMobRight;
    Image tree;
    Image grassblock;
    
    Graphics2D g;
    
    final int numRows = 20;
    final int numColumns = 20;

    int board[][];
    
    static int numMobs = 3;
    Player playerOne, playerTwo;

    int currentRow;
    int currentColumn;

    int columnDir;
    int rowDir;
    
    boolean gameOver, playerOneTurn;
    
    int timeCount;
    int timeSpeedVal;
    
    static Game frame1;
    static GUI gui;
    static SettingsMenu settings;
    public static void main(String[] args) {
        frame1 = new Game();
        frame1.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setVisible(false);
        
        gui = new GUI();
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
                if (e.VK_RIGHT == e.getKeyCode())
                {
                    if(playerOneTurn)
                    for(int i = 0; i < numMobs;i++)
                    {
                      if(playerOne.mobs[i].isSelected())
                      {
                          if(playerOne.getNumTurns() >0)
                          {
                            playerOne.mobs[i].setCurrColumn(playerOne.mobs[i].getCurrColumn()+1);
                            playerOne.setNumTurns(playerOne.getNumTurns()-1);
                            playerOne.mobs[i].setDir(0);
                          }
                      }
                    }
                    else
                    for(int i = 0; i < numMobs;i++)
                    {
                      if(playerTwo.mobs[i].isSelected())
                      {
                          if(playerTwo.getNumTurns() >0)
                          {
                            playerTwo.mobs[i].setCurrColumn(playerTwo.mobs[i].getCurrColumn()+1);
                            playerTwo.setNumTurns(playerTwo.getNumTurns()-1);
                            playerTwo.mobs[i].setDir(0);
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
                if (e.VK_LEFT == e.getKeyCode())
                {
                    if(playerOneTurn)
                        for(int i = 0; i < numMobs;i++)
                        {
                          if(playerOne.mobs[i].isSelected())
                          {
                              if(playerOne.getNumTurns() >0)
                                {
                                   playerOne.mobs[i].setCurrColumn(playerOne.mobs[i].getCurrColumn()-1);
                                   playerOne.setNumTurns(playerOne.getNumTurns()-1);
                                   playerOne.mobs[i].setDir(1);
                                }
                          }
                        }
                    else
                        for(int i = 0; i < numMobs;i++)
                        {
                          if(playerTwo.mobs[i].isSelected())
                          {
                              if(playerTwo.getNumTurns() >0)
                                {
                                   playerTwo.mobs[i].setCurrColumn(playerTwo.mobs[i].getCurrColumn()-1);
                                   playerTwo.setNumTurns(playerTwo.getNumTurns()-1);
                                   playerTwo.mobs[i].setDir(1);
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
                              if(playerOne.getNumTurns() >0)
                                {
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
                              if(playerTwo.getNumTurns() >0)
                                {
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
                              if(playerOne.getNumTurns() >0)
                                {
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
                              if(playerTwo.getNumTurns() >0)
                                {
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

                for(int i = 0; i < numMobs;i++)
                {
                    if(playerOne.mobs[i].getDir() == 1)
                    drawCharacter(playerOneMobLeft,(getX(0)+playerOne.mobs[i].getCurrColumn()*getWidth2()/numColumns) + ((getWidth2()/numColumns)/2),
                    (getY(0)+playerOne.mobs[i].getCurrRow()*getHeight2()/numRows) + ((getHeight2()/numRows)/2)  ,0.0,
                    .5,
                    .5);
                    else if(playerOne.mobs[i].getDir() == 0)
                    drawCharacter(playerOneMobRight,(getX(0)+playerOne.mobs[i].getCurrColumn()*getWidth2()/numColumns) + ((getWidth2()/numColumns)/2),
                    (getY(0)+playerOne.mobs[i].getCurrRow()*getHeight2()/numRows) + ((getHeight2()/numRows)/2)  ,0.0,
                    .5,
                    .5);
                    
                     if(playerTwo.mobs[i].getDir() == 1)
                    drawCharacter(playerOneMobLeft,(getX(0)+playerTwo.mobs[i].getCurrColumn()*getWidth2()/numColumns) + ((getWidth2()/numColumns)/2),
                    (getY(0)+playerTwo.mobs[i].getCurrRow()*getHeight2()/numRows) + ((getHeight2()/numRows)/2)  ,0.0,
                    .5,
                    .5);
                     else if(playerTwo.mobs[i].getDir() == 0)
                    drawCharacter(playerOneMobRight,(getX(0)+playerTwo.mobs[i].getCurrColumn()*getWidth2()/numColumns) + ((getWidth2()/numColumns)/2),
                    (getY(0)+playerTwo.mobs[i].getCurrRow()*getHeight2()/numRows) + ((getHeight2()/numRows)/2)  ,0.0,
                    .5,
                    .5);
                }
                //draws trees
                    for(int i = 0;i < numRows;i++)
                    drawTree(tree,(getX(0)+i*getWidth2()/numColumns) + ((getWidth2()/numColumns)/2),
                    (getY(0)+0*getHeight2()/numRows) + ((getHeight2()/numRows)/2)  ,0.0,
                    .25,
                    .25);
                    for(int i = 0;i < numRows;i++)
                    drawTree(tree,(getX(0)+i*getWidth2()/numColumns) + ((getWidth2()/numColumns)/2),
                    (getY(0)+(numRows-1)*getHeight2()/numRows) + ((getHeight2()/numRows)/2)  ,0.0,
                    .25,
                    .25);
                      for(int i = 0;i < numRows;i++)
                    drawTree(tree,(getX(0)+(numColumns-1)*getWidth2()/numColumns) + ((getWidth2()/numColumns)/2),
                    (getY(0)+i*getHeight2()/numRows) + ((getHeight2()/numRows)/2)  ,0.0,
                    .25,
                    .25);
                    for(int i = 0;i < numRows;i++)
                    drawTree(tree,(getX(0)+0*getWidth2()/numColumns) + ((getWidth2()/numColumns)/2),
                    (getY(0)+i*getHeight2()/numRows) + ((getHeight2()/numRows)/2)  ,0.0,
                    .25,
                    .25);
               
                 g.drawString("Player 1 Turns Left : " + playerOne.getNumTurns(), 32, 50);  
                 g.drawString("Player 2 Turns Left : " + playerTwo.getNumTurns(), 332, 50);  
                 
                 if(playerOneTurn)
                 g.drawString("Player One Turn", 532, 50);  
                 else
                 g.drawString("Player Two Turn", 532, 50);  

                 
                 
 
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
    public void drawTree(Image image,int xpos,int ypos,double rot,double xscale,
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
        playerOne = new Player();
        playerTwo = new Player();
        playerOneTurn = true;

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
            reset();
            playerOneMobLeft = Toolkit.getDefaultToolkit().getImage("./starwarscharacterleft.GIF");
            playerOneMobRight = Toolkit.getDefaultToolkit().getImage("./starwarscharacterright.GIF");
            tree = Toolkit.getDefaultToolkit().getImage("./tree.GIF");
            grassblock = Toolkit.getDefaultToolkit().getImage("./grassblock.GIF");
        }
        
        if(playerOne.getNumTurns() <= 0 && playerOneTurn)
        {
            playerOneTurn = false;
            playerTwo.setNumTurns((int)(Math.random() * 16 +1));
            for(int i = 0; i < numMobs;i++)
             {
                playerTwo.mobs[i].setSelected(false);
             } 
        }
        else if(playerTwo.getNumTurns() <= 0 && !playerOneTurn)
        {
            playerOneTurn = true;
            playerOne.setNumTurns((int)(Math.random() * 16 +1));
            for(int i = 0; i < numMobs;i++)
             {
                playerOne.mobs[i].setSelected(false);
             }
        }
        
      timeCount++;  
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

