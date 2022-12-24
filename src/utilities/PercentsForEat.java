package utilities;

import java.util.HashMap;
import java.util.Map;

public class PercentsForEat {

    public final Map<String, Map<String, Integer>> eatPercents = new HashMap<>();

    {
        //Проценты поедания для волка
        Map<String, Integer> wolfMap = new HashMap<>();
        wolfMap.put("snake", 0);
        wolfMap.put("fox", 0);
        wolfMap.put("bear", 0);
        wolfMap.put("eagle", 0);
        wolfMap.put("horse", 10);
        wolfMap.put("deer", 15);
        wolfMap.put("rabbit", 60);
        wolfMap.put("mouse", 80);
        wolfMap.put("goat", 60);
        wolfMap.put("sheep", 70);
        wolfMap.put("boar", 15);
        wolfMap.put("buffalo", 10);
        wolfMap.put("duck", 40);
        wolfMap.put("caterpillar", 0);
        wolfMap.put("plant", 0);
        eatPercents.put("wolf", wolfMap);

        //Проценты поедания для удава
        Map<String, Integer> snakeMap = new HashMap<>();
        snakeMap.put("wolf", 0);
        snakeMap.put("fox", 15);
        snakeMap.put("bear", 0);
        snakeMap.put("eagle", 0);
        snakeMap.put("horse", 0);
        snakeMap.put("deer", 0);
        snakeMap.put("rabbit", 20);
        snakeMap.put("mouse", 40);
        snakeMap.put("goat", 0);
        snakeMap.put("sheep", 0);
        snakeMap.put("boar", 0);
        snakeMap.put("buffalo", 0);
        snakeMap.put("duck", 10);
        snakeMap.put("caterpillar", 0);
        snakeMap.put("plant", 0);
        eatPercents.put("snake", snakeMap);

        //Проценты поедания для лисы
        Map<String, Integer> foxMap = new HashMap<>();
        foxMap.put("wolf", 0);
        foxMap.put("snake", 0);
        foxMap.put("bear", 0);
        foxMap.put("eagle", 0);
        foxMap.put("horse", 0);
        foxMap.put("deer", 0);
        foxMap.put("rabbit", 70);
        foxMap.put("mouse", 90);
        foxMap.put("goat", 0);
        foxMap.put("sheep", 0);
        foxMap.put("boar", 0);
        foxMap.put("buffalo", 0);
        foxMap.put("duck", 60);
        foxMap.put("caterpillar", 40);
        foxMap.put("plant", 0);
        eatPercents.put("fox", foxMap);

        //Проценты поедания для медведя
        Map<String, Integer> bearMap = new HashMap<>();
        bearMap.put("wolf", 0);
        bearMap.put("snake", 80);
        bearMap.put("fox", 0);
        bearMap.put("eagle", 0);
        bearMap.put("horse", 40);
        bearMap.put("deer", 80);
        bearMap.put("rabbit", 80);
        bearMap.put("mouse", 90);
        bearMap.put("goat", 70);
        bearMap.put("sheep", 70);
        bearMap.put("boar", 50);
        bearMap.put("buffalo", 20);
        bearMap.put("duck", 10);
        bearMap.put("caterpillar", 0);
        bearMap.put("plant", 0);
        eatPercents.put("bear", bearMap);

        //Проценты поедания для орла
        Map<String, Integer> eagleMap = new HashMap<>();
        eagleMap.put("wolf", 0);
        eagleMap.put("snake", 0);
        eagleMap.put("fox", 10);
        eagleMap.put("bear", 0);
        eagleMap.put("horse", 0);
        eagleMap.put("deer", 0);
        eagleMap.put("rabbit", 90);
        eagleMap.put("mouse", 90);
        eagleMap.put("goat", 0);
        eagleMap.put("sheep", 0);
        eagleMap.put("boar", 0);
        eagleMap.put("buffalo", 0);
        eagleMap.put("duck", 80);
        eagleMap.put("caterpillar", 0);
        eagleMap.put("plant", 0);
        eatPercents.put("eagle", eagleMap);

        //Проценты поедания для лошади
        Map<String, Integer> horseMap = new HashMap<>();
        horseMap.put("wolf", 0);
        horseMap.put("snake", 0);
        horseMap.put("fox", 0);
        horseMap.put("bear", 0);
        horseMap.put("eagle", 0);
        horseMap.put("deer", 0);
        horseMap.put("rabbit", 0);
        horseMap.put("mouse", 0);
        horseMap.put("goat", 0);
        horseMap.put("sheep", 0);
        horseMap.put("boar", 0);
        horseMap.put("buffalo", 0);
        horseMap.put("duck", 0);
        horseMap.put("caterpillar", 0);
        horseMap.put("plant", 100);
        eatPercents.put("horse", horseMap);

        //Проценты поедания для оленя
        Map<String, Integer> deerMap = new HashMap<>();
        deerMap.put("wolf", 0);
        deerMap.put("snake", 0);
        deerMap.put("fox", 0);
        deerMap.put("bear", 0);
        deerMap.put("eagle", 0);
        deerMap.put("horse", 0);
        deerMap.put("rabbit", 0);
        deerMap.put("mouse", 0);
        deerMap.put("goat", 0);
        deerMap.put("sheep", 0);
        deerMap.put("boar", 0);
        deerMap.put("buffalo", 0);
        deerMap.put("duck", 0);
        deerMap.put("caterpillar", 0);
        deerMap.put("plant", 100);
        eatPercents.put("deer", deerMap);

        //Проценты поедания для кролика
        Map<String, Integer> rabbitMap = new HashMap<>();
        rabbitMap.put("wolf", 0);
        rabbitMap.put("snake", 0);
        rabbitMap.put("fox", 0);
        rabbitMap.put("bear", 0);
        rabbitMap.put("eagle", 0);
        rabbitMap.put("horse", 0);
        rabbitMap.put("deer", 0);
        rabbitMap.put("mouse", 0);
        rabbitMap.put("goat", 0);
        rabbitMap.put("sheep", 0);
        rabbitMap.put("boar", 0);
        rabbitMap.put("buffalo", 0);
        rabbitMap.put("duck", 0);
        rabbitMap.put("caterpillar", 0);
        rabbitMap.put("plant", 100);
        eatPercents.put("rabbit", rabbitMap);

        //Проценты поедания для мыши
        Map<String, Integer> mouseMap = new HashMap<>();
        mouseMap.put("wolf", 0);
        mouseMap.put("snake", 0);
        mouseMap.put("fox", 0);
        mouseMap.put("bear", 0);
        mouseMap.put("eagle", 0);
        mouseMap.put("horse", 0);
        mouseMap.put("deer", 0);
        mouseMap.put("rabbit", 0);
        mouseMap.put("goat", 0);
        mouseMap.put("sheep", 0);
        mouseMap.put("boar", 0);
        mouseMap.put("buffalo", 0);
        mouseMap.put("duck", 0);
        mouseMap.put("caterpillar", 90);
        mouseMap.put("plant", 100);
        eatPercents.put("mouse", mouseMap);

        //Проценты поедания для козы
        Map<String, Integer> goatMap = new HashMap<>();
        goatMap.put("wolf", 0);
        goatMap.put("snake", 0);
        goatMap.put("fox", 0);
        goatMap.put("bear", 0);
        goatMap.put("eagle", 0);
        goatMap.put("horse", 0);
        goatMap.put("deer", 0);
        goatMap.put("rabbit", 0);
        goatMap.put("mouse", 0);
        goatMap.put("sheep", 0);
        goatMap.put("boar", 0);
        goatMap.put("buffalo", 0);
        goatMap.put("duck", 0);
        goatMap.put("caterpillar", 0);
        goatMap.put("plant", 100);
        eatPercents.put("goat", goatMap);

        //Проценты поедания для овцы
        Map<String, Integer> sheepMap = new HashMap<>();
        sheepMap.put("wolf", 0);
        sheepMap.put("snake", 0);
        sheepMap.put("fox", 0);
        sheepMap.put("bear", 0);
        sheepMap.put("eagle", 0);
        sheepMap.put("horse", 0);
        sheepMap.put("deer", 0);
        sheepMap.put("rabbit", 0);
        sheepMap.put("mouse", 0);
        sheepMap.put("goat", 0);
        sheepMap.put("boar", 0);
        sheepMap.put("buffalo", 0);
        sheepMap.put("duck", 0);
        sheepMap.put("caterpillar", 0);
        sheepMap.put("plant", 100);
        eatPercents.put("sheep", sheepMap);

        //Проценты поедания для кабана
        Map<String, Integer> boarMap = new HashMap<>();
        boarMap.put("wolf", 0);
        boarMap.put("snake", 0);
        boarMap.put("fox", 0);
        boarMap.put("bear", 0);
        boarMap.put("eagle", 0);
        boarMap.put("horse", 0);
        boarMap.put("deer", 0);
        boarMap.put("rabbit", 0);
        boarMap.put("mouse", 50);
        boarMap.put("goat", 0);
        boarMap.put("sheep", 0);
        boarMap.put("buffalo", 0);
        boarMap.put("duck", 0);
        boarMap.put("caterpillar", 90);
        boarMap.put("plant", 100);
        eatPercents.put("boar", boarMap);

        //Проценты поедания для буйвола
        Map<String, Integer> buffaloMap = new HashMap<>();
        buffaloMap.put("wolf", 0);
        buffaloMap.put("snake", 0);
        buffaloMap.put("fox", 0);
        buffaloMap.put("bear", 0);
        buffaloMap.put("eagle", 0);
        buffaloMap.put("horse", 0);
        buffaloMap.put("deer", 0);
        buffaloMap.put("rabbit", 0);
        buffaloMap.put("mouse", 0);
        buffaloMap.put("goat", 0);
        buffaloMap.put("sheep", 0);
        buffaloMap.put("boar", 0);
        buffaloMap.put("duck", 0);
        buffaloMap.put("caterpillar", 0);
        buffaloMap.put("plant", 100);
        eatPercents.put("buffalo", buffaloMap);

        //Проценты поедания для утки
        Map<String, Integer> duckMap = new HashMap<>();
        duckMap.put("wolf", 0);
        duckMap.put("snake", 0);
        duckMap.put("fox", 0);
        duckMap.put("bear", 0);
        duckMap.put("eagle", 0);
        duckMap.put("horse", 0);
        duckMap.put("deer", 0);
        duckMap.put("rabbit", 0);
        duckMap.put("mouse", 0);
        duckMap.put("goat", 0);
        duckMap.put("sheep", 0);
        duckMap.put("boar", 0);
        duckMap.put("buffalo", 0);
        duckMap.put("caterpillar", 90);
        duckMap.put("plant", 100);
        eatPercents.put("duck", duckMap);

        //Проценты поедания для гусеницы
        Map<String, Integer> caterpillarMap = new HashMap<>();
        caterpillarMap.put("wolf", 0);
        caterpillarMap.put("snake", 0);
        caterpillarMap.put("fox", 0);
        caterpillarMap.put("bear", 0);
        caterpillarMap.put("eagle", 0);
        caterpillarMap.put("horse", 0);
        caterpillarMap.put("deer", 0);
        caterpillarMap.put("rabbit", 0);
        caterpillarMap.put("mouse", 0);
        caterpillarMap.put("goat", 0);
        caterpillarMap.put("sheep", 0);
        caterpillarMap.put("boar", 0);
        caterpillarMap.put("buffalo", 0);
        caterpillarMap.put("duck", 0);
        caterpillarMap.put("plant", 100);
        eatPercents.put("caterpillar", caterpillarMap);
    }

    public int getPercent(String eater, String victim) {
        if (eatPercents.containsKey(eater)&&eatPercents.get(eater).containsKey(victim)){
            return eatPercents.get(eater).get(victim);
        }
        return -1;
    }
}
