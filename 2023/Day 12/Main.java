import java.util.*;
import java.io.File;
import java.lang.Math;
import java.lang.Thread;

public class Main{
    public static void main (String[] args) throws Exception{
        Scanner input = new Scanner(new File("b.txt"));
        long sum = 0;
        long lineCounter = 0;
        ArrayList<HotSpringRecord> threads = new ArrayList<>();
        long cores = Runtime.getRuntime().availableProcessors();
        System.out.println("NUMBER OF CPUS: " + cores);
        final long startTime = System.currentTimeMillis();
        while(input.hasNextLine()){
            String line = input.nextLine().trim();
            lineCounter++;
            //System.out.println(line);
            String springs = line.split(" ")[0];
            ArrayList<Long> springSizes = new ArrayList<>();
            Scanner parser = new Scanner(line.split(" ")[1].replaceAll(",", " "));
            while(parser.hasNextLong()){
                springSizes.add(parser.nextLong());
            }
            
            // Part 2
            String toRepeatSpring = springs;
            ArrayList<Long> toRepeatSizes = new ArrayList<>(springSizes);
            for(long i = 0; i < 4; i++){
                springs += "?" + toRepeatSpring;
                for(long size: toRepeatSizes){
                    springSizes.add(size);
                }
            }
            // Part 2 end
            HotSpringRecord thread = new HotSpringRecord(springs, springSizes, lineCounter);
            //System.out.println("RUNNING THREAD: " + lineCounter);
            thread.start();
            threads.add(thread);
        }
        for(int i = 0; i < threads.size(); i++){ // wait for all threads
            threads.get(i).join();
            sum += threads.get(i).combinations;
        }
        final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime));
        System.out.println(sum);
    }
}
class HotSpringRecord extends Thread{

    public String currentSpring;
    public ArrayList<Long> springSizes;
    public long combinations;
    public long lineNumber;
    public HotSpringRecord(String s, ArrayList<Long> sizes, long num){
        currentSpring = s;
        springSizes = sizes;
        lineNumber = num;
        combinations = -1;
    }

    @Override
    public void run(){
        combinations = countMatches(currentSpring + '.', 0, 0, '.', springSizes);
        System.out.println("FINISHED LINE: " + lineNumber + " | OUTPUT: " + combinations);
        this.interrupt();
    }

    private static long countMatches(String currentSpring, int position, long pickedUpHashtags, char prevChar, ArrayList<Long> springSizes){
        if(position == currentSpring.length()){
            if(springSizes.isEmpty()){ // correct number of hashtags
                return 1;
            }
            else{ // not enough hashtags
                return 0;
            }
        }
        if(currentSpring.charAt(position) == '.'){
            if(prevChar == '#'){ // drop your hashtags
                if(springSizes.isEmpty()){
                    return 0;
                }
                else if(springSizes.get(0) != pickedUpHashtags){ // mismatch
                    return 0;
                }
                else{ // mark contiguous springs
                    springSizes.remove(0);
                    return countMatches(currentSpring, position + 1, 0, currentSpring.charAt(position), springSizes);
                }
            }
            else{
                return countMatches(currentSpring, position + 1, pickedUpHashtags, currentSpring.charAt(position), springSizes);
            }
        }
        else if(currentSpring.charAt(position) == '#'){
            return countMatches(currentSpring, position + 1, pickedUpHashtags + 1, currentSpring.charAt(position), springSizes);
        }
        // currentSpring.charAt(position) == '?'
        // new recursion branches
        long count = 0;
        String str1 = currentSpring.substring(0, position) + '.' + currentSpring.substring(position + 1);
        String str2 = currentSpring.substring(0, position) + '#' + currentSpring.substring(position + 1);
        //System.out.println("STR1: " + str1 + "| STR2: " + str2);  
        count += countMatches(str1, position, pickedUpHashtags, prevChar, new ArrayList<Long>(springSizes));
        count += countMatches(str2, position, pickedUpHashtags, prevChar, new ArrayList<Long>(springSizes));
        return count;
    }
}