package edu.hsutx;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Stack;

public class WeightedDirectedGraph {
    private List<List<Edge>> adjacencyList;
    private int numVertices;  

    /***
     *
     * @param vertexQuantity: Total number of vertices, as an int.  We will start counting at vertex 1, not 0.
     * @param edgeList: an List of Edges containing start and end vertex # and weight.
     ***/
    public WeightedDirectedGraph(int vertexQuantity, List<Edge> edgeList) {
        this.numVertices = vertexQuantity;
        adjacencyList = new ArrayList<>(vertexQuantity + 1);
        int i, j, k; 
        
        for (i=0; i<=vertexQuantity; i++) {
            adjacencyList.add(new ArrayList<Edge>());
        }

        for (j=0; j<edgeList.size(); j++) {
            Edge e = edgeList.get(j);
            int start = e.getStart();
            int end = e.getEnd();
            double weight = e.getWeight();

            adjacencyList.get(start).add(e);
        }
    }

    /***
     * returns true if vertex[start] has an edge to vertex[end], otherwise returns false
     * @param start
     * @param end
     */
    public boolean isAdjacent(int start, int end) {
        List<Edge> edgesFromStart = adjacencyList.get(start);
        int i;

        for (i=0; i<edgesFromStart.size(); i++) {
            Edge e = edgesFromStart.get(i);

            if (e.getEnd() == end) {
                return true;
            }
        } 
        return false;
    }

    /***
     * returns a 2d matrix of adjacency weights, with 0 values for non-adjacent vertices.
     * @return matrix of doubles representing adjacent edge weights
     */
    public double[][] adjacencyMatrix() {
        double [][] weightMatrix = new double[numVertices + 1][numVertices + 1];
        int i, j;
        double value = 0;
        for (i=1; i<adjacencyList.size(); i++) {
            List<Edge> edgesFromI = adjacencyList.get(i);

            for (j=0; j<edgesFromI.size(); j++) {
                Edge e = edgesFromI.get(j);
                value = e.getWeight();
                int end = e.getEnd();

                weightMatrix[i][end] = value;
            }
        }
        return weightMatrix;
    }

    /***
     * Conducts a Breadth First Search and returns the path from start to end, or null if not connected.
     * For accurate testing reproduction, add new vertices to the queue from smallest to largest.
     * @param start
     * @param end
     * @return an array of integers containing the path of vertices to be traveled, including start and end.
     */
    public int[] getBFSPath(int start, int end) {
        boolean [] visited = new boolean[numVertices + 1];
        int [] parent = new int[numVertices + 1];
        int i, j, k;

        for (i=0; i<=numVertices; i++) {
            parent[i] = -1;
        }

        Queue<Integer> queue = new LinkedList<>();
        queue.add(start);
        visited[start] = true;

        while (!queue.isEmpty()) {
            int current = queue.poll();
            List<Edge> edgesFromCurrent = adjacencyList.get(current);

            for (j=0; j<edgesFromCurrent.size(); j++) {
                Edge e = edgesFromCurrent.get(j);
                int neighbor = e.getEnd();

                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    parent[neighbor] = current;
                    queue.add(neighbor);

                    if (neighbor == end) {
                        queue.clear();
                        break;
                    }
                }
            }
        }
        if (!visited[end])  return null;

        List<Integer> pathList = new ArrayList<>();
        int current = end;

        while (current != -1) {
            pathList.add(0, current);
            current = parent[current];
        }

        int [] path = new int[pathList.size()];

        for (k=0; k<pathList.size();k++) {
            path[k] = pathList.get(k);
        }
        return path;
    }

    /***
     * Conducts a Depth First Search, and returns the path from start to end, or null if not connected.
     * Again, for accurate testing reproduction, add new vertices to the stack from smallest to largest.
     * @param start
     * @param end
     * @return an array of integers containing the path of vertices to be traveled, including start and end.
     */
    public int[] getDFSPath(int start, int end) {
      boolean [] visited = new boolean[numVertices + 1];
        int [] parent = new int[numVertices + 1];
        int i, j, k;

        for (i=0; i<=numVertices; i++) {
            parent[i] = -1;
        }

        Stack<Integer> stack = new Stack<>();
        stack.push(start);

        while (!stack.isEmpty()) {
            int current = stack.pop();

            if (!visited[current]) {
                visited[current] = true;

                if (current == end) break;
                
                List<Edge> edgesFromCurrent = adjacencyList.get(current);
                edgesFromCurrent.sort((a, b) -> Integer.compare(a.getEnd(), b.getEnd()));

                for (j=0; j<edgesFromCurrent.size(); j++) {
                    Edge e = edgesFromCurrent.get(j);
                    int neighbor = e.getEnd();

                    if (!visited[neighbor]) {
                        stack.push(neighbor);

                        if (parent[neighbor] == -1) parent[neighbor] = current;
                    }
                }
            }
        }
        if (!visited[end])  return null;

        List<Integer> pathList = new ArrayList<>();
        int current = end;

        while (current != -1) {
            pathList.add(0, current);
            current = parent[current];
        }

        int [] path = new int[pathList.size()];

        for (k=0; k<pathList.size();k++) {
            path[k] = pathList.get(k);
        }
        return path;
    }

    /***
     * Returns a list of vertices in order of traversal for the shortest path generated using Dykstra's Algorithm
     * @param start
     * @param end
     * @return
     */
    public int[] getDijkstrasPath(int start, int end) {
        double [] distance = new double[numVertices + 1];
        int [] parent = new int[numVertices + 1];
        boolean [] visited = new boolean[numVertices + 1];
        int i, j, k;

        for (i=0; i<=numVertices; i++) {
            distance[i] = Double.POSITIVE_INFINITY;
            parent[i] = -1;
        }
        distance[start] = 0;

        PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> Double.compare(distance[a], distance[b]));
        pq.add(start);

        while(!pq.isEmpty()) {
            int current = pq.poll();

            if (visited[current]) continue;
            visited[current] = true;

            List<Edge> edgesFromCurrent = adjacencyList.get(current);
            
            for (j=0; j<edgesFromCurrent.size(); j++) {
                Edge e = edgesFromCurrent.get(j);
                int neighbor = e.getEnd();
                double newDistance = distance[current] + e.getWeight();

                if (newDistance < distance[neighbor]) {
                    distance[neighbor] = newDistance;
                    parent[neighbor] = current;
                    pq.add(neighbor);
                }
            }
        }

        List<Integer> pathList = new ArrayList<>();
        int current = end;

        while (current != -1) {
            pathList.add(0, current);
            current = parent[current];
        }

        int [] path = new int [pathList.size()];
        for (k=0; k<pathList.size(); k++) {
            path[i] = pathList.get(i);
        }

        if (Double.isInfinite(distance[end])) return null;

        return path;
    }
}

