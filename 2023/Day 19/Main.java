import java.util.*;
import java.io.File;
import java.lang.Math;

public class Main{
    public static void main(String[] args) throws Exception{
        Scanner input = new Scanner(new File("b.txt"));
        HashMap<String, ArrayList<Rule>> rules = new HashMap<>();
        while(input.hasNextLine()){
            String line = input.nextLine();
            if(line.equals("")){
                break;
            }
            String name = line.substring(0, line.indexOf("{"));
            line = line.substring(line.indexOf("{")+1).replaceAll("}", "");
            String[] rulesLine = line.split(",");
            ArrayList<Rule> rulesList = new ArrayList<>();
            for(String s: rulesLine){
                if(s.indexOf(":") == -1){ // just a state
                    rulesList.add(new Rule('?', 'A', -1, s));
                }
                else{
                    String comparison = s.split(":")[0];
                    String dest = s.split(":")[1];
                    char category = comparison.charAt(0);
                    char comparisonOperator = comparison.charAt(1);
                    comparison = comparison.substring(2);
                    int number = Integer.parseInt(comparison);
                    rulesList.add(new Rule(category, comparisonOperator, number, dest));
                }
            }
            rules.put(name, rulesList);
        }
        ArrayList<Part> parts = new ArrayList<>();
        while(input.hasNextLine()){
            String line = input.nextLine();
            line = line.replaceAll("[{}xmas=]", "");
            String[] numberStrings = line.split(",");
            int x = Integer.parseInt(numberStrings[0]);
            int m = Integer.parseInt(numberStrings[1]);
            int a = Integer.parseInt(numberStrings[2]);
            int s = Integer.parseInt(numberStrings[3]);
            parts.add(new Part(x, m, a, s));
        }
        int totalRating = 0;
        for(Part p: parts){
            if(applyRules(rules, p).equals("A")){
                totalRating += p.getRating();
            }
        }
        System.out.println(totalRating);
        System.out.println(countValidCombinations(rules));
    }
    public static long countValidCombinations(HashMap<String, ArrayList<Rule>> rules){
        String workflow = "in";
        CategoriesRange initialRange = new CategoriesRange();
        ArrayList<Long> counts = new ArrayList<>();
        countValidCombinationsHelper(workflow, initialRange, counts, rules);
        long count = 0;
        for(Long num: counts)
            count += num;
        return count;
    }
    public static void countValidCombinationsHelper(String currentWorkflow, CategoriesRange original, ArrayList<Long> counts, HashMap<String, ArrayList<Rule>> rules){
        if(currentWorkflow.equals("A")){
            counts.add(original.getCountWithinRange());
            return;
        }
        else if(currentWorkflow.equals("R")){
            return;
        }
        ArrayList<Rule> currentRules = rules.get(currentWorkflow);
        CategoriesRange range = new CategoriesRange(original.minX, original.maxX, original.minM, original.maxM, original.minA, original.maxA, original.minS, original.maxS);
        for(Rule r: currentRules){
            CategoriesRange range1 = applyRuleToRange(range, r);
            if(range1 != null)
                countValidCombinationsHelper(r.destination, range1, counts, rules);
            range = applyInverseRuleToRange(range, r);
            if(range == null)
                return;
        }
    }
    public static CategoriesRange applyRuleToRange(CategoriesRange original, Rule rule){
        CategoriesRange range = new CategoriesRange(original.minX, original.maxX, original.minM, original.maxM, original.minA, original.maxA, original.minS, original.maxS);
        char category = rule.category;
        if(rule.comparison == 'A'){
            // hi
        }
        else if(rule.comparison == '<'){
            if(range.getMinCategory(category) >= rule.number)
                return null;
            range.changeMax(category, rule.number-1);
        }
        else if(rule.comparison == '>'){
            if(range.getMaxCategory(category) <= rule.number)
                return null;
            range.changeMin(category, rule.number+1);
        }
        return range;
    }
    public static CategoriesRange applyInverseRuleToRange(CategoriesRange original, Rule rule){
        CategoriesRange range = new CategoriesRange(original.minX, original.maxX, original.minM, original.maxM, original.minA, original.maxA, original.minS, original.maxS);
        char category = rule.category;
        if(rule.comparison == 'A'){
            // hi
            return null;
        }
        else if(rule.comparison == '<'){
            // checking >=
            if(range.getMaxCategory(category) < rule.number)
                return null;
            range.changeMin(category, rule.number);
        }
        else if(rule.comparison == '>'){
            // checking <=
            if(range.getMinCategory(category) > rule.number)
                return null;
            range.changeMax(category, rule.number);
        }
        return range;
    }

