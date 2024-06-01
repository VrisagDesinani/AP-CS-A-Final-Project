package entity;

import java.awt.Graphics2D;
import entity.LaserSpawner;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Projectile {
   private int x;
   private int y;
   private double speed;
   private BufferedImage image;
   private boolean hitPlayer;
   LaserSpawner laserSpawner = new LaserSpawner();
   private int width;
   private int height;
   
 
   
   public Projectile(int x, int y, int speed, BufferedImage image) {
	   this.x = x;
       this.y = y;
       this.speed = speed;
       this.image = image;
       this.width = image.getWidth();  // Initialize the width
       this.height = image.getHeight(); // Initialize the height
   }
   
   public void setSize(int width, int height) {
	   this.image = resizeImage(image, width, height);
	  
   }
   
   public void setPosition(int x, int y) {
	   this.x = x;
	   this.y = y;
   }
   
   public void update() {
	  if (!hitPlayer) {
      x -= speed;
	  }
	  
	  
   }
   
   public boolean collidesWithPlayer(Player player) {
	   if (!hitPlayer && player.getX() < x + image.getWidth() &&
               player.getX() + player.getWidth() > x &&
               player.getY() < y + image.getHeight() &&
               player.getY() + player.getHeight() > y) {
           hitPlayer = true;
           x = -100;
           y = -100;
           
		   
           
        		   
           return true;
       }
       return false;
   }
   
   public boolean isHitPlayer() {
	   return hitPlayer;
   }
   
   public void setHitPlayer (boolean hitPlayer) {
	   this.hitPlayer = hitPlayer;
   }
   
   public void draw(Graphics2D g2) {
      
      if (!hitPlayer) {
    	  g2.drawImage(image, x, y, null);
      }
      
     
   }
   
   public int getX() {
      return x;
   }
   
   public int getY() {
      return y;
   }
   
   public int getWidth() {
      return image.getWidth();
   }
   
   public int getHeight() {
      return image.getHeight();
   }


	private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
		BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = resizedImage.createGraphics();
		
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();
	
		return resizedImage;
		
	}
	
}
