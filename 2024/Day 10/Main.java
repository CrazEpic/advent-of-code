
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(new File("b.txt"));
        ArrayList<ArrayList<Integer>> topoMap = new ArrayList<>();
        while (input.hasNextLine()) {
            String line = input.nextLine();
            ArrayList<Integer> row = new ArrayList<>();
            for (int i = 0; i < line.length(); i++) {
                row.add(line.charAt(i) - '0');
            }
            topoMap.add(row);
        }

        int sum = 0;
        int distinctPathSum = 0;
        for (int r = 0; r < topoMap.size(); r++) {
            for (int c = 0; c < topoMap.get(0).size(); c++) {
                if (topoMap.get(r).get(c) == 0) {
                    sum += climb(topoMap, new HashSet<>(), r, c);
                }
            }
        }
        for (int r = 0; r < topoMap.size(); r++) {
            for (int c = 0; c < topoMap.get(0).size(); c++) {
                if (topoMap.get(r).get(c) == 0) {
                    distinctPathSum += climbDistinct(topoMap, new HashSet<>(), r, c);
                }
            }
        }
        System.out.println(sum);
        System.out.println(distinctPathSum);
    }

    public static int climb(ArrayList<ArrayList<Integer>> topoMap, HashSet<Pair> visited, int r, int c) {
        if(visited.contains(new Pair(r, c))) {
            return 0;
        }
        visited.add(new Pair(r, c));
        if (topoMap.get(r).get(c) == 9) {
            return 1;
        }
        int currentLevel = topoMap.get(r).get(c);
        int sum = 0;
        if (checkBounds(r - 1, c, topoMap.size(), topoMap.get(0).size()) && topoMap.get(r - 1).get(c) - currentLevel == 1) {
            sum += climb(topoMap, visited, r - 1, c);
        }
        if (checkBounds(r + 1, c, topoMap.size(), topoMap.get(0).size()) && topoMap.get(r + 1).get(c) - currentLevel == 1) {
            sum += climb(topoMap, visited, r + 1, c);
        }
        if (checkBounds(r, c - 1, topoMap.size(), topoMap.get(0).size()) && topoMap.get(r).get(c - 1) - currentLevel == 1) {
            sum += climb(topoMap, visited, r, c - 1);
        }
        if (checkBounds(r, c + 1, topoMap.size(), topoMap.get(0).size()) && topoMap.get(r).get(c + 1) - currentLevel == 1) {
            sum += climb(topoMap, visited, r, c + 1);
        }
        return sum;
    }

    public static int climbDistinct(ArrayList<ArrayList<Integer>> topoMap, HashSet<Pair> visited, int r, int c) {
        if(visited.contains(new Pair(r, c))) {
            return 0;
        }
        visited.add(new Pair(r, c));
        if (topoMap.get(r).get(c) == 9) {
            return 1;
        }
        int currentLevel = topoMap.get(r).get(c);
        int sum = 0;
        if (checkBounds(r - 1, c, topoMap.size(), topoMap.get(0).size()) && topoMap.get(r - 1).get(c) - currentLevel == 1) {
            sum += climbDistinct(topoMap, new HashSet<>(visited), r - 1, c);
        }
        if (checkBounds(r + 1, c, topoMap.size(), topoMap.get(0).size()) && topoMap.get(r + 1).get(c) - currentLevel == 1) {
            sum += climbDistinct(topoMap, new HashSet<>(visited), r + 1, c);
        }
        if (checkBounds(r, c - 1, topoMap.size(), topoMap.get(0).size()) && topoMap.get(r).get(c - 1) - currentLevel == 1) {
            sum += climbDistinct(topoMap, new HashSet<>(visited), r, c - 1);
        }
        if (checkBounds(r, c + 1, topoMap.size(), topoMap.get(0).size()) && topoMap.get(r).get(c + 1) - currentLevel == 1) {
            sum += climbDistinct(topoMap, new HashSet<>(visited), r, c + 1);
        }
        return sum;
    }

    public static boolean checkBounds(int r, int c, int maxR, int maxC) {
        return r >= 0 && r < maxR && c >= 0 && c < maxC;
    }
}

class Pair {

    public int r;
    public int c;

    Pair(int r, int c) {
        this.r = r;
        this.c = c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pair)) {
            return false;
        }
        Pair p = (Pair) o;
        return r == p.r && c == p.c;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(r * 100) + Integer.hashCode(c * -100);
    }
}
