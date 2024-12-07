
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int count = 0;
        while (input.hasNextLine()) {
            String line = input.nextLine();
            Scanner s = new Scanner(line);
            ArrayList<Integer> a = new ArrayList<>();
            while (s.hasNextInt()) {
                a.add(s.nextInt());
            }
            //  boolean increasing = a.get(1) - a.get(0) > 0;
            //  boolean safe = true;
            //  for(int i = 0; i < a.size() - 1; i++) {
            //      if(Math.abs(a.get(i+1) - a.get(i)) > 3 || Math.abs(a.get(i+1) - a.get(i)) < 1 || (increasing ? (a.get(i+1) <= a.get(i)) : (a.get(i+1) >= a.get(i)))) {
            //          safe = false;
            //          break;
            //      }
            //  }
            for (int d = -1; d < a.size(); d++) {
                int removed = -1;
                if (d != -1) {
                    removed = a.remove(d);
                }

                boolean increasing = a.get(1) - a.get(0) > 0;
                boolean safe = true;
                for (int i = 0; i < a.size() - 1; i++) {
                    if (Math.abs(a.get(i + 1) - a.get(i)) > 3 || Math.abs(a.get(i + 1) - a.get(i)) < 1 || (increasing ? (a.get(i + 1) <= a.get(i)) : (a.get(i + 1) >= a.get(i)))) {
                        safe = false;
                        break;
                    }
                }
                if (safe) {
                    count++;
                    break;
                }

                if (d != -1) {
                    a.add(d, removed);
                }
            }
        }
        System.out.println(count);
    }
}
