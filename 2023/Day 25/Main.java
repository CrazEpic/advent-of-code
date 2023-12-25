import java.util.*;
import java.io.File;
import java.lang.Math;

public class Main{
    public static void main(String[] args) throws Exception{
        Scanner input = new Scanner(new File("b.txt"));
        HashMap<String, HashSet<String>> map = new HashMap<>();
        while(input.hasNextLine()){
            String line = input.nextLine().strip();
            String source = line.split(":")[0];
            String[] dests = line.split(":")[1].strip().split(" ");
            if(map.containsKey(source) == false)
                map.put(source, new HashSet<>());
            for(String d: dests){
                map.get(source).add(d);
                if(map.containsKey(d) == false)
                    map.put(d, new HashSet<>());
                map.get(d).add(source);
            }
        }
        System.out.println(part1EdgeCut(map));
        // for(String key: map.keySet()){
        //     System.out.println(key+"="+map.get(key).toString());
        // }
    }
    public static int part1EdgeCut(HashMap<String, HashSet<String>> map){
        int totalVertices = map.keySet().size();
        ArrayList<Edge> edgeCut;
        do{ edgeCut = kargerAlgorithm(map); System.out.println(edgeCut.size()); } while(edgeCut.size() != 3); // get an edge cut of size 3
        removeEdgesUndirected(edgeCut, map);
        int reachableTest = reachableFromVertex(map.keySet().iterator().next(), map);
        addEdgesUndirected(edgeCut, map);
        return reachableTest * (totalVertices - reachableTest);
        // int totalVertices = map.keySet().size();
        // ArrayList<Edge> edges = new ArrayList<>();
        // for(String source: map.keySet()){
        //     HashSet<String> dests = map.get(source);
        //     for(String dest: dests){
        //         Edge edge = new Edge(source, dest);
        //         if(edges.contains(edge) == false)
        //             edges.add(edge);
        //     }
        // }
        // // remove 3 edges at a time
        // for(int i = 0; i < edges.size(); i++){
        //     for(int j = 0; j < edges.size(); j++){
        //         for(int k = 0; k < edges.size(); k++){
        //             if(i == j || j == k || k == i)
        //                 continue;
        //             ArrayList<Edge> toRemove = new ArrayList<>();
        //             toRemove.add(edges.get(i));
        //             toRemove.add(edges.get(j));
        //             toRemove.add(edges.get(k));
        //             removeEdgesUndirected(toRemove, map);
        //             int reachableTest = reachableFromVertex(map.keySet().iterator().next(), map);
        //             addEdgesUndirected(toRemove, map);
        //             if(totalVertices > reachableTest)
        //                 return reachableTest * (totalVertices - reachableTest);
        //         }
        //     }
        // }
    }

    public static void removeEdgesUndirected(ArrayList<Edge> edges, HashMap<String, HashSet<String>> map){
        for(Edge e: edges){
            if(map.containsKey(e.a))
                map.get(e.a).remove(e.b);
            if(map.containsKey(e.b))
                map.get(e.b).remove(e.a);
        }
    }
    public static void addEdgesUndirected(ArrayList<Edge> edges, HashMap<String, HashSet<String>> map){
        for(Edge e: edges){
            if(map.containsKey(e.a) == false)
                map.put(e.a, new HashSet<>());
            if(map.containsKey(e.b) == false)
                map.put(e.b, new HashSet<>());
            map.get(e.a).add(e.b);
            map.get(e.b).add(e.a);
        }
    }
    public static int reachableFromVertex(String start, HashMap<String, HashSet<String>> map){
        HashSet<String> visited = new HashSet<>();
        Queue<String> toVisit = new LinkedList<>();
        visited.add(start);
        toVisit.add(start);
        while(toVisit.isEmpty() == false){
            String v = toVisit.remove();
            for(String d: map.get(v)){
                if(visited.contains(d) == false){
                    visited.add(d);
                    toVisit.add(d);
                }
            }
        }
        return visited.size();
    }

