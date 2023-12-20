import java.util.*;
import java.io.File;
import java.lang.Math;

public class Main{
    public static void main(String[] args) throws Exception{
        Scanner input = new Scanner(new File("b.txt"));
        ArrayList<String> lines = new ArrayList<>();
        HashMap<String, Module> modules = new HashMap<>();
        // map module outputs
        while(input.hasNextLine()){
            String line = input.nextLine().strip();
            lines.add(line);
            String source = line.split(" -> ")[0];
            String destinations = line.split(" -> ")[1];
            String[] destinationsList = destinations.split(", ");
            if(source.charAt(0) == 'b'){
                Broadcaster broadcaster = new Broadcaster(source);
                broadcaster.outputs.addAll(Arrays.asList(destinationsList));
                modules.put(source, broadcaster);
            }
            else if(source.charAt(0) == '%'){
                String name = source.substring(1);
                FlipFlop flipflop = new FlipFlop(name);
                flipflop.outputs.addAll(Arrays.asList(destinationsList));
                modules.put(name, flipflop);
            }
            else if(source.charAt(0) == '&'){
                String name = source.substring(1);
                Conjuction conjuction = new Conjuction(name);
                conjuction.outputs.addAll(Arrays.asList(destinationsList));
                modules.put(name, conjuction);
            }
        }
        // map module inputs
        for(String line: lines){
            String source = line.split(" -> ")[0];
            source = source.replaceAll("[%&]", "");
            String destinations = line.split(" -> ")[1];
            String[] destinationsList = destinations.split(", ");
            for(String d: destinationsList){
                Module module = modules.get(d);
                if(module != null && module.inputs.contains(source) == false)
                    module.inputs.add(source);
            }
        }
        // map conjuction inputs to track last inputs, parallel arrays
        for(String k: modules.keySet()){
            Module m = modules.get(k);
            if(m instanceof Conjuction){
                Conjuction c = (Conjuction) m;
                for(String s: c.inputs){
                    c.lastInputPulse.add(false);
                }
            }
        }

        // for(String k: modules.keySet()){
        //     Module m = modules.get(k);
        //     System.out.println("MODULE " + k);
        //     System.out.println(m.inputs.toString());
        //     if(m instanceof Conjuction){
        //         Conjuction c = (Conjuction) m;
        //         System.out.println(c.lastInputPulse.toString());
        //     }
        //     System.out.println(m.outputs.toString());
        // }


        // for(int i = 0; i < 1000; i++)
        //     pushButton(modules);
        // System.out.println("LOW: " + countLowPulses);
        // System.out.println("HIGH: " + countHighPulses);
        // System.out.println(countLowPulses * countHighPulses);
        
        // final long startTime = System.currentTimeMillis();
        // while(lowToRX == false){
        //     pushButton(modules);
        // }
        // final long endTime = System.currentTimeMillis();
        // System.out.println("Total execution time: " + (endTime - startTime) + "ms");
        // System.out.println("Button Presses: " + buttonPresses);

        System.out.println(part2(modules));
    }

    public static long countLowPulses = 0;
    public static long countHighPulses = 0;
    public static long buttonPresses = 0;
    public static boolean lowToRX = false;

    public static void pushButton(HashMap<String, Module> modules){
        Module.currentPulses.add(new Pulse(false, "", "broadcaster"));
        buttonPresses++;
        while(Module.currentPulses.isEmpty() == false){
            Pulse currentPulse = Module.currentPulses.remove();
            Boolean strength = currentPulse.pulse;
            if(strength == false) countLowPulses++;
            else countHighPulses++;
            String dest = currentPulse.dest;
            if(modules.containsKey(dest))
                modules.get(dest).processPulse(currentPulse);
            
            if(dest.equals("rx") && strength == false)
                lowToRX = true;
        }
    }

    public static long part2(HashMap<String, Module> modules){
        long count = 0;
        HashMap<String, Long> cycles = new HashMap<>();
        String inputToRX = "gq"; // conjuction, 1 for my input
        ArrayList<String> inputsToInputsToRX = new ArrayList<>(); // assuming these are all conjuctions, 4 for my input
        for(String key: modules.keySet()){
            if(modules.get(key).outputs.contains(inputToRX)){
                inputsToInputsToRX.add(key);
            }
        }
        for(String s: inputsToInputsToRX){
            cycles.put(s, Long.MIN_VALUE);
        }
        while(cycles.values().contains(Long.MIN_VALUE)){
            Module.currentPulses.add(new Pulse(false, "", "broadcaster"));
            count++;
            while(Module.currentPulses.isEmpty() == false){
                Pulse currentPulse = Module.currentPulses.remove();
                String dest = currentPulse.dest;
                if(modules.containsKey(dest))
                    modules.get(dest).processPulse(currentPulse);

                String iteration = "";
                if(inputsToInputsToRX.contains(currentPulse.source) && currentPulse.pulse == true && currentPulse.dest.equals(inputToRX)){
                    if(cycles.get(currentPulse.source) == Long.MIN_VALUE){
                        iteration += currentPulse.source;
                        iteration = "ITERATION: " + count + " | " + iteration;
                        cycles.put(currentPulse.source, count);
                    }
                }
                if(iteration.equals("") == false){
                    System.out.println(iteration);
                }
            }
        }
        return lcm(new ArrayList<Long>(cycles.values()));
    }
    public static long gcd(long a, long b){
        if (b == 0) return a;
        return gcd(b, a % b);
    }
    public static long lcm(ArrayList<Long> list){
        long ans = list.get(0);
        for (int i = 1; i < list.size(); i++)
            ans = (((list.get(i) * ans)) / (gcd(list.get(i), ans)));
        return ans;
    }
}

class Pulse{
    public boolean pulse;
    public String source;
    public String dest;
    public Pulse(boolean p, String s, String d){
        pulse = p;
        source = s;
        dest = d;
    }
}

abstract class Module{
    public static Queue<Pulse> currentPulses;
    public ArrayList<String> inputs;
    public ArrayList<String> outputs;
    public String name;
    public Module(){
        if(currentPulses == null)
            currentPulses = new LinkedList<>();
        inputs = new ArrayList<>();
        outputs = new ArrayList<>();
        name = "";
    }
    public Module(String n){
        this();
        name = n;
    }
    abstract void processPulse(Pulse pulse);
}

class Broadcaster extends Module{
    public Broadcaster(String n){
        super(n);
    }

    @Override
    void processPulse(Pulse pulse){
        for(String out: outputs){ // send
            currentPulses.add(new Pulse(pulse.pulse, name, out));
        }
    }
}

class FlipFlop extends Module{
    public boolean state;

    public FlipFlop(String n){
        super(n);
        state = false;
    }

    @Override
    void processPulse(Pulse pulse){
        if(pulse.pulse == false){ // if low
            state = !state; // flip
            for(String out: outputs){ // and send
                currentPulses.add(new Pulse(state, name, out));
            }
        }
    }
}

class Conjuction extends Module{
    public ArrayList<Boolean> lastInputPulse;

    public Conjuction(String n){
        super(n);
        lastInputPulse = new ArrayList<>();
    }
    public boolean allLastInputsTrue(){
        return !(lastInputPulse.contains(false));
    }

    @Override
    void processPulse(Pulse pulse){
        int index = inputs.indexOf(pulse.source);
        lastInputPulse.set(index, pulse.pulse); // update memory
        boolean strength = true;
        if(allLastInputsTrue()) // remember and send low if all last inputs are high
            strength = false;
        for(String out: outputs){ // and send
            currentPulses.add(new Pulse(strength, name, out));
        }
    }
}