package edu.hsutx;

/***
 * Author: Kailey Pierce
 * 
 * LLM use: ChatGPT was used to help debug and troubleshoot the code, 
 * we found the error was incorrectly inputed x, y coordinates 
 */

import java.util.ArrayList;
import java.util.List;

public class CSpaceGraph extends WeightedDirectedGraph {
    private int[][] cspace;
    private int rows, columns;
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

        rows = 300;
        columns = 300;

        ArrayList<Edge> edges = new ArrayList<>();

        int [][] directions = { // N, NE, E, SE, S, SW, W, NW
            {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, 
            {1, 0}, {1, -1}, {0, -1}, {-1, -1}
        };

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                
                if (cspace[c][r] != 0) continue;

                int fromVertex = r * columns + c;

                for (int k = 0; k < directions.length; k++) {
                    int dr = directions[k][0];
                    int dc = directions[k][1];
                    int newRow = r + dr;
                    int newCol = c + dc;

                    boolean diagonal = (dr != 0 && dc != 0);

                    if (diagonal) {
                        if (cspace[c][r + dr] != 0 || cspace[c + dc][r] != 0) continue;
                    }  
                    else {
                        int midR = (r + newRow) / 2;
                        int midC = (c + newCol) / 2;
                        if (cspace[midC][midR] != 0) continue;
                    }
                    
                    int toVertex = newRow * columns + newCol;
                    double weight = diagonal ? Math.sqrt(2) : 1.0;
                    edges.add(new Edge(fromVertex, toVertex, weight));
                }
            }
        }

        super.numVertices = rows * columns;
        super.adjacencyList = new ArrayList<>(super.numVertices + 1);
        for (int i=0; i<=super.numVertices; i++) {
            super.adjacencyList.add(new ArrayList<Edge>());
        }

        for (Edge e : edges) {
            if (e.getStart() <= super.numVertices && e.getEnd() <= super.numVertices) {
                super.adjacencyList.get(e.getStart()).add(e);
            }
        }
    }

    /***
     * Wrapper Class for getDykstraPath using points for cspaces
     * @param start
     * @param end
     * @return
     */
    public Point[] getDijkstrasPath(Point start, Point end) {
        int startV = start.getY() * columns + start.getX();
        int endV = end.getY() * columns + end.getX();

        int[] path = super.getDijkstrasPath(startV, endV);

        if (path == null || path.length == 0) return new Point[0];

        Point[] pointList = new Point[path.length];
        for (int i=0; i<path.length; i++) {
            int v = path[i] - 1;
            int row = v / columns;
            int col = v % columns;
            pointList[i] = new Point(col, row);
        }

        return pointList;
    }
}