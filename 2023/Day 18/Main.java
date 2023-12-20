import java.util.*;
import java.io.File;
import java.lang.Math;

class Pair{
    int r;
    int c;
    public Pair(int row, int col){
        r = row;
        c = col;
    }
}
class LongPair{
    long r;
    long c;
    public LongPair(long row, long col){
        r = row;
        c = col;
    }
}
public class Main{
    public static void main(String[] args) throws Exception{
        Scanner input = new Scanner(new File("b.txt"));
        ArrayList<String> lines = new ArrayList<>();
        while(input.hasNextLine()){
            String line = input.nextLine();
            line = translateLineForPart2(line);
            lines.add(line);
        }
        // int[] dimensions = determineSize(lines);
        // System.out.println(dimensions[0] * dimensions[1]);
        // ArrayList<ArrayList<Character>> grid = new ArrayList<>();
        // initializeGrid(dimensions, grid);
        // digExterior(lines, dimensions, grid);
        // printGrid(grid);
        // System.out.println("");
        // bucketFill(grid);
        // isolateRegion('A', grid);
        // printGrid(grid);
        // System.out.println(countHashtags(grid));
        System.out.println(interiorAndEdgeArea(lines));
    }
    public static long interiorAndEdgeArea(ArrayList<String> lines){
        ArrayList<LongPair> points = new ArrayList<>();
        long curR = 0;
        long curC = 0;
        long perimeter = 0;
        for(String s: lines){
            char direction = s.charAt(0);
            long distance = Long.parseLong(s.split(" ")[1]);
            switch(direction){
                case 'U': curR += distance; break;
                case 'D': curR -= distance; break;
                case 'L': curC -= distance; break;
                case 'R': curC += distance; break;
            }
            perimeter += distance;
            points.add(new LongPair(curR, curC));
        }
        long minR = Long.MAX_VALUE;
        long minC = Long.MAX_VALUE;
        for(LongPair p: points){
            minR = Math.min(minR, p.r);
            minC = Math.min(minC, p.c);
        }
        minR = Math.abs(minR);
        minC = Math.abs(minC);
        for(int i = 0; i < points.size(); i++){
            points.get(i).r = points.get(i).r + minR;
            points.get(i).c = points.get(i).c + minC;
        }
        // for(LongPair p: points){
        //     System.out.println(p.r + " " + p.c);
        // }
        long sum = 0;
        for(int i = 0; i < points.size(); i++){
            int j = (i + 1) % points.size();
            sum += (points.get(i).c * points.get(j).r) - (points.get(j).c * points.get(i).r);
        }
        sum /= 2;
        sum = Math.abs(sum);
        // shoelace ^
        // pick's v
        return sum+(perimeter/2)+1;
    }
    public static String translateLineForPart2(String line){
        String hex = line.split(" ")[2];
        hex = hex.replaceAll("[()#]","");
        int distance = Integer.parseInt(hex.substring(0, hex.length()-1), 16);
        char direction = 'X';
        switch(hex.charAt(hex.length()-1)){
            case '0': direction = 'R'; break;
            case '1': direction = 'D'; break;
            case '2': direction = 'L'; break;
            case '3': direction = 'U'; break;
        }
        return (((Character)direction).toString() + " " + distance + " ######");
    }
    public static int countHashtags(ArrayList<ArrayList<Character>> grid){
        int count = 0;
        for(int r = 0; r < grid.size(); r++){
            for(int c = 0; c < grid.get(r).size(); c++){
                if(grid.get(r).get(c) == '#')
                    count++;
            }
        }
        return count;
    }
    public static void isolateRegion(char region, ArrayList<ArrayList<Character>> grid){
        for(int r = 0; r < grid.size(); r++){
            for(int c = 0; c < grid.get(r).size(); c++){
                if(grid.get(r).get(c) == region){
                    grid.get(r).set(c, '#');
                }
                else if(grid.get(r).get(c) != '#'){
                    grid.get(r).set(c, '.');
                }
            }
        }
    }
    public static void bucketFill(ArrayList<ArrayList<Character>> grid){
        char currentChar = 'A';
        for(int r = 0; r < grid.size(); r++){
            for(int c = 0; c < grid.get(r).size(); c++){
                if(grid.get(r).get(c) == '.'){ // start the bucket
                    Queue<Pair> queue = new LinkedList<>();
                    grid.get(r).set(c, currentChar);
                    queue.add(new Pair(r, c));
                    while(queue.isEmpty() == false){
                        Pair visit = queue.remove();
                        int row = visit.r;
                        int col = visit.c;
                        if(inBounds(row-1, col, grid) && grid.get(row-1).get(col) == '.'){
                            grid.get(row-1).set(col, currentChar);
                            queue.add(new Pair(row-1, col));
                        }
                        if(inBounds(row+1, col, grid) && grid.get(row+1).get(col) == '.'){
                            grid.get(row+1).set(col, currentChar);
                            queue.add(new Pair(row+1, col));
                        }
                        if(inBounds(row, col-1, grid) && grid.get(row).get(col-1) == '.'){
                            grid.get(row).set(col-1, currentChar);
                            queue.add(new Pair(row, col-1));
                        }
                        if(inBounds(row, col+1, grid) && grid.get(row).get(col+1) == '.'){
                            grid.get(row).set(col+1, currentChar);
                            queue.add(new Pair(row, col+1));
                        }
                    }
                    currentChar++;
                }
            }
        }
    }
    public static void digExterior(ArrayList<String> lines, int[] dimensions, ArrayList<ArrayList<Character>> grid){
        int r = dimensions[2];
        int c = dimensions[3];
        grid.get(r).set(c, '#');
        for(String s: lines){
            char direction = s.charAt(0);
            int distance = Integer.parseInt(s.split(" ")[1]);
            for(int i = 0; i < distance; i++){
                switch(direction){
                    case 'U': r--; break;
                    case 'D': r++; break;
                    case 'L': c--; break;
                    case 'R': c++; break;
                }
                grid.get(r).set(c, '#');
            } 
        }
    }
    public static int[] determineSize(ArrayList<String> lines){
        int[] dimensions = new int[4];
        int maxR = 0;
        int maxC = 0;
        int minR = 0;
        int minC = 0;
        int curR = 0;
        int curC = 0;
        for(String s: lines){
            char direction = s.charAt(0);
            int distance = Integer.parseInt(s.split(" ")[1]);
            switch(direction){
                case 'U': curR -= distance; break;
                case 'D': curR += distance; break;
                case 'L': curC -= distance; break;
                case 'R': curC += distance; break;
            }
            maxR = Math.max(maxR, curR);
            maxC = Math.max(maxC, curC);
            minR = Math.min(minR, curR);
            minC = Math.min(minC, curC);
        }
        //System.out.println(minR + " " + maxR + " " + minC + " " + maxC);
        dimensions[0] = Math.abs(maxR) + Math.abs(minR) + 1;
        dimensions[1] = Math.abs(maxC) + Math.abs(minC) + 1;
        dimensions[2] = Math.abs(minR);
        dimensions[3] = Math.abs(minC);
        //System.out.println(dimensions[0] + " " + dimensions[1]);
        return dimensions;
    }
    public static void initializeGrid(int[] dimensions, ArrayList<ArrayList<Character>> grid){
        int rows = dimensions[0];
        int cols = dimensions[1];
        for(int r = 0; r < rows; r++){
            grid.add(new ArrayList<>());
            for(int c = 0; c < cols; c++){
                grid.get(r).add('.');
            }
        }
    }
    public static void printGrid(ArrayList<ArrayList<Character>> grid){
        for(int r = 0; r < grid.size(); r++){
            for(int c = 0; c < grid.get(0).size(); c++){
                System.out.print(grid.get(r).get(c));
            }
            System.out.println("");
        }
    }
    public static boolean inBounds(int r, int c, ArrayList<ArrayList<Character>> grid){
        return inBounds(r, c, grid.size(), grid.get(0).size());
    }
    public static boolean inBounds(int r, int c, int maxR, int maxC){
        return (0 <= r && r < maxR) && (0 <= c && c < maxC);
    }
}
