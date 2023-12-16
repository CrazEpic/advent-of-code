import java.util.*;
import java.io.File;
import java.lang.Math;

public class Main{
    public static void main(String[] args) throws Exception{
        Scanner input = new Scanner(new File("b.txt"));
        ArrayList<ArrayList<Character>> grid = new ArrayList<>();
        while(input.hasNextLine()){
            String line = input.nextLine();
            grid.add(new ArrayList<>());
            for(int i = 0; i < line.length(); i++){
                grid.get(grid.size()-1).add(line.charAt(i));
            }
        }
        int maxCount = Integer.MIN_VALUE;
        //maxCount = simulateBeams(0, 0, 'R', grid);
        for(int i = 0; i < grid.size(); i++) // left side
            maxCount = Math.max(maxCount, simulateBeams(i, 0, 'R', grid));
        for(int i = 0; i < grid.size(); i++) // right side
            maxCount = Math.max(maxCount, simulateBeams(i, grid.get(0).size()-1, 'L', grid));
        for(int i = 0; i < grid.get(0).size(); i++) // top side
            maxCount = Math.max(maxCount, simulateBeams(0, i, 'D', grid));
        for(int i = 0; i < grid.get(0).size(); i++) // bottom side
            maxCount = Math.max(maxCount, simulateBeams(grid.size()-1, i, 'U', grid));
        System.out.println(maxCount);
    }
    public static int simulateBeams(int startR, int startC, char startDirection, ArrayList<ArrayList<Character>> grid){
        ArrayList<ArrayList<EnergizedTile>> energizedTiles = new ArrayList<>();
        for(int r = 0; r < grid.size(); r++){
            energizedTiles.add(new ArrayList<>());
            for(int c = 0; c < grid.get(r).size(); c++){
                energizedTiles.get(energizedTiles.size()-1).add(new EnergizedTile());
            }
        }
        ArrayList<Beam> beams = new ArrayList<>();
        beams.add(new Beam(startR, startC, startDirection));
        rotateBeam(beams.get(beams.size()-1), grid); // rotates if necessary, if starting point is at a mirror
        while(!beams.isEmpty()){
            for(int i = beams.size()-1; i >= 0; i--){
                Beam b = beams.get(i);
                if(energizedTiles.get(b.r).get(b.c).isEnergizedDirection(b.direction)){
                    // cycle
                    beams.remove(i);
                    continue;
                }
                energizedTiles.get(b.r).get(b.c).setEnergizedDirection(b.direction);
                if(moveBeam(b, grid)){
                    rotateBeam(b, grid); // rotates if necessary
                    if(grid.get(b.r).get(b.c) == '|' || grid.get(b.r).get(b.c) == '-'){
                        beams.add(new Beam(b.r, b.c, getOppositeDirection(b.direction)));
                    }
                }
                else{
                    beams.remove(i);
                    continue;
                }
            }
        }
        return countEnergized(energizedTiles);
    }
    public static boolean moveBeam(Beam b, ArrayList<ArrayList<Character>> grid){
        char direction = b.direction;
        int row = b.r;
        int col = b.c;
        switch(direction){
            case 'U': row--; break;
            case 'D': row++; break;
            case 'L': col--; break;
            case 'R': col++; break;
        }
        if(inBounds(row, col, grid.size(), grid.get(0).size())){
            b.r = row;
            b.c = col;
            return true;
        }
        return false;
    }
    public static char rotateBeam(Beam b, ArrayList<ArrayList<Character>> grid){
        int r = b.r;
        int c = b.c;
        if(grid.get(r).get(c) == '.')
            return b.direction;
        if(grid.get(r).get(c) == '/'){
            // up -> right
            // down -> left
            // left -> down
            // right -> up
            switch(b.direction){
                case 'U': b.direction = 'R'; break;
                case 'D': b.direction = 'L'; break;
                case 'L': b.direction = 'D'; break;
                case 'R': b.direction = 'U'; break;
            }
        }
        else if(grid.get(r).get(c) == '\\'){
            // up -> left
            // down -> right
            // left -> up
            // right -> down
            switch(b.direction){
                case 'U': b.direction = 'L'; break;
                case 'D': b.direction = 'R'; break;
                case 'L': b.direction = 'U'; break;
                case 'R': b.direction = 'D'; break;
            }
        }
        else if(grid.get(r).get(c) == '|'){
            switch(b.direction){
                case 'U': b.direction = 'U'; break;
                case 'D': b.direction = 'D'; break;
                case 'L': b.direction = 'U'; break;
                case 'R': b.direction = 'U'; break;
            }
        }
        else if(grid.get(r).get(c) == '-'){
            switch(b.direction){
                case 'U': b.direction = 'L'; break;
                case 'D': b.direction = 'L'; break;
                case 'L': b.direction = 'L'; break;
                case 'R': b.direction = 'R'; break;
            }
        }
        return b.direction;
    }
    public static char getOppositeDirection(char d){
        switch(d){
            case 'U': return 'D';
            case 'D': return 'U';
            case 'L': return 'R';
            case 'R': return 'L';
        }
        return 'X';
    }

    public static boolean inBounds(int r, int c, int maxR, int maxC){
        return (0 <= r && r < maxR) && (0 <= c && c < maxC);
    }
    public static int countEnergized(ArrayList<ArrayList<EnergizedTile>> energizedTiles){
        int count = 0;
        for(int r = 0; r < energizedTiles.size(); r++)
            for(int c = 0; c < energizedTiles.get(r).size(); c++)
                if(energizedTiles.get(r).get(c).energized)
                    count++;
        return count;
    }
    public static void printEnergizedGrid(ArrayList<ArrayList<EnergizedTile>> grid){
        for(int r = 0; r < grid.size(); r++){
            for(int c = 0; c < grid.get(r).size(); c++){
                if(grid.get(r).get(c).energized)
                    System.out.print('#');
                else
                    System.out.print('.');
            }
            System.out.println("");
        }
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

class Beam{
    public int r;
    public int c;
    public char direction; // U, D, L, R

    public Beam(int row, int col, char d){
        r = row;
        c = col;
        direction = d;
    }
}

class EnergizedTile{
    public boolean energized;
    public boolean up;
    public boolean down;
    public boolean right;
    public boolean left;
    public EnergizedTile(){
        energized = false;
        up = false;
        down = false;
        right = false;
        left = false;
    }
    public boolean isEnergizedDirection(char direction){
        if(energized == false)
            return false;
        switch(direction){
            case 'U': return up;
            case 'D': return down;
            case 'L': return left;
            case 'R': return right;
        }
        return false;
    }
    public void setEnergizedDirection(char direction){
        energized = true;
        switch(direction){
            case 'U': up = true; break;
            case 'D': down = true; break;
            case 'L': left = true; break;
            case 'R': right = true; break;
        }
    }
}