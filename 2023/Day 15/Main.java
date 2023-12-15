import java.util.*;
import java.io.File;
import java.lang.Math;

public class Main{
    public static void main(String[] args) throws Exception{
        Scanner input = new Scanner(new File("a.txt"));
        String line = input.nextLine();
        String[] sequence = line.split(",");
        // int totalHash = 0;
        // for(String s: sequence){
        //     totalHash += hash(s);
        // }
        // System.out.println(totalHash);
        ArrayList<ArrayList<Pair>> boxes = new ArrayList<>();
        for(int i = 0; i < 256; i++)
            boxes.add(new ArrayList<Pair>());
        for(String s: sequence){
            if(s.indexOf("-") != -1){
                String k = s.split("-")[0];
                int box = hash(k);
                Pair copyPair = new Pair(k, -1);
                boxes.get(box).remove(copyPair);
            }
            else if(s.indexOf("=") != -1){
                String k = s.split("=")[0];
                int focal = Integer.parseInt(s.split("=")[1]);
                int box = hash(k);
                Pair copyPair = new Pair(k, focal);
                int index = boxes.get(box).indexOf(copyPair);
                if(index != -1){
                   boxes.get(box).set(index, copyPair); 
                }
                else{
                    boxes.get(box).add(copyPair); 
                }
            }
        }
        int totalFocusingPower = 0;
        for(int i = 0; i < boxes.size(); i++){
            for(int j = 0; j < boxes.get(i).size(); j++){
                totalFocusingPower += (i + 1) * (j + 1) * (boxes.get(i).get(j).value);
            }
        }
        System.out.println(totalFocusingPower);
    }

    public static int hash(String str){
        int hash = 0;
        for(int i = 0; i < str.length(); i++){
            hash += str.charAt(i);
            hash *= 17;
            hash %= 256;
        }
        return hash;
    }
}

class Pair{
    public String key;
    public int value;
    public Pair(String k, int v){
        key = k;
        value = v;
    }
    @Override
    public boolean equals(Object o){
        if(o == this){
            return true;
        }
        if(!(o instanceof Pair)){
            return false;
        }
        Pair p = (Pair) o;
        return key.equals(p.key);
    }
}