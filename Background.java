package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

public class Background extends Entity{

    GamePanel gp;
    
    public Background(GamePanel gp, KeyHandler keyH) {
        
        this.gp = gp;
        setDefaultValues();
        getBackgroundImage();
        
    }
    
    public void setDefaultValues() {
        x = 0;
        y = 0;
        
    }
    
public void getBackgroundImage(){
        
        try {
            
            gameBg = ImageIO.read(getClass().getResourceAsStream("/background/background1.png"));
            
        }catch(IOException e) {
            
            e.printStackTrace();
            
        }
        
    }

	public void drawBg(Graphics2D g2) {
    
     BufferedImage backgroundImage = null;
     
     backgroundImage = gameBg;
     
     g2.drawImage(backgroundImage, x, y, gp.screenWidth, gp.screenHeight, null);
     
     
     
    
}
    
    
    
}