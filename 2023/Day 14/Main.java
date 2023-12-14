import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.lang.Math;

public class Main{ // note, this is too slow, so the cutoff is for a heuristic solution
	public static void main(String[] args) throws Exception{
        Scanner input = new Scanner(new File("b.txt"));
        ArrayList<ArrayList<Character>> grid = new ArrayList<>();
        while(input.hasNextLine()){
            String line = input.nextLine();
            grid.add(new ArrayList<Character>());
            for(int i = 0; i < line.length(); i++){
                grid.get(grid.size()-1).add(line.charAt(i));
            }
        }
        int cycles = 1000000000;
        int cutoff = 100000;
        double runningAverageAmount = 0;
        FileWriter data = new FileWriter("data.csv");
        for(int i = 1; i <= cutoff; i++){
            //System.out.println(i);
            slideNorth(grid);
            slideWest(grid);
            slideSouth(grid);
            slideEast(grid);
            runningAverageAmount += getWeight(grid);
            //System.out.println(runningAverageAmount/i);
            data.write(i + "," + getWeight(grid) + "\n");
            data.flush();
        }
        //printGrid(grid);
        System.out.println(getWeight(grid));
    }
    public static int getWeight(ArrayList<ArrayList<Character>> grid){ // north support beam
        int weight = 0;
        int currentLoad = grid.size();
        for(int r = 0; r < grid.size(); r++){
            for(int c = 0; c < grid.get(r).size(); c++){
                if(grid.get(r).get(c) == 'O')
                    weight += currentLoad;
            }
            currentLoad--;
        }
        return weight;
    }
    public static void slideNorth(ArrayList<ArrayList<Character>> grid){
        for(int r = 0; r < grid.size(); r++){
            for(int c = 0; c < grid.get(r).size(); c++){
                if(grid.get(r).get(c) == 'O'){
                    int counter = 0;
                    while(moveUp(r - counter, c, grid)){
                        counter++;
                    }
                }
            }
        }
    }
    public static void slideSouth(ArrayList<ArrayList<Character>> grid){
        for(int r = grid.size()-1; r >= 0; r--){
            for(int c = 0; c < grid.get(r).size(); c++){
                if(grid.get(r).get(c) == 'O'){
                    int counter = 0;
                    while(moveDown(r + counter, c, grid)){
                        counter++;
                    }
                }
            }
        }
    }
    public static void slideEast(ArrayList<ArrayList<Character>> grid){
        for(int c = grid.get(0).size()-1; c >= 0; c--){
            for(int r = 0; r < grid.size(); r++){
                if(grid.get(r).get(c) == 'O'){
                    int counter = 0;
                    while(moveRight(r, c+counter, grid)){
                        counter++;
                    }
                }
            }
        }
    }
    public static void slideWest(ArrayList<ArrayList<Character>> grid){
        for(int c = 0; c < grid.get(0).size(); c++){
            for(int r = 0; r < grid.size(); r++){
                if(grid.get(r).get(c) == 'O'){
                    int counter = 0;
                    while(moveLeft(r, c-counter, grid)){
                        counter++;
                    }
                }
            }
        }
    }
    public static boolean moveUp(int r, int c, ArrayList<ArrayList<Character>> grid){
        if(inBounds(r - 1, c, grid.size(), grid.get(r).size()) && grid.get(r-1).get(c) != '#' && grid.get(r-1).get(c) != 'O'){
            grid.get(r-1).set(c, 'O');
            grid.get(r).set(c, '.');
            return true;
        }
        return false;
    }
    public static boolean moveDown(int r, int c, ArrayList<ArrayList<Character>> grid){
        if(inBounds(r +1, c, grid.size(), grid.get(r).size()) && grid.get(r+1).get(c) != '#' && grid.get(r+1).get(c) != 'O'){
            grid.get(r+1).set(c, 'O');
            grid.get(r).set(c, '.');
            return true;
        }
        return false;
    }
    public static boolean moveRight(int r, int c, ArrayList<ArrayList<Character>> grid){
        if(inBounds(r, c+1, grid.size(), grid.get(r).size()) && grid.get(r).get(c+1) != '#' && grid.get(r).get(c+1) != 'O'){
            grid.get(r).set(c+1, 'O');
            grid.get(r).set(c, '.');
            return true;
        }
        return false;
    }
    public static boolean moveLeft(int r, int c, ArrayList<ArrayList<Character>> grid){
        if(inBounds(r, c-1, grid.size(), grid.get(r).size()) && grid.get(r).get(c-1) != '#' && grid.get(r).get(c-1) != 'O'){
            grid.get(r).set(c-1, 'O');
            grid.get(r).set(c, '.');
            return true;
        }
        return false;
    }
    public static boolean inBounds(int r, int c, int maxR, int maxC){
        return (0 <= r && r < maxR) && (0 <= c && c < maxC);
    }
    public static boolean compare2DGrids(ArrayList<ArrayList<Character>> grid1, ArrayList<ArrayList<Character>> grid2){
        if(grid1.size() != grid2.size())
            return false;
        for(int r = 0; r < grid1.size(); r++){
            if(grid1.get(r).size() != grid2.get(r).size())
                return false;
            for(int c = 0; c < grid1.get(r).size(); c++){
                if(grid1.get(r).get(c) != grid2.get(r).get(c))
                    return false;
            }
        }
        return true;
    }
    public static void printGrid(ArrayList<ArrayList<Character>> grid){
        for(int r = 0; r < grid.size(); r++){
            for(int c = 0; c < grid.get(r).size(); c++){
                System.out.print(grid.get(r).get(c));
            }
            System.out.println("");
        }
    }
}