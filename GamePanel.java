package main;
import javax.swing.JPanel; 
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import entity.Player;
import entity.Projectile;
import entity.Background;
import entity.Monster;
import java.util.List;
import java.util.ArrayList;
import entity.Laser;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GamePanel extends JPanel implements Runnable {
    // settings of the screen
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale; // 48x48 tile
    final int maxScreenCol = 22;
    final int maxScreenRow = 13;
    public final int screenWidth = tileSize * maxScreenCol; // 1056 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 624 pixels
    Projectile projectile;
    int FPS = 60;
    private int playerScore;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this, keyH, projectile);
    Background background = new Background(this, keyH);
    Monster monster;
    Laser laser;

    List<Monster> monsters = new ArrayList<>();

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        createMonsters();
        int health = player.getHealth();
    }

    private void createLaser() {
        double laserSpeed = 5.0;
        // Load the laser image
        BufferedImage laserImage = loadImage("/Projectile/LaserProjectile.png");

        // Create the laser instance with the desired speed and image
        laser = new Laser(player.getX(), player.getY(), laserSpeed, laserImage);
    }

    private BufferedImage loadImage(String imagePath) {
        try {
            return ImageIO.read(getClass().getResource(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createMonsters() {
        int x = getRandomCoordinateOutsideFrame(screenWidth);
        int y = getRandomCoordinateOutsideFrame(screenHeight);
        Monster monster = new Monster(x, y, player);
        this.monster = monster;
    }

    private int getRandomCoordinateOutsideFrame(int max) {
        int coordinate;
        int random = (int) (Math.random() * 2); // Randomly select either 0 or 1

        if (random == 0) {
            // Spawn on the left or right side
            int side = (int) (Math.random() * 2); // Randomly select either 0 or 1
            coordinate = side == 0 ? -Monster.WIDTH : max;
        } else {
            // Spawn on the top or bottom side
            int side = (int) (Math.random() * 2); // Randomly select either 0 or 1
            coordinate = side == 0 ? -Monster.HEIGHT : max;
        }

        return coordinate;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run() {
        double drawInterval = 1000000000.0 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        double startTime = System.currentTimeMillis();

        while (gameThread != null) {
            double seconds = System.currentTimeMillis();
            double timeNow = (seconds - startTime) / 1000;

            playerScore = (int) ((1 / 3.0) * (timeNow) * (timeNow)) + 100;

            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta > 2) {
                delta = 2;
            }

            while (delta >= 1) {
                update();
                delta--;
            }

            repaint();
        }
    }

    public void update() {
        int health = player.getHealth();
        player.update();
        monster.update();

        if (laser != null) {
            laser.update(player.getX(), player.getY());
            if (laser.getY() < 0 || laser.getY() > screenHeight) {
                // Remove the laser if it goes off-screen
                laser = null;
            } else if (monster != null && laser.intersects(monster.getBounds())) {
                // Handle collision between laser and monster
                playerScore += 10;
                monster = null;
                laser = null;
                health += 5;
            }
        }

        // Update monster
        if (monster == null) {
            createMonsters();
        }
        monster.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        background.drawBg(g2);
        player.draw(g2);

        int health = player.getHealth();
        if (health != 0) {
            monster.draw(g2);
        }

        g2.setColor(Color.yellow);
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.drawString("Player Score: " + playerScore, 400, 50);

        if (health <= 0) {
            g2.setColor(Color.black);
            g2.fillRect(0, 0, 1300, 1300);
            g2.setColor(Color.red);
            String Message = "Gameover complete";
            String finalMessage = Message.substring(0,8);
            
            g2.setFont(new Font("Arial", Font.PLAIN, 100));
            g2.drawString("Game Over!", 275, 300);
            g2.setColor(Color.yellow);
            g2.setFont(new Font(finalMessage, Font.PLAIN, 20));
            g2.drawString("Player Score: " + playerScore, 400, 50);

            gameThread = null;
        }

        g2.dispose();
    }
}
