import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.lang.Math;

class Mapping{
    public long source;
    public long dest;
    public long range;
    public Mapping(long s, long d, long r){
        source = s;
        dest = d;
        range = r;
    }
}

public class Main{
    
    public static long mapValue(ArrayList<Mapping> maps, long x){
        for(Mapping m: maps){
            if(m.source <= x && x < m.source + m.range){
                return x + m.dest - m.source;
            }
            
        }
        return x;
    }

    
	public static void main(String[] args) throws Exception {
		Scanner input = new Scanner(new File("b.txt"));
		ArrayList<Long> seeds = new ArrayList<Long>();
		String firstLine = input.nextLine();
		firstLine = firstLine.split(":")[1];
		Scanner parserFirst = new Scanner(firstLine);
		/*while(parserFirst.hasNextLong()){
		    seeds.add(parserFirst.nextLong());
		}*/
		input.nextLine();
		ArrayList<Mapping> seedToSoil = new ArrayList<Mapping>();
		ArrayList<Mapping> soilToFert = new ArrayList<Mapping>();
		ArrayList<Mapping> fertToWater = new ArrayList<Mapping>();
		ArrayList<Mapping> waterToLight = new ArrayList<Mapping>();
		ArrayList<Mapping> lightToTemp = new ArrayList<Mapping>();
		ArrayList<Mapping> tempToHumid = new ArrayList<Mapping>();
		ArrayList<Mapping> humidToLocation = new ArrayList<Mapping>();
        input.nextLine();
        int counter = 0;
        while(input.hasNextLine()){
            String line = input.nextLine();
            if(line.equals("")){
                counter++;
                continue;
            }
            else if(Character.isLetter(line.charAt(0))){
                continue;
            }
            Scanner parser = new Scanner(line);
            long startDest = parser.nextLong();
            long startSource = parser.nextLong();
            long range = parser.nextLong();
            Mapping map = new Mapping(startSource, startDest, range);
            System.out.println(startDest + " " + startSource + " " + range);
            switch(counter){
                case 0:
                    seedToSoil.add(map);
                    break;
                case 1:
                    soilToFert.add(map);
                    break;
                case 2:
                    fertToWater.add(map);
                    break;
                case 3:
                    waterToLight.add(map);
                    break;
                case 4:
                    lightToTemp.add(map);
                    break;
                case 5:
                    tempToHumid.add(map);
                    break;
                case 6:
                    humidToLocation.add(map);
                    break;
            }
        }
        long lowest = Long.MAX_VALUE;
        /*for(long seed: seeds){
            long x = seed;
            x = mapValue(seedToSoil, x);
            x = mapValue(soilToFert, x);
            x = mapValue(fertToWater, x);
            x = mapValue(waterToLight, x);
            x = mapValue(lightToTemp, x);
            x = mapValue(tempToHumid, x);
            x = mapValue(humidToLocation, x);
            lowest = Math.min(lowest, x);
        }*/
        int number = 0;
        while(parserFirst.hasNextLong()){
            System.out.println(number);
            long s = parserFirst.nextLong();
            long r = parserFirst.nextLong();
            for(long i = 0; i < r; i++){
                long x = s + i;
                x = mapValue(seedToSoil, x);
                x = mapValue(soilToFert, x);
                x = mapValue(fertToWater, x);
                x = mapValue(waterToLight, x);
                x = mapValue(lightToTemp, x);
                x = mapValue(tempToHumid, x);
                x = mapValue(humidToLocation, x);
                lowest = Math.min(lowest, x);
            }
            number++;
        }
        System.out.println(lowest);
	}
}
