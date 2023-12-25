import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.lang.Math;
import java.lang.Thread;

public class Main{
	public static void main(String[] args) throws Exception {
		Scanner input = new Scanner(new File("./b.txt"));
		//ArrayList<Long> seeds = new ArrayList<Long>();
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
            parser.close();
            Mapping map = new Mapping(startSource, startDest, range);
            //System.out.println(startDest + " " + startSource + " " + range);
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
        long lowest = Long.MAX_VALUE;
        ArrayList<Mapper> threads = new ArrayList<Mapper>();
        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("NUMBER OF CPUS: " + cores);
        final long startTime = System.currentTimeMillis();
        while(parserFirst.hasNextLong()){
            long s = parserFirst.nextLong();
            long r = parserFirst.nextLong();
            Mapper thread = new Mapper(s, r, seedToSoil, soilToFert, fertToWater, waterToLight, 
                                        lightToTemp, tempToHumid, humidToLocation);
            System.out.println("RUNNING THREAD: " + threads.size());
            thread.start();
            threads.add(thread);
        }
        parserFirst.close();
        for(int i = 0; i < threads.size(); i++){ // wait for all threads
            threads.get(i).join();
            lowest = Math.min(lowest, threads.get(i).getLowest());
        }
        final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime));
        System.out.println(lowest);
	}
}





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

class Mapper extends Thread {
    private long start;
    private long range;
    private ArrayList<Mapping> seedToSoil;
	private ArrayList<Mapping> soilToFert;
	private ArrayList<Mapping> fertToWater;
	private ArrayList<Mapping> waterToLight;
	private ArrayList<Mapping> lightToTemp;
	private ArrayList<Mapping> tempToHumid;
	private ArrayList<Mapping> humidToLocation;
	private long lowest;
    public Mapper(long s, long r, ArrayList<Mapping> a, ArrayList<Mapping> b, ArrayList<Mapping> c, 
                    ArrayList<Mapping> d, ArrayList<Mapping> e, ArrayList<Mapping> f, ArrayList<Mapping> g){
        start = s;
        range = r;
        seedToSoil = a;
		soilToFert = b;
		fertToWater = c;
		waterToLight = d;
		lightToTemp = e;
		tempToHumid = f;
		humidToLocation = g;
		lowest = Long.MAX_VALUE;
    }
    public long getLowest(){
        return lowest;
    }
    public long mapValue(ArrayList<Mapping> maps, long x){
        for(Mapping m: maps){
            if(m.source <= x && x < m.source + m.range){
                return x + m.dest - m.source;
            }
        }
        return x;
    }
    @Override
    public void run(){
        System.out.println("START RANGE: [" + start + ", " + (start + range) + ")");
        for(long i = 0; i < range; i++){
            long x = start + i;
            x = mapValue(seedToSoil, x);
            x = mapValue(soilToFert, x);
            x = mapValue(fertToWater, x);
            x = mapValue(waterToLight, x);
            x = mapValue(lightToTemp, x);
            x = mapValue(tempToHumid, x);
            x = mapValue(humidToLocation, x);
            lowest = Math.min(lowest, x);
        }
        System.out.println("FINISHED RANGE: [" + start + ", " + (start + range) + ")");
        this.interrupt();
    }
}
