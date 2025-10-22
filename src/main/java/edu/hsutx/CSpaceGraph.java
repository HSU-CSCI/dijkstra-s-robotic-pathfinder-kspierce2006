package edu.hsutx;

import java.util.ArrayList;
import java.util.List;
import java.util.Hashmap;

public class CSpaceGraph extends WeightedDirectedGraph {

    private int[][] cspace;
    private Map<Point, Integer> pointToVertex = new HashMap<>();
    private Map<Integer, Point> vertexToPoint = new HashMap<>();

    /***
     * Convert a cspace to weighted, directed graph
     * a cspace is a 2d matrix representing a robot's ability to navigate through a location in physical space.
     * cspace[x][y] represents location x,y, with coordinate 0,0 at the upper left.
     * The value is set to 1 if the space is blocked (the robot cannot safely pass) and 0 if it is not blocked
     * The graph should be created so that each empty (0) spot is converted to a vertex, with edges to each adjacent unblocked spot
     * For edges moving straight left, right, up, down, the weight of the edge should be set to 1.
     * For diagonal edges, the weight of the edge should be set to the square root of 2
     * You will want to add the x,y coordinates to your vertex data
     * @param cspace
     */
    public CSpaceGraph(int[][] cspace) {
        super(1, new ArrayList<Edge>());
        this.cspace = cspace;

        int rows = cspace.length;
        int columns = cspace[0].length;

        ArrayList<Edge> edges = new ArrayList<>();

        int count = 1;

        for (int i=0; i<rows; i++) {
            for (int j=0; j<columns; j++) {
                if (cspace[i][j] == 0) {
                    Point p = new Point(i, j);
                    pointToVertex.put(p, count);
                    vertexToPoint.put(count, p);
                    count++;
                }
            }
        }

        int [][] directions = { // N, NE, E, SE, S, SW, W, NW
            {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}
        }

        for (int i=0; i<rows; i++) {
            for (int j=0; j<columns; j++) {
                if (cspace[i][j] == 0) {
                    Point current = new Point(i, j);
                    int fromVertex = pointToVertex.get(current);

                    for (int k=0; k<directions.length; k++) {
                        int x = i + directions[k][0];
                        int y = i + directions[k][1];

                        if (x >= 0 && x < rows && y >= 0 && y < columns) {
                            if (cspace[x][y] == 0) {
                                Point neighbor = new Point(x, y);
                                int toVertex = pointToVertex.get(neighbor);

                                int dx = directions[k][0];
                                int dy = directions[k][1];
                                double weight;

                                if (dx!= 0 && dy != 0) {
                                    weight = Math.sqrt(2);
                                }
                                else {
                                    weight = 1.0;
                                }

                                edges.add(new Edge(fromVertex, toVertex, weight));
                            }
                        }
                    }
                }
            }
        }

        super.numVertices = count - 1;
        super. adjacencyList = new ArrayList<>(count + 1);

        for (int i=0; i<count; i++) {
            super.adjacencyList.add(new ArrayList<Edge>());
        }

        for (int i=0 i<edges.size(); i++) {
            Edge e = edges.get(i);
            super.adjacencyList.get(e.getStart()).add(e);
        }
    }

    /***
     * Wrapper Class for getDykstraPath using points for cspaces
     * @param start
     * @param end
     * @return
     */
    public Point[] getDijkstrasPath(Point start, Point end) {
        Integer start = pointToVertex.get(start);
        Integer end = pointToVertex.get(end);

        if (start == null || end == null) return new Point[0];

        int[] path = super.getDijkstrasPath(start, end);

        if (path == null) return new Point[0];

        Point[] pointList = new Point[path.length];
        for (int i=0; i<path.length; i++) {
            pointList[i] = vertexToPoint.get(path[i]);
        }

        return pointList;
    }
}
