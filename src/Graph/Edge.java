package Graph;

// ребро
public class Edge {
    public double pheromone;
    public int weight;
    public int firstNode;         // от вершины
    public int secondNode;        // до вершины
    public boolean inCurrentPath; // принадлежность к пути

    public Edge(int weight_, int firstNode_, int secondNode_) {
        pheromone = 0.0;
        weight = weight_;
        firstNode = firstNode_;
        secondNode = secondNode_;
        inCurrentPath = false;
    }
}
