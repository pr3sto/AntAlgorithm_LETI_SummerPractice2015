package Algorithm;

import java.util.*;

public class AntAlgorithm {
    private Graph field;                // graph
    private List<Edge> edges;           // edges list
    private double greed;               // жадность алгоритма - больше влияет длина ребра
    private double gregariousness;      // стадность алгоритма - больше влияет кол-во феромонов
    private double evaporationSpeed;    // скорость испарения
    private int count;                  // steps counter
    private int startIndex;             // starting node index
    private int finishIndex;            // finish node index
    private int currentIndex;           // current node index

    public AntAlgorithm(Graph fld, double k, double p, int start, int finish){    // constructor
        field = fld;
        count = 0;
        startIndex = start;
        finishIndex = finish;

        if (k <= 1){
            greed = k;
            gregariousness = 1 - k;
        }
        else{
            greed = 0.5;
            gregariousness = 0.5;
        }

        if (evaporationSpeed < 1)
            evaporationSpeed = p;
        else
            evaporationSpeed = 0.5;

        // initialisation of edges list
        for (int i = 0; i < field.numberOfVertices; ++i)
            for (int j = i; j < field.numberOfVertices; ++j)
                if ( field.adjacencyMatrix[i][j] != 0){
                    edges.add(new Edge(field.adjacencyMatrix[i][j], i, j));
                }
    }

    public void step(){                                     // ДОБАВИТЬ ПРОВЕРКУ НА ВОЗМОЖНОСТЬ ПЕРЕХОДА В ПРИНЦИПЕ
        List<Integer> wayN = new ArrayList<>();             // way ant goes (nodes)
        List<Edge> wayE = new ArrayList<>();                // way ant goes (edges) MAYBE DON'T NEED IT
        wayN.add(startIndex);
        currentIndex = startIndex;
        if (count < 5) {                                    // first 5 ants go randomly
            while (currentIndex != finishIndex){            // while ant not find finish
                List<Integer> possibleNN = new ArrayList<>();   // list of possible next nodes

                for (int i = 0; i < field.numberOfVertices; ++i)
                    if (field.adjacencyMatrix[currentIndex][i] != 0) {
                        boolean ok = true;                  // true if an ant not been in possible next node
                        for (int j = 0; j < wayN.size(); ++j)
                            if (i == wayN.get(j))
                                ok = false;                 // already been here
                        if (ok)
                            possibleNN.add(i);
                    }

                Random rand = new Random();

                if (possibleNN.size() != 0) {                // random choice
                    currentIndex = possibleNN.get(rand.nextInt() % possibleNN.size());
                } else {
                    currentIndex = wayN.get(wayN.size() - 1);
                    wayN.remove(wayN.size() - 1);
                }

                for (Edge i : edges)                       // adding edge to way
                    if ((i.firstNode == Math.min(currentIndex, wayN.get(wayN.size() - 1)))
                            && (i.secondNode == Math.max(currentIndex, wayN.get(wayN.size() - 1)))) {
                        wayE.add(i);
                        i.inCurrentPath = true;
                        break;
                    }
            }

            wayN.clear();

            int wayWeight = 0;                                  // weight of way
            for (Edge i : wayE) {
                wayWeight += i.weight;
            }

            for (Edge i : edges){                               // pheromone update
                i.pheromone = (1.0 - evaporationSpeed) * i.pheromone;
                if (i.inCurrentPath)
                    i.pheromone += 1 / wayWeight;
            }
        }
        else{                                               // other steps, when the choice depends on the pheromone
            while(currentIndex != finishIndex){             // while ant not find finish
                List<Integer> possibleNN = new ArrayList<>();   // list of possible next nodes
                List<Edge> possibleNE = new ArrayList<>();      // list of possible next edges

                for (int i = 0; i < field.numberOfVertices; ++i)
                    if (field.adjacencyMatrix[currentIndex][i] != 0){
                        boolean ok = true;                  // true if an ant not been in possible next node
                        for (int j = 0; j < wayN.size(); ++j)
                            if (i == wayN.get(j))
                                ok = false;                 // already been here
                        if (ok)
                            possibleNN.add(i);
                    }

                for (int j : possibleNN){
                    for (Edge i : edges)
                        if ((i.firstNode == Math.min(j, wayN.get(wayN.size() - 1)))
                                && (i.secondNode == Math.max(j, wayN.get(wayN.size() - 1)))) {
                            possibleNE.add(i);
                            break;
                        }
                }

                List<Double> probability = new ArrayList<>(possibleNN.size());      //probabilities of going that way

                double sum = 0.0;

                for (int i = 0; i < possibleNE.size(); ++i){
                    double k = Math.pow( possibleNE.get(i).pheromone,gregariousness ) * Math.pow (1/possibleNE.get(i).weight, greed);
                    sum += k;
                    probability.add(k);
                }

                for (int i = 0; i < probability.size(); ++i){
                    double temp = probability.get(i);
                    probability.set(i, temp/sum);
                }

                for (int i = 1; i < probability.size(); ++i){
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

                if (possibleNN.size() == 0){                // random choice
                    currentIndex = wayN.get( wayN.size() - 1);
                    wayN.remove( wayN.size() - 1);
                }

                for ( Edge i : edges)                       // adding edge to way
                    if ( (i.firstNode == Math.min(currentIndex, wayN.get(wayN.size() - 1)) )
                            && (i.secondNode == Math.max(currentIndex, wayN.get(wayN.size() - 1)) ) ){
                        wayE.add(i);
                        i.inCurrentPath = true;
                        break;
                    }

                wayN.add(currentIndex);
            }

            wayN.clear();

            int wayWeight = 0;                                  // weight of way
            for (Edge i : wayE){
                wayWeight += i.weight;
            }

            for (Edge i : edges){                               // pheromone update
                i.pheromone = (1.0 - evaporationSpeed) * i.pheromone;
                if (i.inCurrentPath)
                    i.pheromone += 1/wayWeight;
            }
        }
    }


}
