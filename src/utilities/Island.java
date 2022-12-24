package utilities;

import animals.Plant;
import animals.herbivores.*;
import animals.predators.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Island {
    Settings settings = new Settings();
    public ArrayList[][] location;
    public static Map<String, Integer> islandMobCount = new HashMap<>();

    public void fillAnimals() throws FileNotFoundException, InterruptedException {
        settings.getSettings();
        int island_height = Integer.parseInt(settings.settingsMap.get("island height"));
        int island_width = Integer.parseInt(settings.settingsMap.get("island width"));
        mobMapFilling();
        location = new ArrayList[island_height][island_width];
        Thread[] spawnThreads = new Thread[island_height * island_width];
        int i = 0;
        for (int x = 0; x < location.length; x++) {
            for (int y = 0; y < location[x].length; y++) {
                int finalX = x;
                int finalY = y;
                Thread thread = new Thread(() -> spawnInCell(location, finalX, finalY));
                spawnThreads[i] = thread;
                i++;
            }
        }
        for (Thread spawnThread : spawnThreads) {
            spawnThread.start();
        }
        for (Thread spawnThread : spawnThreads) {
            spawnThread.join();
        }
    }

    private void spawnInCell(ArrayList[][] location, int x, int y) {
        int plantOrMob = new Random().nextInt(4);
        if (plantOrMob == 0) {
            location[x][y] = null;
        } else if (plantOrMob == 1) {
            spawnPredator(location, x, y);
        } else if (plantOrMob == 2) {
            spawnHerbivore(location, x, y);
        } else {
            location[x][y] = new ArrayList<Plant>();
            int count = randomMobInCellCount(settings.settingsMap.get("plant maxInCell"));
            double weight = Double.parseDouble(settings.settingsMap.get("plant weight"));
            for (int k = 0; k < count; k++) {
                location[x][y].add(new Plant(weight));
                int value = islandMobCount.get("plant");
                value++;
                islandMobCount.replace("plant", value);
            }
        }
    }

    private void spawnHerbivore(ArrayList[][] location, int x, int y) {
        double weight;
        double saturationFood;
        int speed;
        int count;
        int key = getRandomHerbivore();
        if (key == 0) {
            location[x][y] = new ArrayList<Boar>();
            count = randomMobInCellCount(settings.settingsMap.get("boar maxInCell"));
            weight = Double.parseDouble(settings.settingsMap.get("boar weight"));
            saturationFood = Double.parseDouble(settings.settingsMap.get("boar saturationFood"));
            speed = Integer.parseInt(settings.settingsMap.get("boar maxCellSpeed"));
            for (int k = 0; k < count; k++) {
                location[x][y].add(new Boar(weight, saturationFood, speed));
                int value = islandMobCount.get("boar");
                value++;
                islandMobCount.replace("boar", value);
            }

        } else if (key == 1) {
            location[x][y] = new ArrayList<Buffalo>();
            count = randomMobInCellCount(settings.settingsMap.get("buffalo maxInCell"));
            weight = Double.parseDouble(settings.settingsMap.get("buffalo weight"));
            saturationFood = Double.parseDouble(settings.settingsMap.get("buffalo saturationFood"));
            speed = Integer.parseInt(settings.settingsMap.get("buffalo maxCellSpeed"));
            for (int k = 0; k < count; k++) {
                location[x][y].add(new Buffalo(weight, saturationFood, speed));
                int value = islandMobCount.get("buffalo");
                value++;
                islandMobCount.replace("buffalo", value);
            }
        } else if (key == 2) {
            location[x][y] = new ArrayList<Caterpillar>();
            count = randomMobInCellCount(settings.settingsMap.get("caterpillar maxInCell"));
            weight = Double.parseDouble(settings.settingsMap.get("caterpillar weight"));
            saturationFood = Double.parseDouble(settings.settingsMap.get("caterpillar saturationFood"));
            speed = Integer.parseInt(settings.settingsMap.get("caterpillar maxCellSpeed"));
            for (int k = 0; k < count; k++) {
                location[x][y].add(new Caterpillar(weight, saturationFood, speed));
                int value = islandMobCount.get("caterpillar");
                value++;
                islandMobCount.replace("caterpillar", value);
            }
        } else if (key == 3) {
            location[x][y] = new ArrayList<Deer>();
            count = randomMobInCellCount(settings.settingsMap.get("deer maxInCell"));
            weight = Double.parseDouble(settings.settingsMap.get("deer weight"));
            saturationFood = Double.parseDouble(settings.settingsMap.get("deer saturationFood"));
            speed = Integer.parseInt(settings.settingsMap.get("deer maxCellSpeed"));
            for (int k = 0; k < count; k++) {
                location[x][y].add(new Deer(weight, saturationFood, speed));
                int value = islandMobCount.get("deer");
                value++;
                islandMobCount.replace("deer", value);
            }
        } else if (key == 4) {
            location[x][y] = new ArrayList<Duck>();
            count = randomMobInCellCount(settings.settingsMap.get("duck maxInCell"));
            weight = Double.parseDouble(settings.settingsMap.get("duck weight"));
            saturationFood = Double.parseDouble(settings.settingsMap.get("duck saturationFood"));
            speed = Integer.parseInt(settings.settingsMap.get("duck maxCellSpeed"));
            for (int k = 0; k < count; k++) {
                location[x][y].add(new Duck(weight, saturationFood, speed));
                int value = islandMobCount.get("duck");
                value++;
                islandMobCount.replace("duck", value);
            }
        } else if (key == 5) {
            location[x][y] = new ArrayList<Goat>();
            count = randomMobInCellCount(settings.settingsMap.get("goat maxInCell"));
            weight = Double.parseDouble(settings.settingsMap.get("goat weight"));
            saturationFood = Double.parseDouble(settings.settingsMap.get("goat saturationFood"));
            speed = Integer.parseInt(settings.settingsMap.get("goat maxCellSpeed"));
            for (int k = 0; k < count; k++) {
                location[x][y].add(new Goat(weight, saturationFood, speed));
                int value = islandMobCount.get("goat");
                value++;
                islandMobCount.replace("goat", value);
            }

        } else if (key == 6) {
            location[x][y] = new ArrayList<Horse>();
            count = randomMobInCellCount(settings.settingsMap.get("horse maxInCell"));
            weight = Double.parseDouble(settings.settingsMap.get("horse weight"));
            saturationFood = Double.parseDouble(settings.settingsMap.get("horse saturationFood"));
            speed = Integer.parseInt(settings.settingsMap.get("horse maxCellSpeed"));
            for (int k = 0; k < count; k++) {
                location[x][y].add(new Horse(weight, saturationFood, speed));
                int value = islandMobCount.get("horse");
                value++;
                islandMobCount.replace("horse", value);
            }
        } else if (key == 7) {
            location[x][y] = new ArrayList<Mouse>();
            count = randomMobInCellCount(settings.settingsMap.get("mouse maxInCell"));
            weight = Double.parseDouble(settings.settingsMap.get("mouse weight"));
            saturationFood = Double.parseDouble(settings.settingsMap.get("mouse saturationFood"));
            speed = Integer.parseInt(settings.settingsMap.get("mouse maxCellSpeed"));
            for (int k = 0; k < count; k++) {
                location[x][y].add(new Mouse(weight, saturationFood, speed));
                int value = islandMobCount.get("mouse");
                value++;
                islandMobCount.replace("mouse", value);
            }
        } else if (key == 8) {
            location[x][y] = new ArrayList<Rabbit>();
            count = randomMobInCellCount(settings.settingsMap.get("rabbit maxInCell"));
            weight = Double.parseDouble(settings.settingsMap.get("rabbit weight"));
            saturationFood = Double.parseDouble(settings.settingsMap.get("rabbit saturationFood"));
            speed = Integer.parseInt(settings.settingsMap.get("rabbit maxCellSpeed"));
            for (int k = 0; k < count; k++) {
                location[x][y].add(new Rabbit(weight, saturationFood, speed));
                int value = islandMobCount.get("rabbit");
                value++;
                islandMobCount.replace("rabbit", value);
            }
        } else if (key == 9) {
            location[x][y] = new ArrayList<Sheep>();
            count = randomMobInCellCount(settings.settingsMap.get("sheep maxInCell"));
            weight = Double.parseDouble(settings.settingsMap.get("sheep weight"));
            saturationFood = Double.parseDouble(settings.settingsMap.get("sheep saturationFood"));
            speed = Integer.parseInt(settings.settingsMap.get("sheep maxCellSpeed"));
            for (int k = 0; k < count; k++) {
                location[x][y].add(new Sheep(weight, saturationFood, speed));
                int value = islandMobCount.get("sheep");
                value++;
                islandMobCount.replace("sheep", value);
            }
        }
    }

    private void spawnPredator(ArrayList[][] location, int x, int y) {
        double weight;
        double saturationFood;
        int speed;
        int count;
        int key = getRandomPredator();
        if (key == 0) {
            location[x][y] = new ArrayList<Bear>();
            count = randomMobInCellCount(settings.settingsMap.get("bear maxInCell"));
            weight = Double.parseDouble(settings.settingsMap.get("bear weight"));
            speed = Integer.parseInt(settings.settingsMap.get("bear maxCellSpeed"));
            saturationFood = Double.parseDouble(settings.settingsMap.get("bear saturationFood"));
            for (int k = 0; k < count; k++) {
                location[x][y].add(new Bear(weight, saturationFood, speed));
                int value = islandMobCount.get("bear");
                value++;
                islandMobCount.replace("bear", value);
            }

        }
        if (key == 1) {
            location[x][y] = new ArrayList<Eagle>();
            count = randomMobInCellCount(settings.settingsMap.get("eagle maxInCell"));
            weight = Double.parseDouble(settings.settingsMap.get("eagle weight"));
            saturationFood = Double.parseDouble(settings.settingsMap.get("eagle saturationFood"));
            speed = Integer.parseInt(settings.settingsMap.get("eagle maxCellSpeed"));
            for (int k = 0; k < count; k++) {
                location[x][y].add(new Eagle(weight, saturationFood, speed));
                int value = islandMobCount.get("eagle");
                value++;
                islandMobCount.replace("eagle", value);
            }
        }
        if (key == 2) {
            location[x][y] = new ArrayList<Fox>();
            count = randomMobInCellCount(settings.settingsMap.get("fox maxInCell"));
            weight = Double.parseDouble(settings.settingsMap.get("fox weight"));
            saturationFood = Double.parseDouble(settings.settingsMap.get("fox saturationFood"));
            speed = Integer.parseInt(settings.settingsMap.get("fox maxCellSpeed"));
            for (int k = 0; k < count; k++) {
                location[x][y].add(new Fox(weight, saturationFood, speed));
                int value = islandMobCount.get("fox");
                value++;
                islandMobCount.replace("fox", value);
            }

        }
        if (key == 3) {
            location[x][y] = new ArrayList<Snake>();
            count = randomMobInCellCount(settings.settingsMap.get("snake maxInCell"));
            weight = Double.parseDouble(settings.settingsMap.get("snake weight"));
            saturationFood = Double.parseDouble(settings.settingsMap.get("snake saturationFood"));
            speed = Integer.parseInt(settings.settingsMap.get("snake maxCellSpeed"));
            for (int k = 0; k < count; k++) {
                location[x][y].add(new Snake(weight, saturationFood, speed));
                int value = islandMobCount.get("snake");
                value++;
                islandMobCount.replace("snake", value);
            }
        }
        if (key == 4) {
            location[x][y] = new ArrayList<Wolf>();
            count = randomMobInCellCount(settings.settingsMap.get("wolf maxInCell"));
            weight = Double.parseDouble(settings.settingsMap.get("wolf weight"));
            saturationFood = Double.parseDouble(settings.settingsMap.get("wolf saturationFood"));
            speed = Integer.parseInt(settings.settingsMap.get("wolf maxCellSpeed"));
            for (int k = 0; k < count; k++) {
                location[x][y].add(new Wolf(weight, saturationFood, speed));
                int value = islandMobCount.get("wolf");
                value++;
                islandMobCount.replace("wolf", value);
            }

        }
    }


    public void showIsland() {
//        for (Map.Entry<String, Integer> pair : islandMobCount.entrySet()) {
//            String key = pair.getKey();
//            int value = pair.getValue();
//            System.out.println(settings.settingsMap.get(key + " image") + " " + value);
//        }
        for (ArrayList[] arrayLists : location) {
            for (ArrayList arrayList : arrayLists) {
                if (arrayList == null || arrayList.size() == 0) {
                    System.out.print("      |");
                } else {
                    String image = settings.settingsMap.get(arrayList.get(0).getClass().getSimpleName().toLowerCase() + " image");
                    System.out.print(image + arrayList.size() + "|");
                }
            }
            System.out.println();
        }
    }

    private static int randomMobInCellCount(String maxInCell) {
        return new Random().nextInt(Integer.parseInt(maxInCell)) + 1;
    }

    private static int getRandomHerbivore() {
        return new Random().nextInt(10);
    }

    private static int getRandomPredator() {
        return new Random().nextInt(5);
    }

    private static void mobMapFilling() {
        islandMobCount.put("bear", 0);
        islandMobCount.put("eagle", 0);
        islandMobCount.put("fox", 0);
        islandMobCount.put("snake", 0);
        islandMobCount.put("wolf", 0);
        islandMobCount.put("boar", 0);
        islandMobCount.put("buffalo", 0);
        islandMobCount.put("caterpillar", 0);
        islandMobCount.put("deer", 0);
        islandMobCount.put("duck", 0);
        islandMobCount.put("goat", 0);
        islandMobCount.put("horse", 0);
        islandMobCount.put("mouse", 0);
        islandMobCount.put("rabbit", 0);
        islandMobCount.put("sheep", 0);
        islandMobCount.put("plant", 0);
    }
}
