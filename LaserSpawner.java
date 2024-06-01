package entity;

import java.awt.image.BufferedImage; 
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

    
public class LaserSpawner  {
    private List<Coordinate> spawnCoordinates;
    private Random random;

    public LaserSpawner() {
        spawnCoordinates = new ArrayList<>();
        spawnCoordinates.add(new Coordinate(710, 70));
        spawnCoordinates.add(new Coordinate(776, 44));
        spawnCoordinates.add(new Coordinate(874, 22));
        spawnCoordinates.add(new Coordinate(714, 222));
        spawnCoordinates.add(new Coordinate(772, 246));
        spawnCoordinates.add(new Coordinate(864, 268));
        spawnCoordinates.add(new Coordinate(716, 366));
        spawnCoordinates.add(new Coordinate(774, 342));
        spawnCoordinates.add(new Coordinate(874, 318));
        spawnCoordinates.add(new Coordinate(720, 514));
        spawnCoordinates.add(new Coordinate(780, 541));
        spawnCoordinates.add(new Coordinate(878, 561));

        random = new Random();
    }

    public Coordinate spawnLaser() {
        int index = random.nextInt(spawnCoordinates.size());
        return spawnCoordinates.get(index);
    }

    public static class Coordinate {
        private int x;
        private int y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;                         
        }

        public int getY() {
            return y;
        }
    }
    
}