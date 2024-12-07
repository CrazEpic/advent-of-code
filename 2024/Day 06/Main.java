
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        ArrayList<ArrayList<Character>> grid = new ArrayList<>();
        int posR = -1;
        int posC = -1;
        char direction = 'X';
        while (input.hasNextLine()) {
            String line = input.nextLine();
            grid.add(new ArrayList<>());
            for (int i = 0; i < line.length(); i++) {
                grid.get(grid.size() - 1).add(line.charAt(i));
                if (line.charAt(i) != '#' && line.charAt(i) != '.') {
                    posR = grid.size() - 1;
                    posC = grid.get(grid.size() - 1).size() - 1;
                    direction = line.charAt(i);
                }
            }
        }

        boolean exited = false;
        while (!exited) {
            // check for needed rotation
            boolean rotated = false;
            switch (direction) {
                case '^':
                    if (checkBounds(posR - 1, posC, grid.size(), grid.get(0).size()) && grid.get(posR - 1).get(posC) == '#') {
                        grid.get(posR).set(posC, '>');
                        rotated = true;
                    }
                    break;
                case '>':
                    if (checkBounds(posR, posC + 1, grid.size(), grid.get(0).size()) && grid.get(posR).get(posC + 1) == '#') {
                        grid.get(posR).set(posC, 'v');
                        rotated = true;
                    }
                    break;
                case 'v':
                    if (checkBounds(posR + 1, posC, grid.size(), grid.get(0).size()) && grid.get(posR + 1).get(posC) == '#') {
                        grid.get(posR).set(posC, '<');
                        rotated = true;
                    }
                    break;
                case '<':
                    if (checkBounds(posR, posC - 1, grid.size(), grid.get(0).size()) && grid.get(posR).get(posC - 1) == '#') {
                        grid.get(posR).set(posC, '^');
                        rotated = true;
                    }
                    break;
            }
            if (rotated) {
                direction = grid.get(posR).get(posC);
                continue;
            }
            // move
            grid.get(posR).set(posC, 'X');
            switch (direction) {
                case '^':
                    if (!checkBounds(posR - 1, posC, grid.size(), grid.get(0).size())) {
                        exited = true;
                    } else {
                        posR--;
                        grid.get(posR).set(posC, direction);
                    }
                    break;
                case '>':
                    if (!checkBounds(posR, posC + 1, grid.size(), grid.get(0).size())) {
                        exited = true;
                    } else {
                        posC++;
                        grid.get(posR).set(posC, direction);
                    }
                    break;
                case 'v':
                    if (!checkBounds(posR + 1, posC, grid.size(), grid.get(0).size())) {
                        exited = true;
                    } else {
                        posR++;
                        grid.get(posR).set(posC, direction);
                    }
                    break;
                case '<':
                    if (!checkBounds(posR, posC - 1, grid.size(), grid.get(0).size())) {
                        exited = true;
                    } else {
                        posC--;
                        grid.get(posR).set(posC, direction);
                    }
                    break;
            }
        }
        printGrid(grid);

        int visitedCount = 0;
        for (int r = 0; r < grid.size(); r++) {
            for (int c = 0; c < grid.get(0).size(); c++) {
                if (grid.get(r).get(c) == 'X') {
                    visitedCount++;
                }
            }
        }
        System.out.println(visitedCount);
    }

    public static boolean checkBounds(int r, int c, int maxR, int maxC) {
        return 0 <= r && r < maxR && 0 <= c && c < maxC;
    }

    public static void printGrid(ArrayList<ArrayList<Character>> arr) {
        for (int r = 0; r < arr.size(); r++) {
            for (int c = 0; c < arr.get(0).size(); c++) {
                System.out.print(arr.get(r).get(c));
            }
            System.out.println("");
        }
    }
}

