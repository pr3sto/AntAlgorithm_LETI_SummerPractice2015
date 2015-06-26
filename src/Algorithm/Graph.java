package Algorithm;

import java.util.*;

public class Graph {
    public int num;                // Number of graph nodes
    public int[][] graph;          // Adjacency matrix with weights
    public boolean[][] links;      // Adjacency matrix with links only. We'll use Warshall algorithm to it.
                                    // There would be nodes list

    public Graph(int n){            // Constructor
        num = n;
        graph = new int[num][num];
        links = new boolean[num][num];
    }

    private void makeWarshallAlgorithm(){       // Warshall algorithm
        for (int k=0; k < num; ++k)
            for (int i=0; i < num; ++i)
                for (int j=0; j < num; ++j)
                    links[i][j] = links[i][j] || ( links[i][j] && links[i][j] );
    }

    public void generateGraph(int linksPercent, int leftBound, int rightBound){     // Graph generation
        Random rand = new Random();             // random generator

        /*for(int i = 0; i < num; ++i)
            for(int j = 0; j < num; ++j)
                a[i][j] = 0;*/

        if(linksPercent > 100)
            linksPercent = 100;

        List<Pair<Integer,Integer> > list = new ArrayList<>();

        int edges = num * (num - 1) * linksPercent / 200;           // 200 because graph isn't oriented

        for(int i = 0; i < num; ++i)
            for(int j = i; j < num; ++j)
                if(i != j)
                    list.add(new Pair(i, j));

        Collections.shuffle(list);              // Random_shuffle

        boolean b = rand.nextBoolean();

        if(b)
        {
            for(int i = 0; i < edges; ++i)
            {
                links[list.get(0).first][list.get(0).second] = true;
                list.remove(0);
            }
        }
        else
        {
            for(int i = 0; i < edges; ++i)
            {
                links[list.get( list.size() - 1).first][list.get( list.size() - 1).second] = true;
                list.remove(list.size() - 1);
            }
        }

        int range = rightBound - leftBound;

        for (int i = 0; i < num; ++i)           // Random weights
            for (int j = 0; j < num; ++j)
                if (links[i][j])
                    graph[i][j] = rand.nextInt(range) + leftBound;

        for (int i = 0; i < num; ++i)           // make matrix symmetric
            for (int j = i; j < num; ++j){
                links[j][i] = links[i][j];
                graph[j][i] = graph[i][j];
            }

        this.makeWarshallAlgorithm();           // WarshallAlgorithm
    }
}
