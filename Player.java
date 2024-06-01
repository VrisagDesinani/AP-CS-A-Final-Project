package entity;

import javax.swing.JPanel;   
import javax.swing.*;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import main.GamePanel;
import main.KeyHandler;
import entity.Projectile;
import entity.LaserSpawner; 
import entity.LaserSpawner.Coordinate;
import entity.Monster;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CopyOnWriteArrayList;
import entity.Laser;

public class Player extends Entity{
   
   GamePanel gp; 
   KeyHandler keyH;
   int health;
   Projectile projectile;
   private int x;
   private int y;
   private int width;
   private int height;
   private boolean dead;
   private int damage;
   private LaserSpawner laserSpawner;
   private BufferedImage laserImage;
   private List<Projectile> projectiles;
   private static final int MAX_PROJECTILES = 5;
   private Monster monster;
   private int lastX;
   private int lastY;
   private boolean isSameAxisTimerRunning;
   private Timer sameAxisTimer;
   private ScheduledExecutorService executorService;
   private static final int HEALTH_LOSS_INTERVAL = 5000; // 5 seconds
   private long lastHealthLossTime;
   private CopyOnWriteArrayList<Monster> monsters = new CopyOnWriteArrayList<>();
   Thread gameThread;
   Laser laser;
   private boolean fired;
   int monsterHealth = 100;
  

   
   
   
   
   	public int getX() {
	      return x;
	   }
	   
	   public int getY() {
	      return y;
	   }
	   
	   public int getWidth() {
	      return width;
	   }
	   
	   public int getHeight() {
	      return height;
	   }
	   
	   public Rectangle getBounds() {
	        return new Rectangle(x, y, width, height);
	    }
	   
   
   public Player (GamePanel gp, KeyHandler keyH, Projectile projectile) {
	  
       
	   
      this.gp = gp;
      this.keyH = keyH;
      setDefaultValues();
      getPlayerImage();
      health = 300;
      dead = false;
      this.projectile = projectile; 
      laserSpawner = new LaserSpawner();
      Coordinate randomCoordinate = laserSpawner.spawnLaser();
      projectiles = new ArrayList<>();
      loadLaserImage();
      damage = 5;
      lastX = 0;
      lastY = 0;
      isSameAxisTimerRunning = false;
      executorService = Executors.newSingleThreadScheduledExecutor();
      lastHealthLossTime = System.currentTimeMillis();
      laser = new Laser(0, 0, 4, laserImage);
      
    
      // coordinates are top ship: (710, 70) (776, 44) (874, 22) (714, 222) (772, 246) (864, 268)
      //coodinates for the bottom ship are: (716, 366) (774, 342) (874, 318) (720, 514) (780,541) (878, 561)
      
     
      
   }
   
   
   
   public void takeDamage(int damage) {
       health -= damage;
       if (health <= 0) {
           health = 0;
           dead = true;
       }
   }

   public int getHealth() {
       return health;
   }

   public boolean isDead() {
       return dead;
   }

   public int getDamage() {
       return damage;
   }

   

  
   
   
   public void setDefaultValues() {
      x = 100;
      y = 100;
      speed = 4; //change this back after finding all the x and y values 
      direction = "forward";
   }
   
