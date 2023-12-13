import java.util.*;
import java.io.File;
import java.lang.Math;

public class Main{
    public static void main(String[] args) throws Exception{
        Scanner input = new Scanner(new File("b.txt"));
        ArrayList<ArrayList<ArrayList<Character>>> grids = new ArrayList<>();
        grids.add(new ArrayList<ArrayList<Character>>());
        while(input.hasNextLine()){
            String line = input.nextLine().trim();
            if(line.equals("")){
                grids.add(new ArrayList<ArrayList<Character>>());
                continue;
            }
            ArrayList<Character> row = new ArrayList<>();
            for(int i = 0; i < line.length(); i++)
                row.add(line.charAt(i));
            grids.get(grids.size()-1).add(row);
        }
        int sum = 0;
        for(ArrayList<ArrayList<Character>> grid: grids){
            int vertical = getVertical(grid);
            int horizontal = getHorizontal(grid);
            if(vertical != -1){
                sum += vertical + 1;
            }
            else if(horizontal != -1){
                sum += (horizontal + 1) * 100;
            }
        }
        System.out.println(sum);
        
        // Part 2 with 1 smudge
        int smudgeSum = 0;
        int counter = 0;
        for(ArrayList<ArrayList<Character>> grid: grids){
            boolean foundAnotherReflection = false;
            for(int r = 0; r < grid.size(); r++){
                for(int c = 0; c < grid.get(r).size(); c++){
                    int originalVertical = getVertical(grid);
                    int originalHorizontal = getHorizontal(grid);
                    if(grid.get(r).get(c) == '#'){
                        grid.get(r).set(c, '.');
                        int vertical = getDifferentVertical(grid, originalVertical);
                        int horizontal = getDifferentHorizontal(grid, originalHorizontal);
                        if(vertical != -1  && vertical != originalVertical){
                            foundAnotherReflection = true;
                            smudgeSum += vertical + 1;
                            break;
                        }
                        else if(horizontal != -1 && horizontal != originalHorizontal){
                            foundAnotherReflection = true;
                            smudgeSum += (horizontal + 1) * 100;
                            break;
                        }
                        else{
                            grid.get(r).set(c, '#');
                        }
                    }
                    else if(grid.get(r).get(c) == '.'){
                        grid.get(r).set(c, '#');
                        int vertical = getDifferentVertical(grid, originalVertical);
                        int horizontal = getDifferentHorizontal(grid, originalHorizontal);
                        if(vertical != -1 && vertical != originalVertical){
                            foundAnotherReflection = true;
                            smudgeSum += vertical + 1;
                            break;
                        }
                        else if(horizontal != -1 && horizontal != originalHorizontal){
                            foundAnotherReflection = true;
                            smudgeSum += (horizontal + 1) * 100;
                            break;
                        }
                        else{
                            grid.get(r).set(c, '.');
                        }
                    }
                }
                if(foundAnotherReflection){
                    counter++;
                    break;
                }
            }
            if(foundAnotherReflection == false){
                printGrid(grid);
                System.out.println("");
            }
        }
        System.out.println(smudgeSum);
        System.out.println(grids.size());
        System.out.println(counter);
    }
    public static void printGrid(ArrayList<ArrayList<Character>> grid){
        for(int r = 0; r < grid.size(); r++){
            for(int c = 0; c < grid.get(r).size(); c++){
                System.out.print(grid.get(r).get(c)); 
            }
            System.out.println("");
        }
    }
    public static int getVertical(ArrayList<ArrayList<Character>> grid){
        double minDistFromMiddle = Double.MAX_VALUE;
        int leftOfVertical = -1;
        int leftColumn = 0;
        while(leftColumn < grid.get(0).size() - 1){
            if(checkSameColumns(grid, leftColumn, leftColumn + 1)){
                int offset = 1;
                boolean validReflection = true;
                while(0 <= leftColumn - offset && leftColumn + 1 + offset < grid.get(0).size()){
                    if(!checkSameColumns(grid, leftColumn - offset, leftColumn + 1 + offset)){
                        validReflection = false;
                        break;
                    }
                    offset++;
                }
                if(validReflection){
                    double distFromMiddle = Math.abs(leftColumn - (grid.get(0).size() - 1) / 2.0);
                    if(distFromMiddle < minDistFromMiddle){
                        leftOfVertical = leftColumn;
                        minDistFromMiddle = distFromMiddle;
                    }
                }
            }
            leftColumn++;
        }
        return leftOfVertical;
    }
    public static int getDifferentVertical(ArrayList<ArrayList<Character>> grid, int originalVertical){
        double minDistFromMiddle = Double.MAX_VALUE;
        int leftOfVertical = -1;
        int leftColumn = 0;
        while(leftColumn < grid.get(0).size() - 1){
            if(checkSameColumns(grid, leftColumn, leftColumn + 1)){
                int offset = 1;
                boolean validReflection = true;
                while(0 <= leftColumn - offset && leftColumn + 1 + offset < grid.get(0).size()){
                    if(!checkSameColumns(grid, leftColumn - offset, leftColumn + 1 + offset)){
                        validReflection = false;
                        break;
                    }
                    offset++;
                }
                if(validReflection && leftColumn != originalVertical){
                    double distFromMiddle = Math.abs(leftColumn - (grid.get(0).size() - 1) / 2.0);
                    if(distFromMiddle < minDistFromMiddle){
                        leftOfVertical = leftColumn;
                        minDistFromMiddle = distFromMiddle;
                    }
                }
            }
            leftColumn++;
        }
        return leftOfVertical;
    }
    public static boolean checkSameColumns(ArrayList<ArrayList<Character>> grid, int col1, int col2){
        for(int r = 0; r < grid.size(); r++){
            if(grid.get(r).get(col1) != grid.get(r).get(col2)){
                return false;
            }
        }
        return true;
    }
    public static int getHorizontal(ArrayList<ArrayList<Character>> grid){
        double minDistFromMiddle = Double.MAX_VALUE;
        int upOfHorizontal = -1;
        int upRow = 0;
        while(upRow < grid.size() - 1){
            if(checkSameRows(grid, upRow, upRow + 1)){
                int offset = 1;
                boolean validReflection = true;
                while(0 <= upRow - offset && upRow + 1 + offset < grid.size()){
                    if(!checkSameRows(grid, upRow - offset, upRow + 1 + offset)){
                        validReflection = false;
                        break;
                    }
                    offset++;
                }
                if(validReflection){
                    double distFromMiddle = Math.abs(upRow - (grid.size() - 1) / 2.0);
                    if(distFromMiddle < minDistFromMiddle){
                        upOfHorizontal = upRow;
                        minDistFromMiddle = distFromMiddle;
                    }
                }
            }
            upRow++;
        }
        return upOfHorizontal;
    }
    public static int getDifferentHorizontal(ArrayList<ArrayList<Character>> grid, int originalHorizontal){
        double minDistFromMiddle = Double.MAX_VALUE;
        int upOfHorizontal = -1;
        int upRow = 0;
        while(upRow < grid.size() - 1){
            if(checkSameRows(grid, upRow, upRow + 1)){
                int offset = 1;
                boolean validReflection = true;
                while(0 <= upRow - offset && upRow + 1 + offset < grid.size()){
                    if(!checkSameRows(grid, upRow - offset, upRow + 1 + offset)){
                        validReflection = false;
                        break;
                    }
                    offset++;
                }
                if(validReflection && upRow != originalHorizontal){
                    double distFromMiddle = Math.abs(upRow - (grid.size() - 1) / 2.0);
                    if(distFromMiddle < minDistFromMiddle){
                        upOfHorizontal = upRow;
                        minDistFromMiddle = distFromMiddle;
                    }
                }
            }
            upRow++;
        }
        return upOfHorizontal;
    }
    public static boolean checkSameRows(ArrayList<ArrayList<Character>> grid, int row1, int row2){
        for(int c = 0; c < grid.get(0).size(); c++){
            if(grid.get(row1).get(c) != grid.get(row2).get(c)){
                return false;
            }
        }
        return true;
    }
}