/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.List;

/**
 *
 * @author milosz
 */
public class Knapsack {

    private long regionFreeArea;

    //represents whole region that will be filled with rectangles
    private final Main.Region baseRegion;

    /**
     * Class constructor
     *
     * @param n size of squared region
     */
    public Knapsack(long n) {
        this.regionFreeArea = n * n;
        this.baseRegion = new Main.Region(0, 0, n, n);
    }

    /**
     * Calculates not used part of region filled with rectangles. For each
     * rectangle finds best place to fit, places rectangle in it and then
     * divides free space into smaller free rectangles that will be fit in next
     * iteration. At the end calculates not used space.
     *
     * @param rectangles list of rectangles that will be placed inside whole
     * region
     * @return area not filled with rectangles
     */
    int pack(List<Main.Rectangle> rectangles) {
        for (int i = 0; i < rectangles.size(); i++) {
            Main.Region suitableRegion = findSuitableRegion(this.baseRegion,
                    rectangles.get(i));
            if (suitableRegion != null) {
                divideRegion(suitableRegion, rectangles.get(i));
                rectangles.get(i).setRegion(suitableRegion);
            }
        }

        for (int i = 0; i < rectangles.size(); i++) {
            if (rectangles.get(i).getRegion() != null) {
                regionFreeArea -= rectangles.get(i).getArea();
            }
        }

        return (int) regionFreeArea;
    }

    /**
     * Finds Suitable region for rectangle with certain size. This method is
     * recursive. Checks if region has some rectangle in it. If it does then it
     * checks for space on the right of this already placed rectangle. If there
     * is no space left then method checks below placed rectangle. Checking is
     * done by comparing size of free space to size of rectangle. Rectangle is
     * rotated if needed.
     *
     * @param region region to be searched for suitable "subregion"
     * @param rectangle region is searched for this rectangle size
     * @return region for specified rectangle with certain size. If there is no
     * more suitable region for rectangle then returns null
     */
    private Main.Region findSuitableRegion(Main.Region region,
            Main.Rectangle rectangle) {

        if (region.isRectanglePlaced() == true) {
            Main.Region suitableRegion;
            suitableRegion = findSuitableRegion(region.getRight(), rectangle);

            if (suitableRegion == null) {
                rectangle.rotate();
                suitableRegion = findSuitableRegion(region.getRight(),
                        rectangle);
                if (suitableRegion == null) {
                    rectangle.rotate();
                    suitableRegion = findSuitableRegion(region.getBottom(),
                            rectangle);
                    if (suitableRegion == null) {
                        rectangle.rotate();
                        suitableRegion = findSuitableRegion(region.getBottom(),
                                rectangle);
                    }
                }
            }
            return suitableRegion;
        } else {
            return checkFreeRegion(region,rectangle);
        }
    }

    private Main.Region checkFreeRegion(Main.Region region,
            Main.Rectangle rectangle) {
        if ((rectangle.getWidth() <= region.getWidth())
                && (rectangle.getHeight() <= region.getHeight())) {
            return region;
        }

        rectangle.rotate();
        if ((rectangle.getWidth() <= region.getWidth())
                && (rectangle.getHeight() <= region.getHeight())) {
            return region;
        }
        return null;
    }

    /**
     * Divides region to smaller regions on the right and bottom of current
     * region with rectangle in it
     *
     * @param region will be divided into smaller regions
     * @param rectangle rectangle that is placed inside region parameter
     */
    private void divideRegion(Main.Region region, Main.Rectangle rectangle) {

        region.setRight(new Main.Region(region.getTopLeftX()
                + rectangle.getWidth(), region.getTopLeftY(),
                region.getWidth() - rectangle.getWidth(),
                rectangle.getHeight()));
        region.setBottom(new Main.Region(region.getTopLeftX(),
                region.getTopLeftY() + rectangle.getHeight(), region.getWidth(),
                region.getHeight()
                - rectangle.getHeight()));
        region.setRectanglePlaced(true);
    }

}