   public void getPlayerImage() {
      
     try {
      up1 = ImageIO.read(getClass().getResourceAsStream("/player/SpaceShipMainLeft.png"));
      down1 = ImageIO.read(getClass().getResourceAsStream("/player/SpaceShipMainRight.png"));
      forward1 = ImageIO.read(getClass().getResourceAsStream("/player/SpaceShipMainForward.png"));
      backward1 = ImageIO.read(getClass().getResourceAsStream("/player/SpaceShipMainBackward.png"));
      Mothership =  ImageIO.read(getClass().getResourceAsStream("/Projectile/MotherShip1.png"));
      MotherLaser = ImageIO.read(getClass().getResourceAsStream("/Projectile/LaserProjectile.png"));
      
      
     }catch(IOException e) {
      e.printStackTrace();
     }

      
   }
   
   
   private void loadLaserImage() {
	    try {
	        laserImage = ImageIO.read(getClass().getResourceAsStream("/Projectile/LaserProjectile.png"));
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
   
   public void update() {
	   
	   if (keyH.upPressed) {
		      direction = "up";
		      y -= speed;
		   } else if (keyH.downPressed) {
		      direction = "down";
		      y += speed;
		   } else if (keyH.leftPressed) {
		      direction = "backward";
		      x -= speed;
		   } else if (keyH.rightPressed) {
		      direction = "forward";
		      x += speed;
		   }

	   laser.update(lastX, lastY);
		   if (keyH.spacePressed) {
		      // Set the initial position of the laser based on the player's position
		      laser.setPosition(x + width, y + height / 2);
		      // Move the laser
		      

		      // Check collision with monsters
		      if (laser.collidesWithMonster(monster)) {
		         monsterHealth -= 20;
		      }
		   }
       
       
       		
          if (x <= 0) {
        	 x=0;
         }
       	 
          if (y <= 0) {
        	 y=0;
         }
       
          if (x >= 600) {
        	 x=600;
         }
          if (y>=565) {
        	 y=565;
         }
          
          
		
       	
       //System.out.println("x: "+x+" y: "+y);
       
          Iterator<Projectile> iterator = projectiles.iterator();
          while (iterator.hasNext()) {
              Projectile projectile = iterator.next();
              projectile.update();

              // Check collision with player
              if (collidesWithProjectile(projectile)) {
                  health -= 25;
                  projectile.setHitPlayer(true);
              }

              // Remove projectiles that go off-screen
              if (projectile.getX() < 0 || projectile.getX() > gp.getWidth() ||
                  projectile.getY() < 0 || projectile.getY() > gp.getHeight()) {
                  iterator.remove();
              }
          }

          // Spawn new projectiles
          if (projectiles.size() < MAX_PROJECTILES && Math.random() < 0.04) {
              Coordinate laserCoordinate = laserSpawner.spawnLaser();
              Projectile newProjectile = new Projectile(laserCoordinate.getX(), laserCoordinate.getY(), 5, laserImage);
              projectiles.add(newProjectile);
          }
          
          if (System.currentTimeMillis() - lastHealthLossTime >= HEALTH_LOSS_INTERVAL) {
              health -= 10; // Reduce the player's health by 10
              lastHealthLossTime = System.currentTimeMillis();
          }


       }      

   
   
       
       // Use the x and y values to spawn a laser at the desired position
       // Your code to spawn a laser goes here
   
   
   private BufferedImage getImageForLaser() {
	    
	    BufferedImage laserImage = null;
	    try {
	        laserImage = ImageIO.read(getClass().getResourceAsStream("/res/Projectile/LaserProjectile.png"));
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return laserImage;
	}
       
       // System.out.println(health);
       	 
       
       	
   
   
   public void draw(Graphics g2) {
   //    g2.setColor(Color.white);
   //  g2.fillRect(x ,y, gp.tileSize, gp.tileSize);
        
	  
      BufferedImage image = null;
      switch(direction) {
      case "up":
         image = up1;
         break;
      case "down":     
         image = down1;
         break;
      case "backward":
         image = backward1;
        break;
      case "forward":
         image =  forward1;
         break;
      }
      
     
      
     g2.drawImage(image, x, y, (gp.tileSize*3)/2, (gp.tileSize*3)/2, null);  
     
      g2.drawImage(Mothership, 650, -70, (gp.tileSize*9), (gp.tileSize*9), null); 
      g2.drawImage(Mothership, 650, 225, (gp.tileSize*9), (gp.tileSize*9), null);
      
      g2.setColor(Color.red);
	  g2.setFont(new Font("Arial", Font.PLAIN, 20));
      g2.drawString("Player Health: "+ health, 400,20);
      
      Iterator<Projectile> iterator = projectiles.iterator();
      while (iterator.hasNext()) {
          Projectile projectile = iterator.next();
          projectile.draw((Graphics2D) g2);

          // Check collision with player
          if (collidesWithProjectile(projectile)) {
              health -= 25;
              projectile.setHitPlayer(true);
          }

          // Remove projectiles that go off-screen
          if (projectile.getX() < 0 || projectile.getX() > gp.getWidth() ||
                  projectile.getY() < 0 || projectile.getY() > gp.getHeight()) {
              iterator.remove();
          }
      }
      
      if (keyH.spacePressed) {
          // Draw the laser
          g2.drawImage(laser.getImage(), laser.getX(), laser.getY(), null);
      }      
      
     
      
     
  }
      
 

   
   
   public boolean collidesWithProjectile(Projectile projectile) {
	  
	   int playerWidth = (gp.tileSize * 3) / 2;
	      int playerHeight = (gp.tileSize * 3) / 2;
	      Rectangle playerBounds = new Rectangle(x, y, playerWidth, playerHeight);
	      Rectangle projectileBounds = new Rectangle(projectile.getX(), projectile.getY(), projectile.getWidth(), projectile.getHeight());
	      
	      if (!projectile.isHitPlayer() && playerBounds.intersects(projectileBounds)) {
	         projectile.setHitPlayer(true);
	         return true;
	      }
	      
	      return false;
	   }
}