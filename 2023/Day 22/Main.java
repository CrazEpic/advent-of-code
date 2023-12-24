import java.util.*;
import java.io.File;
import java.lang.Math;

public class Main{
    public static void main(String[] args) throws Exception{
        Scanner input = new Scanner(new File("b.txt"));
        ArrayList<Brick> bricks = new ArrayList<>();
        while(input.hasNextLine()){
            String line = input.nextLine().strip();
            line = line.replaceAll("[~,]", " ");
            Scanner parser = new Scanner(line);
            bricks.add(new Brick(parser.nextInt(), parser.nextInt(), parser.nextInt(), parser.nextInt(), parser.nextInt(), parser.nextInt()));
            parser.close();
        }
        input.close();
        Collections.sort(bricks);
        dropBricks(bricks);
        HashMap<Brick, HashSet<Brick>> brickSupports = new HashMap<>();
        HashMap<Brick, HashSet<Brick>> bricksSupportedBy = new HashMap<>();
        initializeSupports(bricks, brickSupports, bricksSupportedBy);
        System.out.println(countSafeToDisintegrate(bricks, brickSupports, bricksSupportedBy));
        System.out.println(sumToDisintegrate(bricks));
    }
    public static int sumToDisintegrate(ArrayList<Brick> bricks){
        int totalFell = 0;
        for(int i = 0; i < bricks.size(); i++){
            ArrayList<Brick> copyInitialBricks = new ArrayList<>();
            for(int j = 0; j < bricks.size(); j++)
                if(j != i)
                    copyInitialBricks.add(new Brick(bricks.get(j)));
            ArrayList<Brick> toDropBricks = new ArrayList<>();
            for(Brick b: copyInitialBricks)
                toDropBricks.add(new Brick(b));
            dropBricks(toDropBricks);
            int fell = 0;
            for(int b = 0; b < copyInitialBricks.size(); b++){
                if(copyInitialBricks.get(b).equals(toDropBricks.get(b)) == false){
                    fell++;
                }
            }
            totalFell += fell;
        }
        return totalFell;
    }
    public static int countSafeToDisintegrate(ArrayList<Brick> bricks, HashMap<Brick, HashSet<Brick>> brickSupports, HashMap<Brick, HashSet<Brick>> bricksSupportedBy){
        int count = 0;
        for(Brick b: bricks){
            boolean safeToDisintegrate = true;
            for(Brick s: brickSupports.get(b)){ // get the set of bricks that b supports
                if(bricksSupportedBy.get(s).size() < 2){
                    safeToDisintegrate = false;
                    break;
                }
            }
            if(safeToDisintegrate)
                count++;
        }
        return count;
    }
    public static void initializeSupports(ArrayList<Brick> bricks, HashMap<Brick, HashSet<Brick>> brickSupports, HashMap<Brick, HashSet<Brick>> bricksSupportedBy){
        for(int i = 0; i < bricks.size(); i++){
            brickSupports.put(bricks.get(i), new HashSet<>());
            bricksSupportedBy.put(bricks.get(i), new HashSet<>());
        }
        for(int i = 0; i < bricks.size(); i++){
            for(int j = 0; j < i; j++){
                Brick upper = bricks.get(i);
                Brick lower = bricks.get(j);
                if(Brick.xyPlaneIntersection(lower, upper) && lower.z2 + 1 == upper.z1){
                    // if intersect and they are right on top of each other
                    brickSupports.get(lower).add(upper); // lower supports upper
                    bricksSupportedBy.get(upper).add(lower); // upper supported by lower
                }
            }
        }
    }
    public static void dropBricks(ArrayList<Brick> bricks){
        // assume bricks are sorted z-axis ascending already
        for(int i = 0; i < bricks.size(); i++){
            int maxZ = 1;
            for(int j = 0; j < i; j++){ // find max the brick can fall
                if(Brick.xyPlaneIntersection(bricks.get(i), bricks.get(j))){
                    maxZ = Math.max(maxZ, bricks.get(j).z2 + 1); 
                }
            }
            // int zLength = Math.abs(bricks.get(i).z2 - bricks.get(i).z1);
            // bricks.get(i).z1 = maxZ;
            // bricks.get(i).z2 = bricks.get(i).z1 + zLength;
            bricks.get(i).z2 -= bricks.get(i).z1 - maxZ;
            bricks.get(i).z1 = maxZ;
        }
    }
}
class Brick implements Comparable<Brick>{
    public int x1;
    public int y1;
    public int z1;
    public int x2;
    public int y2;
    public int z2;
    public static String sortType = "z1";
    public Brick(int a, int b, int c, int d, int e, int f){
        x1 = a;
        y1 = b;
        z1 = c;
        x2 = d;
        y2 = e;
        z2 = f;
    }
    public Brick(Brick b){
        x1 = b.x1;
        y1 = b.y1;
        z1 = b.z1;
        x2 = b.x2;
        y2 = b.y2;
        z2 = b.z2;
    }
    public static boolean xyPlaneIntersection(Brick b1, Brick b2){
        return (Math.max(b1.x1, b2.x1) <= Math.min(b1.x2, b2.x2)) &&
                (Math.max(b1.y1, b2.y1) <= Math.min(b1.y2, b2.y2));
    }
    @Override
    public int compareTo(Brick b){
        if(sortType.equals("x1"))
            return Integer.compare(x1, b.x1);
        else if(sortType.equals("y1"))
            return Integer.compare(y1, b.y1);
        else if(sortType.equals("z1"))
            return Integer.compare(z1, b.z1);
        else if(sortType.equals("x2"))
            return Integer.compare(x2, b.x2);
        else if(sortType.equals("y2"))
            return Integer.compare(y2, b.y2);
        else if(sortType.equals("z2"))
            return Integer.compare(z2, b.z2);
        return 0;
    }
    @Override
    public boolean equals(Object o){
        if(this == o)
            return true;
        if(!(o instanceof Brick))
            return false;
        Brick b = (Brick) o;
        return (x1 == b.x1) && (y1 == b.y1) && (z1 == b.z1) &&
                (x2 == b.x2) && (y2 == b.y2) && (z2 == b.z2);
    }
    @Override
    public int hashCode(){
        return Integer.hashCode(x1) + Integer.hashCode(y1) + Integer.hashCode(z1) + 
                Integer.hashCode(x2) + Integer.hashCode(y2) + Integer.hashCode(z2);
    }
    public String toString(){
        return String.format("%d %d %d, %d %d %d", x1, y1, z1, x2, y2, z2);
    }
}