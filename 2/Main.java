/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that contains main method
 *
 * @author milosz
 */
public class Main {

    /**
     * Instances of Rectangle class will be placed inside tile/region
     *
     * @author milosz
     */
    public static class Rectangle {

        private long width;
        private long height;
        private long area;
        private Region region;

        /**
         * Class constructor for rectangle
         * 
         * @param width
         * @param height 
         */
        public Rectangle(long width, long height) {
            this.width = width;
            this.height = height;
            this.area = width * height;
        }

        /**
         * 
         * @return rectangle width
         */
        public long getWidth() {
            return width;
        }

        /**
         * 
         * @param width rectangle width to be set
         */
        public void setWidth(long width) {
            this.width = width;
        }

        /**
         * 
         * @return rectangle height
         */
        public long getHeight() {
            return height;
        }

        /**
         * 
         * @param height rectangle height to be set
         */
        public void setHeight(long height) {
            this.height = height;
        }

        /**
         * 
         * @return area of rectangle
         */
        public long getArea() {
            return area;
        }

        /**
         * 
         * @param area area of rectangle to be set
         */
        public void setArea(long area) {
            this.area = area;
        }

        /**
         * 
         * @return region that contains current rectangle
         */
        public Region getRegion() {
            return region;
        }

        /**
         * 
         * @param region region to be set that contains current rectangle 
         */
        public void setRegion(Region region) {
            this.region = region;
        }

        /**
         * Method for rotating current rectangle
         */
        public void rotate() {
            long tmp = this.width;
            this.width = height;
            this.height = tmp;
        }

    }

    static class SortByAreaDescending implements Comparator<Rectangle> {

        /**
         *
         * @param a rectangle for comparison
         * @param b rectangle for comparison
         * @return number that represents which argument is smaller. if result>0
         * then b>a if result == 0 then a==b else a>b
         */
        @Override
        public int compare(Rectangle a, Rectangle b) {
            return (int) (b.getArea() - a.getArea());
        }
    }

    /**
     * Region class represents tile that will be filled with rectangles and
     * divided into smaller tiles
     *
     * @author milosz
     */
    public static class Region {

        private long topLeftX;
        private long topLeftY;
        private long width;
        private long height;
        private boolean rectanglePlaced;
        private Region bottom;
        private Region right;

        /**
         * Constructor for region class
         *
         * @param topLeftX X coordinate of left top corner of region
         * @param topLeftY Y coordinate of left top corner of region
         * @param width width of region
         * @param height height of region
         */
        public Region(long topLeftX, long topLeftY, long width, long height) {
            this.topLeftX = topLeftX;
            this.topLeftY = topLeftY;
            this.width = width;
            this.height = height;
        }

        /**
         *
         * @return top left region X coordinate
         */
        public long getTopLeftX() {
            return topLeftX;
        }

        /**
         *
         * @param topLeftX top left region X coordinate to set
         */
        public void setTopLeftX(long topLeftX) {
            this.topLeftX = topLeftX;
        }

        /**
         *
         * @return top left region Y coordinate
         */
        public long getTopLeftY() {
            return topLeftY;
        }

        /**
         *
         * @param topLeftY top left region Y coordinate to set
         */
        public void setTopLeftY(long topLeftY) {
            this.topLeftY = topLeftY;
        }

        /**
         *
         * @return width of region
         */
        public long getWidth() {
            return width;
        }

        /**
         *
         * @param width width of region to set
         */
        public void setWidth(long width) {
            this.width = width;
        }

        /**
         *
         * @return height of region
         */
        public long getHeight() {
            return height;
        }

        /**
         *
         * @param height height of region to set
         */
        public void setHeight(long height) {
            this.height = height;
        }

        /**
         *
         * @return true if region contains rectangle in it, else false
         */
        public boolean isRectanglePlaced() {
            return rectanglePlaced;
        }

        /**
         *
         * @param rectanglePlaced set it to true if rectangle is inside current
         * region else false
         */
        public void setRectanglePlaced(boolean rectanglePlaced) {
            this.rectanglePlaced = rectanglePlaced;
        }

        /**
         * 
         * @return region below current region
         */
        public Region getBottom() {
            return bottom;
        }

        /**
         * 
         * @param bottom region to be set below current region 
         */
        public void setBottom(Region bottom) {
            this.bottom = bottom;
        }

        /**
         * 
         * @return region to the right of the current region
         */
        public Region getRight() {
            return right;
        }

        /**
         * 
         * @param right region to the right of current region
         */
        public void setRight(Region right) {
            this.right = right;
        }

    }

    /**
     * Creates list of rectangles from list of side sizes
     *
     * @param sizes list of square sides sizes. Each pair represents one
     * rectangle
     * @return list of rectangles with width and height specified by argument
     * 'sizes'
     */
    static List<Rectangle> buildRectangles(List<Long> sizes) {

        if (sizes.size() % 2 != 0) {  //odd number of sizes
            sizes.remove(sizes.size() - 1);
        }

        List<Rectangle> rectangles = new ArrayList<>();
        for (int i = 0; i < sizes.size(); i += 2) {
            rectangles.add(new Rectangle(sizes.get(i), sizes.get(i + 1)));
        }

        return rectangles;
    }

    /**
     * Sorts List of rectangles descending by area
     *
     * @param rectangles list of rectangles to sort descending by area
     */
    static void sortRectanglesDescending(List<Rectangle> rectangles) {

        Collections.sort(rectangles, new SortByAreaDescending());
    }

    /**
     * Reads data from file and extracts from it number values
     *
     * @param path path to the file that will be read
     * @return list of long numbers in same order as in the input file
     */
    public static List<Long> getSizesFromFile(String path) {

        List<Long> sizes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] stringArray = line.split("[^0-9]+");

                for (String s : stringArray) {
                    if (s.isEmpty()) {
                        continue;
                    }

                    sizes.add(Long.parseLong(s));
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sizes;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String inputFile = args[0];
        long squareSize = Long.parseLong(args[1]);
        List<Long> sizes = getSizesFromFile(inputFile);

        List<Rectangle> rectangles = buildRectangles(sizes);
        sortRectanglesDescending(rectangles);

        Knapsack knapsack = new Knapsack(squareSize);
        int notUsedArea = knapsack.pack(rectangles);

        System.out.println("Powierzchnia odpadu : " + notUsedArea);
    }
}
