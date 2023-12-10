import java.util.*;
import java.io.File;
import java.lang.Math;
import java.awt.Point;

public class Main{
	public static void main(String[] args) throws Exception{
		Scanner input = new Scanner(new File("b.txt"));
		ArrayList<ArrayList<Tile>> grid = new ArrayList<ArrayList<Tile>>();
		Tile startingTile = new Tile('X', -1, -1);
		while(input.hasNextLine()){
		    grid.add(new ArrayList<Tile>());
		    String line = input.nextLine().trim();
		    for(int i = 0; i < line.length(); i++){
		        Tile t = new Tile(line.charAt(i), grid.size()-1, i);
		        if(t.symbol == 'S'){
		            startingTile = t;
		        }
		        grid.get(grid.size()-1).add(t);
		    }
		}
		String pipes = "|-LJ7FS";
		for(int i = 0; i < pipes.length(); i++){
		    startingTile.setSymbol(pipes.charAt(i));
		    int loopSize = checkLoop(startingTile.r, startingTile.c, grid);
		    if(loopSize >= 0){
		        System.out.println(loopSize/2);
		        break;
		    }
		}
		
		System.out.println(startingTile.symbol);
		
		// I need another section for part 2
		
		ArrayList<Tile> loopTiles = getLoop(startingTile.r, startingTile.c, grid);
		for(int r = 0; r < grid.size(); r++){
		    for(int c = 0; c < grid.get(0).size(); c++){
                if(grid.get(r).get(c).symbol != '.'){
                    if(!loopTiles.contains(grid.get(r).get(c))){ // clear unnecessary pipes
                        grid.get(r).get(c).setSymbol('.');
                    }
                }
		    }
		}
		
		char[][] detailedGrid = new char[grid.size() * 3][grid.get(0).size() * 3]; // each tile is now 3x3
		System.out.println(detailedGrid.length + " " + detailedGrid[0].length);
		for(int r = 0; r < grid.size(); r++){
		    for(int c = 0; c < grid.get(0).size(); c++){
		        detailedGrid[3*r+0][3*c+0] = '.'; detailedGrid[3*r+0][3*c+1] = '.'; detailedGrid[3*r+0][3*c+2] = '.';
		        detailedGrid[3*r+1][3*c+0] = '.'; detailedGrid[3*r+1][3*c+1] = '.'; detailedGrid[3*r+1][3*c+2] = '.';
		        detailedGrid[3*r+2][3*c+0] = '.'; detailedGrid[3*r+2][3*c+1] = '.'; detailedGrid[3*r+2][3*c+2] = '.';
		        if(grid.get(r).get(c).symbol == '|'){
		            detailedGrid[3*r+0][3*c+0] = ' '; 
		            detailedGrid[3*r+1][3*c+0] = ' '; 
		            detailedGrid[3*r+2][3*c+0] = ' ';
		        }
		        else if(grid.get(r).get(c).symbol == '-'){
		            detailedGrid[3*r+0][3*c+0] = ' '; 
		            detailedGrid[3*r+0][3*c+1] = ' '; 
		            detailedGrid[3*r+0][3*c+2] = ' ';
		        }
		        else if(grid.get(r).get(c).symbol == 'L'){
		            detailedGrid[3*r+0][3*c+1] = ' '; 
		            detailedGrid[3*r+1][3*c+1] = ' '; 
		            detailedGrid[3*r+1][3*c+2] = ' ';
		        }
		        else if(grid.get(r).get(c).symbol == 'J'){
		            detailedGrid[3*r+0][3*c+1] = ' '; 
		            detailedGrid[3*r+1][3*c+1] = ' '; 
		            detailedGrid[3*r+1][3*c+0] = ' ';
		        }
		        else if(grid.get(r).get(c).symbol == '7'){
		            detailedGrid[3*r+1][3*c+0] = ' '; 
		            detailedGrid[3*r+1][3*c+1] = ' '; 
		            detailedGrid[3*r+2][3*c+1] = ' ';
		        }
		        else if(grid.get(r).get(c).symbol == 'F'){
		            detailedGrid[3*r+1][3*c+2] = ' '; 
		            detailedGrid[3*r+1][3*c+1] = ' '; 
		            detailedGrid[3*r+2][3*c+1] = ' ';
		        }
		    }
		}
		for(int r = 0; r < detailedGrid.length; r++){
		    for(int c = 0; c < detailedGrid[0].length; c++){
		        System.out.print(detailedGrid[r][c]);
		    }
		    System.out.println("");
		}
		// BFS to mark disjoint union sets
		// hopefully I don't run out of letters
		// AND TREAT POINT AS POINT(r, c) and NOT POINT(x, y)
		
		// hopefully, there are only 2 disjoint sets
		Queue<Point> toVisit = new LinkedList<>();
		char currentChar = 'a' - 1;
		for(int r = 0; r < detailedGrid.length; r++){
		    for(int c = 0; c < detailedGrid[0].length; c++){
		        if(detailedGrid[r][c] == '.'){
		            currentChar++;
		            detailedGrid[r][c] = currentChar;
		            toVisit.add(new Point(r, c));
		            while(!(toVisit.isEmpty())){
		                Point currentPoint = toVisit.remove();
		                int row = (int)currentPoint.getX();
		                int col = (int)currentPoint.getY();
		                detailedGrid[row][col] = currentChar;
		                if(inBounds(row-1, col, detailedGrid.length, detailedGrid[0].length) && detailedGrid[row-1][col] == '.'){
		                    detailedGrid[row-1][col] = currentChar;
		                    toVisit.add(new Point(row-1, col));
		                }
		                if(inBounds(row+1, col, detailedGrid.length, detailedGrid[0].length) && detailedGrid[row+1][col] == '.'){
		                    detailedGrid[row+1][col] = currentChar;
		                    toVisit.add(new Point(row+1, col));
		                }
		                if(inBounds(row, col-1, detailedGrid.length, detailedGrid[0].length) && detailedGrid[row][col-1] == '.'){
		                    detailedGrid[row][col-1] = currentChar;
		                    toVisit.add(new Point(row, col-1));
		                }
		                if(inBounds(row, col+1, detailedGrid.length, detailedGrid[0].length) && detailedGrid[row][col+1] == '.'){
		                    detailedGrid[row][col+1] = currentChar;
		                    toVisit.add(new Point(row, col+1));
		                }
		            }
		        }  
		    }
		}
		// print new grid
		for(int r = 0; r < detailedGrid.length; r++){
		    for(int c = 0; c < detailedGrid[0].length; c++){
		        System.out.print(detailedGrid[r][c]);
		    }
		    System.out.println("");
		}
		HashMap<Character, Integer> wholeCellFreq = new HashMap<Character, Integer>();
		for(int r = 0; r < detailedGrid.length; r += 3){
		    for(int c = 0; c < detailedGrid[0].length; c += 3){
		        char ch = detailedGrid[r][c];
	            if( detailedGrid[r][c+1] == ch &&
	                detailedGrid[r][c+2] == ch &&
	                detailedGrid[r+1][c] == ch &&
	                detailedGrid[r+1][c+1] == ch &&
	                detailedGrid[r+1][c+2] == ch &&
	                detailedGrid[r+2][c] == ch &&
	                detailedGrid[r+2][c+1] == ch &&
	                detailedGrid[r+2][c+2] == ch){
                    if(wholeCellFreq.containsKey(ch)){
                        wholeCellFreq.put(ch, wholeCellFreq.get(ch) + 1);
                    }
                    else{
                        wholeCellFreq.put(ch, 1);
                    }
	            }
		    }
		}
		System.out.println("It's got to be one of these");
		for (Character name: wholeCellFreq.keySet()) {
            String key = name.toString();
            String value = wholeCellFreq.get(name).toString();
            System.out.println("Key-Value: " + key + " " + value);
        }
	}
	
