package utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Settings {
    public Map<String, String> settingsMap = new HashMap<>();
    File file = new File("src/utilities/settings.txt");

    public void getSettings() throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileReader(file));
        while (scanner.hasNextLine()) {
            String[] tempLine = scanner.nextLine().split(" ");
            if (tempLine.length > 1) {
                settingsMap.put(tempLine[0] + " " + tempLine[1], tempLine[2]);
            }
        }
        scanner.close();
//        for (String key : settingsMap.keySet()) {
//            System.out.println(key+" "+settingsMap.get(key));
//        }
    }
}
