import java.util.*;
import java.io.File;
import java.lang.Math;

// NOTE THIS SOLUTION IS NOT GENERALIZED
// THE ADVENT OF CODE FOR DAY 8 WAS CLEAN TO DO THIS

public class Main{
	public static void main(String[] args) throws Exception{
        Scanner input = new Scanner(new File("b.txt"));
        String instructions = input.nextLine();
        input.nextLine();
        HashMap<String, StringPair> network = new HashMap<String, StringPair>();
        while(input.hasNextLine()){
            String line = input.nextLine();
            line = line.replaceAll("[=(),]", "");
            Scanner parser = new Scanner(line);
            String source = parser.next();
            String left = parser.next();
            String right = parser.next();
            network.put(source, new StringPair(left, right));
            //System.out.println(line);
        }
        
        long steps = 0;
        int instructionIndex = 0;
        // String current = "AAA";
        // String end = "ZZZ";
        // while(!current.equals(end)){
        //     if(instructions.charAt(instructionIndex % instructions.length()) == 'L'){
        //         current = network.get(current).left;
        //     }
        //     else{ // right
        //         current = network.get(current).right;
        //     }
        //     instructionIndex++;
        //     steps++;
        // }
        ArrayList<String> positions = new ArrayList<String>();
        ArrayList<Long> positionsCycles = new ArrayList<Long>(); // keep cycles to get to end of all positions 
        // if all cycles defined, then take LCM of all cycle lengths to find synchronized length to get to end
        for(Map.Entry<String, StringPair> entry : network.entrySet()){
            String key = entry.getKey();
            if(key.charAt(2) == 'A'){
                positions.add(key);
                positionsCycles.add((long)(-1));
            }
        }
        while(!checkAllPositions(positions)){
            steps++;
            for(int i = 0; i < positions.size(); i++){
                if(instructions.charAt(instructionIndex) == 'L'){
                    positions.set(i, network.get(positions.get(i)).left);
                }
                else{ // right
                    positions.set(i, network.get(positions.get(i)).right);
                }
                if(positions.get(i).charAt(2) == 'Z'){
                    if(positionsCycles.get(i) == -1){
                        positionsCycles.set(i, steps);
                    }
                }
            }
            instructionIndex++;
            instructionIndex %= instructions.length();
            long totalSteps = checkAllPositionsCycles(positionsCycles);
            if(totalSteps != -1){
                steps = totalSteps;
                break;
            }
        }
        System.out.println(steps);
	}
	
	public static boolean checkAllPositions(ArrayList<String> positions){
	    for(String p: positions){
	        if(p.charAt(2) != 'Z')
	            return false;
	    }
	    return true;
	}
	public static long checkAllPositionsCycles(ArrayList<Long> positionsCycles){
	    for(Long p: positionsCycles){
	        if(p == -1){
	            return -1;
	        }
	    }
	    long[] arr = new long[positionsCycles.size()];
	    for(int i = 0; i < positionsCycles.size(); i++){
	        arr[i] = positionsCycles.get(i);
	    }
	    return lcm(arr);
	}
	
	// gcd and lcm are standard code, not mine
	// https://stackoverflow.com/questions/4201860/how-to-find-gcd-lcm-on-a-set-of-numbers
	public static long gcd(long a, long b){
        while (b > 0){
            long temp = b;
            b = a % b; // % is remainder
            a = temp;
        }
        return a;
    }
    public static long gcd(long[] input){
        long result = input[0];
        for(int i = 1; i < input.length; i++) result = gcd(result, input[i]);
        return result;
    }
    public static long lcm(long a, long b){
        return a * (b / gcd(a, b));
    }
    public static long lcm(long[] input){
        long result = input[0];
        for(int i = 1; i < input.length; i++) result = lcm(result, input[i]);
        return result;
    }
}

class StringPair{
    public String left;
    public String right;
    public StringPair(String l, String r){
        left = l;
        right = r;
    }
}