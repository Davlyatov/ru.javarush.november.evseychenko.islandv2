package entities;

import java.util.concurrent.atomic.AtomicInteger;

public class Plant {
    public double weight;
    int id;
    public boolean isSeed = false;
    private static final AtomicInteger count = new AtomicInteger(0);

    public Plant(double weight) {
        this.id = count.incrementAndGet();
        this.weight = weight;
    }

    public Plant(boolean isSeed) {
        this.id = count.incrementAndGet();
        this.weight = 0;
        this.isSeed = isSeed;
    }

    public void growUp() {
        if (weight <= 1) {
            weight += 0.5;
            if (weight > 1) {
                weight = 1;
            }
        } else {
            isSeed = false;
        }
    }
}