    public static String applyRules(HashMap<String, ArrayList<Rule>> rules, Part p){
        String workflow = "in";
        //System.out.println("APPLYING WORKFLOW FOR: " + p);
        while(workflow.equals("A") == false && workflow.equals("R") == false){
            //System.out.println(workflow);
            ArrayList<Rule> currentRules = rules.get(workflow);
            int i = 0;
            while(i < currentRules.size()){
                String ruleResult = currentRules.get(i).performRule(p);
                if(ruleResult.equals("") == false){
                    workflow = ruleResult;
                    break;
                }
                i++;
            }
        }
        return workflow;
    }
}

class Rule{
    public char category;
    public char comparison;
    public int number;
    public String destination;
    public Rule(char c, char compare, int n, String d){
        category = c;
        comparison = compare;
        number = n;
        destination = d;
    }
    public String performRule(Part p){
        if(comparison == 'A'){
            return destination;
        }
        int toCompare = -1;
        switch(category){
            case 'x': toCompare = p.x; break;
            case 'm': toCompare = p.m; break;
            case 'a': toCompare = p.a; break;
            case 's': toCompare = p.s; break;
        }
        if(comparison == '<' && toCompare < number){
            return destination;
        }
        else if(comparison == '>' && toCompare > number){
            return destination;
        }
        return "";
    }
    public String toString(){
        return category + " " + comparison + " " + number + " " + destination;
    }
}
class Part{
    public int x;
    public int m;
    public int a;
    public int s;
    public Part(int x1, int m1, int a1, int s1){
        x = x1;
        m = m1;
        a = a1;
        s = s1;
    }
    public int getRating(){
        return x + m + a + s;
    }
    public String toString(){
        return x + " " + m + " " + a + " " + s;
    }
}
class CategoriesRange{
    public int minX;
    public int maxX;
    public int minM;
    public int maxM;
    public int minA;
    public int maxA;
    public int minS;
    public int maxS;
    public CategoriesRange(){
        minX = 1;
        maxX = 4000;
        minM = 1;
        maxM = 4000;
        minA = 1;
        maxA = 4000;
        minS = 1;
        maxS = 4000;
    }
    public CategoriesRange(int x1, int x2, int x3, int x4, int x5, int x6, int x7, int x8){
        minX = x1;
        maxX = x2;
        minM = x3;
        maxM = x4;
        minA = x5;
        maxA = x6;
        minS = x7;
        maxS = x8;
    }
    public long getCountWithinRange(){
        if(validateRange() == false)
            return -1;
        long count = 1;
        count *= (maxX - minX + 1);
        count *= (maxM - minM + 1);
        count *= (maxA - minA + 1);
        count *= (maxS - minS + 1);
        return count;
    }
    public boolean checkValuesWithinRange(int x, int m, int a, int s){
        return (minX <= x && x <= maxX) &&
                (minM <= m && m <= maxM) &&
                (minA <= a && a <= maxA) &&
                (minS <= s && s <= maxS);
    }
    public int getMinCategory(char category){
        switch(category){
            case 'x': return minX;
            case 'm': return minM;
            case 'a': return minA;
            case 's': return minS;
        }
        return -1;
    }
    public int getMaxCategory(char category){
        switch(category){
            case 'x': return maxX;
            case 'm': return maxM;
            case 'a': return maxA;
            case 's': return maxS;
        }
        return -1;
    }
    public void changeMax(char category, int max){
        switch(category){
            case 'x': maxX = max; break;
            case 'm': maxM = max; break;
            case 'a': maxA = max; break;
            case 's': maxS = max; break;
        }
    }
    public void changeMin(char category, int min){
        switch(category){
            case 'x': minX = min; break;
            case 'm': minM = min; break;
            case 'a': minA = min; break;
            case 's': minS = min; break;
        }
    }
    public boolean validateRange(){
        return (minX <= maxX) &&
                (minM <= maxM) &&
                (minA <= maxA) &&
                (minS <= maxS);
    }
    public String toString(){
        return "{" + minX + "," + maxX + "}" + " " + 
                "{" + minM + "," + maxM + "}" + " " + 
                "{" + minA + "," + maxA + "}" + " " + 
                "{" + minS + "," + maxS + "}";
    }
}