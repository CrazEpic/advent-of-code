import java.util.*;
import java.io.File;
import java.lang.Math;

public class Main{
    public static void main(String[] args) throws Exception{
        Scanner input = new Scanner(new File("b.txt"));
        ArrayList<ArrayList<Character>> grid = new ArrayList<>();
        while(input.hasNextLine()){
            String line = input.nextLine().trim();
            grid.add(new ArrayList<>());
            for(int i = 0; i < line.length(); i++){
                grid.get(grid.size()-1).add(line.charAt(i));
            }
        }
        int startR = 0;
        int startC = -1;
        int endR = grid.size()-1;
        int endC = -1;
        for(int i = 0; i < grid.get(startR).size(); i++){
            if(grid.get(startR).get(i) == '.'){
                startC = i;
                break;
            }
        }
        for(int i = 0; i < grid.get(endR).size(); i++){
            if(grid.get(endR).get(i) == '.'){
                endC = i;
                break;
            }
        }
        System.out.println(scenicWalk(startR, startC, endR, endC, grid));
        for(int r = 0; r < grid.size(); r++)
            for(int c = 0; c < grid.get(r).size(); c++)
                if(grid.get(r).get(c) != '.' && grid.get(r).get(c) != '#')
                    grid.get(r).set(c, '.');
        HashMap<Pair, HashMap<Pair, Integer>> part2Graph = new HashMap<>();
        for(int r = 0; r < grid.size(); r++){
            for(int c = 0; c < grid.get(r).size(); c++){
                if(grid.get(r).get(c) == '.'){
                    int neighbors = 0;
                    if(inBounds(r-1, c, grid) && grid.get(r-1).get(c) == '.')
                        neighbors++;
                    if(inBounds(r+1, c, grid) && grid.get(r+1).get(c) == '.')
                        neighbors++;
                    if(inBounds(r, c-1, grid) && grid.get(r).get(c-1) == '.')
                        neighbors++;
                    if(inBounds(r, c+1, grid) && grid.get(r).get(c+1) == '.')
                        neighbors++;
                    if(neighbors >= 3)
                        part2Graph.put(new Pair(r, c), new HashMap<>());
                }
            }
        }
        Pair start = new Pair(startR, startC);
        Pair end = new Pair(endR, endC);
        part2Graph.put(start, new HashMap<>());
        part2Graph.put(end, new HashMap<>());
        createPart2Graph(part2Graph, grid);
        // for(Pair p: part2Graph.keySet()){
        //     System.out.print(p + ": ");
        //     for(Pair p2: part2Graph.get(p).keySet()){
        //         System.out.print(p2 + ", ");
        //     }
        //     System.out.println("");
        // }
        System.out.println(part2Walk(start, end, part2Graph));
        System.out.println("done");
    }
    public static int part2Walk(Pair start, Pair end, HashMap<Pair, HashMap<Pair, Integer>> part2Graph){
        HashSet<Pair> visited = new HashSet<>();
        return part2WalkHelper(start, visited, end, part2Graph);
    }
    public static int part2WalkHelper(Pair current, HashSet<Pair> visited, Pair end, HashMap<Pair, HashMap<Pair, Integer>> part2Graph){
        if(current.equals(end)){
            return 0;
        }
        int maxSteps = -1;
        visited.add(current);
        for(Pair p: part2Graph.get(current).keySet()){
            if(visited.contains(p) == false){
                maxSteps = Math.max(maxSteps, part2Graph.get(current).get(p) + part2WalkHelper(p, visited, end, part2Graph));
            }
        }
        visited.remove(current);
        return maxSteps;

    }
    public static void createPart2Graph(HashMap<Pair, HashMap<Pair, Integer>> part2Graph, ArrayList<ArrayList<Character>> grid){
        for(Pair source: part2Graph.keySet()){
            HashSet<Pair> visited = new HashSet<>();
            visited.add(source);
            createPart2GraphHelper(source.r, source.c, 0, source, visited, part2Graph, grid);
        }
    }
    public static void createPart2GraphHelper(int r, int c, int steps, Pair source, HashSet<Pair> visited, HashMap<Pair, HashMap<Pair, Integer>> part2Graph, ArrayList<ArrayList<Character>> grid){
        if(steps != 0 && part2Graph.containsKey(new Pair(r, c))){
            part2Graph.get(source).put(new Pair(r, c), steps);
            return;
        }
        Pair currentSpot = new Pair(r, c);
        visited.add(currentSpot);
        if(inBounds(r-1, c, grid) && grid.get(r-1).get(c) != '#' && visited.contains(new Pair(r-1, c)) == false)
            createPart2GraphHelper(r-1, c, steps+1, source, visited, part2Graph, grid);
        if(inBounds(r+1, c, grid) && grid.get(r+1).get(c) != '#' && visited.contains(new Pair(r+1, c)) == false)
            createPart2GraphHelper(r+1, c, steps+1, source, visited, part2Graph, grid);
        if(inBounds(r, c-1, grid) && grid.get(r).get(c-1) != '#' && visited.contains(new Pair(r, c-1)) == false)
            createPart2GraphHelper(r, c-1, steps+1, source, visited, part2Graph, grid);
        if(inBounds(r, c+1, grid) && grid.get(r).get(c+1) != '#' && visited.contains(new Pair(r, c+1)) == false)
            createPart2GraphHelper(r, c+1, steps+1, source, visited, part2Graph, grid);
        visited.remove(currentSpot);
    }
    public static int scenicWalk(int row, int col, int endR, int endC, ArrayList<ArrayList<Character>> grid){
        HashSet<Pair> visitedInitial = new HashSet<>();
        Stack<ProgramState> states = new Stack<>();
        states.add(new ProgramState(row, col, 0, visitedInitial));
        int maxSteps = Integer.MIN_VALUE;
        while(states.isEmpty() == false){
            ProgramState program = states.pop();
            int r = program.r;
            int c = program.c;
            int steps = program.steps;
            HashSet<Pair> visited = program.visited;

            if(r == endR && c == endC){
                maxSteps = Math.max(maxSteps, steps);
                continue;
            }
            Pair currentSpot = new Pair(r, c);
            char currentChar = grid.get(r).get(c);
            visited.add(currentSpot);
            if(currentChar == '.'){
                if(inBounds(r-1, c, grid) && grid.get(r-1).get(c) != '#' && visited.contains(new Pair(r-1, c)) == false)
                    states.add(new ProgramState(r-1, c, steps+1, new HashSet<>(visited)));
                if(inBounds(r+1, c, grid) && grid.get(r+1).get(c) != '#' && visited.contains(new Pair(r+1, c)) == false)
                    states.add(new ProgramState(r+1, c, steps+1, new HashSet<>(visited)));
                if(inBounds(r, c-1, grid) && grid.get(r).get(c-1) != '#' && visited.contains(new Pair(r, c-1)) == false)
                    states.add(new ProgramState(r, c-1, steps+1, new HashSet<>(visited)));
                if(inBounds(r, c+1, grid) && grid.get(r).get(c+1) != '#' && visited.contains(new Pair(r, c+1)) == false)
                    states.add(new ProgramState(r, c+1, steps+1, new HashSet<>(visited)));
            }
            else if(currentChar == '^'){
                if(inBounds(r-1, c, grid) && grid.get(r-1).get(c) != '#' && visited.contains(new Pair(r-1, c)) == false)
                    states.add(new ProgramState(r-1, c, steps+1, new HashSet<>(visited)));
            }
            else if(currentChar == 'v'){
                if(inBounds(r+1, c, grid) && grid.get(r+1).get(c) != '#' && visited.contains(new Pair(r+1, c)) == false)
                    states.add(new ProgramState(r+1, c, steps+1, new HashSet<>(visited)));
            }
            else if(currentChar == '<'){
                if(inBounds(r, c-1, grid) && grid.get(r).get(c-1) != '#' && visited.contains(new Pair(r, c-1)) == false)
                    states.add(new ProgramState(r, c-1, steps+1, new HashSet<>(visited)));
            }
            else if(currentChar == '>'){
                if(inBounds(r, c+1, grid) && grid.get(r).get(c+1) != '#' && visited.contains(new Pair(r, c+1)) == false)
                    states.add(new ProgramState(r, c+1, steps+1, new HashSet<>(visited)));
            }
            visited.remove(currentSpot);
        }
        return maxSteps;
    }
    /*public static int scenicWalk(int r, int c, int endR, int endC, ArrayList<ArrayList<Character>> grid){
        HashSet<Pair> visited = new HashSet<>();
        return scenicWalkHelper(r, c, endR, endC, 0, visited, grid);
    }
    public static int scenicWalkHelper(int r, int c, int endR, int endC, int steps, HashSet<Pair> visited, ArrayList<ArrayList<Character>> grid){
        if(r == endR && c == endC){
            return steps;
        }
        Pair currentSpot = new Pair(r, c);
        char currentChar = grid.get(r).get(c);
        int maxSteps = Integer.MIN_VALUE;
        visited.add(currentSpot);
        if(currentChar == '.'){
            if(inBounds(r-1, c, grid) && grid.get(r-1).get(c) != '#' && visited.contains(new Pair(r-1, c)) == false)
                maxSteps = Math.max(maxSteps, scenicWalkHelper(r-1, c, endR, endC, steps+1, visited, grid));
            if(inBounds(r+1, c, grid) && grid.get(r+1).get(c) != '#' && visited.contains(new Pair(r+1, c)) == false)
                maxSteps = Math.max(maxSteps, scenicWalkHelper(r+1, c, endR, endC, steps+1, visited, grid));
            if(inBounds(r, c-1, grid) && grid.get(r).get(c-1) != '#' && visited.contains(new Pair(r, c-1)) == false)
                maxSteps = Math.max(maxSteps, scenicWalkHelper(r, c-1, endR, endC, steps+1, visited, grid));
            if(inBounds(r, c+1, grid) && grid.get(r).get(c+1) != '#' && visited.contains(new Pair(r, c+1)) == false)
                maxSteps = Math.max(maxSteps, scenicWalkHelper(r, c+1, endR, endC, steps+1, visited, grid));
        }
        else if(currentChar == '^'){
            if(inBounds(r-1, c, grid) && grid.get(r-1).get(c) != '#' && visited.contains(new Pair(r-1, c)) == false)
                maxSteps = Math.max(maxSteps, scenicWalkHelper(r-1, c, endR, endC, steps+1, visited, grid));
        }
        else if(currentChar == 'v'){
            if(inBounds(r+1, c, grid) && grid.get(r+1).get(c) != '#' && visited.contains(new Pair(r+1, c)) == false)
                maxSteps = Math.max(maxSteps, scenicWalkHelper(r+1, c, endR, endC, steps+1, visited, grid));
        }
        else if(currentChar == '<'){
            if(inBounds(r, c-1, grid) && grid.get(r).get(c-1) != '#' && visited.contains(new Pair(r, c-1)) == false)
                maxSteps = Math.max(maxSteps, scenicWalkHelper(r, c-1, endR, endC, steps+1, visited, grid));
        }
        else if(currentChar == '>'){
            if(inBounds(r, c+1, grid) && grid.get(r).get(c+1) != '#' && visited.contains(new Pair(r, c+1)) == false)
                maxSteps = Math.max(maxSteps, scenicWalkHelper(r, c+1, endR, endC, steps+1, visited, grid));
        }
        visited.remove(currentSpot);
        return maxSteps;
    }*/
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
        Pair p = (Pair) o;
        return (r == p.r) && (c == p.c);
    }
    @Override
    public int hashCode(){
        return Integer.hashCode(r) + Integer.hashCode(c);
    }
    public String toString(){
        return r + " " + c;
    }
}
class ProgramState{
    public int r;
    public int c;
    public int steps;
    public HashSet<Pair> visited;
    public ProgramState(int row, int col, int s, HashSet<Pair> v){
        r = row;
        c = col;
        steps = s;
        visited = v;
    }
}