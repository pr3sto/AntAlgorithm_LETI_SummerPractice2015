package Algorithm;

import java.util.*;

import Graph.Graph;
import Graph.Edge;
import Staff.Pair;

public class AntAlgorithm {

    private Graph graph;                // graph
    private double greed;               // жадность алгоритма - больше влияет длина ребра
    private double gregariousness;      // стадность алгоритма - больше влияет кол-во феромонов
    public double evaporationSpeed;    // скорость испарения
    private int count;                  // steps counter
    private int startIndex;             // starting node index
    private int finishIndex;            // finish node index
    private int currentIndex;           // current node index
    private int numberOfRandomAnts;
    private int numberOfAnts;

    public AntAlgorithm(Graph graph_,
                        double greed_,
                        double evaporationSpeed_,
                        int numberOfRandomAnts_,
                        int numberOfAnts_,
                        Pair<Integer, Integer> path) {    // constructor

        graph = graph_;

        if (greed_ <= 1) {
            greed = greed_;
            gregariousness = 1 - greed;
        } else {
            greed = 0.5;
            gregariousness = 0.5;
        }

        if (evaporationSpeed < 1)
            evaporationSpeed = evaporationSpeed_;
        else
            evaporationSpeed = 0.5;

        numberOfRandomAnts = numberOfRandomAnts_;
        numberOfAnts = numberOfAnts_;

        startIndex = path.first;
        finishIndex = path.second;

        count = 0;
    }

    public boolean finished() {
        return count == numberOfAnts;
    }

