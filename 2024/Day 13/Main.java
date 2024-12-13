
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(new File("b.txt"));

        int usedTokens = 0;
        BigInteger usedTokensPart2 = new BigInteger("0");
        while (input.hasNextLine()) {
            String line = input.nextLine();
            if (!line.equals("")) {
                String buttonA = line;
                String buttonB = input.nextLine();
                String prize = input.nextLine();

                // Button A: X+94, Y+34
                // Button B: X+22, Y+67
                // Prize: X=8400, Y=5400
                int buttonAX = Integer.parseInt(buttonA.substring(buttonA.indexOf('+') + 1, buttonA.indexOf(',')));
                int buttonAY = Integer.parseInt(buttonA.substring(buttonA.lastIndexOf('+') + 1));

                int buttonBX = Integer.parseInt(buttonB.substring(buttonB.indexOf('+') + 1, buttonB.indexOf(',')));
                int buttonBY = Integer.parseInt(buttonB.substring(buttonB.lastIndexOf('+') + 1));

                int prizeX = Integer.parseInt(prize.substring(prize.indexOf('=') + 1, prize.indexOf(',')));
                int prizeY = Integer.parseInt(prize.substring(prize.lastIndexOf('=') + 1));

                int minCost = Integer.MAX_VALUE;
                for (int a = 0; a < 100; a++) {
                    for (int b = 0; b < 100; b++) {
                        if (a * buttonAX + b * buttonBX == prizeX && a * buttonAY + b * buttonBY == prizeY) {
                            minCost = Math.min(minCost, 3 * a + b);
                        }
                    }
                }
                if (minCost != Integer.MAX_VALUE) {
                    usedTokens += minCost;
                }

                // ax a + bx b = cx
                // ay a + by b = cy
                // b = (ax * cy - ay * cx) / (ax * by - ay * bx)
                // a = ( cx - bx * b ) / ax
                if (buttonAX == buttonBX && buttonAY == buttonBY) {
                    System.out.println("Button A and Button B are the same");
                }

                BigDecimal cx = new BigDecimal("" + (prizeX + 10000000000000l));
                BigDecimal cy = new BigDecimal("" + (prizeY + 10000000000000l));
                BigDecimal ax = new BigDecimal("" + buttonAX);
                BigDecimal ay = new BigDecimal("" + buttonAY);
                BigDecimal bx = new BigDecimal("" + buttonBX);
                BigDecimal by = new BigDecimal("" + buttonBY);

                BigDecimal b;
                BigDecimal a;
                try {
                    b = ax.multiply(cy).subtract(ay.multiply(cx));
                    b = b.divide(ax.multiply(by).subtract(ay.multiply(bx)));
                    a = cx.subtract(bx.multiply(b));
                    a = a.divide(ax);
                } catch (Exception e) {
                    continue;
                }

                if (b.compareTo(new BigDecimal("" + (long) b.longValue())) == 0 && a.compareTo(new BigDecimal("" + (long) a.longValue())) == 0 && a.compareTo(BigDecimal.ZERO) >= 0 && b.compareTo(BigDecimal.ZERO) >= 0) {
                    // usedTokensPart2 = usedTokensPart2.add(new BigDecimal("" + (3 * a.longValue() + b.longValue())));
                    usedTokensPart2 = usedTokensPart2.add(a.multiply(new BigDecimal("3")).add(b).toBigInteger());
                }

                // double b = (ax * cy - ay * cx) / (double)(ax * by - ay * bx);
                // double a = (cx - bx * b) / (double)ax;
                // if(b == (long) b && a == (long) a && a >= 0 && b >= 0) {
                // usedTokensPart2 = (long) (3 * a + b);
                // }
            }
        }

        System.out.println(usedTokens);
        System.out.println(usedTokensPart2);
    }

}
