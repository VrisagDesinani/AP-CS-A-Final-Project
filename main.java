package main;

import javax.swing.ImageIcon;

import javax.swing.JFrame;
import javax.swing.JLabel;



public class main
{
	
	
   public static void main(String[] args)
   {
	  
      JFrame window = new JFrame("Galactic Escape");
      window.setSize(750, 750);
      window.setLocation(200, 100);
      window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      GamePanel GamePanel1 = new GamePanel();
      window.add(GamePanel1);
      window.pack();
      
      window.setResizable(false);
      window.setVisible(true);
   
      GamePanel1.startGameThread();
     
      
      
   }
}