	public static boolean inBounds(int r, int c, int maxR, int maxC){
	    return (0 <= r && r < maxR) && (0 <= c && c < maxC);
	}
	public static int checkLoop(int startR, int startC, ArrayList<ArrayList<Tile>> grid){
	    boolean[][] visited = new boolean[grid.size()][grid.get(0).size()];
	    for(int row = 0; row < grid.size(); row++)
	        for(int col = 0; col < grid.get(0).size(); col++)
	            visited[row][col] = false;
	   int steps = 0;
	   int r = startR;
	   int c = startC;
	   boolean checking = true;
	   while(checking){
	        Tile t = grid.get(r).get(c);
    	    visited[r][c] = true;
    	    steps++;
            // Assume only pipes with 2 directions, so only one of these will run, or none will run
            if(t.north && inBounds(r-1, c, grid.size(), grid.get(0).size()) && grid.get(r-1).get(c).south && !visited[r-1][c]){
                r--;
                continue;
            }
            if(t.south && inBounds(r+1, c, grid.size(), grid.get(0).size()) && grid.get(r+1).get(c).north && !visited[r+1][c]){
                r++;
                continue;
            }
            if(t.west && inBounds(r, c-1, grid.size(), grid.get(0).size()) && grid.get(r).get(c-1).east && !visited[r][c-1]){
                c--;
                continue;
            }
            if(t.east && inBounds(r, c+1, grid.size(), grid.get(0).size()) && grid.get(r).get(c+1).west && !visited[r][c+1]){
                c++;
                continue;
            }
            
            checking = false;
            // If none runs, out of directions, we either got the loop or we didn't 
    	    if(t.north && inBounds(r-1, c, grid.size(), grid.get(0).size()) && (r-1 == startR && c == startC) && grid.get(r-1).get(c).south){
                return steps;
    	    }
            if(t.south && inBounds(r+1, c, grid.size(), grid.get(0).size()) && (r+1 == startR && c == startC) && grid.get(r+1).get(c).north){
                return steps;
            }
            if(t.west && inBounds(r, c-1, grid.size(), grid.get(0).size()) && (r == startR && c-1 == startC) && grid.get(r).get(c-1).east){
                return steps;
            }
            if(t.east && inBounds(r, c+1, grid.size(), grid.get(0).size()) && (r == startR && c+1 == startC) && grid.get(r).get(c+1).west){
                return steps;
            }
	    }
	    // no loop
        return -1;
	}
	public static ArrayList<Tile> getLoop(int startR, int startC, ArrayList<ArrayList<Tile>> grid){
	    ArrayList<Tile> tiles = new ArrayList<Tile>();
	    boolean[][] visited = new boolean[grid.size()][grid.get(0).size()];
	    for(int row = 0; row < grid.size(); row++)
	        for(int col = 0; col < grid.get(0).size(); col++)
	            visited[row][col] = false;
	   int steps = 0;
	   int r = startR;
	   int c = startC;
	   boolean checking = true;
	   while(checking){
	        Tile t = grid.get(r).get(c);
	        tiles.add(t);
    	    visited[r][c] = true;
    	    steps++;
            // Assume only pipes with 2 directions, so only one of these will run, or none will run
            if(t.north && inBounds(r-1, c, grid.size(), grid.get(0).size()) && grid.get(r-1).get(c).south && !visited[r-1][c]){
                r--;
                continue;
            }
            if(t.south && inBounds(r+1, c, grid.size(), grid.get(0).size()) && grid.get(r+1).get(c).north && !visited[r+1][c]){
                r++;
                continue;
            }
            if(t.west && inBounds(r, c-1, grid.size(), grid.get(0).size()) && grid.get(r).get(c-1).east && !visited[r][c-1]){
                c--;
                continue;
            }
            if(t.east && inBounds(r, c+1, grid.size(), grid.get(0).size()) && grid.get(r).get(c+1).west && !visited[r][c+1]){
                c++;
                continue;
            }
            
            checking = false;
            // If none runs, out of directions, we either got the loop or we didn't 
    	    if(t.north && inBounds(r-1, c, grid.size(), grid.get(0).size()) && (r-1 == startR && c == startC) && grid.get(r-1).get(c).south){
                return tiles;
    	    }
            if(t.south && inBounds(r+1, c, grid.size(), grid.get(0).size()) && (r+1 == startR && c == startC) && grid.get(r+1).get(c).north){
                return tiles;
            }
            if(t.west && inBounds(r, c-1, grid.size(), grid.get(0).size()) && (r == startR && c-1 == startC) && grid.get(r).get(c-1).east){
                return tiles;
            }
            if(t.east && inBounds(r, c+1, grid.size(), grid.get(0).size()) && (r == startR && c+1 == startC) && grid.get(r).get(c+1).west){
                return tiles;
            }
	    }
	    // no loop
        return null;
	}
}

class Tile{
    public char symbol;
    public boolean north;
    public boolean east;
    public boolean south;
    public boolean west;
    public int r;
    public int c;
    public Tile(char s, int r, int c){
        this.r = r;
        this.c = c;
        setSymbol(s);
    }
    public void setSymbol(char s){
        symbol = s;
        north = false;
        east = false;
        south = false;
        west = false;
        switch(s){
            case '|':
                north = true;
                south = true;
                break;
            case '-':
                west = true;
                east = true;
                break;
            case 'L':
                north = true;
                east = true;
                break;
            case 'J':
                north = true;
                west = true;
                break;
            case '7':
                west = true;
                south = true;
                break;
            case 'F':
                east = true;
                south = true;
                break;
            default:
                break;
        }
    }
}
