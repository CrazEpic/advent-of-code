import java.util.*;
import java.io.File;
import java.lang.Math;

public class Main{
    public static void main(String[] args) throws Exception{
        Scanner input = new Scanner(new File("b.txt"));
        ArrayList<ArrayList<Integer>> grid = new ArrayList<>();
        while(input.hasNextLine()){
            String line = input.nextLine().strip();
            grid.add(new ArrayList<>());
            for(int i = 0; i < line.length(); i++)
                grid.get(grid.size()-1).add(Integer.parseInt(line.substring(i, i+1)));
        }
        System.out.println(traverse(grid));
        System.out.println(traverseUltraCrucibles(grid));
    }
    public static int traverse(ArrayList<ArrayList<Integer>> grid){
        HashSet<ProgramStateWithoutHeatLoss> visited = new HashSet<>();
        PriorityQueue<ProgramState> toVisit = new PriorityQueue<>();
        ProgramState initialState = new ProgramState(0, 0, 'X', 0, 0);
        toVisit.add(initialState);
        while(toVisit.isEmpty() == false){
            ProgramState currentState = toVisit.remove();
            int r = currentState.r;
            int c = currentState.c;
            char direction = currentState.direction;
            int stepDirection = currentState.stepsInDirection;
            int heatLoss = currentState.heatLoss;
            //System.out.println(heatLoss);
            if(r == grid.size()-1 && c == grid.get(grid.size()-1).size()-1)
                return heatLoss;
            if(visited.contains(new ProgramStateWithoutHeatLoss(currentState))){
                continue;
            }
            visited.add(new ProgramStateWithoutHeatLoss(currentState));
            if(stepDirection < 3 && direction != 'X'){ // keep going forward
                if(direction == 'U' && inBounds(r-1, c, grid))
                    toVisit.add(new ProgramState(r-1, c, 'U', stepDirection + 1, heatLoss + grid.get(r-1).get(c)));
                else if(direction == 'D' && inBounds(r+1, c, grid))
                    toVisit.add(new ProgramState(r+1, c, 'D', stepDirection + 1, heatLoss + grid.get(r+1).get(c)));
                else if(direction == 'L' && inBounds(r, c-1, grid))
                    toVisit.add(new ProgramState(r, c-1, 'L', stepDirection + 1, heatLoss + grid.get(r).get(c-1)));
                else if(direction == 'R' && inBounds(r, c+1, grid))
                    toVisit.add(new ProgramState(r, c+1, 'R', stepDirection + 1, heatLoss + grid.get(r).get(c+1)));
            }
            char reverseDirection = '?';
            switch(direction){
                case 'U': reverseDirection = 'D'; break;
                case 'D': reverseDirection = 'U'; break;
                case 'L': reverseDirection = 'R'; break;
                case 'R': reverseDirection = 'L'; break;
            }
            if(direction != 'U' && reverseDirection != 'U' && inBounds(r-1, c, grid))
                toVisit.add(new ProgramState(r-1, c, 'U', 1, heatLoss + grid.get(r-1).get(c)));
            if(direction != 'D' && reverseDirection != 'D' && inBounds(r+1, c, grid))
                toVisit.add(new ProgramState(r+1, c, 'D', 1, heatLoss + grid.get(r+1).get(c)));
            if(direction != 'L' && reverseDirection != 'L' && inBounds(r, c-1, grid))
                toVisit.add(new ProgramState(r, c-1, 'L', 1, heatLoss + grid.get(r).get(c-1)));
            if(direction != 'R' && reverseDirection != 'R' && inBounds(r, c+1, grid))
                toVisit.add(new ProgramState(r, c+1, 'R', 1, heatLoss + grid.get(r).get(c+1)));
        }
        return -1;
    }
    public static int traverseUltraCrucibles(ArrayList<ArrayList<Integer>> grid){
        HashSet<ProgramStateWithoutHeatLoss> visited = new HashSet<>();
        PriorityQueue<ProgramState> toVisit = new PriorityQueue<>();
        ProgramState initialState = new ProgramState(0, 0, 'X', 0, 0);
        toVisit.add(initialState);
        while(toVisit.isEmpty() == false){
            ProgramState currentState = toVisit.remove();
            int r = currentState.r;
            int c = currentState.c;
            char direction = currentState.direction;
            int stepDirection = currentState.stepsInDirection;
            int heatLoss = currentState.heatLoss;
            //System.out.println(heatLoss);
            if(r == grid.size()-1 && c == grid.get(grid.size()-1).size()-1 && stepDirection >= 4) // need to move at least 4 before can stop
                return heatLoss;
            if(visited.contains(new ProgramStateWithoutHeatLoss(currentState))){
                continue;
            }
            visited.add(new ProgramStateWithoutHeatLoss(currentState));
            if(stepDirection < 10 && direction != 'X'){ // keep going forward
                if(direction == 'U' && inBounds(r-1, c, grid))
                    toVisit.add(new ProgramState(r-1, c, 'U', stepDirection + 1, heatLoss + grid.get(r-1).get(c)));
                else if(direction == 'D' && inBounds(r+1, c, grid))
                    toVisit.add(new ProgramState(r+1, c, 'D', stepDirection + 1, heatLoss + grid.get(r+1).get(c)));
                else if(direction == 'L' && inBounds(r, c-1, grid))
                    toVisit.add(new ProgramState(r, c-1, 'L', stepDirection + 1, heatLoss + grid.get(r).get(c-1)));
                else if(direction == 'R' && inBounds(r, c+1, grid))
                    toVisit.add(new ProgramState(r, c+1, 'R', stepDirection + 1, heatLoss + grid.get(r).get(c+1)));
            }
            char reverseDirection = '?';
            switch(direction){
                case 'U': reverseDirection = 'D'; break;
                case 'D': reverseDirection = 'U'; break;
                case 'L': reverseDirection = 'R'; break;
                case 'R': reverseDirection = 'L'; break;
            }
            if(stepDirection >= 4 || direction == 'X'){ // if moved at least 4 or not moving
                if(direction != 'U' && reverseDirection != 'U' && inBounds(r-1, c, grid))
                    toVisit.add(new ProgramState(r-1, c, 'U', 1, heatLoss + grid.get(r-1).get(c)));
                if(direction != 'D' && reverseDirection != 'D' && inBounds(r+1, c, grid))
                    toVisit.add(new ProgramState(r+1, c, 'D', 1, heatLoss + grid.get(r+1).get(c)));
                if(direction != 'L' && reverseDirection != 'L' && inBounds(r, c-1, grid))
                    toVisit.add(new ProgramState(r, c-1, 'L', 1, heatLoss + grid.get(r).get(c-1)));
                if(direction != 'R' && reverseDirection != 'R' && inBounds(r, c+1, grid))
                    toVisit.add(new ProgramState(r, c+1, 'R', 1, heatLoss + grid.get(r).get(c+1)));
            }
        }
        return -1;
    }
    public static boolean inBounds(int r, int c, ArrayList<ArrayList<Integer>> grid){
        return inBounds(r, c, grid.size(), grid.get(0).size());
    }
    public static boolean inBounds(int r, int c, int maxR, int maxC){
        return (0 <= r && r < maxR) && (0 <= c && c < maxC);
    }
}
class ProgramState implements Comparable{
    public int r;
    public int c;
    public char direction;
    public int stepsInDirection;
    public int heatLoss;
    public ProgramState(int row, int col, char d, int s, int h){
        r = row;
        c = col;
        direction = d;
        stepsInDirection = s;
        heatLoss = h;
    }
    @Override
    public int compareTo(Object o){
        if(o == this)
            return 0;
        if(!(o instanceof ProgramState))
            return -1;
        ProgramState other = (ProgramState) o;
        return Integer.compare(heatLoss, other.heatLoss);
    }
}
class ProgramStateWithoutHeatLoss{
    public int r;
    public int c;
    public char direction;
    public int stepsInDirection;
    public ProgramStateWithoutHeatLoss(ProgramState p){
        r = p.r;
        c = p.c;
        direction = p.direction;
        stepsInDirection = p.stepsInDirection;
    }
    @Override
    public boolean equals(Object o){
        if(o == this)
            return true;
        if(!(o instanceof ProgramStateWithoutHeatLoss))
            return false;
        ProgramStateWithoutHeatLoss other = (ProgramStateWithoutHeatLoss) o;
        return (r == other.r) && (c == other.c) && (direction == other.direction) && 
                (stepsInDirection == other.stepsInDirection);
    }
    @Override
    public int hashCode(){ // java implementation -.-
        return Integer.hashCode(r) + Integer.hashCode(c) + Integer.hashCode(direction) + Integer.hashCode(stepsInDirection);
    }
}