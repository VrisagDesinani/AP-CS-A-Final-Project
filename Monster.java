package entity;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import main.GamePanel;

public class Monster extends Entity {
    private int x;
    private int y;
    private int width;
    private int height;
    private BufferedImage monsterImage;
    private Player player;
    private int damage;
    public static final int WIDTH = 20;
    public static final int HEIGHT = 20;
    private long lastDamageTime;
    private static final int DAMAGE_COOLDOWN = 200;
    private static final int MAX_MONSTERS = 5; // Adjust the maximum number of monsters
    private static List<Monster> monsters = new ArrayList<>();

    public Monster(int x, int y, Player player) {
        this.player = player;
        setDefaultValues();
        getMonsterImage();
        damage = 5; // Set the damage value of the monster
        lastDamageTime = System.currentTimeMillis();
        int monsterHealth = 100;
        
    }

    public void dealDamageToPlayer() {
        player.takeDamage(damage);
    }

    public int getDamage() {
        return damage;
    }

    private void setDefaultValues() {
        // Generate random spawn coordinates outside the frame
        x = getRandomCoordinateOutsideFrame(GamePanel.WIDTH)+1000;
        y = getRandomCoordinateOutsideFrame(GamePanel.HEIGHT)-350;
        width = Monster.WIDTH*2; // Adjust the width of the monster
        height = Monster.HEIGHT*2;  // Adjust the height of the monster
        speed = 5/3; // Movement speed of the monster
        direction = "down"; // Initial direction of the monster (empty for stationary)
    }

    private void getMonsterImage() {
        try {
            monsterImage = ImageIO.read(getClass().getResourceAsStream("/projectile/monster.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getRandomCoordinateOutsideFrame(int max) {
        int coordinate;
        int random = (int) (Math.random() * 2); // Randomly select either 0 or 1

        if (random == 0) {
            // Spawn on the left or right side
            int side = (int) (Math.random() * 2); // Randomly select either 0 or 1
            coordinate = side == 0 ? -width : max;
        } else {
            // Spawn on the top or bottom side
            int side = (int) (Math.random() * 2); // Randomly select either 0 or 1
            coordinate = side == 0 ? -height : max;
        }

        return coordinate;
    }

    public static void createMonsters(Player player) {
        if (monsters.size() < MAX_MONSTERS) {
            monsters.add(new Monster(0, 0, player));
        }
    }

    public static void updateMonsters() {
    	Iterator<Monster> iterator = monsters.iterator();
        while (iterator.hasNext()) {
            Monster monster = iterator.next();
            monster.update();
            if (monster.collidesWithPlayer() && monster.isDamageCooldownOver()) {
                monster.dealDamageToPlayer();
                monster.lastDamageTime = System.currentTimeMillis();
            }
            if (monster.isOutsideFrame()) {
                iterator.remove();
            }
        }
    }
    
    public void monsterDeath() {
    	//if (monsterHealth == 0) {
    		
    	
    }

    

    public void update() {
        // Calculate the direction based on player's position
        
        if (x <= player.getX()) {
            x += speed; // Move right
            direction = "right";
        } else if (x >= player.getX()) {
            x -= speed; // Move left
            direction = "left";
        }

        if (y <= player.getY()) {
            y += speed; // Move down
            direction = "down";
        } else if (y >= player.getY()) {
            y -= speed; // Move up
            direction = "up";
        }
        
        

        if (collidesWithPlayer() && isDamageCooldownOver()) {
            dealDamageToPlayer();
            lastDamageTime = System.currentTimeMillis();
        }
        
    }
    
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

    public void draw(Graphics g) {
        g.drawImage(monsterImage, x, y, width, height, null);
    }

    public boolean collidesWithPlayer() {
        int playerX = player.getX()-1;
        int playerY = player.getY()-1;
        int playerWidth = player.getWidth()-2;
        int playerHeight = player.getHeight()-2;

        int monsterX = x;
        int monsterY = y;
        int monsterWidth = width;
        int monsterHeight = height;

        // Check for intersection between player and monster
        if (playerX < monsterX + monsterWidth &&
                playerX + playerWidth > monsterX &&
                playerY < monsterY + monsterHeight &&
                playerY + playerHeight > monsterY) {
            return true; // Collision detected
        }

        return false; // No collision
    }

    private boolean isDamageCooldownOver() {
        return System.currentTimeMillis() - lastDamageTime >= DAMAGE_COOLDOWN;
    }

    private boolean isOutsideFrame() {
        return x + width < 0 || x > GamePanel.WIDTH || y + height < 0 || y > GamePanel.HEIGHT;
    }

	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
}
