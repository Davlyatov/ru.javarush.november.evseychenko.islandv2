package animals;

import java.util.concurrent.atomic.AtomicInteger;

public class Plant {
    public double weight;
    int id;
    private static final AtomicInteger count = new AtomicInteger(0);

    public Plant(double weight) {
        this.id = count.incrementAndGet();
        this.weight = weight;
    }
}
