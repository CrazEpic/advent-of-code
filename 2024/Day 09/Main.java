
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(new File("b.txt"));
        int idCounter = 0;
        String denseDiskMap = input.nextLine();
        // ArrayList<Integer> diskMap = new ArrayList<>();
        // for (int i = 0; i < denseDiskMap.length(); i++) {
        //     if (i % 2 == 0) {
        //         int fileSize = Integer.parseInt(denseDiskMap.substring(i, i + 1));
        //         for (int j = 0; j < fileSize; j++) {
        //             diskMap.add(idCounter);
        //         }
        //         idCounter++;
        //     } else {
        //         int freeSize = Integer.parseInt(denseDiskMap.substring(i, i + 1));
        //         for (int j = 0; j < freeSize; j++) {
        //             diskMap.add(-1);
        //         }
        //     }
        // }

        // int left = 0;
        // int right = diskMap.size() - 1;
        // while (left < right) {
        //     if (diskMap.get(left) == -1) {
        //         if (diskMap.get(right) == -1) {
        //             right--;
        //         } else {
        //             diskMap.set(left, diskMap.get(right));
        //             diskMap.remove(right);
        //             right--;
        //         }
        //     } else {
        //         left++;
        //     }
        // }
        ArrayList<Pair> diskMap = new ArrayList<>();
        for (int i = 0; i < denseDiskMap.length(); i++) {
            if (i % 2 == 0) {
                int fileSize = Integer.parseInt(denseDiskMap.substring(i, i + 1));
                diskMap.add(new Pair(idCounter, fileSize));
                idCounter++;
            } else {
                int freeSize = Integer.parseInt(denseDiskMap.substring(i, i + 1));
                diskMap.add(new Pair(-1, -1 * freeSize));
            }
        }

        int right = diskMap.size() - 1;
        while (right >= 0) {
            if (diskMap.get(right).first != -1) { // look for candidate
                int left = 0;
                while (left < right) {
                    if (diskMap.get(left).first == -1 && Math.abs(diskMap.get(left).second) >= diskMap.get(right).second) {
                        diskMap.get(left).second += diskMap.get(right).second;
                        diskMap.add(left, diskMap.get(right));
                        // diskMap.remove(right + 1);
                        diskMap.set(right + 1, new Pair(-1, -1 * diskMap.get(left).second));
                        if (diskMap.get(left + 1).second == 0) {
                            diskMap.remove(left + 1);
                        }
                        break;
                    }
                    left++;
                }
            }
            right--;
        }

        long sum = 0;
        int indexCount = 0;
        for (int i = 0; i < diskMap.size(); i++) {
            if (diskMap.get(i).first != -1) {
                for (int j = 0; j < diskMap.get(i).second; j++) {
                    sum += diskMap.get(i).first * (indexCount + j);
                }
            }
            indexCount += Math.abs(diskMap.get(i).second);
        }
        // System.out.println(diskMap);

        System.out.println(sum);

    }

    // public static String getPairString(ArrayList<Pair> diskMap) {
    //     String result = "";
    //     for (int i = 0; i < diskMap.size(); i++) {
    //         if(diskMap.get(i).first != -1) {
    //             for(int j = 0; j < diskMap.get(i).second; j++) {
    //                 result += diskMap.get(i).first + " ";
    //             }
    //         } else {
    //             for(int j = 0; j < Math.abs(diskMap.get(i).second); j++) {
    //                 result += ". ";
    //             }
    //         }
    //     }
    //     return result;
    // }
}

class Pair {

    int first;
    int second;

    public Pair(int first, int second) {
        this.first = first;
        this.second = second;
    }

    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}
