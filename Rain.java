/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.irex_game;

/**
 *
 * @author luciagil
 */
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Rain {
    
    // image that represents the rain's position on the board
    private BufferedImage image;
    // current position of the rain on the board grid
    private Point pos;

    public Rain(int x, int y) {
        // load the assets
        loadImage();

        // initialize the state
        pos = new Point(x, y);
       // moveRain ();
    }

    private void loadImage() {
        try {
          
            image = ImageIO.read(new File("/Users/luciagil/Desktop/NetBeansProjects/IREX_Game/src/main/java/com/mycompany/irex_game/water.png"));
        } catch (IOException exc) {
            System.out.println("Error opening image file: " + exc.getMessage());
        }
    }

    public void draw(Graphics g, ImageObserver observer) {
        // with the Point class, note that pos.getX() returns a double, but 
        // pos.x reliably returns an int. https://stackoverflow.com/a/30220114/4655368
        // this is also where we translate board grid position into a canvas pixel
        // position by multiplying by the tile size.
        g.drawImage(
            image, 
            pos.x * Board.TILE_SIZE, 
            pos.y * Board.TILE_SIZE, 
            observer
        );
    }

 

    public void moveRain ( int distance){
        
        pos.y -= (distance);
        
    }

    public Point getPos() {
        return pos;
    }

    public int getPosX(){
        return pos.x;
    }
    public int getPosY(){
        return pos.y;
    }

    
}
