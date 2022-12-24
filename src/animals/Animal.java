package animals;

import utilities.Directions;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public abstract class Animal {

    public abstract boolean toEat(Object o, Map<String, String> settingsMap);

    public abstract Directions[] chooseDirection(int x, int y, int height, int width);

    public abstract int[] toMove(int x, int y, int height, int width);

    public double weight;
    public int speed;
    public double saturationWeight;
    static final AtomicInteger count = new AtomicInteger(0);
    public int id;
}
