import utilities.Island;
import utilities.Simulation;
import utilities.Settings;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Main {

    public static int width, height;

    public static void main(String[] args) throws IOException, InterruptedException {
        Island island = new Island();
        Settings settings = new Settings();
        settings.getSettings();
        getIslandSize(settings);
        if (height == 0 || width == 0) {
            System.out.println("The island`s size cant be 0");
            System.exit(0);
        }
        island.fillAnimals();
        System.out.println("How to display statistics on the island? (default 1)");
        System.out.println("1. Show how many entities.animals are on the island in total");
        System.out.println("2. Show statistics for each island cell");
        island.howToShow = new Scanner(System.in).nextInt();
        island.showIsland();
        System.out.println("Type 'go' for start simulation");
        String command = new Scanner(System.in).nextLine();
        while (!command.equals("go")) {
            System.out.println("Type 'go' for start simulation");
            command = new Scanner(System.in).nextLine();
        }
        System.out.println("how much steps do you need?");
        int stepsCount = new Scanner(System.in).nextInt();
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);
        Simulation simulation = new Simulation(island, settings);
        executorService.scheduleAtFixedRate(simulation, 0, 5, TimeUnit.SECONDS);
        simulation.stepsCount = stepsCount;
        while (simulation.steps != stepsCount) {
            Thread.sleep(1000);
        }
        executorService.shutdown();
    }


    private static void getIslandSize(Settings settings) {
        height = Integer.parseInt(settings.settingsMap.get("island height"));
        width = Integer.parseInt(settings.settingsMap.get("island width"));
    }

}