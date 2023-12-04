import java.util.Scanner;
import java.io.File;
import java.lang.Math;
import java.util.ArrayList;

public class Main
{
    static ArrayList<Integer> preload = new ArrayList<Integer>();
	public static void main(String[] args) throws Exception{    
	    Scanner input = new Scanner(new File("scratchcardpart2_2.txt"));
	    ArrayList<String> cards = new ArrayList<String>();
	    int totalPoints = 0;
	    while(input.hasNextLine()){
	        String line = input.nextLine();
	        line = (line.split(": "))[1];
	        cards.add(line);
	        preload.add(-1);
	        /*String line = input.nextLine();
	        line = (line.split(": "))[1];
	        int correct = 0;
	        String winningLine = (line.split("\\|"))[0];
	        String guesses = (line.split("\\|"))[1];
	        ArrayList<Integer> winning = new ArrayList<Integer>();
	        Scanner parser = new Scanner(winningLine);
	        while(parser.hasNextInt()){
	            winning.add(parser.nextInt());
	        }
	        parser = new Scanner(guesses);
	        while(parser.hasNextInt()){
	            int guess = parser.nextInt();
	            if(winning.contains(guess)){
	                correct++;
	            }
	        }
	        if(correct > 0){
	            totalPoints += Math.pow(2, correct - 1);
	        }*/
	    }
	    //System.out.println(totalPoints);
	    int total = 0;
	    for(int i = cards.size() - 1; i >= 0; i--){
	        System.out.println("Processing Card " + i);
            total += calculateDFS(cards, i);
	    }
	    System.out.println(total);
	}
	
	public static int calculateDFS(ArrayList<String> cards, int lineNumber){
	    if(preload.get(lineNumber) != -1){
	        return preload.get(lineNumber);
	    }
	    int correct = 0;
	    String line = cards.get(lineNumber);
        String winningLine = (line.split("\\|"))[0];
        String guesses = (line.split("\\|"))[1];
        ArrayList<Integer> winning = new ArrayList<Integer>();
        Scanner parser = new Scanner(winningLine);
        while(parser.hasNextInt()){
            winning.add(parser.nextInt());
        }
        parser = new Scanner(guesses);
        while(parser.hasNextInt()){
            int guess = parser.nextInt();
            if(winning.contains(guess)){
                correct++;
            }
        }
        int instances = 1;
        int i = 1;
        while(i <= correct && i < cards.size()){
            instances += calculateDFS(cards, lineNumber + i);
            i++;
        }
        //System.out.println("On Line: " + lineNumber + " Returning " + instances);
        preload.set(lineNumber, instances);
        return instances;
	}
}
