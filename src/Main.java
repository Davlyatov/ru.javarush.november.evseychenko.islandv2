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
        island.showIsland();
        System.out.println("Type 'go' for start simulation");
        String command = new Scanner(System.in).nextLine();
        while (!command.equals("go")) {
            System.out.println("Type 'go' for start simulation");
            command = new Scanner(System.in).nextLine();
        }
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);
        executorService.scheduleAtFixedRate(new Simulation(island, settings), 0, 3, TimeUnit.SECONDS);
        Thread.sleep(60000);
        executorService.shutdown();
        island.showIsland();
    }


    private static void getIslandSize(Settings settings) {
        height = Integer.parseInt(settings.settingsMap.get("island height"));
        width = Integer.parseInt(settings.settingsMap.get("island width"));
    }

}