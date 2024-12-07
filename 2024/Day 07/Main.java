
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        long sumResults = 0;
        while (input.hasNextLine()) {
            String line = input.nextLine();
            int colonIndex = line.indexOf(":");
            long result = Long.parseLong(line.substring(0, colonIndex));
            String[] termStrs = line.substring(colonIndex + 2).split(" ");
            ArrayList<Long> terms = new ArrayList<>();
            for (String t : termStrs) {
                terms.add(Long.parseLong(t));
            }
            // if(recurse(terms, new ArrayList<>(), result, 0, '+') || recurse(terms, new ArrayList<>(), result, 0, '*')) {
            //     sumResults += result;
            // }
            if (recurse(terms, new ArrayList<>(), result, 0, '+') || recurse(terms, new ArrayList<>(), result, 0, '*') || recurse(terms, new ArrayList<>(), result, 0, 'C')) {
                sumResults += result;
            }
        }
        System.out.println(sumResults);
    }

    public static boolean recurse(ArrayList<Long> terms, ArrayList<Character> opSequence, long target, int index, char op) {
        if (index == terms.size() - 1) {
            // evaluate
            long result = terms.get(0);
            for (int i = 0; i < terms.size() - 1; i++) {
                if (opSequence.get(i) == '+') {
                    result += terms.get(i + 1);
                } else if (opSequence.get(i) == '*') {
                    result *= terms.get(i + 1);
                } else if (opSequence.get(i) == 'C') {
                    result = Long.parseLong(result + "" + terms.get(i + 1));
                }
            }
            return result == target;
        }
        ArrayList<Character> newOpSequence1 = new ArrayList<>(opSequence);
        ArrayList<Character> newOpSequence2 = new ArrayList<>(opSequence);
        ArrayList<Character> newOpSequence3 = new ArrayList<>(opSequence);
        newOpSequence1.add(op);
        newOpSequence2.add(op);
        newOpSequence3.add(op);
        return recurse(terms, newOpSequence1, target, index + 1, '+') || recurse(terms, newOpSequence2, target, index + 1, '*') || recurse(terms, newOpSequence3, target, index + 1, 'C');
    }
}
