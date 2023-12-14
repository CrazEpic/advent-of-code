import java.util.*;
import java.io.File;
import java.lang.Math;

public class Main{
    public static void main (String[] args) throws Exception{
        Scanner input = new Scanner(new File("b.txt"));
        long sum = 0;
        long lineCounter = 0;
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
            long count = countMatches(springs + '.', 0, '.', springSizes);
            System.out.println(count);
            sum += count;
        }
        final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime));
        System.out.println(sum);
        // for (String name: memoize.keySet()) {
        //     System.out.println("ALL MAPPINGS FOR " + name);
        //     HashMap<ArrayList<Long>, Long> map = memoize.get(name);
        //     for (ArrayList<Long> m: map.keySet()) {
        //         String key = m.toString();
        //         String value = map.get(m).toString();
        //         System.out.printf("%10s%10s\n", key, value);
        //     }
        //     System.out.println("");
        // }
    }

    public static HashMap<String, HashMap<ArrayList<Long>, Long>> memoize = new HashMap<String, HashMap<ArrayList<Long>, Long>>();

    public static long countMatches(String currentSpring, long pickedUpHashtags, char prevChar, ArrayList<Long> springSizes){
        if(currentSpring.equals("")){
            if(springSizes.isEmpty()){ // correct number of hashtags
                return 1;
            }
            else{ // not enough hashtags
                return 0;
            }
        }
        if(currentSpring.charAt(0) == '.'){
            if(prevChar == '#'){ // drop your hashtags
                if(springSizes.isEmpty()){
                    return 0;
                }
                else if(springSizes.get(0) != pickedUpHashtags){ // mismatch
                    return 0;
                }
                else{ // mark contiguous springs
                    springSizes.remove(0);
                    return countMatches(currentSpring.substring(1), 0, currentSpring.charAt(0), springSizes);
                }
            }
            else{
                return countMatches(currentSpring.substring(1), pickedUpHashtags, currentSpring.charAt(0), springSizes);
            }
        }
        else if(currentSpring.charAt(0) == '#'){
            if(prevChar == '.'){ // starting a hot spring
                if(memoize.containsKey(currentSpring)){
                    if(memoize.get(currentSpring).containsKey(springSizes)){
                        return memoize.get(currentSpring).get(springSizes);
                    }
                }
                ArrayList<Long> copySpringSizes = new ArrayList<>(springSizes);
                long countFromHotSpringStart = countMatches(currentSpring.substring(1), pickedUpHashtags + 1, currentSpring.charAt(0), springSizes);
                if(!(memoize.containsKey(currentSpring)))
                    memoize.put(currentSpring, new HashMap<>());
                if(!memoize.get(currentSpring).containsKey(copySpringSizes))
                    memoize.get(currentSpring).put(copySpringSizes, countFromHotSpringStart);
                return countFromHotSpringStart;
            }
            else{
                return countMatches(currentSpring.substring(1), pickedUpHashtags + 1, currentSpring.charAt(0), springSizes);
            }
        }
        // currentSpring.charAt(position) == '?'
        // new recursion branches
        String str1 = '.' + currentSpring.substring(1);
        String str2 = '#' + currentSpring.substring(1);
        //System.out.println("STR1: " + str1 + "| STR2: " + str2);  
        long count1 = countMatches(str1, pickedUpHashtags, prevChar, new ArrayList<Long>(springSizes));
        long count2 = countMatches(str2, pickedUpHashtags, prevChar, new ArrayList<Long>(springSizes));
        return count1 + count2;
    }
}