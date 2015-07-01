package Graph;

import java.util.*;

import Staff.Pair;

public class Graph {

    public static Character[] alphabet = {'A',       // алфавит для списков вершин
            'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};

    public int numberOfVertices;         // Number of graph nodes
    public int[][] adjacencyMatrix;      // Adjacency matrix with weights
    public boolean[][] linksMatrix;      // Adjacency matrix with links only. We'll use Warshall algorithm to it.

    public List<Edge> edges;             // Graph's edges
    public List<Vertex> vertices;        // Graph's vertices

    public Graph() {                     // Constructor
        numberOfVertices = 0;
        adjacencyMatrix = null;
        linksMatrix = null;
        edges = null;
        vertices = null;
    }

    public Graph(Graph g) {              // Copy constructor
        numberOfVertices = g.numberOfVertices;

        createMatrix(numberOfVertices);

        for (int i = 0; i < numberOfVertices; ++i)
            for (int j = 0; j < numberOfVertices; ++j) {
                adjacencyMatrix[i][j] = g.adjacencyMatrix[i][j];
                linksMatrix[i][j] = g.linksMatrix[i][j];
            }

        if (g.edges != null) edges = new ArrayList<>(g.edges);
        if (g.vertices != null) vertices = new ArrayList<>(g.vertices);
    }

    public boolean isNull() {
        for (int i = 0; i < numberOfVertices; ++i)
            for (int j = 0; j < numberOfVertices; ++j)
                if (linksMatrix[i][j])
                    return false;

        return true;    // no edges
    }

    public boolean isCreated() {         // is graph created
        return numberOfVertices != 0;
    }

    public void deleteGraph() {
        numberOfVertices = 0;
        adjacencyMatrix = null;
        linksMatrix = null;
        edges = null;
        vertices = null;
    }

    private void createMatrix(int n) {     // create graph
        numberOfVertices = n;
        adjacencyMatrix = new int[n][n];
        linksMatrix = new boolean[n][n];
    }

    private void createVerticesAndEdges() {
        // create vertices
        vertices = new ArrayList<>(numberOfVertices);

        int centerOfPanelX = 342;
        int centerOfPanelY = 330;
        int distanceToCenterOfVertex = 290;

        // координаты вершины
        int vertexX = centerOfPanelX;
        int vertexY = centerOfPanelY - distanceToCenterOfVertex;

        // дополнительные переменные
        double radNextX = vertexX - centerOfPanelX;
        double radNextY = vertexY - centerOfPanelY;
        double polarX, polarY;
        double tempX, tempY;

        for(int i = 0; i < numberOfVertices ; ++i) {
            vertices.add(new Vertex(vertexX - 25, vertexY - 25, alphabet[i]));

            polarX = 1 * Math.cos(Math.acos(-1.0) * 2 / numberOfVertices);
            polarY = 1 * Math.sin(Math.acos(-1.0) * 2 / numberOfVertices);

            tempX = radNextX * polarX - radNextY * polarY;
            tempY = radNextX * polarY + radNextY * polarX;

            radNextX = tempX;
            radNextY = tempY;

            vertexX = (int)(centerOfPanelX + radNextX);
            vertexY = (int)(centerOfPanelY + radNextY);
        }

        // create edges
        edges = new ArrayList<>();
        for (int i = 0; i < numberOfVertices; ++i)
            for (int j = i; j < numberOfVertices; ++j)
                if (adjacencyMatrix[i][j] != 0) {
                    edges.add(new Edge(adjacencyMatrix[i][j], i, j));
                }
    }

    private void makeWarshallAlgorithm() {       // Warshall algorithm
        for (int k = 0; k < numberOfVertices; ++k)
            for (int i = 0; i < numberOfVertices; ++i)
                for (int j = 0; j < numberOfVertices; ++j)
                    linksMatrix[i][j] = linksMatrix[i][j] || (linksMatrix[i][k] && linksMatrix[k][j]);

        for (int i = 0; i < numberOfVertices; ++i)
            linksMatrix[i][i] = false;
    }

    public void generateGraph(int numberOfVertices, int linksPercent,
                              int leftBound, int rightBound) { // Graph generation

        deleteGraph();

        createMatrix(numberOfVertices);

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

        makeWarshallAlgorithm();                        // WarshallAlgorithm

        createVerticesAndEdges();
    }

    public boolean createGraphFromFile(Scanner input) {      // create graph from file

        deleteGraph();

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
            createMatrix(numberOfElements);
            for (int i = 0; i < numberOfElements; i++)
                for (int j = 0; j < numberOfElements; j++) {
                    adjacencyMatrix[i][j] = massOfDigits[i * numberOfElements + j];
                    linksMatrix[i][j] = adjacencyMatrix[i][j] != 0;
                }

            makeWarshallAlgorithm();

            createVerticesAndEdges();

            return true; // graph created
        }

        return false;  // graph is not created
    }

    private boolean isNullRow(int[][] mm, int index){
        for(int i = 0; i < 10; ++i)
            if (mm[index][i] != 0)
                return false;

        for (int i = 0; i < vertices.size(); i++)
            if (vertices.get(i).letter == Graph.alphabet[index])
                return false;

        return true;
    }

    public void removePheromone() {
        for (Edge i : edges)
            i.pheromone = 0;
    }

    public void createMatrixFromEdgesAndVertices() {
        int[][] adjacencyMatrixTmp = new int[10][10];

        for (int i = 0; i < 10; ++i)
            for (int j = 0; j < 10; ++j)
                adjacencyMatrixTmp[i][j] = 0;

        for (Edge i : edges) {
            adjacencyMatrixTmp[i.firstNode][i.secondNode] = i.weight;
            adjacencyMatrixTmp[i.secondNode][i.firstNode] = i.weight;
        }

        adjacencyMatrix = new int[numberOfVertices][numberOfVertices];
        linksMatrix = new boolean[numberOfVertices][numberOfVertices];

        for (int i = 0; i < numberOfVertices; ++i)
            for (int j = 0; j < numberOfVertices; ++j) {
                adjacencyMatrix[i][j] = 0;
                linksMatrix[i][j] = false;
            }

        int[] mass = new int[numberOfVertices];

        int ind = 0;

        for (int i = 0; i < 10; ++i)
            if (!isNullRow(adjacencyMatrixTmp, i))
                    mass[ind++] = i;

        int indexI = 0, indexJ = 0;
        for (int i = 0; i < 10; i++) {
            if (isNullRow(adjacencyMatrixTmp, i))
                continue;

            for ( int j : mass) {
                adjacencyMatrix[indexI][indexJ] = adjacencyMatrixTmp[i][j];
                indexJ++;
            }
            indexJ = 0;
            indexI++;
        }

        for (int i = 0; i < numberOfVertices; ++i)
            for (int j = 0; j < numberOfVertices; ++j) {
                if (adjacencyMatrix[i][j] != 0)
                    linksMatrix[i][j] = true;
            }

        makeWarshallAlgorithm();

        // create edges
        edges = new ArrayList<>();
        for (int i = 0; i < numberOfVertices; ++i)
            for (int j = i; j < numberOfVertices; ++j)
                if (adjacencyMatrix[i][j] != 0) {
                    edges.add(new Edge(adjacencyMatrix[i][j], i, j));
                }
    }
}