    public void step() {
        List<Integer> wayN = new ArrayList<>();             // way ant goes (nodes)
        List<Edge> wayE = new ArrayList<>();                // way ant goes (edges)
        wayN.add(startIndex);
        currentIndex = startIndex;
        List<Integer> bannedN = new ArrayList<>();

        if (count < numberOfRandomAnts) {                    // first numberOfRandomAnts ants go randomly
            while (currentIndex != finishIndex) {            // while ant not find finish

                List<Integer> possibleNN = new ArrayList<>();   // list of possible next nodes

                for (int i = 0; i < graph.numberOfVertices; ++i)
                    if (graph.adjacencyMatrix[currentIndex][i] != 0) {
                        boolean ok = true;                  // true if an ant not been in possible next node

                        for (Integer j : wayN)
                            if (i == j) {
                                ok = false;                 // already been here
                                break;
                            }
                        if (ok) {
                            for (Integer j : bannedN)
                                if (j == i)
                                    ok = false;
                            if (ok) possibleNN.add(i);
                        }
                    }

                Random rand = new Random();

                if (possibleNN.size() != 0) {                // random choice
                    currentIndex = possibleNN.get(Math.abs(rand.nextInt()) % possibleNN.size());
                    wayN.add(currentIndex);
                } else {
                    bannedN.add(currentIndex);
                    currentIndex = wayN.get(wayN.size() - 2);
                    wayN.remove(wayN.size() - 1);
                    wayE.remove(wayE.size() - 1);
                    possibleNN.clear();
                    continue;
                }

                for (Edge i : graph.edges)                    // adding edge to way
                    if ((i.firstNode == Math.min(currentIndex, wayN.get(wayN.size() - 2)))
                            && (i.secondNode == Math.max(currentIndex, wayN.get(wayN.size() - 2)))) {
                        wayE.add(i);
                        break;
                    }
            }

            for(Edge i : wayE)
                i.inCurrentPath = true;

            wayN.clear();

            double wayWeight = 0.0;                              // weight of way
            for (Edge i : wayE) {
                wayWeight += i.weight;
            }

            for (Edge i : graph.edges) {                               // pheromone update
                i.pheromone = (1.0 - evaporationSpeed) * i.pheromone;
                if (i.inCurrentPath) {
                    i.pheromone += 1.0 / wayWeight;
                    i.inCurrentPath = false;
                }
            }

            bannedN.clear();

        } else {                                             // other steps, when the choice depends on the pheromone
            while (currentIndex != finishIndex) {             // while ant not find finish

                List<Integer> possibleNN = new ArrayList<>();   // list of possible next nodes
                List<Edge> possibleNE = new ArrayList<>();      // list of possible next edges

                for (int i = 0; i < graph.numberOfVertices; ++i) {
                    if (graph.adjacencyMatrix[currentIndex][i] != 0) {
                        boolean ok = true;                  // true if an ant not been in possible next node
                        for (Integer j : wayN)
                            if (i == j) {
                                ok = false;                 // already been here
                                break;
                            }
                        if (ok) {
                            for (Integer j : bannedN)
                                if (j == i)
                                    ok = false;
                            if (ok) possibleNN.add(i);
                        }
                    }
                }

                for (int j : possibleNN) {
                    for (Edge i : graph.edges)
                        if ((i.firstNode == Math.min(j, currentIndex))
                                && (i.secondNode == Math.max(j, currentIndex))) {
                            possibleNE.add(i);
                            break;
                        }
                }

                List<Double> probability = new ArrayList<>();      //probabilities of going that way

                double sum = 0.0;

                for (Edge i : possibleNE) {
                    double k = Math.pow(i.pheromone,gregariousness) * Math.pow (1.0 / i.weight, greed);
                    sum += k;
                    probability.add(k);
                }

                if (sum == 0.0) {
                    bannedN.add(currentIndex);
                    currentIndex = wayN.get(wayN.size() - 2);
                    wayN.remove(wayN.size() - 1);
                    wayE.remove(wayE.size() - 1);
                    possibleNN.clear();
                    continue;
                }

                for (int i = 0; i < probability.size(); ++i) {
                    double temp = probability.get(i);
                    probability.set(i, temp/sum);
                }

                for (int i = 1; i < probability.size(); ++i) {
                    double temp = probability.get(i);
                    probability.set(i, temp + probability.get(i - 1));
                }

                sum = Math.random();                            // random real number [0,1)

                for (int i = 0; i < probability.size(); ++i)    // random choice
                    if ( probability.get(i) >= sum) {
                        if ( possibleNE.get(i).firstNode == wayN.get(wayN.size() - 1) )
                            currentIndex = possibleNE.get(i).secondNode;
                        else
                            currentIndex = possibleNE.get(i).firstNode;
                        break;
                    }

                if (possibleNN.size() == 0) {                // random choice
                    bannedN.add(currentIndex);
                    currentIndex = wayN.get(wayN.size() - 2);
                    wayN.remove(wayN.size() - 1);
                    wayE.remove(wayE.size() - 1);
                    possibleNN.clear();
                    continue;
                }

                for ( Edge i : graph.edges)                       // adding edge to way
                    if ( (i.firstNode == Math.min(currentIndex, wayN.get(wayN.size() - 1)) )
                            && (i.secondNode == Math.max(currentIndex, wayN.get(wayN.size() - 1)) ) ){
                        wayE.add(i);
                        break;
                    }

                wayN.add(currentIndex);
            }

            for(Edge i : wayE)
                i.inCurrentPath = true;

            wayN.clear();

            double wayWeight = 0.0;                              // weight of way
            for (Edge i : wayE) {
                wayWeight += i.weight;
            }

            for (Edge i : graph.edges) {                               // pheromone update
                i.pheromone = (1.0 - evaporationSpeed) * i.pheromone;
                if (i.inCurrentPath){
                    i.pheromone += 1.0 / wayWeight;
                    i.inCurrentPath = false;}
            }

            bannedN.clear();
        }

        ++count;
    }

    public List<Integer> autoAlgorithm() {
        while (count <= numberOfAnts)
            step();

        return findPath();
    }

    public List<Integer> findPath() {
        List<Integer> nodesInPath = new ArrayList<>();
        List<Edge> probEdges = new ArrayList<>();
        nodesInPath.add(startIndex);
        List<Edge> banned = new ArrayList<>();
        currentIndex = startIndex;
        int wayWeight = 0;                  // weight of way

        while (currentIndex != finishIndex) {
            for (Edge i : graph.edges)
                if ( i.firstNode == currentIndex || i.secondNode == currentIndex ){
                    boolean ok = true;
                    for (Edge k : banned)
                        if (k == i)
                            ok = false;
                    if (ok)
                        probEdges.add(i);
                }

            Edge edge = probEdges.get(0);
            for (Edge i : probEdges)
                if (i.pheromone > edge.pheromone)
                    edge = i;

            banned.add(edge);
            probEdges.clear();

            wayWeight += edge.weight;

            if (edge.firstNode == currentIndex)
                nodesInPath.add(edge.secondNode);
            else
                nodesInPath.add(edge.firstNode);

            currentIndex = nodesInPath.get(nodesInPath.size() - 1);
        }

        nodesInPath.add(wayWeight);

        return nodesInPath; // path of ant
    }
}
