import java.util.*;
import java.io.File;
import java.lang.Math;

public class Main{
	public static void main(String[] args) throws Exception{
		Scanner input = new Scanner(new File("b.txt"));
		ArrayList<Long> times = new ArrayList<Long>();
		ArrayList<Long> distances = new ArrayList<Long>();
        // String line1 = input.nextLine().split(":")[1];
        // Scanner parser = new Scanner(line1);
        // while(parser.hasNextInt()){
        //     times.add(parser.nextInt());
        // }
        // String line2 = input.nextLine().split(":")[1];
        // parser = new Scanner(line2);
        // while(parser.hasNextInt()){
        //     distances.add(parser.nextInt());
        // }
        String line1 = input.nextLine().split(":")[1];
        String line2 = input.nextLine().split(":")[1];
        line1 = line1.replaceAll("\\s", "");
        line2 = line2.replaceAll("\\s", "");
        times.add(Long.parseLong(line1));
        distances.add(Long.parseLong(line2));

        long product = 1;
        for(int i = 0; i < times.size(); i++){
            long a = -1;
            long b = times.get(i);
            long c = -1 * distances.get(i);
            double root1 = ((-1 * b) + Math.sqrt(b * b - 4 * a * c))/(2.0 * a);
            double root2 = ((-1 * b) - Math.sqrt(b * b - 4 * a * c))/(2.0 * a);
            root1 = Math.floor(root1 + 1);
            root2 = Math.ceil(root2 - 1);
            product *= (root2 - root1 + 1);
            System.out.println(root1 + " " + root2);
        }
        System.out.println(product);
	}
}
