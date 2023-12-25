import java.util.*;
import java.io.File;
import java.lang.Math;

public class Main{
	public static void main(String[] args) throws Exception{
		Scanner input = new Scanner(new File("b.txt"));
		ArrayList<Sequence> sequences = new ArrayList<Sequence>();
		while(input.hasNextLine()){
		    String line = input.nextLine();
		    sequences.add(new Sequence(line));
		}
		int forwardsum = 0;
		int backwardsum = 0;
		for(int i = 0; i < sequences.size(); i++){
		    forwardsum += sequences.get(i).advanceSequence();
		    backwardsum += sequences.get(i).backAdvanceSequence();
		}
		System.out.println(forwardsum);
		System.out.println(backwardsum);
	}
}

class Sequence{
    public ArrayList<ArrayList<Integer>> differences;
    
    public Sequence(String sequence){
        differences = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> original = new ArrayList<Integer>();
        Scanner parser = new Scanner(sequence);
        while(parser.hasNextInt()){
            original.add(parser.nextInt());
        }
        differences.add(original);
        boolean reachedZeros = false;
        while(!reachedZeros){
            ArrayList<Integer> currentDiff = differences.get(differences.size()-1);
            ArrayList<Integer> newDiff = new ArrayList<Integer>();
            for(int i = 1; i < currentDiff.size(); i++){
                newDiff.add(currentDiff.get(i) - currentDiff.get(i-1));
            }
            differences.add(newDiff);
            boolean allZeros = true;
            for(int num: newDiff){
                if(num != 0){
                    allZeros = false;
                    break;
                }
            }
            if(allZeros){
                reachedZeros = true;
            }
        }
    }
    
    public int advanceSequence(){
        ArrayList<Integer> zeroRow = differences.get(differences.size()-1);
        zeroRow.add(0);
        for(int i = differences.size()-2; i >= 0; i--){
            ArrayList<Integer> currentDiff = differences.get(i);
            currentDiff.add(currentDiff.get(currentDiff.size()-1) 
                        + differences.get(i+1).get(differences.get(i+1).size()-1));
        }
        return differences.get(0).get(differences.get(0).size()-1);
    }
    
    public int backAdvanceSequence(){
        ArrayList<Integer> zeroRow = differences.get(differences.size()-1);
        zeroRow.add(0, 0);
        for(int i = differences.size()-2; i >= 0; i--){
            ArrayList<Integer> currentDiff = differences.get(i);
            currentDiff.add(0, currentDiff.get(0) 
                        - differences.get(i+1).get(0));
        }
        return differences.get(0).get(0);
    }
}
