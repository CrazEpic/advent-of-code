import java.util.*;
import java.io.File;
import java.lang.Math;

public class Main{
    public static void main(String[] args) throws Exception{
        Scanner input = new Scanner(new File("b.txt"));
        ArrayList<ArrayList<Character>> grid = new ArrayList<>();
        int startR = -1;
        int startC = -1;
        while(input.hasNextLine()){
            String line = input.nextLine().strip();
            grid.add(new ArrayList<>());
            if(startC == -1){
                startR++;
            }
            for(int i = 0; i < line.length(); i++){
                if(line.charAt(i) == 'S'){
                    startC = i;
                }
                grid.get(grid.size()-1).add(line.charAt(i));
            }
        }
        System.out.println(specificSteps(startR, startC, 64, grid));

        // doing the diamond thing for part 2 is cool and all
        // I think the math here is easier, clever
        
        // 65 steps from S to edge, grid size is 131 x 131

        System.out.println(specificStepsPart2(startR, startC, 65, grid));
        System.out.println(specificStepsPart2(startR, startC, 65 + 131, grid));
        System.out.println(specificStepsPart2(startR, startC, 65 + 131 + 131, grid));
        //System.out.println(specificStepsPart2(startR, startC, 65 + 131 * 131 * 131 * 131, grid));

    }
    public static int modulo(int x, int moduloClass){
        while(x < 0) x += moduloClass;
        return x % moduloClass;
    }
    public static int specificStepsPart2(int startR, int startC, int totalSteps, ArrayList<ArrayList<Character>> grid){
        HashSet<Pair> currentPositions = new HashSet<>();
        currentPositions.add(new Pair(startR, startC));
        return specificStepsPart2Helper(totalSteps, currentPositions, grid);
    }
    public static int specificStepsPart2Helper(int stepsLeft, HashSet<Pair> currentPositions, ArrayList<ArrayList<Character>> grid){
        // ArrayList<ArrayList<Character>> gridCopy = new ArrayList<>();
        // for(int r = 0; r < grid.size(); r++)
        //     gridCopy.add(new ArrayList<>(grid.get(r)));
        // for(Pair p: currentPositions)
        //     gridCopy.get(p.r).set(p.c, 'O');
        // printGrid(gridCopy);
        // System.out.println("");
        
        if(stepsLeft == 0){
            return currentPositions.size();
        }
        ArrayList<Pair> previousPositions = new ArrayList<>(currentPositions);
        currentPositions.clear();
        for(Pair p: previousPositions){
            int r = modulo(p.r, grid.size());
            int c = modulo(p.c, grid.get(0).size());
            if(grid.get(modulo(r-1, grid.size())).get(c) != '#'){
                currentPositions.add(new Pair(p.r-1, p.c));
            }
            if(grid.get(modulo(r+1, grid.size())).get(c) != '#'){
                currentPositions.add(new Pair(p.r+1, p.c));
            }
            if(grid.get(r).get(modulo(c-1, grid.get(0).size())) != '#'){
                currentPositions.add(new Pair(p.r, p.c-1));
            }
            if(grid.get(r).get(modulo(c+1, grid.get(0).size())) != '#'){
                currentPositions.add(new Pair(p.r, p.c+1));
            }
        }
        return specificStepsPart2Helper(stepsLeft-1, currentPositions, grid);
    }
    public static int specificSteps(int startR, int startC, int totalSteps, ArrayList<ArrayList<Character>> grid){
        HashSet<Pair> currentPositions = new HashSet<>();
        currentPositions.add(new Pair(startR, startC));
        return specificStepsHelper(totalSteps, currentPositions, grid);
    }
    public static int specificStepsHelper(int stepsLeft, HashSet<Pair> currentPositions, ArrayList<ArrayList<Character>> grid){
        if(stepsLeft == 0){
            return currentPositions.size();
        }
        ArrayList<Pair> previousPositions = new ArrayList<>(currentPositions);
        currentPositions.clear();
        for(Pair p: previousPositions){
            if(inBounds(p.r-1, p.c, grid) && grid.get(p.r-1).get(p.c) != '#'){
                currentPositions.add(new Pair(p.r-1, p.c));
            }
            if(inBounds(p.r+1, p.c, grid) && grid.get(p.r+1).get(p.c) != '#'){
                currentPositions.add(new Pair(p.r+1, p.c));
            }
            if(inBounds(p.r, p.c-1, grid) && grid.get(p.r).get(p.c-1) != '#'){
                currentPositions.add(new Pair(p.r, p.c-1));
            }
            if(inBounds(p.r, p.c+1, grid) && grid.get(p.r).get(p.c+1) != '#'){
                currentPositions.add(new Pair(p.r, p.c+1));
            }
        }
        return specificStepsHelper(stepsLeft-1, currentPositions, grid);
    }

    public static boolean inBounds(int r, int c, ArrayList<ArrayList<Character>> grid){
        return inBounds(r, c, grid.size(), grid.get(0).size());
    }
    public static boolean inBounds(int r, int c, int maxR, int maxC){
        return (0 <= r && r < maxR) && (0 <= c && c < maxC);
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
class Pair{
    public int r;
    public int c;
    public Pair(int row, int col){
        r = row;
        c = col;
    }
    @Override
    public boolean equals(Object o){
        if(o == this)
            return true;
        if(!(o instanceof Pair))
            return false;
        Pair other = (Pair) o;
        return (r == other.r) && (c == other.c);
    }
    @Override
    public int hashCode(){
        return Integer.hashCode(r) + Integer.hashCode(c);
    }
    public String toString(){
        return r + " " + c;
    }
}