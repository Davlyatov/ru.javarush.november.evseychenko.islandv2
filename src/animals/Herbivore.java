package animals;


import utilities.Directions;
import utilities.PercentsForEat;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import static utilities.Directions.*;

public class Herbivore extends Animal {

    public Herbivore(double weight, double saturationWeight, int speed) {
        this.id = count.incrementAndGet();
        this.weight = weight;
        this.saturationWeight = saturationWeight;
        this.speed = speed;
    }

    @Override
    public boolean toEat(Object victim, Map<String, String> settingsMap) {
        int percentForEat = new PercentsForEat().getPercent(this.getClass().getSimpleName().toLowerCase(), victim.getClass().getSimpleName().toLowerCase());
        if (percentForEat == 0 || percentForEat == -1) {
            return false;
        }
        double maxWeight = Double.parseDouble(settingsMap.get(this.getClass().getSimpleName().toLowerCase() + " weight"));

        return ((new Random().nextInt(100) + 1) < percentForEat) && (maxWeight - this.weight == this.saturationWeight);
    }

    @Override
    public Directions[] chooseDirection(int x, int y, int height, int width) {
        ArrayList<Directions> directions = new ArrayList<>();
        if (x == 0 && y == 0) {
            directions.add(RIGHT);
            directions.add(DOWN);
        }
        if (x == 0 && y == height) {
            directions.add(UP);
            directions.add(RIGHT);
        }
        if (x == 0 && y != 0 && y != height) {
            directions.add(UP);
            directions.add(DOWN);
            directions.add(RIGHT);
        }
        if (x == width && y == 0) {
            directions.add(LEFT);
            directions.add(DOWN);
        }
        if (x == width && y == height) {
            directions.add(LEFT);
            directions.add(UP);
        }
        if (x == width && y != 0 && y != height) {
            directions.add(UP);
            directions.add(DOWN);
            directions.add(LEFT);
        }
        if (y == 0 && x != 0 && x != width) {
            directions.add(DOWN);
            directions.add(LEFT);
            directions.add(RIGHT);
        }
        if (y != 0 && y != height && x != 0 && x != width) {
            directions.add(UP);
            directions.add(LEFT);
            directions.add(RIGHT);
            directions.add(DOWN);
        }
        return directions.toArray(new Directions[0]);
    }

    @Override
    public int[] toMove(int x, int y, int height, int width) {
        Directions[] directions = chooseDirection(x, y, height, width);
        int[] coordinates = new int[2];
        Directions chosenDirection = directions[new Random().nextInt(directions.length)];
        switch (chosenDirection) {
            case UP: {
                int tempY = new Random().nextInt(this.speed) + 1;
                y += tempY;
                coordinates[0] = x;
                coordinates[1] = y;
                break;
            }
            case DOWN: {
                int tempY = new Random().nextInt(this.speed) + 1;
                y -= tempY;
                coordinates[0] = x;
                coordinates[1] = y;
                break;
            }
            case RIGHT: {
                int tempX = new Random().nextInt(this.speed) + 1;
                x += tempX;
                coordinates[0] = x;
                coordinates[1] = y;
                break;
            }
            case LEFT: {
                int tempX = new Random().nextInt(this.speed) + 1;
                x -= tempX;
                coordinates[0] = x;
                coordinates[1] = y;
                break;
            }
        }
        return coordinates;
    }

}
