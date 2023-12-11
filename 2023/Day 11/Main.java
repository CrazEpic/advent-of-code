import java.util.*;
import java.io.File;
import java.lang.Math;

public class Main{
	public static void main(String[] args) throws Exception{
		Scanner input = new Scanner(new File("b.txt"));
		ArrayList<ArrayList<Character>> grid = new ArrayList<ArrayList<Character>>();
		while(input.hasNextLine()){
		    String line = input.nextLine().trim();
		    grid.add(new ArrayList<Character>());
		    for(int i = 0; i < line.length(); i++){
                grid.get(grid.size()-1).add(line.charAt(i));
		    }
		}
		//cosmisExpand(grid, true); // true for a true domain expansion, 1000000x
		
		// part 2 edit: instead of actually expanding, just keep track
		long[][][] trueCoordinates = pretendCosmicExpand(grid, 1000000);
		ArrayList<Node> allGalaxies = new ArrayList<Node>();
		for(int r = 0; r < grid.size(); r++){
		    for(int c = 0; c < grid.get(0).size(); c++){
		        if(grid.get(r).get(c) == '#'){
		            //allGalaxies.add(new Node(r, c, -1));
		            allGalaxies.add(new Node(trueCoordinates[r][c][0], trueCoordinates[r][c][1], -1));
		        }
		    }
		}
		long sumPaths = 0;
// 		for(Node n: allGalaxies){
// 		    ArrayList<Node> nodesFromSource = shortestPaths(n.row, n.col, grid);
// 		    for(Node d: nodesFromSource){
// 		        sumPaths += d.dist;
// 		    }
// 		}
        for(int n = 0; n < allGalaxies.size(); n++){
            Node source = allGalaxies.get(n);
            for(int d = 0; d < allGalaxies.size(); d++){
                if(n == d)
                    continue;
                Node dest = allGalaxies.get(d);
                sumPaths += Math.abs(source.row - dest.row) + Math.abs(source.col - dest.col);
            }
        }
		sumPaths /= 2;
		System.out.println(sumPaths);
	}
	public static long[][][] pretendCosmicExpand(ArrayList<ArrayList<Character>> grid, long expansionSize){
	    expansionSize -= 1;
	    long[][][] trueCoordinates = new long[grid.size()][grid.get(0).size()][2]; // each [r][c] holds a true [r][c]
	    for(int r = 0; r < grid.size(); r++){
	        for(int c = 0; c < grid.get(0).size(); c++){
                trueCoordinates[r][c][0] = r;
                trueCoordinates[r][c][1] = c;
	        }
	    }
	    // Insert rows
	    for(int r = 0; r < grid.size(); r++){
	        boolean emptyRow = true;
	        for(int c = 0; c < grid.get(r).size(); c++){
	            if(grid.get(r).get(c) != '.'){
	                emptyRow = false;
	                break;
	            }
	        }
	        if(emptyRow){
	            for(int i = r + 1; i < grid.size(); i++){
	                for(int j = 0; j < grid.get(0).size(); j++){
        	            trueCoordinates[i][j][0] += expansionSize;
        	        }
	            }
	        }
	    }
	    // Insert cols
	    for(int c = 0; c < grid.get(0).size(); c++){
	        boolean emptyCol = true;
	        for(int r = 0; r < grid.size(); r++){
	            if(grid.get(r).get(c) != '.'){
	                emptyCol = false;
	                break;
	            }
	        }
	        if(emptyCol){
                for(int i = c + 1; i < grid.get(0).size(); i++){
	                for(int j = 0; j < grid.size(); j++){
        	            trueCoordinates[j][i][1] += expansionSize;
        	        }
	            }
	        }
	    }
	    return trueCoordinates;
	}
	
