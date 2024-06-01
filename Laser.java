package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class Laser {
    private int x;
    private int y;
    private double speed;
    private BufferedImage image;
    private boolean hitMonster;
    private int width;
    private int height;
    int monsterHealth = 100;
    public static final int WIDTH = 10;
    public static final int HEIGHT = 20;
    Laser laser;

    public Laser(int x, int y, double laserSpeed, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.speed = laserSpeed;
        this.image = image;
        this.width = image.getWidth();  // Initialize the width
        this.height = image.getHeight(); // Initialize the height
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void update(int playerX, int playerY) {
    	
    //	System.out.println("Laser update "+this.x);
    	
//    	x = playerX; // Set the laser's x-position to be the same as the player's x-position
//        y = playerY;
    	this.x += 10; // Move the laser horizontally

        if (x > GamePanel.WIDTH) {
            laser = null; // Reset the laser if it goes off the screen
        }

    }

    public boolean collidesWithMonster(Monster monster) {
        if (monster != null && !hitMonster &&
                monster.getX() < x + width &&
                monster.getX() + monster.getWidth() > x &&
                monster.getY() < y + height &&
                monster.getY() + monster.getHeight() > y) {
            hitMonster = true;
            monsterHealth = monsterHealth - 20;
            return true;
        }
        return false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
    
    public boolean intersects(Rectangle other) {
        return getBounds().intersects(other);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setSize(int width, int height) {
        this.image = resizeImage(image, width, height);
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();

        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        return resizedImage;
    }
}
