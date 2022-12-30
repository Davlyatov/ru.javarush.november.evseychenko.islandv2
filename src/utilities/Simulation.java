package utilities;

import entities.animals.Animal;
import entities.animals.Herbivore;
import entities.Plant;
import entities.animals.Predator;
import entities.animals.predators.*;
import entities.animals.herbivores.*;

import java.util.ArrayList;
import java.util.Random;

public class Simulation implements Runnable {

    Island island;
    Settings settings;

    int island_height;
    int island_width;
    int threadPoolSize;
    public int steps = 0;
    public int stepsCount = 0;
    int percentsAfterMove, percentsAfterMultiply;


    public Simulation(Island island, Settings settings) {
        this.island = island;
        this.settings = settings;
        island_height = Integer.parseInt(settings.settingsMap.get("island height"));
        island_width = Integer.parseInt(settings.settingsMap.get("island width"));
        threadPoolSize = island_height * island_width;
        percentsAfterMove = Integer.parseInt(settings.settingsMap.get("percents afterMove"));
        percentsAfterMultiply = Integer.parseInt(settings.settingsMap.get("percents afterMultiply"));
    }

    @Override
    public void run() {
        if (steps != stepsCount) {
            try {
                System.out.println("Step #" + (steps + 1));
                createMoveThreads(island);
                createSatietyThreads(island, settings, percentsAfterMove);
                createFeedThreads(island, settings);
                createMultiplyThreads(island, settings);
                createSatietyThreads(island, settings, percentsAfterMultiply);
                createGrowUpThreads(island);
                island.showIsland();
                steps++;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void createGrowUpThreads(Island island) throws InterruptedException {
        Thread[] growUpThreads = new Thread[island_height * island_width];
        int i = 0;
        for (int x = 0; x < island.location.length; x++) {
            for (int y = 0; y < island.location[x].length; y++) {
                int finalX = x;
                int finalY = y;
                Thread thread = new Thread(() -> growUp(finalX, finalY, island));
                growUpThreads[i] = thread;
                i++;
            }
        }
        for (Thread growUpThread : growUpThreads) {
            growUpThread.start();
        }
        for (Thread growUpThread : growUpThreads) {
            growUpThread.join();
        }
    }

    private void growUp(int x, int y, Island island) {
        if (island.location[x][y] != null && island.location[x][y].size() != 0) {
            for (int i = 0; i < island.location[x][y].size(); i++) {
                if (island.location[x][y].get(i) instanceof Plant) {
                    ((Plant) island.location[x][y].get(i)).growUp();
                } else {
                    ((Animal) island.location[x][y].get(i)).growUp();
                }
            }
        }
    }

    private void createMultiplyThreads(Island island, Settings settings) throws InterruptedException {
        Thread[] multiplyThreads = new Thread[island_height * island_width];
        int i = 0;
        for (int x = 0; x < island.location.length; x++) {
            for (int y = 0; y < island.location[x].length; y++) {
                int finalX = x;
                int finalY = y;
                Thread thread = new Thread(() -> multiplyEntity(finalX, finalY, island, settings));
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

    private void multiplyEntity(int x, int y, Island island, Settings settings) {
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
        int maxInCell = Integer.parseInt(settings.settingsMap.get("plant maxInCell"));
        int multiplyChance = 10;
        if (new Random().nextInt() < multiplyChance) {
            island.location[x][y] = new ArrayList<Plant>();
            int randomCount = new Random().nextInt(maxInCell) + 1;
            for (int i = 0; i < randomCount; i++) {
                island.location[x][y].add(new Plant(true));
                int count = Island.islandMobCount.get("plant");
                count++;
                Island.islandMobCount.replace("plant", count);
            }
        }
    }

    private void multiplyPlant(Island island, int x, int y, Settings settings) {
        String mobClassToString = island.location[x][y].get(0).getClass().getSimpleName().toLowerCase();
        int maxInCell = Integer.parseInt(settings.settingsMap.get(mobClassToString + " maxInCell"));
        int availableMultiplies = maxInCell - island.location[x][y].size();
        int multiplyChance = 40;
        for (int i = 0; i < availableMultiplies; i++) {
            if (new Random().nextInt(100) > multiplyChance) {
                island.location[x][y].add(new Plant(true));
                int count = Island.islandMobCount.get("plant");
                count++;
                Island.islandMobCount.replace("plant", count);
            }
        }
    }

    private void multiplyHerbivores(Island island, int x, int y, Settings settings) {
        Object currentMobInCell = island.location[x][y].get(0);
        String mobClassToString = currentMobInCell.getClass().getSimpleName().toLowerCase();
        int maxInCell = Integer.parseInt(settings.settingsMap.get(mobClassToString + " maxInCell"));
        int availableMultiplies = island.location[x][y].size() / 2;
        double weight = Double.parseDouble(settings.settingsMap.get(mobClassToString + " weight"));
        double saturationFood = Double.parseDouble(settings.settingsMap.get(mobClassToString + " saturationFood"));
        int speed = Integer.parseInt(settings.settingsMap.get(mobClassToString + " maxCellSpeed"));
        for (int i = 0; i < availableMultiplies; i++) {
            if (currentMobInCell instanceof Boar) {
                if (island.location[x][y].size() < maxInCell && ((Boar) currentMobInCell).weight * 2 >= weight) {
                    island.location[x][y].add(new Boar(weight, saturationFood, speed, true));
                    int count = Island.islandMobCount.get("boar");
                    count++;
                    Island.islandMobCount.replace("boar", count);
                }
            } else if (currentMobInCell instanceof Buffalo) {
                if (island.location[x][y].size() < maxInCell && ((Buffalo) currentMobInCell).weight * 2 >= weight) {
                    island.location[x][y].add(new Buffalo(weight, saturationFood, speed, true));
                    int count = Island.islandMobCount.get("buffalo");
                    count++;
                    Island.islandMobCount.replace("buffalo", count);
                }
            } else if (currentMobInCell instanceof Caterpillar) {
                if (island.location[x][y].size() < maxInCell) {
                    island.location[x][y].add(new Caterpillar(weight, saturationFood, speed, true));
                    int count = Island.islandMobCount.get("caterpillar");
                    count++;
                    Island.islandMobCount.replace("caterpillar", count);
                }
            } else if (currentMobInCell instanceof Deer) {
                if (island.location[x][y].size() < maxInCell && ((Deer) currentMobInCell).weight * 2 >= weight) {
                    island.location[x][y].add(new Deer(weight, saturationFood, speed, true));
                    int count = Island.islandMobCount.get("deer");
                    count++;
                    Island.islandMobCount.replace("deer", count);
                }
            } else if (currentMobInCell instanceof Duck) {
                if (island.location[x][y].size() < maxInCell && ((Duck) currentMobInCell).weight * 2 >= weight) {
                    island.location[x][y].add(new Duck(weight, saturationFood, speed, true));
                    int count = Island.islandMobCount.get("duck");
                    count++;
                    Island.islandMobCount.replace("duck", count);
                }
            } else if (currentMobInCell instanceof Goat) {
                if (island.location[x][y].size() < maxInCell && ((Goat) currentMobInCell).weight * 2 >= weight) {
                    island.location[x][y].add(new Goat(weight, saturationFood, speed, true));
                    int count = Island.islandMobCount.get("goat");
                    count++;
                    Island.islandMobCount.replace("goat", count);
                }
            } else if (currentMobInCell instanceof Horse) {
                if (island.location[x][y].size() < maxInCell && ((Horse) currentMobInCell).weight * 2 >= weight) {
                    island.location[x][y].add(new Horse(weight, saturationFood, speed, true));
                    int count = Island.islandMobCount.get("horse");
                    count++;
                    Island.islandMobCount.replace("horse", count);
                }
            } else if (currentMobInCell instanceof Mouse) {
                if (island.location[x][y].size() < maxInCell && ((Mouse) currentMobInCell).weight * 2 >= weight) {
                    island.location[x][y].add(new Mouse(weight, saturationFood, speed, true));
                    int count = Island.islandMobCount.get("mouse");
                    count++;
                    Island.islandMobCount.replace("mouse", count);
                }
            } else if (currentMobInCell instanceof Rabbit) {
                if (island.location[x][y].size() < maxInCell && ((Rabbit) currentMobInCell).weight * 2 >= weight) {
                    island.location[x][y].add(new Rabbit(weight, saturationFood, speed, true));
                    int count = Island.islandMobCount.get("rabbit");
                    count++;
                    Island.islandMobCount.replace("rabbit", count);
                }
            } else if (currentMobInCell instanceof Sheep) {
                if (island.location[x][y].size() < maxInCell && ((Sheep) currentMobInCell).weight * 2 >= weight) {
                    island.location[x][y].add(new Sheep(weight, saturationFood, speed, true));
                    int count = Island.islandMobCount.get("sheep");
                    count++;
                    Island.islandMobCount.replace("sheep", count);
                }
            }
        }
    }

    private void multiplyPredators(Island island, int x, int y, Settings settings) {
        Object currentMobInCell = island.location[x][y].get(0);
        String mobClassToString = currentMobInCell.getClass().getSimpleName().toLowerCase();
        int maxInCell = Integer.parseInt(settings.settingsMap.get(mobClassToString + " maxInCell"));
        int availableMultiplies = island.location[x][y].size() / 2;
        double weight = Double.parseDouble(settings.settingsMap.get(mobClassToString + " weight"));
        double saturationFood = Double.parseDouble(settings.settingsMap.get(mobClassToString + " saturationFood"));
        int speed = Integer.parseInt(settings.settingsMap.get(mobClassToString + " maxCellSpeed"));
        for (int i = 0; i < availableMultiplies; i++) {
            if (currentMobInCell instanceof Bear) {
                if (island.location[x][y].size() < maxInCell && ((Bear) currentMobInCell).weight * 2 >= weight) {
                    island.location[x][y].add(new Bear(weight, saturationFood, speed, true));
                    int count = Island.islandMobCount.get("bear");
                    count++;
                    Island.islandMobCount.replace("bear", count);
                }
            } else if (currentMobInCell instanceof Eagle) {
                if (island.location[x][y].size() < maxInCell && ((Eagle) currentMobInCell).weight * 2 >= weight) {
                    island.location[x][y].add(new Eagle(weight, saturationFood, speed, true));
                    int count = Island.islandMobCount.get("eagle");
                    count++;
                    Island.islandMobCount.replace("eagle", count);
                }
            } else if (currentMobInCell instanceof Fox) {
                if (island.location[x][y].size() < maxInCell && ((Fox) currentMobInCell).weight * 2 >= weight) {
                    island.location[x][y].add(new Fox(weight, saturationFood, speed, true));
                    int count = Island.islandMobCount.get("fox");
                    count++;
                    Island.islandMobCount.replace("fox", count);
                }
            } else if (currentMobInCell instanceof Snake) {
                if (island.location[x][y].size() < maxInCell && ((Snake) currentMobInCell).weight * 2 >= weight) {
                    island.location[x][y].add(new Snake(weight, saturationFood, speed, true));
                    int count = Island.islandMobCount.get("snake");
                    count++;
                    Island.islandMobCount.replace("snake", count);
                }
            } else if (currentMobInCell instanceof Wolf) {
                if (island.location[x][y].size() < maxInCell && ((Wolf) currentMobInCell).weight * 2 >= weight) {
                    island.location[x][y].add(new Wolf(weight, saturationFood, speed, true));
                    int count = Island.islandMobCount.get("wolf");
                    count++;
                    Island.islandMobCount.replace("wolf", count);
                }
            }
        }
    }

    private void createFeedThreads(Island island, Settings settings) throws InterruptedException {
        Thread[] feedThreads = new Thread[island_height * island_width];
        int i = 0;
        for (int x = 0; x < island.location.length; x++) {
            for (int y = 0; y < island.location[x].length; y++) {
                int finalX = x;
                int finalY = y;
                Thread thread = new Thread(() -> checkLocationForFeed(island, finalX, finalY, settings));
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

    private void checkLocationForFeed(Island island, int x, int y, Settings settings) {
        if (island.location[x][y] != null && island.location[x][y].size() > 0) {
            if (checkBorder(x + 1, y)) {
                if (!(island.location[x][y].get(0) instanceof Caterpillar) && !(island.location[x][y].get(0) instanceof Plant)) {
                    feedAnimal(x, y, x + 1, y, settings);
                }
            } else if (checkBorder(x - 1, y)) {
                if (!(island.location[x][y].get(0) instanceof Caterpillar) && !(island.location[x][y].get(0) instanceof Plant)) {
                    feedAnimal(x, y, x - 1, y, settings);
                }
            } else if (checkBorder(x, y + 1)) {
                if (!(island.location[x][y].get(0) instanceof Caterpillar) && !(island.location[x][y].get(0) instanceof Plant)) {
                    feedAnimal(x, y, x, y + 1, settings);
                }
            } else if (checkBorder(x, y - 1)) {
                if (!(island.location[x][y].get(0) instanceof Caterpillar) && !(island.location[x][y].get(0) instanceof Plant)) {
                    feedAnimal(x, y, x, y - 1, settings);
                }
            }
        }
    }

    private void feedAnimal(int x, int y, int x1, int y1, Settings settings) {
        for (int i = 0; i < island.location[x][y].size(); i++) {
            Object currentMobInCell = island.location[x][y].get(i);
            String mobClassToString = currentMobInCell.getClass().getSimpleName().toLowerCase();
            double saturationWeight = Double.parseDouble(settings.settingsMap.get(mobClassToString + " saturationFood"));
            if (island.location[x1][y1] != null) {
                for (int j = 0; j < island.location[x1][y1].size(); j++) {
                    boolean isCanEat = ((Animal) currentMobInCell).toEat(island.location[x1][y1].get(j), settings.settingsMap);
                    if (((Animal) currentMobInCell).isChild && isCanEat) {
                        if (island.location[x1][y1].get(j) instanceof Plant && !((Plant) island.location[x1][y1].get(j)).isSeed) {
                            if (saturationWeight > ((Plant) island.location[x1][y1].get(j)).weight) {
                                ((Animal) currentMobInCell).weight += ((Plant) island.location[x1][y1].get(j)).weight;
                            } else {
                                ((Animal) currentMobInCell).weight += ((Animal) currentMobInCell).saturationWeight;
                            }
                        } else if (island.location[x1][y1].get(j) instanceof Animal) {
                            if (saturationWeight > ((Animal) island.location[x1][y1].get(j)).weight) {
                                ((Animal) currentMobInCell).weight += ((Animal) island.location[x1][y1].get(j)).weight;
                            } else {
                                ((Animal) currentMobInCell).weight += ((Animal) currentMobInCell).saturationWeight;
                            }
                        }
                        String key = island.location[x1][y1].get(j).getClass().getSimpleName().toLowerCase();
                        int count = Island.islandMobCount.get(key);
                        count--;
                        Island.islandMobCount.replace(key, count);
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
        return (y < island_width && x < island_height) && (x >= 0 && y >= 0);
    }

    private void createSatietyThreads(Island island, Settings settings, int percent) throws InterruptedException {
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
        if (island.location[x][y] != null && island.location[x][y].size() > 0 && !(island.location[x][y].get(0) instanceof Plant)
                && !(island.location[x][y].get(0) instanceof Caterpillar)) {
            Object currentMobInCell = island.location[x][y].get(0);
            String mobClassToString = currentMobInCell.getClass().getSimpleName().toLowerCase();
            double maxWeight = Double.parseDouble(settings.settingsMap.get(mobClassToString + " weight"));
            double starvationWeight = maxWeight / 100 * percent;
            for (int i = 0; i < island.location[x][y].size(); i++) {
                if (((Animal) island.location[x][y].get(i)).isChild) {
                    ((Animal) island.location[x][y].get(i)).weight -= starvationWeight;
                }
                if ((((Animal) island.location[x][y].get(i)).weight <= 0)) {
                    String key = island.location[x][y].get(i).getClass().getSimpleName().toLowerCase();
                    int count = Island.islandMobCount.get(key);
                    count--;
                    if (!(count < 0)) {
                        Island.islandMobCount.replace(key, count);
                    }
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

    private void createMoveThreads(Island island) throws InterruptedException {
        Thread[] moveThreads = new Thread[threadPoolSize];
        int i = 0;
        for (int x = 0; x < island.location.length; x++) {
            for (int y = 0; y < island.location[x].length; y++) {
                int finalX = x;
                int finalY = y;
                Thread thread = new Thread(() -> checkCell(finalX, finalY, island));
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

    private synchronized void checkCell(int x, int y, Island island) {
        if (island.location[x][y] != null && island.location[x][y].size() > 0
                && !(island.location[x][y].get(0) instanceof Plant) && !(island.location[x][y].get(0) instanceof Caterpillar)) {
            moveAnimal(x, y, island);
        }
    }

    private void moveAnimal(int x, int y, Island island) {
        for (int i = 0; i < island.location[x][y].size(); i++) {
            int[] coordinates = ((Animal) island.location[x][y].get(i)).toMove(x, y, island_height, island_width);
            int x1 = coordinates[0], y1 = coordinates[1];
            if (checkBorder(x1, y1)) {
                if (island.location[x1][y1] == null) {
                    island.location[x1][y1] = new ArrayList<Animal>();
                    island.location[x1][y1].add(island.location[x][y].get(i));
                    island.location[x][y].remove(i);
                    i--;
                } else {
                    Object mobTypeInCurrentCell = island.location[x1][y1].get(0);
                    Object mobTypeInMovingCell = island.location[x][y].get(i);
                    String classToString = mobTypeInCurrentCell.getClass().getSimpleName().toLowerCase();
                    int maxMobsInCurrentCell = Integer.parseInt(settings.settingsMap.get(classToString + " maxInCell"));
                    if (mobTypeInMovingCell instanceof Bear && mobTypeInCurrentCell instanceof Bear && island.location[x1][y1].size() < maxMobsInCurrentCell) {
                        island.location[x1][y1].add(island.location[x][y].get(i));
                        island.location[x][y].remove(i);
                        i--;
                    } else if (mobTypeInMovingCell instanceof Eagle && mobTypeInCurrentCell instanceof Eagle && island.location[x1][y1].size() < maxMobsInCurrentCell) {
                        island.location[x1][y1].add(island.location[x][y].get(i));
                        island.location[x][y].remove(i);
                        i--;
                    } else if (mobTypeInMovingCell instanceof Fox && mobTypeInCurrentCell instanceof Fox && island.location[x1][y1].size() < maxMobsInCurrentCell) {
                        island.location[x1][y1].add(island.location[x][y].get(i));
                        island.location[x][y].remove(i);
                        i--;
                    } else if (mobTypeInMovingCell instanceof Snake && mobTypeInCurrentCell instanceof Snake && island.location[x1][y1].size() < maxMobsInCurrentCell) {
                        island.location[x1][y1].add(island.location[x][y].get(i));
                        island.location[x][y].remove(i);
                        i--;
                    } else if (mobTypeInMovingCell instanceof Wolf && mobTypeInCurrentCell instanceof Wolf && island.location[x1][y1].size() < maxMobsInCurrentCell) {
                        island.location[x1][y1].add(island.location[x][y].get(i));
                        island.location[x][y].remove(i);
                        i--;
                    } else if (mobTypeInMovingCell instanceof Boar && mobTypeInCurrentCell instanceof Boar && island.location[x1][y1].size() < maxMobsInCurrentCell) {
                        island.location[x1][y1].add(island.location[x][y].get(i));
                        island.location[x][y].remove(i);
                        i--;
                    } else if (mobTypeInMovingCell instanceof Buffalo && mobTypeInCurrentCell instanceof Buffalo && island.location[x1][y1].size() < maxMobsInCurrentCell) {
                        island.location[x1][y1].add(island.location[x][y].get(i));
                        island.location[x][y].remove(i);
                        i--;
                    } else if (mobTypeInMovingCell instanceof Deer && mobTypeInCurrentCell instanceof Deer && island.location[x1][y1].size() < maxMobsInCurrentCell) {
                        island.location[x1][y1].add(island.location[x][y].get(i));
                        island.location[x][y].remove(i);
                        i--;
                    } else if (mobTypeInMovingCell instanceof Duck && mobTypeInCurrentCell instanceof Duck && island.location[x1][y1].size() < maxMobsInCurrentCell) {
                        island.location[x1][y1].add(island.location[x][y].get(i));
                        island.location[x][y].remove(i);
                        i--;
                    } else if (mobTypeInMovingCell instanceof Goat && mobTypeInCurrentCell instanceof Goat && island.location[x1][y1].size() < maxMobsInCurrentCell) {
                        island.location[x1][y1].add(island.location[x][y].get(i));
                        island.location[x][y].remove(i);
                        i--;
                    } else if (mobTypeInMovingCell instanceof Horse && mobTypeInCurrentCell instanceof Horse && island.location[x1][y1].size() < maxMobsInCurrentCell) {
                        island.location[x1][y1].add(island.location[x][y].get(i));
                        island.location[x][y].remove(i);
                        i--;
                    } else if (mobTypeInMovingCell instanceof Mouse && mobTypeInCurrentCell instanceof Mouse && island.location[x1][y1].size() < maxMobsInCurrentCell) {
                        island.location[x1][y1].add(island.location[x][y].get(i));
                        island.location[x][y].remove(i);
                        i--;
                    } else if (mobTypeInMovingCell instanceof Rabbit && mobTypeInCurrentCell instanceof Rabbit && island.location[x1][y1].size() < maxMobsInCurrentCell) {
                        island.location[x1][y1].add(island.location[x][y].get(i));
                        island.location[x][y].remove(i);
                        i--;
                    } else if (mobTypeInMovingCell instanceof Sheep && mobTypeInCurrentCell instanceof Sheep && island.location[x1][y1].size() < maxMobsInCurrentCell) {
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
}
