
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(new File("b.txt"));
        // ArrayList<Long> currentStones = new ArrayList<>();
        HashMap<Long, Long> currentStones = new HashMap<>();
        while (input.hasNextLong()) { // assume input unique
            currentStones.put(input.nextLong(), 1l);
        }
        for (int i = 0; i < 75; i++) {
            HashMap<Long, Long> original = new HashMap<>(currentStones);
            original.keySet().forEach((stone) -> {
                if(original.get(stone) == 0) {
                    return;
                }

                if (stone == 0) {
                    if(currentStones.get(1l) == null) {
                        currentStones.put(1l, 0l);
                    }
                    currentStones.put(1l, currentStones.get(1l) + original.get(stone));
                } else if (Long.toString(stone).length() % 2 == 0) {
                    String stoneStr = Long.toString(stone);
                    int half = stoneStr.length() / 2;
                    long left = Long.parseLong(stoneStr.substring(0, half));
                    long right = Long.parseLong(stoneStr.substring(half));
                    if(currentStones.get(left) == null) {
                        currentStones.put(left, 0l);
                    }
                    if(currentStones.get(right) == null) {
                        currentStones.put(right, 0l);
                    }
                    currentStones.put(left, currentStones.get(left) + original.get(stone));
                    currentStones.put(right, currentStones.get(right) + original.get(stone));
                } else {
                    if(currentStones.get(stone * 2024) == null) {
                        currentStones.put(stone * 2024, 0l);
                    }
                    currentStones.put(stone * 2024, currentStones.get(stone * 2024) + original.get(stone));
                }
            });

            original.keySet().forEach((stone) -> { // delete the stones we just looked at
                currentStones.put(stone, currentStones.get(stone) - original.get(stone));
            });
        }

        long count = 0;
        for(long key: currentStones.keySet()) {
            count += currentStones.get(key);
        }
        System.out.println(count);

        // for(int i = 0; i < 25; i++) {
        //     for(int j = 0; j < currentStones.size(); j++) {
        //         long stone = currentStones.get(j);
        //         if(stone == 0) {
        //             currentStones.set(j, 1l);
        //         } else if(Long.toString(stone).length() % 2 == 0) {
        //             String stoneStr = Long.toString(stone);
        //             int half = stoneStr.length() / 2;
        //             long left = Long.parseLong(stoneStr.substring(0, half));
        //             long right = Long.parseLong(stoneStr.substring(half));
        //             currentStones.set(j, left);
        //             currentStones.add(j, right);
        //             j++;
        //         } else {
        //             currentStones.set(j, stone * 2024);
        //         }
        //     }
        // }
        // System.out.println(currentStones.size());
    }
}
