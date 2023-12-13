import java.util.*;
import java.io.File;
import java.lang.Math;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main{
    public static void main (String[] args) throws Exception{
        Scanner input = new Scanner(new File("a.txt"));
        int sum = 0;
        while(input.hasNextLine()){
            String line = input.nextLine().trim();
            System.out.println(line);
            String springs = line.split(" ")[0];
            ArrayList<Integer> springSizes = new ArrayList<>();
            Scanner parser = new Scanner(line.split(" ")[1].replaceAll(",", " "));
            while(parser.hasNextInt()){
                springSizes.add(parser.nextInt());
            }
            // Part 2
            String toRepeatSpring = springs;
            ArrayList<Integer> toRepeatSizes = new ArrayList<>(springSizes);
            for(int i = 0; i < 4; i++){
                springs += "?" + toRepeatSpring;
                for(int size: toRepeatSizes){
                    springSizes.add(size);
                }
            }
            // Part 2 end
            //sum += generateAndCountMatches(springs, springSizes, getRegexFromSizes(springSizes));
            sum += generateAndCountMatchesPart2(springs, springSizes, getRegexFromSizes(springSizes));
        }
        System.out.println(sum);
    }
    public static int generateAndCountMatchesPart2(String spring, ArrayList<Integer> springSizes, String regex){
        int countQuestionMarks = 0;
        int countHashtags = 0;
        int totalHashtags = 0;
        ArrayList<Integer> questionMarkPositions = new ArrayList<>();
        for(int i = 0; i < spring.length(); i++){
            if(spring.charAt(i) == '?'){
                questionMarkPositions.add(i);
                countQuestionMarks++;
            }
            if(spring.charAt(i) == '#')
                countHashtags++;
        }
        for(int size: springSizes)
            totalHashtags += size;
        return part2Helper("", 0, countQuestionMarks, 0, totalHashtags - countHashtags, spring, regex, questionMarkPositions);
    }
    public static int part2Helper(String currentString, int pos, int length, int countHashTags, int limit, String spring, String regex, ArrayList<Integer> questionMarkPositions){
        int count = 0;
        System.out.println(length);
        // Base Case
        if(pos == length){
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            int markCounter = 0;
            String testString = spring.toString();
            // replace the ?s
            for(int i: questionMarkPositions){
                testString = testString.substring(0, i) + currentString.charAt(markCounter) + testString.substring(i+1);
                markCounter++;
            }
            Matcher matcher = pattern.matcher(testString);
            boolean matchFound = matcher.matches();
            if(matchFound){
                return 1;
            }
            else{
                return 0;
            }
        }
        // Recursive Step
        count += part2Helper(currentString + ".", pos + 1, length, countHashTags, limit, spring, regex, questionMarkPositions);
        if(countHashTags < limit)
            count += part2Helper(currentString + "#", pos + 1, length, countHashTags + 1, limit, spring, regex, questionMarkPositions);
        return count;
    }
    public static int generateAndCountMatches(String spring, ArrayList<Integer> springSizes, String regex){
        int count = 0;
        int countQuestionMarks = 0;
        int countHashtags = 0;
        int totalHashtags = 0;
        ArrayList<Integer> questionMarkPositions = new ArrayList<>();
        for(int i = 0; i < spring.length(); i++){
            if(spring.charAt(i) == '?'){
                questionMarkPositions.add(i);
                countQuestionMarks++;
            }
            if(spring.charAt(i) == '#')
                countHashtags++;
        }
        for(int size: springSizes)
            totalHashtags += size;
        // permutations of # and . in a specfic order, to fill the question marks ? 
        ArrayList<String> possibilities = getAllPossibilitiesLimitHashtags(countQuestionMarks, totalHashtags - countHashtags);
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        for(String p: possibilities){
            int markCounter = 0;
            String testString = spring.toString();
            // replace the ?s
            for(int i: questionMarkPositions){
                testString = testString.substring(0, i) + p.charAt(markCounter) + testString.substring(i+1);
                markCounter++;
            }
            Matcher matcher = pattern.matcher(testString);
            boolean matchFound = matcher.matches();
            if(matchFound){
                count++;
            }
        }
        return count;
    }
    public static ArrayList<String> getAllPossibilities(int length){
        ArrayList<String> possibilities = new ArrayList<>();
        getAllPossibilitiesHelper("", 0, length, possibilities);
        return possibilities;
    }
    public static void getAllPossibilitiesHelper(String currentString, int pos, int length, ArrayList<String> possibilities){
        // Base Case
        if(pos == length){
            possibilities.add(currentString);
            return;
        }
        // Recursive Step
        getAllPossibilitiesHelper(currentString + ".", pos + 1, length, possibilities);
        getAllPossibilitiesHelper(currentString + "#", pos + 1, length, possibilities);
    }
    public static ArrayList<String> getAllPossibilitiesLimitHashtags(int length, int limit){
        ArrayList<String> possibilities = new ArrayList<>();
        System.out.println(length);
        getAllPossibilitiesLimitHashtagsHelper("", 0, length, 0, limit, possibilities);
        return possibilities;
    }
    public static void getAllPossibilitiesLimitHashtagsHelper(String currentString, int pos, int length, int countHashTags, int limit, ArrayList<String> possibilities){
        // Base Case
        if(pos == length){
            possibilities.add(currentString);
            return;
        }
        // Recursive Step
        getAllPossibilitiesLimitHashtagsHelper(currentString + ".", pos + 1, length, countHashTags, limit, possibilities);
        if(countHashTags < limit)
            getAllPossibilitiesLimitHashtagsHelper(currentString + "#", pos + 1, length, countHashTags + 1, limit, possibilities);
    }
    public static String getRegexFromSizes(ArrayList<Integer> sizes){
        String anyDots = "[.]*";
        String oneOrMoreDots = "[.]+";
        String springRegex = anyDots;
        for(int i = 0; i < sizes.size() - 1; i++){
            springRegex += "#{" + sizes.get(i) + "}";
            springRegex += oneOrMoreDots;
        }
        springRegex += "#{" + sizes.get(sizes.size()-1) + "}";
        springRegex += anyDots;
        return springRegex;
    }
}