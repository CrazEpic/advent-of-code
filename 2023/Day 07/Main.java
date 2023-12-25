import java.util.*;
import java.io.File;
import java.lang.Math;



public class Main
{
	public static void main(String[] args) throws Exception{
		Scanner input = new Scanner(new File("b.txt"));
		ArrayList<PokerHand> hands = new ArrayList<PokerHand>();
		while(input.hasNextLine()){
		    String line = input.nextLine();
		    String hand = line.split(" ")[0].trim();
		    int bid = Integer.parseInt(line.split(" ")[1].trim());
		    hands.add(new PokerHand(hand, bid));
		}
		Collections.sort(hands);
		long totalWinnings = 0;
		for(int i = 0; i < hands.size(); i++){
		    totalWinnings += (i + 1) * hands.get(i).bid;
		    System.out.println(hands.get(i));
		}
		System.out.println(totalWinnings);
	}
}

class PokerHand implements Comparable<PokerHand>{
    public ArrayList<String> typeRanks;
    public ArrayList<String> cardRanks;
    public String hand;
    public int bid;
    public String type;
    public PokerHand(String h, int b){
        hand = h;
        bid = b;
        String[] t = {"Five", "Four", "Full", "ThreeKind", "TwoPair", "OnePair", "High"};
        //String[] c = {"A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2"};
        String[] c = {"A", "K", "Q", "T", "9", "8", "7", "6", "5", "4", "3", "2", "J"};
        typeRanks = new ArrayList<String>(Arrays.asList(t));
        cardRanks = new ArrayList<String>(Arrays.asList(c));
        Collections.reverse(typeRanks);
        Collections.reverse(cardRanks);
        type = getType(hand);
    }
    public int compareTo(PokerHand p){ 
        if(typeRanks.indexOf(type) == typeRanks.indexOf(p.type)){
            for(int i = 0; i < 5; i++){
                if(hand.charAt(i) != p.hand.charAt(i)){
                    return cardRanks.indexOf(String.valueOf(hand.charAt(i))) - cardRanks.indexOf(String.valueOf(p.hand.charAt(i)));
                }
            }
            return 0;
        }
        else{
            return typeRanks.indexOf(type) - typeRanks.indexOf(p.type);
        }
    } 
    public String getType(String hand){
	    /*if(checkFive(hand)){
	        return "Five";
	    }
	    else if(checkFour(hand)){
	        return "Four";
	    }
	    else if(checkFull(hand)){
	        return "Full";
	    }
	    else if(checkThreeKind(hand)){
	        return "ThreeKind";
	    }
	    else if(checkTwoPair(hand)){
	        return "TwoPair";
	    }
	    else if(checkOnePair(hand)){
	        return "OnePair";
	    }
	    return "High";*/
	    String trueType = "High";
	    // brute force all joker combos
	    for(String r: cardRanks){
	        String tempHand = hand.replaceAll("J", r);
	        String tempType;
	        if(checkFive(tempHand)) tempType = "Five";
    	    else if(checkFour(tempHand)) tempType = "Four";
    	    else if(checkFull(tempHand)) tempType = "Full";
    	    else if(checkThreeKind(tempHand)) tempType = "ThreeKind";
    	    else if(checkTwoPair(tempHand)) tempType = "TwoPair";
    	    else if(checkOnePair(tempHand)) tempType = "OnePair";
    	    else tempType = "High";
    	    if(typeRanks.indexOf(tempType) > typeRanks.indexOf(trueType))
                trueType = tempType;
	    }
	    return trueType;
	}
	public boolean checkFive(String hand){
	    char label = hand.charAt(0);
	    for(char c: hand.toCharArray()){
	        if(c != label)
	            return false;
	    }
	    return true;
	}
	public boolean checkFour(String hand){
	    HashMap<Character, Integer> labels = getFreq(hand);
	    if(labels.size() == 2){
	        boolean haveFour = false;
	        boolean haveOne = false;
	        for (Map.Entry<Character, Integer> set : labels.entrySet()) {
	            if(set.getValue() == 1){
	                haveOne = true;
	            }
	            if(set.getValue() == 4){
	                haveFour = true;
	            }
            }
            return haveFour && haveOne;
	    }
	    return false;
	}
	public boolean checkFull(String hand){
	    HashMap<Character, Integer> labels = getFreq(hand);
	    if(labels.size() == 2){
	        boolean haveThree = false;
	        boolean haveTwo = false;
	        for (Map.Entry<Character, Integer> set : labels.entrySet()) {
	            if(set.getValue() == 3){
	                haveThree = true;
	            }
	            if(set.getValue() == 2){
	                haveTwo = true;
	            }
            }
            return haveThree && haveTwo;
	    }
	    return false;
	}
	public boolean checkThreeKind(String hand){
	    HashMap<Character, Integer> labels = getFreq(hand);
	    if(labels.size() == 3){
	        boolean haveThree = false;
	        for (Map.Entry<Character, Integer> set : labels.entrySet()) {
	            if(set.getValue() == 3){
	                haveThree = true;
	            }
            }
            return haveThree;
	    }
	    return false;
	}
	public boolean checkTwoPair(String hand){
	    HashMap<Character, Integer> labels = getFreq(hand);
	    if(labels.size() == 3){
	        boolean haveOne = false;
	        for (Map.Entry<Character, Integer> set : labels.entrySet()) {
	            if(set.getValue() == 1){
	                haveOne = true;
	            }
            }
            return haveOne;
	    }
	    return false;
	}
	public boolean checkOnePair(String hand){
	    HashMap<Character, Integer> labels = getFreq(hand);
	    if(labels.size() == 4){
	        return true;
	    }
	    return false;
	}
	public boolean checkHigh(String hand){
	    HashMap<Character, Integer> labels = getFreq(hand);
	    if(labels.size() == 5){
	        return true;
	    }
	    return false;
	}
	public HashMap<Character, Integer> getFreq(String hand){
	    HashMap<Character, Integer> labels = new HashMap<>();
	    for(char c: hand.toCharArray()){
	        if(!labels.containsKey(c)){
	            labels.put(c, 1);
	        }
	        else{
	            labels.put(c, labels.get(c) + 1);
	        }
	    }
	    return labels;
	}
	public String toString(){
	    return hand + " " + type + " " + bid;
	}
}
