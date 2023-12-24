import java.util.*;
import java.io.File;
import java.lang.Math;

public class Main{
    public static void main(String[] args) throws Exception{
        Scanner input = new Scanner(new File("b.txt"));
        ArrayList<HailstoneVector> stones = new ArrayList<>();
        while(input.hasNextLine()){
            String line = input.nextLine();
            line = line.replaceAll("[,@]", " ");
            line = line.replaceAll("[ ]+", " ");
            Scanner parser = new Scanner(line);
            long px = parser.nextLong();
            long py = parser.nextLong();
            long pz = parser.nextLong();
            long vx = parser.nextLong();
            long vy = parser.nextLong();
            long vz = parser.nextLong();
            stones.add(new HailstoneVector(px, py, pz, vx, vy, vz));
            parser.close();
        }
        input.close();
        long lower = 200000000000000l;
        long upper = 400000000000000l;
        System.out.println(countIntersectionsIgnoreZAxis(lower, upper, stones));
    }
    public static long countIntersectionsIgnoreZAxis(long lowerBound, long upperBound, ArrayList<HailstoneVector> stones){
        long count = 0;
        for(int i = 0; i < stones.size(); i++){
            for(int j = i+1; j < stones.size(); j++){
                Vector2D test = HailstoneVector.checkIntersectionPositiveTimeIgnoreZAxis(stones.get(i), stones.get(j));
                if(test != null && test.x != Double.NEGATIVE_INFINITY && test.y != Double.NEGATIVE_INFINITY){
                    if(test != null && (lowerBound <= test.x && test.x <= upperBound) && (lowerBound <= test.y && test.y <= upperBound)){
                        count++;
                    }
                }
            }
        }
        return count;
    }
}

class Vector3D{
    public double x;
    public double y;
    public double z;
    public Vector3D(double a, double b, double c){
        x = a;
        y = b;
        z = c;
    }
    public static Vector3D add(Vector3D a, Vector3D b){
        return new Vector3D(a.x + b.x, a.y + b.y, a.z + b.z);
    }
    public static Vector3D sub(Vector3D a, Vector3D b){
        return new Vector3D(a.x - b.x, a.y - b.y, a.z - b.z);
    }
    public static Vector3D multiply(Vector3D a, double scalar){
        return new Vector3D(a.x * scalar, a.y * scalar, a.z * scalar);
    }
    public static Vector3D divide(Vector3D a, double scalar){
        return new Vector3D(a.x / scalar, a.y / scalar, a.z / scalar);
    }
    public static Vector3D cross(Vector3D a, Vector3D b){
        return new Vector3D(a.y * b.z - a.z * b.y,
                            a.z * b.x - a.x * b.z,
                            a.x * b.y - a.y * b.x);
    }
    public String toString(){
        return x + " " + y + " " + z;
    }
}

class Vector2D{
    public double x;
    public double y;
    public Vector2D(double a, double b){
        x = a;
        y = b;
    }
    public static Vector2D add(Vector2D a, Vector2D b){
        return new Vector2D(a.x + b.x, a.y + b.y);
    }
    public static Vector2D sub(Vector2D a, Vector2D b){
        return new Vector2D(a.x - b.x, a.y - b.y);
    }
    public static Vector2D multiply(Vector2D a, double scalar){
        return new Vector2D(a.x * scalar, a.y * scalar);
    }
    public static Vector2D divide(Vector2D a, double scalar){
        return new Vector2D(a.x / scalar, a.y / scalar);
    }
    public static double cross(Vector2D a, Vector2D b){
        return a.x * b.y - a.y * b.x;
    }
    public String toString(){
        return x + " " + y;
    }
}

class HailstoneVector{
    public Vector3D pos;
    public Vector3D vel;
    public HailstoneVector(long p1, long p2, long p3, long p4, long p5, long p6){
        pos = new Vector3D(p1, p2, p3);
        vel = new Vector3D(p4, p5, p6);
    }
    public static Vector2D checkIntersectionIgnoreZAxis(HailstoneVector h1, HailstoneVector h2){
        Vector2D a1 = new Vector2D(h1.pos.x, h1.pos.y);
        Vector2D d1 = new Vector2D(h1.vel.x, h1.vel.y);
        Vector2D a2 = new Vector2D(h2.pos.x, h2.pos.y);
        Vector2D d2 = new Vector2D(h2.vel.x, h2.vel.y);
        // a1 + ( cross(a2 - a1, d2) / cross(d1, d2) ) * d1;
        // a1 + t * d1
        double t = Vector2D.cross(Vector2D.sub(a2, a1), d2) / Vector2D.cross(d1, d2);
        return Vector2D.add(a1, Vector2D.multiply(d1, t));
    }
    public static Vector2D checkIntersectionPositiveTimeIgnoreZAxis(HailstoneVector h1, HailstoneVector h2){
        Vector2D a1 = new Vector2D(h1.pos.x, h1.pos.y);
        Vector2D d1 = new Vector2D(h1.vel.x, h1.vel.y);
        Vector2D a2 = new Vector2D(h2.pos.x, h2.pos.y);
        Vector2D d2 = new Vector2D(h2.vel.x, h2.vel.y);
        // a1 + ( cross(a2 - a1, d2) / cross(d1, d2) ) * d1;
        // a1 + t * d1
        double t1 = Vector2D.cross(Vector2D.sub(a2, a1), d2) / Vector2D.cross(d1, d2);
        double t2 = Vector2D.cross(Vector2D.sub(a1, a2), d1) / Vector2D.cross(d2, d1);
        if(t1 <= 0 || t2 <= 0)
            return null;
        return Vector2D.add(a1, Vector2D.multiply(d1, t1));
    }
    public String toString(){
        return String.format("%d, %d, %d @ %d, %d, %d", pos.x, pos.y, pos.z, vel.x, vel.y, vel.z);
    }
}