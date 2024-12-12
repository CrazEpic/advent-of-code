
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(new File("b.txt"));
        ArrayList<ArrayList<Character>> grid = new ArrayList<>();
        while (input.hasNextLine()) {
            String line = input.nextLine();
            ArrayList<Character> row = new ArrayList<>();
            for (int i = 0; i < line.length(); i++) {
                row.add(line.charAt(i));
            }
            grid.add(row);
        }
        HashSet<Pair> visited = new HashSet<>();
        ArrayList<ArrayList<Pair>> regions = new ArrayList<>();

        for (int r = 0; r < grid.size(); r++) {
            for (int c = 0; c < grid.get(0).size(); c++) {
                if (!visited.contains(new Pair(r, c))) {
                    ArrayList<Pair> region = new ArrayList<>();
                    explore(r, c, grid.get(r).get(c), grid, visited, region);
                    regions.add(region);
                }
            }
        }

        long totalPrice = 0;

        for (ArrayList<Pair> region : regions) {
            long area = region.size();
            long perimeter = 0;
            for (Pair p : region) { // if adjacent cell is not same plant, increment perimeter
                if (!checkBounds(p.r + 1, p.c, grid.size(), grid.get(0).size())
                        || !region.contains(new Pair(p.r + 1, p.c))) {
                    perimeter++;
                }
                if (!checkBounds(p.r - 1, p.c, grid.size(), grid.get(0).size())
                        || !region.contains(new Pair(p.r - 1, p.c))) {
                    perimeter++;
                }
                if (!checkBounds(p.r, p.c + 1, grid.size(), grid.get(0).size())
                        || !region.contains(new Pair(p.r, p.c + 1))) {
                    perimeter++;
                }
                if (!checkBounds(p.r, p.c - 1, grid.size(), grid.get(0).size())
                        || !region.contains(new Pair(p.r, p.c - 1))) {
                    perimeter++;
                }
            }
            System.out.println(
                    "Area: " + area + " Perimeter: " + perimeter + " Price: " + calculatePrice(area, perimeter));
            totalPrice += calculatePrice(area, perimeter);
        }

        System.out.println("Total Price: " + totalPrice);

        long totalPriceWithSides = 0;

        for (ArrayList<Pair> region : regions) {
            long area = region.size();
            HashMap<Pair, ArrayList<Boolean>> usedSides = new HashMap<>();
            for (Pair r : region) {
                ArrayList<Boolean> sides = new ArrayList<>();
                sides.add(false);
                sides.add(false);
                sides.add(false);
                sides.add(false);
                usedSides.put(r, sides);
            }
            // 0 is up, 1 is down, 2 is left, 3 is right
            long sides = 0;

            for (Pair p : region) {
                // top side, iterate horizontally

                if (!usedSides.get(p).get(0) && (!checkBounds(p.r - 1, p.c, grid.size(), grid.get(0).size())
                        || !region.contains(new Pair(p.r - 1, p.c)))) {
                    sides++;
                    usedSides.get(p).set(0, true);
                    int left = p.c;
                    int right = p.c;
                    while (!region.contains(new Pair(p.r - 1, left)) && region.contains(new Pair(p.r, left))) {
                        usedSides.get(new Pair(p.r, left)).set(0, true);
                        left--;
                    }
                    while (!region.contains(new Pair(p.r - 1, right)) && region.contains(new Pair(p.r, right))) {
                        usedSides.get(new Pair(p.r, right)).set(0, true);
                        right++;
                    }
                }
                // bottom side, iterate horizontally

                if (!usedSides.get(p).get(1) && (!checkBounds(p.r + 1, p.c, grid.size(), grid.get(0).size())
                        || !region.contains(new Pair(p.r + 1, p.c)))) {
                    sides++;
                    usedSides.get(p).set(1, true);
                    int left = p.c;
                    int right = p.c;
                    while (!region.contains(new Pair(p.r + 1, left)) && region.contains(new Pair(p.r, left))) {
                        usedSides.get(new Pair(p.r, left)).set(1, true);
                        left--;
                    }
                    while (!region.contains(new Pair(p.r + 1, right))
                            && region.contains(new Pair(p.r, right))) {
                        usedSides.get(new Pair(p.r, right)).set(1, true);
                        right++;
                    }
                }

                // left side, iterate vertically

                if (!usedSides.get(p).get(2) && (!checkBounds(p.r, p.c - 1, grid.size(), grid.get(0).size())
                        || !region.contains(new Pair(p.r, p.c - 1)))) {
                    sides++;
                    usedSides.get(p).set(2, true);
                    int up = p.r;
                    int down = p.r;
                    while (!region.contains(new Pair(up, p.c - 1)) && region.contains(new Pair(up, p.c))) {
                        usedSides.get(new Pair(up, p.c)).set(2, true);
                        up--;
                    }
                    while (!region.contains(new Pair(down, p.c - 1)) && region.contains(new Pair(down, p.c))) {
                        usedSides.get(new Pair(down, p.c)).set(2, true);
                        down++;
                    }
                }

                // right side, iterate vertically

                if (!usedSides.get(p).get(3) && (!checkBounds(p.r, p.c + 1, grid.size(), grid.get(0).size())
                        || !region.contains(new Pair(p.r, p.c + 1)))) {
                    sides++;
                    usedSides.get(p).set(3, true);
                    int up = p.r;
                    int down = p.r;
                    while (!region.contains(new Pair(up, p.c + 1)) && region.contains(new Pair(up, p.c))) {
                        usedSides.get(new Pair(up, p.c)).set(3, true);
                        up--;
                    }
                    while (!region.contains(new Pair(down, p.c + 1)) && region.contains(new Pair(down, p.c))) {
                        usedSides.get(new Pair(down, p.c)).set(3, true);
                        down++;
                    }
                }

            }

            totalPriceWithSides += calculatePriceWithSides(area, sides);
            System.out
                    .println("Area: " + area + " Sides: " + sides + " Price: " + calculatePriceWithSides(area, sides));
        }

        System.out.println("Total Price with Sides: " + totalPriceWithSides);
    }

    public static void explore(int r, int c, char currentPlant, ArrayList<ArrayList<Character>> grid,
            HashSet<Pair> visited, ArrayList<Pair> region) {
        if (!checkBounds(r, c, grid.size(), grid.get(0).size())) {
            return;
        }
        if (visited.contains(new Pair(r, c))) {
            return;
        }
        if (grid.get(r).get(c) != currentPlant) {
            return;
        }
        visited.add(new Pair(r, c));
        region.add(new Pair(r, c));
        explore(r + 1, c, currentPlant, grid, visited, region);
        explore(r - 1, c, currentPlant, grid, visited, region);
        explore(r, c + 1, currentPlant, grid, visited, region);
        explore(r, c - 1, currentPlant, grid, visited, region);
    }

    public static long calculatePrice(long area, long perimeter) {
        return area * perimeter;
    }

    public static long calculatePriceWithSides(long area, long sides) {
        return area * sides;
    }

    public static boolean checkBounds(int r, int c, int maxR, int maxC) {
        return r >= 0 && r < maxR && c >= 0 && c < maxC;
    }
}

class Pair {
    public int r, c;

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
