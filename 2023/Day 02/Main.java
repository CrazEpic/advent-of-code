import java.util.Scanner;
import java.io.File;

public class Main{
	public static void main(String[] args) throws Exception{
		Scanner input = new Scanner(new File("cubeconundrum2.txt"));
		int sumIDs = 0;
		int sumPowers = 0;
		int maxRed = 12;
		int maxGreen = 13;
		int maxBlue = 14;
		int id = 1;
		while(input.hasNextLine()){
		    String line = input.nextLine();
		    line = (line.split(":"))[1].substring(1);
		    String[] turns = line.split(";");
		    int maxR = 0;
		    int maxG = 0;
		    int maxB = 0;
		    //boolean invalidGame = false;
		    for(int i = 0; i < turns.length; i++){
		        Scanner tokenizer = new Scanner(turns[i]);
		        while(tokenizer.hasNext()){
		            int num = tokenizer.nextInt();
		            String color = tokenizer.next();
		            if(color.charAt(color.length()-1) == ',')
		                color = color.substring(0, color.length() - 1);
		            /*if(color.equals("red")){
		                if(num > maxRed){
		                    invalidGame = true;
		                    break;
		                }
		            }
		            else if(color.equals("green")){
		                if(num > maxGreen){
		                    invalidGame = true;
		                    break;
		                }
		            }
		            else if(color.equals("blue")){
		                if(num > maxBlue){
		                    invalidGame = true;
		                    break;
		                }
		            }*/
		            if(color.equals("red")){
		                if(num > maxR){
                            maxR = num;
		                }
		            }
		            else if(color.equals("green")){
		                if(num > maxG){
                            maxG = num;
		                }
		            }
		            else if(color.equals("blue")){
		                if(num > maxB){
                            maxB = num;
		                }
		            }
		        }
		    }
	        /*if(!invalidGame){
	            System.out.println("Adding ID: " + id);
	            sumIDs += id;
	        }
		    id++;*/
		    sumPowers += maxR * maxG * maxB;
		}
		System.out.println(sumIDs);
		System.out.println(sumPowers);
	}
}
