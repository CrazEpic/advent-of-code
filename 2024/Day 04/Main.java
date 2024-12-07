
import java.util.*;

public class Main {

    // here bc easier
    public static char[][] grid;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        ArrayList<ArrayList<Character>> arr = new ArrayList<>();
        while (input.hasNextLine()) {
            String line = input.nextLine();
            arr.add(new ArrayList<Character>());
            for (int i = 0; i < line.length(); i++) {
                arr.get(arr.size() - 1).add(line.charAt(i));
            }
        }
        grid = new char[arr.size()][arr.get(0).size()];
        for (int r = 0; r < arr.size(); r++) {
            for (int c = 0; c < arr.size(); c++) {
                grid[r][c] = arr.get(r).get(c);
            }
        }

        int xmasCount = 0;

        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                // for(int d = 0; d < 8; d++) {
                // if(checkXMAS(r,c,d)) {
                if (checkXMASPart2(r, c)) {
                    xmasCount++;
                }
                // }
            }
        }

        System.out.println(xmasCount);
    }

    public static boolean checkXMASPart2(int r, int c) {
        if (grid[r][c] == 'A'
                && checkBounds(r - 1, c - 1, grid.length, grid[0].length)
                && checkBounds(r + 1, c + 1, grid.length, grid[0].length)
                && checkBounds(r - 1, c + 1, grid.length, grid[0].length)
                && checkBounds(r + 1, c - 1, grid.length, grid[0].length)) {
            return ((grid[r - 1][c - 1] == 'M' && grid[r + 1][c + 1] == 'S') || (grid[r - 1][c - 1] == 'S' && grid[r + 1][c + 1] == 'M'))
                    && ((grid[r - 1][c + 1] == 'M' && grid[r + 1][c - 1] == 'S') || (grid[r - 1][c + 1] == 'S' && grid[r + 1][c - 1] == 'M'));
        }
        return false;
    }

    public static boolean checkXMAS(int r, int c, int direction) {
        switch (direction) {
            case 0: // up-left
                if (checkBounds(r - 3, c - 3, grid.length, grid[0].length)) {
                    return grid[r][c] == 'X'
                            && grid[r - 1][c - 1] == 'M'
                            && grid[r - 2][c - 2] == 'A'
                            && grid[r - 3][c - 3] == 'S';
                }
                break;
            case 1: // up
                if (checkBounds(r - 3, c, grid.length, grid[0].length)) {
                    return grid[r][c] == 'X'
                            && grid[r - 1][c] == 'M'
                            && grid[r - 2][c] == 'A'
                            && grid[r - 3][c] == 'S';
                }
                break;
            case 2: // up-right
                if (checkBounds(r - 3, c + 3, grid.length, grid[0].length)) {
                    return grid[r][c] == 'X'
                            && grid[r - 1][c + 1] == 'M'
                            && grid[r - 2][c + 2] == 'A'
                            && grid[r - 3][c + 3] == 'S';
                }
                break;
            case 3: // right
                if (checkBounds(r, c + 3, grid.length, grid[0].length)) {
                    return grid[r][c] == 'X'
                            && grid[r][c + 1] == 'M'
                            && grid[r][c + 2] == 'A'
                            && grid[r][c + 3] == 'S';
                }
                break;
            case 4: // down-right
                if (checkBounds(r + 3, c + 3, grid.length, grid[0].length)) {
                    return grid[r][c] == 'X'
                            && grid[r + 1][c + 1] == 'M'
                            && grid[r + 2][c + 2] == 'A'
                            && grid[r + 3][c + 3] == 'S';
                }
                break;
            case 5: // down
                if (checkBounds(r + 3, c, grid.length, grid[0].length)) {
                    return grid[r][c] == 'X'
                            && grid[r + 1][c] == 'M'
                            && grid[r + 2][c] == 'A'
                            && grid[r + 3][c] == 'S';
                }
                break;
            case 6: // down-left
                if (checkBounds(r + 3, c - 3, grid.length, grid[0].length)) {
                    return grid[r][c] == 'X'
                            && grid[r + 1][c - 1] == 'M'
                            && grid[r + 2][c - 2] == 'A'
                            && grid[r + 3][c - 3] == 'S';
                }
                break;
            case 7: // left
                if (checkBounds(r, c - 3, grid.length, grid[0].length)) {
                    return grid[r][c] == 'X'
                            && grid[r][c - 1] == 'M'
                            && grid[r][c - 2] == 'A'
                            && grid[r][c - 3] == 'S';
                }
                break;
        }
        return false;
    }

    public static boolean checkBounds(int r, int c, int maxR, int maxC) {
        return 0 <= r && r < maxR && 0 <= c && c < maxC;
    }
}
