/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.irex_game;

/**
 *
 * @author luciagil
 */
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Board extends JPanel implements ActionListener, KeyListener {

    // controls the delay between each tick in ms
    private final int DELAY = 100;
   
    // controls the size of the board
    public static final int TILE_SIZE = 25;
    public static final int ROWS = 30;
    public static final int COLUMNS = 50;
    // controls how many rains appear on the board
    public static final int NUM_rainS = 5;
    // suppress serialization warning
    private static final long serialVersionUID = 490905409104883233L;
    
    // keep a reference to the timer object that triggers actionPerformed() in
    // case we need access to it in another method
    private Timer timer;
    // objects that appear on the game board
    private Player player;
    private Background background;
    private ArrayList<WaterCollector> waterC;
    
    private ArrayList<Rain> rains;
    private int numRainDrops;
    public Board() {
        // set the game board size
        setPreferredSize(new Dimension(TILE_SIZE * COLUMNS, TILE_SIZE * ROWS));
        // set the game board background color
        setBackground(new Color(232, 232, 232));
        

        // initialize the game state
        player = new Player();
        waterC= populatesWaterC();
        
        rains = populaterains();
        
        background = new Background();

        // this timer will call the actionPerformed() method every DELAY ms
        timer = new Timer(DELAY, this);
        
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // this method is called by the timer every DELAY ms.
        // use this space to update the state of your game or animation
        // before the graphics are redrawn.

        // prevent the player from disappearing off the board
        player.tick();

        // give the player points for collecting rains
        collectrains();
        addingScore();

        if( timer.getDelay() % 10 == 0) {
            
            populaterains();
        
        for (Rain rain : rains) {
            // update the rain's position
            rain.moveRain(-1);
            // check if the rain collides with the player
        }
    }
        // calling repaint() will trigger paintComponent() to run again,
        // which will refresh/redraw the graphics.
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // when calling g.drawImage() we can use "this" for the ImageObserver 
        // because Component implements the ImageObserver interface, and JPanel 
        // extends from Component. So "this" Board instance, as a Component, can 
        // react to imageUpdate() events triggered by g.drawImage()

        // draw our graphics.
       // drawBackground(g);
       background.draw(g, this);
       drawScore(g);
        for (Rain rain : rains) {
          rain.draw(g, this);
        }
        player.draw(g, this);
        for (Rain rain : rains) {
            // draw the rain at its new position
            rain.draw(g, this);
        }

        // this smooths out animations on some systems
        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // this is not used but must be defined as part of the KeyListener interface
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // react to key down events
        player.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // react to key up events
    }

     /*   private void drawBackground(Graphics g) {
        // draw a checkered background
        g.setColor(new Color(200, 214, 214));
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                // only color every other tile
                if ((row + col) % 2 == 1) {
                    // draw a square tile at the current row/column position
                    g.fillRect(
                        col * TILE_SIZE, 
                        row * TILE_SIZE, 
                        TILE_SIZE, 
                        TILE_SIZE
                    );
                }
            }    
        }
    }*/

    private void drawScore(Graphics g) {
        // set the text to be displayed
        String text = "$" + player.getScore() ;
        // we need to cast the Graphics to Graphics2D to draw nicer text
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(
            RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(
            RenderingHints.KEY_FRACTIONALMETRICS,
            RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        // set the text color and font
        g2d.setColor(new Color(30, 201, 139));
        g2d.setFont(new Font("Lato", Font.BOLD, 25));
        // draw the score in the bottom center of the screen
        // https://stackoverflow.com/a/27740330/4655368
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        // the text will be contained within this rectangle.
        // here I've sized it to be the entire bottom row of board tiles
        Rectangle rect = new Rectangle(0, TILE_SIZE * (ROWS - 1), TILE_SIZE * COLUMNS, TILE_SIZE);
        // determine the x coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        // determine the y coordinate for the text
        // (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        // draw the string
        g2d.drawString(text, x, y);
    }
    ArrayList<Rain> rainList = new ArrayList<>();
    private ArrayList<Rain> populaterains() {
        Random rand = new Random();
        for (int i = 0; i < rand.nextInt(3); i++) {
            int rainX = rand.nextInt(COLUMNS);
            int rainY = 0;
            rainList.add(new Rain(rainX, rainY));
        }
        return rainList;
    }

    ArrayList<WaterCollector> waterRec = new ArrayList<>();
   private ArrayList<WaterCollector> populatesWaterC(){
    for(int i =0; i<8; i++){
        int recX = 2+i;
        int recY = ROWS-11;
        waterRec.add(new WaterCollector(recX, recY));
    }
    return waterRec;
   }
    

    private void collectrains() {
        // allow player to pickup rains
        ArrayList<Rain> collectedrains = new ArrayList<>();
        for (Rain rain : rains) {
            // if the player is on the same tile as a rain, collect it
            if (player.getPos().equals(rain.getPos())|| (player.getPosX()+1==rain.getPosX() && player.getPosY()==rain.getPosY())
            ||(player.getPosX()-1==rain.getPosX() && player.getPosY()==rain.getPosY())||(player.getPosX()+2==rain.getPosX() && player.getPosY()==rain.getPosY())) {
                // give the player some points for picking this up
                //player.addScore(100);
                numRainDrops ++;
                collectedrains.add(rain);
                for( WaterCollector waterC : waterRec){
                    if(player.getPos().equals(waterC.getPos()) && numRainDrops != 0)
                        for( int i = 1; i < numRainDrops; i++){
                            player.addScore(10);
                            numRainDrops--;
                        }
                    
                    
                    
                }
                
            }
            

           
        }
        // remove collected rains from the board
        rains.removeAll(collectedrains);
        
        
    }

    private void addingScore(){  
    }
}
