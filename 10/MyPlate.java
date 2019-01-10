

import java.util.Collections;
import java.util.List;

/**
 *
 * @author milosz
 */
public class MyPlate {

    /**
     * Calculates minimal cost of plate cutting
     *
     * @param xCostList list of costs in x direction
     * @param yCostList list of costs in y direction
     * @return minimal cost of plate cutting
     */
    public static double calculateMinCost(List<Float> xCostList,
            List<Float> yCostList) {

        double totalCost = 0.0;

        Collections.sort(xCostList, Collections.reverseOrder());
        Collections.sort(yCostList, Collections.reverseOrder());

        int xCuts = 1;
        int yCuts = 1;
        float maxX;
        float maxY;

        while (!xCostList.isEmpty() && !yCostList.isEmpty()) {
            maxX = xCostList.get(0);
            maxY = yCostList.get(0);
            if (maxX >= maxY) {
                totalCost += maxX * yCuts;
                xCostList.remove(0);
                ++xCuts;
            } else {
                totalCost += maxY * xCuts;
                yCostList.remove(0);
                ++yCuts;
            }
        }

        totalCost += sumCosts(xCostList) * yCuts;
        totalCost += sumCosts(yCostList) * xCuts;

        return totalCost;
    }

    /**
     * Sums all cutting costs in list
     *
     * @param costList list of cutting costs
     * @return sum of cutting costs in list
     */
    private static double sumCosts(List<Float> costList) {
        double cost = 0.0;
        while (!costList.isEmpty()) {
            cost += costList.get(0);
            costList.remove(0);
        }
        return cost;
    }
}
