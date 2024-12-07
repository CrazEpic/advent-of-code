
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String s = "";
		while(input.hasNextLine()) {
			s += input.nextLine();
		}

        Pattern pattern = Pattern.compile("(do[(][)]|don't[(][)]|mul[(][0-9]+,[0-9]+[)])");
        Matcher matcher = pattern.matcher(s);
		boolean enabled = true;
		int sum = 0;
		while(matcher.find()) {
			String match = matcher.group();
			if(match.equals("do()")) {
				enabled = true;
			} else if(match.equals("don't()")) {
				enabled = false;
			} else {
				String[] nums = match.substring(4, match.length() - 1).split(",");
				if(enabled) {
					sum += Integer.parseInt(nums[0]) * Integer.parseInt(nums[1]);
				}
			}
		}
		System.out.println(sum);
    }
}
