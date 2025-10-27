package edu.hsutx;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PathFinder {

    /***
     * Loads a CSV of integer values and places the values in an int[][] array
     * @param filePath
     * @return
     * @throws FileNotFoundException
     */
    public static int[][] loadArrayFromCSV(String filePath) throws FileNotFoundException {
        int[][] array = new int[300][300];
        Scanner scanner = new Scanner(new File(filePath));

        int row = 0;
        while (scanner.hasNextLine() && row < 300) {
            String[] line = scanner.nextLine().split(",");
            for (int col = 0; col < 300; col++) {
                array[col][row] = Integer.parseInt(line[col].trim());
            }
            row++;
        }
        scanner.close();
        return array;
    }

    public static void main(String[] args) {

        int[][] array;

        // Load CSV Data from our Configuration Space csv file
        try {
            array = loadArrayFromCSV("data/cspace.csv");
            // Now you can use the array in your Java program.
            System.out.println("Array loaded successfully.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            array = new int[300][300];
        }


        // Generate a starting image with white representing blocked space and black representing navigable space
        BufferedImage pathImage;
        pathImage = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 300; x++) {
            for (int y = 0; y < 300; y++) {
                if (array[x][y]==1) {
                    pathImage.setRGB(x, y, Color.WHITE.getRGB());
                }
                else {
                    pathImage.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }

        // Create our graph
        CSpaceGraph graph = new CSpaceGraph(array);
        

        // Get our Dijkstra Path from point 130, 20 to point 280, 50
        Point[] pointList = graph.getDijkstrasPath(new Point(130,20), new Point(280,50));
        for (Point point : pointList) {
            pathImage.setRGB(point.getX(), point.getY(), Color.YELLOW.getRGB());
        }

        // Generate our output file
        File outputfile = new File("data/pathfinder_image.jpg");
        try {
            ImageIO.write(pathImage, "jpg", outputfile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
