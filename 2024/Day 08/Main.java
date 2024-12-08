
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
        int height = grid.size();
        int width = grid.get(0).size();
        HashMap<Character, ArrayList<Pair>> antennaLocations = new HashMap<>();
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                char cell = grid.get(r).get(c);
                if (cell != '.') {
                    if (!antennaLocations.containsKey(cell)) {
                        antennaLocations.put(cell, new ArrayList<>());
                    }
                    antennaLocations.get(cell).add(new Pair(r, c));
                }
            }
        }
        ArrayList<ArrayList<Character>> uniqueAntinodeLocations = new ArrayList<>();
        for (int r = 0; r < height; r++) {
            ArrayList<Character> row = new ArrayList<>();
            for (int c = 0; c < width; c++) {
                row.add('.');
            }
            uniqueAntinodeLocations.add(row);
        }

        for (char antenna : antennaLocations.keySet()) {
            ArrayList<Pair> locations = antennaLocations.get(antenna);

            for(int i = 0; i < locations.size(); i++) {
                for(int j = i + 1; j  < locations.size(); j++) {
                    Pair location1 = locations.get(i);
                    Pair location2 = locations.get(j);
                    int r1 = location1.r;
                    int c1 = location1.c;
                    int r2 = location2.r;
                    int c2 = location2.c;
                    int dr = r2 - r1;
                    int dc = c2 - c1;
                    // if(checkBounds(r1 - dr, c1 - dc, height, width)) {
                    //     uniqueAntinodeLocations.get(r1 - dr).set(c1 - dc, '#');
                    // }
                    // if(checkBounds(r2 + dr, c2 + dc, height, width)) {
                    //     uniqueAntinodeLocations.get(r2 + dr).set(c2 + dc, '#');
                    // }
                    int multiple = 0;
                    while(checkBounds(r1 - dr * multiple, c1 - dc * multiple, height, width)) {
                        uniqueAntinodeLocations.get(r1 - dr * multiple).set(c1 - dc * multiple, '#');
                        multiple++;
                    }
                    multiple = 0;
                    while(checkBounds(r2 + dr * multiple, c2 + dc * multiple, height, width)) {
                        uniqueAntinodeLocations.get(r2 + dr * multiple).set(c2 + dc * multiple, '#');
                        multiple++;
                    }
                }
            }
        }

        int countUniqueAntinodeLocations = 0;
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (uniqueAntinodeLocations.get(r).get(c) == '#') {
                    countUniqueAntinodeLocations++;
                }
            }
        }
        System.out.println(countUniqueAntinodeLocations);
    }

    public static boolean checkBounds(int r, int c, int maxR, int maxC) {
        return 0 <= r && r < maxR && 0 <= c && c < maxC;
    }
}

class Pair {

    public int r;
    public int c;

    public Pair(int r, int c) {
        this.r = r;
        this.c = c;
    }
}
