package Algorithm;

import java.util.*;

public class Graph {

    public int numberOfVertices;         // Number of graph nodes
    public int[][] adjacencyMatrix;      // Adjacency matrix with weights
    public boolean[][] linksMatrix;      // Adjacency matrix with links only. We'll use Warshall algorithm to it.

    public Graph() {                     // Constructor
        numberOfVertices = 0;
        adjacencyMatrix = null;
        linksMatrix = null;
    }

    public void createGraph(int n) {     // create graph
        numberOfVertices = n;
        adjacencyMatrix = new int[n][n];
        linksMatrix = new boolean[n][n];
    }

    public boolean isCreated() {         // is graph created
        return numberOfVertices != 0;
    }

    private void makeWarshallAlgorithm(){       // Warshall algorithm
        for (int k = 0; k < numberOfVertices; ++k)
            for (int i = 0; i < numberOfVertices; ++i)
                for (int j = 0; j < numberOfVertices; ++j)
                    linksMatrix[i][j] = linksMatrix[i][j] || (linksMatrix[i][j] && linksMatrix[i][j]);
    }

    public void generateGraph(int linksPercent,
                              int leftBound,
                              int rightBound) { // Graph generation

        Random rand = new Random();             // random generator

        if (linksPercent > 100) linksPercent = 100;

        List<Pair<Integer, Integer> > list = new ArrayList<>();

        int edges = numberOfVertices * (numberOfVertices - 1) * linksPercent / 200; // 200 because graph isn't oriented

        for (int i = 0; i < numberOfVertices; ++i)
            for (int j = i; j < numberOfVertices; ++j)
                if (i != j) list.add(new Pair(i, j));

        Collections.shuffle(list);              // Random_shuffle

        boolean b = rand.nextBoolean();

        if(b) {
            for(int i = 0; i < edges; ++i) {
                linksMatrix[list.get(0).first][list.get(0).second] = true;
                list.remove(0);
            }
        } else {
            for(int i = 0; i < edges; ++i) {
                linksMatrix[list.get( list.size() - 1).first][list.get( list.size() - 1).second] = true;
                list.remove(list.size() - 1);
            }
        }

        int range = rightBound - leftBound;

        for (int i = 0; i < numberOfVertices; ++i)           // Random weights
            for (int j = 0; j < numberOfVertices; ++j)
                if (linksMatrix[i][j])
                    adjacencyMatrix[i][j] = rand.nextInt(range + 1) + leftBound;

        for (int i = 0; i < numberOfVertices; ++i)           // make matrix symmetric
            for (int j = i; j < numberOfVertices; ++j) {
                linksMatrix[j][i] = linksMatrix[i][j];
                adjacencyMatrix[j][i] = adjacencyMatrix[i][j];
            }

        this.makeWarshallAlgorithm();                        // WarshallAlgorithm
    }

    public boolean createGraphFromFile(Scanner input) {      // create graph from file
        int[] massOfDigits = new int[100];
        int numberOfDigits = 0;

        while (input.hasNextInt()) {
            massOfDigits[numberOfDigits++] = input.nextInt();
        }

        if (Math.sqrt(numberOfDigits) % 1 == 0) {
            int numberOfElements = (int)Math.sqrt(numberOfDigits);
            int[][] elements = new int[numberOfElements][numberOfElements];

            if (numberOfElements < 2)                        // too few elements
                return false;

            for (int i = 0; i < numberOfElements; i++)
                for (int j = 0; j < numberOfElements; j++)
                    elements[i][j] = massOfDigits[i * numberOfElements + j];

            for (int i = 0; i < numberOfElements; ++i)
                if (elements[i][i] != 0)                     // wrong elements on diagonal
                    return false;

            for (int i = 0; i < numberOfElements; ++i)
                for (int j = 0; j < numberOfElements; ++j)
                    if (elements[i][j] != elements[j][i])    // wrong elements
                        return false;


            // real creation of graph
            createGraph(numberOfElements);
            for (int i = 0; i < numberOfElements; i++)
                for (int j = 0; j < numberOfElements; j++) {
                    adjacencyMatrix[i][j] = massOfDigits[i * numberOfElements + j];
                    linksMatrix[i][j] = adjacencyMatrix[i][j] != 0;
                }
            this.makeWarshallAlgorithm();

            return true;
        }

        return false;
    }
}
