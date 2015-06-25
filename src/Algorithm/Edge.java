package Algorithm;

public class Edge {
    public double pheromone;
    public int weight;
    public int firstNode;
    public int secondNode;
    public boolean inCurrentPath;

    public Edge(int w, int first, int second){
        pheromone = 0.0;
        weight = w;
        firstNode = first;
        secondNode = second;
        inCurrentPath = false;
    }
}
