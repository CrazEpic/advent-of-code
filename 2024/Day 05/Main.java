
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        HashMap<Integer, HashSet<Integer>> laterDependents = new HashMap<>();
        while (input.hasNextLine()) {
            String rule = input.nextLine();
            if (rule.equals("")) {
                break;
            }
            int pipeIndex = rule.indexOf('|');
            int req = Integer.parseInt(rule.substring(0, pipeIndex));
            int dep = Integer.parseInt(rule.substring(pipeIndex + 1));
            if (laterDependents.get(req) == null) {
                laterDependents.put(req, new HashSet<>());
            }
            laterDependents.get(req).add(dep);
        }
        int sum = 0;
        int invalidSum = 0;
        while (input.hasNextLine()) {
            String line = input.nextLine();
            String[] numsStr = line.split(",");
            ArrayList<Integer> nums = new ArrayList<>();
            for (String s : numsStr) {
                nums.add(Integer.parseInt(s));
            }

            boolean valid = checkValidOrder(nums, laterDependents);
            if (valid) {
                sum += nums.get(nums.size() / 2);
            } else {
                fixValidOrder(nums, laterDependents);
                invalidSum += nums.get(nums.size() / 2);
                // System.out.println(nums.toString());
            }
        }
        System.out.println(sum);
        System.out.println(invalidSum);
    }

    public static boolean checkValidOrder(ArrayList<Integer> nums, HashMap<Integer, HashSet<Integer>> laterDependents) {
        boolean valid = true;
        for (int i = 0; i < nums.size(); i++) {
            // check that later dependents are not before this number
            HashSet<Integer> currentLaterDeps = laterDependents.get(nums.get(i));
            if (currentLaterDeps != null) {
                for (int j = i; j >= 0; j--) {
                    if (currentLaterDeps.contains(nums.get(j))) {
                        valid = false;
                        break;
                    }
                }
            }
            if (!valid) {
                break;
            }
        }
        return valid;
    }

    public static void fixValidOrder(ArrayList<Integer> nums, HashMap<Integer, HashSet<Integer>> laterDependents) {
        int i = 0;
        while (i < nums.size()) {
            boolean noBefore = true;
            HashSet<Integer> currentLaterDeps = laterDependents.get(nums.get(i));
            // check that later dependents are not before this number
            int badOrderIndex = -1;
            if (currentLaterDeps != null) {
                for (int j = i; j >= 0; j--) {
                    if (currentLaterDeps.contains(nums.get(j))) {
                        noBefore = false;
                        badOrderIndex = j;
                        break;
                    }
                }
            }
            if (!noBefore) {
                int removed = nums.remove(badOrderIndex);
                nums.add(removed);
                i--;
            } else {
                i++;
            }
        }
    }
}