    public static ArrayList<Edge> kargerAlgorithm(HashMap<String, HashSet<String>> map){
        /*
            1)  Initialize contracted graph CG as copy of original graph
            2)  While there are more than 2 vertices.
                a) Pick a random edge (u, v) in the contracted graph.
                b) Merge (or contract) u and v into a single vertex (update the contracted graph).
                c) Remove self-loops
            3) Return cut represented by two vertices.
        */


        // 1)  Initialize contracted graph CG as copy of original graph
        ArrayList<Edge> edges = new ArrayList<>();
        HashMap<String, HashSet<String>> contracted = new HashMap<>();
        for(String source: map.keySet()){
            contracted.put(source, new HashSet<>(map.get(source)));
            for(String dest: contracted.get(source)){
                if(edges.contains(new Edge(source, dest)) == false){
                    edges.add(new Edge(source, dest, Integer.toString(edges.size())));
                }
            }
        }
        HashMap<String, Edge> originalEdges = new HashMap<>();
        for(Edge edge: edges){ // copy original edges to find the cut later
            Edge copy = new Edge(edge);
            originalEdges.put(copy.name, copy);
        }
        /*
            2)  While there are more than 2 vertices.
                a) Pick a random edge (u, v) in the contracted graph.
                b) Merge (or contract) u and v into a single vertex (update the contracted graph).
                c) Remove self-loops
        */
        Random rand = new Random();
        while(contracted.keySet().size() > 2){
            int randomIndex = rand.nextInt(edges.size());
            Edge randomEdge = edges.get(randomIndex);
            contract(randomEdge, edges, contracted);
            removeSelfLoops(edges, contracted);
        }
        
        // 3) Return cut represented by two vertices.
        ArrayList<Edge> edgeCut = new ArrayList<>();
        for(Edge edge: edges){
            edgeCut.add(originalEdges.get(edge.name));
        }
        return edgeCut;
    }
    public static void contract(Edge edge, ArrayList<Edge> edges, HashMap<String, HashSet<String>> contracted){
        String u = edge.a;
        String v = edge.b;
        // remove the edge
        contracted.get(u).remove(v);
        contracted.get(v).remove(u);
        for(int i = 0; i < edges.size(); i++){
            if(edges.get(i).name.equals(edge.name)){
                edges.remove(i);
                break;
            }
        }
        // merge u onto v
        for(int i = 0; i < edges.size(); i++){
            if(edges.get(i).a.equals(u))
                edges.get(i).a = v;
            if(edges.get(i).b.equals(u))
                edges.get(i).b = v;
        }
        for(String key: contracted.keySet()){ // redirect all arrows towards u to v
            HashSet<String> dests = contracted.get(key);
            if(dests.contains(u)){
                dests.remove(u);
                dests.add(v);
            }
        }
        for(String dest: contracted.get(u)){ // redirect all arrows from u to be sourced from v instead
            contracted.get(v).add(dest);
        }
        contracted.remove(u);
    }

    public static void removeSelfLoops(ArrayList<Edge> edges, HashMap<String, HashSet<String>> contracted){
        for(int i = edges.size()-1; i >= 0; i--){
            if(edges.get(i).a.equals(edges.get(i).b)){
                edges.remove(i);
            }
        }
        for(String key: contracted.keySet()){
            HashSet<String> dests = contracted.get(key);
            while(dests.remove(key));
        }
    }
}
class Edge{
    public String a;
    public String b;
    public String name;
    public Edge(String u, String v){
        a = u;
        b = v;
        name = "";
    }
    public Edge(String u, String v, String n){
        a = u;
        b = v;
        name = n;
    }
    public Edge(Edge copy){
        a = copy.a;
        b = copy.b;
        name = copy.name;
    }
    @Override
    public boolean equals(Object o){
        if(this == o)
            return true;
        if(!(o instanceof Edge))
            return false;
        HashSet<String> connection1 = new HashSet<>();
        connection1.add(a);
        connection1.add(b);
        Edge e = (Edge) o;
        HashSet<String> connection2 = new HashSet<>();
        connection2.add(e.a);
        connection2.add(e.b);
        return connection1.equals(connection2);
    }
    @Override
    public int hashCode(){
        return a.hashCode() + b.hashCode();
    }
}