
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        ArrayList<Integer> l1 = new ArrayList<>();
        ArrayList<Integer> l2 = new ArrayList<>();
        while (input.hasNextInt()) {
            l1.add(input.nextInt());
            l2.add(input.nextInt());
        }
        Collections.sort(l1);
        Collections.sort(l2);
        int distance = 0;
        for (int i = 0; i < l1.size(); i++) {
            distance += Math.abs(l1.get(i) - l2.get(i));
        }
        System.out.println(distance);

        HashMap<Integer, Integer> freq = new HashMap<>();
        for (int n : l2) {
            if (freq.get(n) == null) {
                freq.put(n, 0);
            }
            freq.put(n, freq.get(n) + 1);
        }
        int similarity = 0;
        for (int n : l1) {
            if (freq.get(n) != null) {
                similarity += n * freq.get(n);
            }
        }
        System.out.println(similarity);
    }
}
