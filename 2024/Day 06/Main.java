
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(new File("b.txt"));
        ArrayList<ArrayList<Character>> grid = new ArrayList<>();

        // could've used set but I didn't want to save the starting position too
        HashSet<Pair> visitedSpots = new HashSet<>();

        int startingR = -1;
        int startingC = -1;
        char startingDirection = 'X';
        while (input.hasNextLine()) {
            String line = input.nextLine();
            grid.add(new ArrayList<>());
            for (int i = 0; i < line.length(); i++) {
                grid.get(grid.size() - 1).add(line.charAt(i));
                if (line.charAt(i) != '#' && line.charAt(i) != '.') {
                    startingR = grid.size() - 1;
                    startingC = grid.get(grid.size() - 1).size() - 1;
                    startingDirection = line.charAt(i);
                }
            }
        }

        int posR = startingR;
        int posC = startingC;
        char direction = startingDirection;

        ArrayList<ArrayList<Character>> originalGrid = new ArrayList<>();
        for (int r = 0; r < grid.size(); r++) {
            originalGrid.add(new ArrayList<>());
            for (int c = 0; c < grid.get(0).size(); c++) {
                originalGrid.get(r).add(grid.get(r).get(c));
            }
        }

        boolean exited = false;
        while (!exited) {
            visitedSpots.add(new Pair(posR, posC));
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

        // find time paradoxes
        int timeParadoxes = 0;
        visitedSpots.remove(new Pair(startingR, startingC)); // cannot place obstacle at start
        for (Pair p : visitedSpots) {
            grid = new ArrayList<>();
            for (int r = 0; r < originalGrid.size(); r++) {
                grid.add(new ArrayList<>());
                for (int c = 0; c < originalGrid.get(0).size(); c++) {
                    grid.get(r).add(originalGrid.get(r).get(c));
                }
            }
            grid.get(p.r).set(p.c, '#');
            exited = false;
            posR = startingR;
            posC = startingC;
            direction = startingDirection;
            // should've went to python
            HashSet<VisitState> visitedStates = new HashSet<>();
            while (!exited) {
                if (visitedStates.contains(new VisitState(posR, posC, direction))) {
                    timeParadoxes++;
                    break;
                }
                visitedStates.add(new VisitState(posR, posC, direction));

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

        }
        System.out.println(timeParadoxes);
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

class VisitState {

    int r;
    int c;
    char direction;

    public VisitState(int r, int c, char direction) {
        this.r = r;
        this.c = c;
        this.direction = direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VisitState)) {
            return false;
        }
        VisitState p = (VisitState) o;
        return r == p.r && c == p.c && direction == p.direction;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(r * 100) + Integer.hashCode(c * -100) + Character.hashCode(direction);
    }
}

class Pair {

    int r;
    int c;

    public Pair(int r, int c) {
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
