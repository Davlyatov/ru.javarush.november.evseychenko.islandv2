package utilities;

import animals.Animal;
import animals.Herbivore;
import animals.Plant;
import animals.Predator;
import animals.predators.*;
import animals.herbivores.*;

import java.util.ArrayList;
import java.util.Random;

public class Simulation implements Runnable {

    Island island;
    Settings settings;

    int island_height;
    int island_width;
    int threadPoolSize;
    int ticks = 1;


    public Simulation(Island island, Settings settings) {
        this.island = island;
        this.settings = settings;
        island_height = Integer.parseInt(settings.settingsMap.get("island height"));
        island_width = Integer.parseInt(settings.settingsMap.get("island width"));
        threadPoolSize = island_height * island_width;
    }

    @Override
    public void run() {
        try {
            System.out.println("Tick #" + ticks);
            System.out.println("moving");
            move(island);
            System.out.println("starvation");
            loverSatiety(island, settings, 2);
            System.out.println("feeding");
            feed(island, settings);
            System.out.println("multiplying");
            multiply(island, settings);
            System.out.println("starvation");
            loverSatiety(island, settings, 5);
            ticks++;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void multiply(Island island, Settings settings) throws InterruptedException {
        Thread[] multiplyThreads = new Thread[island_height * island_width];
        int i = 0;
        for (int x = 0; x < island.location.length; x++) {
            for (int y = 0; y < island.location[x].length; y++) {
                int finalX = x;
                int finalY = y;
                Thread thread = new Thread(() -> multiplyMob(finalX, finalY, island, settings));
                multiplyThreads[i] = thread;
                i++;
            }
        }
        for (Thread multiplyThread : multiplyThreads) {
            multiplyThread.start();
        }
        for (Thread multiplyThread : multiplyThreads) {
            multiplyThread.join();
        }
    }

    private void multiplyMob(int x, int y, Island island, Settings settings) {
        if (island.location[x][y] != null) {
            if (island.location[x][y].size() > 0) {
                if (island.location[x][y].get(0) instanceof Predator) {
                    multiplyPredators(island, x, y, settings);
                } else if (island.location[x][y].get(0) instanceof Herbivore) {
                    multiplyHerbivores(island, x, y, settings);
                } else {
                    multiplyPlant(island, x, y, settings);
                }
            }
        } else {
            tryToSpawnPlant(island, x, y, settings);
        }
    }

    private void tryToSpawnPlant(Island island, int x, int y, Settings settings) {
        double weight = Double.parseDouble(settings.settingsMap.get("plant weight"));
        int maxInCell = Integer.parseInt(settings.settingsMap.get("plant maxInCell"));
        int multiplyChance = 10;
        if (new Random().nextInt() < multiplyChance) {
            island.location[x][y] = new ArrayList<Plant>();
            int randomCount = new Random().nextInt(maxInCell) + 1;
            for (int i = 0; i < randomCount; i++) {
                island.location[x][y].add(new Plant(weight));
                int count = Island.islandMobCount.get("plant");
                count++;
                Island.islandMobCount.replace("plant",count);
            }
        }
    }

    private void multiplyPlant(Island island, int x, int y, Settings settings) {
        String mobClassToString = island.location[x][y].get(0).getClass().getSimpleName().toLowerCase();
        double weight = Double.parseDouble(settings.settingsMap.get(mobClassToString + " weight"));
        int maxInCell = Integer.parseInt(settings.settingsMap.get(mobClassToString + " maxInCell"));
        int availableMultiplies = maxInCell - island.location[x][y].size();
        int multiplyChance = 40;
        for (int i = 0; i < availableMultiplies; i++) {
            if (new Random().nextInt(100) > multiplyChance) {
                island.location[x][y].add(new Plant(weight));
                int count = Island.islandMobCount.get("plant");
                count++;
                Island.islandMobCount.replace("plant",count);
            }
        }
    }

    private void multiplyHerbivores(Island island, int x, int y, Settings settings) {
        Object currentMobInCell = island.location[x][y].stream().findFirst().get();
        String mobClassToString = currentMobInCell.getClass().getSimpleName().toLowerCase();
        int maxInCell = Integer.parseInt(settings.settingsMap.get(mobClassToString + " maxInCell"));
        int availableMultiplies = island.location[x][y].size() / 2;
        double weight = Double.parseDouble(settings.settingsMap.get(mobClassToString + " weight"));
        double saturationFood = Double.parseDouble(settings.settingsMap.get(mobClassToString + " saturationFood"));
        int speed = Integer.parseInt(settings.settingsMap.get(mobClassToString + " maxCellSpeed"));
        int multiplyChance = 50;
        for (int i = 0; i < availableMultiplies; i++) {
            if (currentMobInCell instanceof Boar) {
                if (new Random().nextInt(100) > multiplyChance && island.location[x][y].size() < maxInCell && ((Boar) currentMobInCell).weight * 2 == weight) {
                    island.location[x][y].add(new Boar(weight, saturationFood, speed));
                    int count = Island.islandMobCount.get("boar");
                    count++;
                    Island.islandMobCount.replace("boar",count);
                }
            } else if (currentMobInCell instanceof Buffalo) {
                if (new Random().nextInt(100) > multiplyChance && island.location[x][y].size() < maxInCell && ((Buffalo) currentMobInCell).weight * 2 == weight) {
                    island.location[x][y].add(new Buffalo(weight, saturationFood, speed));
                    int count = Island.islandMobCount.get("buffalo");
                    count++;
                    Island.islandMobCount.replace("buffalo",count);
                }
            } else if (currentMobInCell instanceof Caterpillar) {
                if (new Random().nextInt(100) > multiplyChance && island.location[x][y].size() < maxInCell) {
                    island.location[x][y].add(new Caterpillar(weight, saturationFood, speed));
                    int count = Island.islandMobCount.get("caterpillar");
                    count++;
                    Island.islandMobCount.replace("caterpillar",count);
                }
            } else if (currentMobInCell instanceof Deer) {
                if (new Random().nextInt(100) > multiplyChance && island.location[x][y].size() < maxInCell && ((Deer) currentMobInCell).weight * 2 == weight) {
                    island.location[x][y].add(new Deer(weight, saturationFood, speed));
                    int count = Island.islandMobCount.get("deer");
                    count++;
                    Island.islandMobCount.replace("deer",count);
                }
            } else if (currentMobInCell instanceof Duck) {
                if (new Random().nextInt(100) > multiplyChance && island.location[x][y].size() < maxInCell && ((Duck) currentMobInCell).weight * 2 == weight) {
                    island.location[x][y].add(new Duck(weight, saturationFood, speed));
                    int count = Island.islandMobCount.get("duck");
                    count++;
                    Island.islandMobCount.replace("duck",count);
                }
            } else if (currentMobInCell instanceof Goat) {
                if (new Random().nextInt(100) > multiplyChance && island.location[x][y].size() < maxInCell && ((Goat) currentMobInCell).weight * 2 == weight) {
                    island.location[x][y].add(new Goat(weight, saturationFood, speed));
                    int count = Island.islandMobCount.get("goat");
                    count++;
                    Island.islandMobCount.replace("goat",count);
                }
            } else if (currentMobInCell instanceof Horse) {
                if (new Random().nextInt(100) > multiplyChance && island.location[x][y].size() < maxInCell && ((Horse) currentMobInCell).weight * 2 == weight) {
                    island.location[x][y].add(new Horse(weight, saturationFood, speed));
                    int count = Island.islandMobCount.get("horse");
                    count++;
                    Island.islandMobCount.replace("horse",count);
                }
            } else if (currentMobInCell instanceof Mouse) {
                if (new Random().nextInt(100) > multiplyChance && island.location[x][y].size() < maxInCell && ((Mouse) currentMobInCell).weight * 2 == weight) {
                    island.location[x][y].add(new Mouse(weight, saturationFood, speed));
                    int count = Island.islandMobCount.get("mouse");
                    count++;
                    Island.islandMobCount.replace("mouse",count);
                }
            } else if (currentMobInCell instanceof Rabbit) {
                if (new Random().nextInt(100) > multiplyChance && island.location[x][y].size() < maxInCell && ((Rabbit) currentMobInCell).weight * 2 == weight) {
                    island.location[x][y].add(new Rabbit(weight, saturationFood, speed));
                    int count = Island.islandMobCount.get("rabbit");
                    count++;
                    Island.islandMobCount.replace("rabbit",count);
                }
            } else if (currentMobInCell instanceof Sheep) {
                if (new Random().nextInt(100) > multiplyChance && island.location[x][y].size() < maxInCell && ((Sheep) currentMobInCell).weight * 2 == weight) {
                    island.location[x][y].add(new Sheep(weight, saturationFood, speed));
                    int count = Island.islandMobCount.get("sheep");
                    count++;
                    Island.islandMobCount.replace("sheep",count);
                }
            }
        }
    }

    private void multiplyPredators(Island island, int x, int y, Settings settings) {
        Object currentMobInCell = island.location[x][y].stream().findFirst().get();
        String mobClassToString = currentMobInCell.getClass().getSimpleName().toLowerCase();
        int maxInCell = Integer.parseInt(settings.settingsMap.get(mobClassToString + " maxInCell"));
        int availableMultiplies = island.location[x][y].size() / 2;
        double weight = Double.parseDouble(settings.settingsMap.get(mobClassToString + " weight"));
        double saturationFood = Double.parseDouble(settings.settingsMap.get(mobClassToString + " saturationFood"));
        int speed = Integer.parseInt(settings.settingsMap.get(mobClassToString + " maxCellSpeed"));
        int multiplyChance = 50;
        for (int i = 0; i < availableMultiplies; i++) {
            if (currentMobInCell instanceof Bear) {
                if (new Random().nextInt(100) > multiplyChance && island.location[x][y].size() < maxInCell && ((Bear) currentMobInCell).weight * 2 == weight) {
                    island.location[x][y].add(new Bear(weight, saturationFood, speed));
                    int count = Island.islandMobCount.get("bear");
                    count++;
                    Island.islandMobCount.replace("bear",count);
                }
            } else if (currentMobInCell instanceof Eagle) {
                if (new Random().nextInt(100) > multiplyChance && island.location[x][y].size() < maxInCell && ((Eagle) currentMobInCell).weight * 2 == weight) {
                    island.location[x][y].add(new Eagle(weight, saturationFood, speed));
                    int count = Island.islandMobCount.get("eagle");
                    count++;
                    Island.islandMobCount.replace("eagle",count);
                }
            } else if (currentMobInCell instanceof Fox) {
                if (new Random().nextInt(100) > multiplyChance && island.location[x][y].size() < maxInCell && ((Fox) currentMobInCell).weight * 2 == weight) {
                    island.location[x][y].add(new Fox(weight, saturationFood, speed));
                    int count = Island.islandMobCount.get("fox");
                    count++;
                    Island.islandMobCount.replace("fox",count);
                }
            } else if (currentMobInCell instanceof Snake) {
                if (new Random().nextInt(100) > multiplyChance && island.location[x][y].size() < maxInCell && ((Snake) currentMobInCell).weight * 2 == weight) {
                    island.location[x][y].add(new Snake(weight, saturationFood, speed));
                    int count = Island.islandMobCount.get("snake");
                    count++;
                    Island.islandMobCount.replace("snake",count);
                }
            } else if (currentMobInCell instanceof Wolf) {
                if (new Random().nextInt(100) > multiplyChance && island.location[x][y].size() < maxInCell && ((Wolf) currentMobInCell).weight * 2 == weight) {
                    island.location[x][y].add(new Wolf(weight, saturationFood, speed));
                    int count = Island.islandMobCount.get("wolf");
                    count++;
                    Island.islandMobCount.replace("wolf",count);
                }
            }
        }
    }

    private void feed(Island island, Settings settings) throws InterruptedException {
        Thread[] feedThreads = new Thread[island_height * island_width];
        int i = 0;
        for (int x = 0; x < island.location.length; x++) {
            for (int y = 0; y < island.location[x].length; y++) {
                int finalX = x;
                int finalY = y;
                Thread thread = new Thread(() -> feedMob(island, finalX, finalY, settings));
                feedThreads[i] = thread;
                i++;
            }
        }
        for (Thread feedThread : feedThreads) {
            feedThread.start();
        }
        for (Thread feedThread : feedThreads) {
            feedThread.join();
        }
    }

    private synchronized void feedMob(Island island, int x, int y, Settings settings) {
        if (island.location[x][y] != null && island.location[x][y].size() > 0) {
            if (checkBorder(x + 1, y)) {
                if (!(island.location[x][y].stream().findFirst().get() instanceof Caterpillar) && !(island.location[x][y].stream().findFirst().get() instanceof Plant)) {
                    feedAnimal(x, y, x + 1, y, settings);
                }
            } else if (checkBorder(x - 1, y)) {
                if (!(island.location[x][y].stream().findFirst().get() instanceof Caterpillar) && !(island.location[x][y].stream().findFirst().get() instanceof Plant)) {
                    feedAnimal(x, y, x - 1, y, settings);
                }
            } else if (checkBorder(x, y + 1)) {
                if (!(island.location[x][y].stream().findFirst().get() instanceof Caterpillar) && !(island.location[x][y].stream().findFirst().get() instanceof Plant)) {
                    feedAnimal(x, y, x, y + 1, settings);
                }
            } else if (checkBorder(x, y - 1)) {
                if (!(island.location[x][y].stream().findFirst().get() instanceof Caterpillar) && !(island.location[x][y].stream().findFirst().get() instanceof Plant)) {
                    feedAnimal(x, y, x, y - 1, settings);
                }
            }
        }
    }

    private synchronized void feedAnimal(int x, int y, int x1, int y1, Settings settings) {
        for (int i = 0; i < island.location[x][y].size(); i++) {
            Object currentMobInCell = island.location[x][y].get(i);
            String mobClassToString = currentMobInCell.getClass().getSimpleName().toLowerCase();
            double maxWeight = Double.parseDouble(settings.settingsMap.get(mobClassToString + " weight"));
            double saturationWeight = Double.parseDouble(settings.settingsMap.get(mobClassToString + " saturationFood"));
            if (maxWeight - ((Animal) currentMobInCell).weight == saturationWeight && island.location[x1][y1]!=null) {
                for (int j = 0; j < island.location[x1][y1].size(); j++) {
                    boolean isCanEat = ((Animal) currentMobInCell).toEat(island.location[x1][y1].get(j), settings.settingsMap);
                    if (isCanEat) {
                        System.out.println(currentMobInCell.getClass().getSimpleName()+ " eaten " + island.location[x1][y1].get(j).getClass().getSimpleName());
                        if (saturationWeight > ((Animal) island.location[x1][y1].get(j)).weight) {
                            ((Animal) currentMobInCell).weight += ((Animal) island.location[x1][y1].get(j)).weight;
                        } else {
                            ((Animal) currentMobInCell).weight += ((Animal) currentMobInCell).saturationWeight;
                        }
                        String key = island.location[x1][y1].get(j).getClass().getSimpleName().toLowerCase();
                        int count = Island.islandMobCount.get(key);
                        count--;
                        Island.islandMobCount.replace(key,count);
                        island.location[x1][y1].remove(j);
                        j--;
                    }
                    if (island.location[x1][y1].size() == 0) {
                        island.location[x1][y1] = null;
                        break;
                    }
                }
            }
        }
    }

    private boolean checkBorder(int x, int y) {
        return (x < island_width && y < island_height) && (x >= 0 && y >= 0);
    }

    private void loverSatiety(Island island, Settings settings, int percent) throws InterruptedException {
        Thread[] satietyThreads = new Thread[island_height * island_width];
        int i = 0;
        for (int x = 0; x < island.location.length; x++) {
            for (int y = 0; y < island.location[x].length; y++) {
                int finalX = x;
                int finalY = y;
                Thread thread = new Thread(() -> starvation(island, finalX, finalY, settings, percent));
                satietyThreads[i] = thread;
                i++;
            }
        }
        for (Thread satietyThread : satietyThreads) {
            satietyThread.start();
        }
        for (Thread satietyThread : satietyThreads) {
            satietyThread.join();
        }
    }

    private void starvation(Island island, int x, int y, Settings settings, int percent) {
        if (island.location[x][y] != null) {
            if (island.location[x][y].size() > 0) {
                if (!(island.location[x][y].stream().findFirst().get() instanceof Plant)
                        && !(island.location[x][y].stream().findFirst().get() instanceof Caterpillar)) {
                    Object currentMobInCell = island.location[x][y].stream().findFirst().get();
                    String mobClassToString = currentMobInCell.getClass().getSimpleName().toLowerCase();
                    double maxWeight = Double.parseDouble(settings.settingsMap.get(mobClassToString + " weight"));
                    double starvationWeight = maxWeight / 100 * percent;
                    for (int i = 0; i < island.location[x][y].size(); i++) {
                        ((Animal) island.location[x][y].get(i)).weight -= starvationWeight;
                        if ((((Animal) island.location[x][y].get(i)).weight <= 0)) {
//                            System.out.println(island.location[x][y].get(i).getClass().getSimpleName()+"#"+((Animal) island.location[x][y].get(i)).id+" died from hunger");
                            String key = island.location[x][y].get(i).getClass().getSimpleName().toLowerCase();
                            int count = Island.islandMobCount.get(key);
                            count--;
                            Island.islandMobCount.replace(key,count);
                            island.location[x][y].remove(i);
                            i--;
                        }
                        if (island.location[x][y].size() == 0) {
                            island.location[x][y] = null;
                            break;
                        }
                    }
                }
            }
        }
    }

    private void move(Island island) throws InterruptedException {
        Thread[] moveThreads = new Thread[threadPoolSize];
        int i = 0;
        for (int x = 0; x < island.location.length; x++) {
            for (int y = 0; y < island.location[x].length; y++) {
                int finalX = x;
                int finalY = y;
                Thread thread = new Thread(() -> moveFromCell(finalX, finalY, island));
                moveThreads[i] = thread;
                i++;
            }
        }
        for (Thread moveThread : moveThreads) {
            moveThread.start();
        }
        for (Thread moveThread : moveThreads) {
            moveThread.join();
        }
    }

    private synchronized void moveFromCell(int x, int y, Island island) {
        if (island.location[x][y] != null) {
            if (island.location[x][y].size() > 0) {
                if (island.location[x][y].get(0) instanceof Predator) {
                    movePredator(x, y, island);
                } else if (island.location[x][y].get(0) instanceof Herbivore) {
                    if (!(island.location[x][y].get(0) instanceof Caterpillar)) {
                        moveHerbivore(x, y, island);
                    }
                }
            }
        }
    }

    private synchronized void moveHerbivore(int x, int y, Island island) {
        for (int i = 0; i < island.location[x][y].size(); i++) {
            int[] coordinates = ((Animal) island.location[x][y].get(i)).toMove(x, y, island_height, island_width);
            int x1 = coordinates[0], y1 = coordinates[1];
            if (island.location[x1][y1] == null || island.location[x1][y1].size() == 0) {
                island.location[x1][y1] = new ArrayList<Animal>();
                island.location[x1][y1].add(island.location[x][y].get(i));
                island.location[x][y].remove(i);
                i--;
            } else {
                Object mobsInCurrentCell = island.location[x1][y1].get(0);
                String classToSting = mobsInCurrentCell.getClass().getSimpleName().toLowerCase();
                int maxMobsInCurrentCell = Integer.parseInt(settings.settingsMap.get(classToSting + " maxInCell"));
                if (mobsInCurrentCell instanceof Boar && island.location[x][y].size() < maxMobsInCurrentCell) {
                    island.location[x1][y1].add(island.location[x][y].get(i));
                    island.location[x][y].remove(i);
                    i--;
                } else if (mobsInCurrentCell instanceof Buffalo && island.location[x][y].size() < maxMobsInCurrentCell) {
                    island.location[x1][y1].add(island.location[x][y].get(i));
                    island.location[x][y].remove(i);
                    i--;
                } else if (mobsInCurrentCell instanceof Deer && island.location[x][y].size() < maxMobsInCurrentCell) {
                    island.location[x1][y1].add(island.location[x][y].get(i));
                    island.location[x][y].remove(i);
                    i--;
                } else if (mobsInCurrentCell instanceof Duck && island.location[x][y].size() < maxMobsInCurrentCell) {
                    island.location[x1][y1].add(island.location[x][y].get(i));
                    island.location[x][y].remove(i);
                    i--;
                } else if (mobsInCurrentCell instanceof Goat && island.location[x][y].size() < maxMobsInCurrentCell) {
                    island.location[x1][y1].add(island.location[x][y].get(i));
                    island.location[x][y].remove(i);
                    i--;
                } else if (mobsInCurrentCell instanceof Horse && island.location[x][y].size() < maxMobsInCurrentCell) {
                    island.location[x1][y1].add(island.location[x][y].get(i));
                    island.location[x][y].remove(i);
                    i--;
                } else if (mobsInCurrentCell instanceof Mouse && island.location[x][y].size() < maxMobsInCurrentCell) {
                    island.location[x1][y1].add(island.location[x][y].get(i));
                    island.location[x][y].remove(i);
                    i--;
                } else if (mobsInCurrentCell instanceof Rabbit && island.location[x][y].size() < maxMobsInCurrentCell) {
                    island.location[x1][y1].add(island.location[x][y].get(i));
                    island.location[x][y].remove(i);
                    i--;
                } else if (mobsInCurrentCell instanceof Sheep && island.location[x][y].size() < maxMobsInCurrentCell) {
                    island.location[x1][y1].add(island.location[x][y].get(i));
                    island.location[x][y].remove(i);
                    i--;
                }
            }
            if (island.location[x][y].size() == 0) {
                island.location[x][y] = null;
                break;
            }
        }
    }

    private synchronized void movePredator(int x, int y, Island island) {
        for (int i = 0; i < island.location[x][y].size(); i++) {
            int[] coordinates = ((Animal) island.location[x][y].get(i)).toMove(x, y, island_height, island_width);
            int x1 = coordinates[0], y1 = coordinates[1];
            if (island.location[x1][y1] == null || island.location[x1][y1].size() == 0) {
                island.location[x1][y1] = new ArrayList<Animal>();
                island.location[x1][y1].add(island.location[x][y].get(i));
                island.location[x][y].remove(i);
                i--;
            } else {
                Object mobsInCurrentCell = island.location[x1][y1].get(0);
                String classToSting = mobsInCurrentCell.getClass().getSimpleName().toLowerCase();
                int maxMobsInCurrentCell = Integer.parseInt(settings.settingsMap.get(classToSting + " maxInCell"));
                if (mobsInCurrentCell instanceof Bear && island.location[x][y].size() < maxMobsInCurrentCell) {
                    island.location[x1][y1].add(island.location[x][y].get(i));
                    island.location[x][y].remove(i);
                    i--;
                } else if (mobsInCurrentCell instanceof Eagle && island.location[x][y].size() < maxMobsInCurrentCell) {
                    island.location[x1][y1].add(island.location[x][y].get(i));
                    island.location[x][y].remove(i);
                    i--;
                } else if (mobsInCurrentCell instanceof Fox && island.location[x][y].size() < maxMobsInCurrentCell) {
                    island.location[x1][y1].add(island.location[x][y].get(i));
                    island.location[x][y].remove(i);
                    i--;
                } else if (mobsInCurrentCell instanceof Snake && island.location[x][y].size() < maxMobsInCurrentCell) {
                    island.location[x1][y1].add(island.location[x][y].get(i));
                    island.location[x][y].remove(i);
                    i--;
                } else if (mobsInCurrentCell instanceof Wolf && island.location[x][y].size() < maxMobsInCurrentCell) {
                    island.location[x1][y1].add(island.location[x][y].get(i));
                    island.location[x][y].remove(i);
                    i--;
                }
            }
            if (island.location[x][y].size() == 0) {
                island.location[x][y] = null;
                break;
            }
        }
    }
}