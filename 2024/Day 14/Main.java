
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(new File("b.txt"));
        ArrayList<Robot> robots = new ArrayList<>();
        while (input.hasNextLine()) {
            String line = input.nextLine();
            String[] parts = line.split(" ");
            String pos = parts[0];
            String vel = parts[1];
            int x = Integer.parseInt(pos.substring(pos.indexOf("=") + 1, pos.indexOf(",")));
            int y = Integer.parseInt(pos.substring(pos.indexOf(",") + 1));
            int vx = Integer.parseInt(vel.substring(vel.indexOf("=") + 1, vel.indexOf(",")));
            int vy = Integer.parseInt(vel.substring(vel.indexOf(",") + 1));
            robots.add(new Robot(x, y, vx, vy));
        }

        // int width = 11;
        // int height = 7;

        int width = 101;
        int height = 103;

        String row = "";
        for (int i = 0; i < width; i++) {
            row += ".";
        }

        // for(int t = 0; t < 100; t++) {
        for (int t = 0; t < 10000000000000000l; t++) {
            for (int i = 0; i < robots.size(); i++) {
                Robot r = robots.get(i);
                r.x = (r.x + r.vx);
                r.y = (r.y + r.vy);
                if (r.x < 0) {
                    r.x = width + r.x;
                }
                if (r.y < 0) {
                    r.y = height + r.y;
                }
                r.x %= width;
                r.y %= height;
            }

            // print robots
            String[] grid = new String[height];

            for (int r = 0; r < height; r++) {
                grid[r] = row;
            }
            for (Robot r : robots) {
                grid[r.y] = grid[r.y].substring(0, r.x) + "#" + grid[r.y].substring(r.x + 1);
            }
            boolean good = false;
            for (int r = 0; r < height; r++) {
                if (grid[r].contains("#############")) {
                    good = true;
                    break;
                }
            }
            if (good) {
                System.out.println(t);
                for (int r = 0; r < height; r++) {
                    System.out.println(grid[r]);
                }
                System.out.println();
                break;
                // 7285 + 1 was my answer
            }
        }

        int quad1 = 0;
        int quad2 = 0;
        int quad3 = 0;
        int quad4 = 0;
        for (Robot r : robots) {
            if (r.x >= 0 && r.x < width / 2 && r.y >= 0 && r.y < height / 2) {
                quad1++;
            } else if (r.x > width / 2 && r.x < width && r.y >= 0 && r.y < height / 2) {
                quad2++;
            } else if (r.x >= 0 && r.x < width / 2 && r.y > height / 2 && r.y < height) {
                quad3++;
            } else if (r.x > width / 2 && r.x < width && r.y > height / 2 && r.y < height) {
                quad4++;
            }
        }
        System.out.println(quad1 + " " + quad2 + " " + quad3 + " " + quad4);
        System.out.println(quad1 * quad2 * quad3 * quad4);
    }
}

class Robot {
    public int x, y, vx, vy;

    public Robot(int x, int y, int vx, int vy) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
    }
}