	/*public static ArrayList<Node> shortestPaths(long startR, long startC, ArrayList<ArrayList<Character>> grid){
        ArrayList<Node> shortestPathsFromSource = new ArrayList<Node>();
        Node source = new Node(startR, startC, 0);
        Queue<Node> q = new LinkedList<>();
        boolean[][] visited = new boolean[grid.size()][grid.get(0).size()];
        for(long r = 0; r < grid.size(); r++)
            for(long c = 0; c < grid.get(0).size(); c++)
                visited[r][c] = false;
        visited[startR][startC] = true;
        q.add(source);
        while(!q.isEmpty()){
            Node n = q.remove();
            if(grid.get(n.row).get(n.col) == '#'){
                shortestPathsFromSource.add(n);
            }
            if(inBounds(n.row-1, n.col, grid.size(), grid.get(0).size()) && !visited[n.row-1][n.col]){
               q.add(new Node(n.row-1, n.col, n.dist+1));
               visited[n.row-1][n.col] = true;
            }
            if(inBounds(n.row+1, n.col, grid.size(), grid.get(0).size()) && !visited[n.row+1][n.col]){
               q.add(new Node(n.row+1, n.col, n.dist+1));
               visited[n.row+1][n.col] = true;
            }
            if(inBounds(n.row, n.col-1, grid.size(), grid.get(0).size()) && !visited[n.row][n.col-1]){
               q.add(new Node(n.row, n.col-1, n.dist+1));
               visited[n.row][n.col-1] = true;
            }
            if(inBounds(n.row, n.col+1, grid.size(), grid.get(0).size()) && !visited[n.row][n.col+1]){
               q.add(new Node(n.row, n.col+1, n.dist+1));
               visited[n.row][n.col+1] = true;
            }
        }
        return shortestPathsFromSource;
	}
	public static boolean inBounds(long r, long c, long maxR, long maxC){
	    return (0 <= r && r < maxR) && (0 <= c && c < maxC);
	}
	public static void cosmisExpand(ArrayList<ArrayList<Character>> grid, boolean domainExpansion){
	    // Insert rows
	    for(long r = 0; r < grid.size(); r++){
	        boolean emptyRow = true;
	        for(long c = 0; c < grid.get(r).size(); c++){
	            if(grid.get(r).get(c) != '.'){
	                emptyRow = false;
	                break;
	            }
	        }
	        if(emptyRow){
	            // ArrayList<Character> newRow = new ArrayList<Character>();
	            // for(long i = 0; i < grid.get(r).size(); i++){
    	        //     newRow.add('.');
    	        // }
    	        // grid.add(r, newRow);
    	        // r++;
    	        if(domainExpansion){
        	        for(long counter = 0; counter < 1000000; counter++){
        	            ArrayList<Character> newRow = new ArrayList<Character>();
                        for(long i = 0; i < grid.get(r).size(); i++){
                            newRow.add('.');
                        }
                        grid.add(r, newRow);
                        r++;
        	        }
    	        }
    	        else{
    	            ArrayList<Character> newRow = new ArrayList<Character>();
	                for(long i = 0; i < grid.get(r).size(); i++){
    	                newRow.add('.');
    	            }
    	            grid.add(r, newRow);
    	            r++;
    	        }
	        }
	    }
	    // Insert cols
	    for(long c = 0; c < grid.get(0).size(); c++){
	        boolean emptyCol = true;
	        for(long r = 0; r < grid.size(); r++){
	            if(grid.get(r).get(c) != '.'){
	                emptyCol = false;
	                break;
	            }
	        }
	        if(emptyCol){
	           if(domainExpansion){
    	           for(long counter = 0; counter < 1000000; counter++){
                        for(long i = 0; i < grid.size(); i++){
        	                grid.get(i).add(c, '.');
        	            }
    	                c++;
    	           }
	           }
	           else{
                    for(long i = 0; i < grid.size(); i++)
                        grid.get(i).add(c, '.');
                    c++;
	           }
	        }
	    }
	}*/
	public static String getGridString(ArrayList<ArrayList<Character>> grid){
	    String output = "";
	    for(int r = 0; r < grid.size(); r++){
	        for(int c = 0; c < grid.get(r).size(); c++){
	            output += grid.get(r).get(c);
	        }
	        output += "\n";
	    }
	    return output;
	}
}

class Node{
    public long row;
    public long col;
    public long dist;
    public Node(long r, long c, long d){
        row = r; 
        col = c; 
        dist = d;
    }
}
