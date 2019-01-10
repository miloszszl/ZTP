
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;

/**
 *
 * @author milosz
 */
@Stateless
public class Cycle implements ICycleRemote {

    /**
     * Recursive method. Finds cycle inside graph.
     *
     * @param g graph that will be searched
     * @param v start vertex. From this vertex algorithm starts searching
     * @param w current vertex. This vertex is actual vertex that is searched
     * @param visitedVertices boolean array of currently visited vertices
     * @return if cycle is found then true. Otherwise false.
     */
    private boolean findCycleDFS(IGraphRemote g, int v, int w,
            List<Integer> cycle, Map<Integer, Boolean> visitedVertices,
            List<List<Integer>> discoveredCycles) {

        int u;
        visitedVertices.put(w, Boolean.TRUE);
        cycle.add(w);
        List<Integer> verticesList = g.getVertexAdjacencyList(w);
        if (verticesList != null) {
            for (Integer vertex : verticesList) {
                u = vertex;

                if ((u == v) || (!visitedVertices.get(u)
                        && findCycleDFS(g, v, u, cycle,
                                new HashMap<>(visitedVertices),
                                discoveredCycles))) {

                    if (!checkIfCycleIsInList(discoveredCycles, cycle)) {
                        return true;
                    }
                }
            }
        }

        cycle.remove(cycle.size() - 1);
        return false;
    }

    /**
     * Checks if two cycles are equal
     *
     * @param cycle1 first list of vertices for comparison
     * @param cycle2 second list of vertices for comparison
     * @return if cycles are same returns true, otherwise false
     */
    private boolean checkIfCyclesAreEqual(List<Integer> cycle1,
            List<Integer> cycle2) {

        if (cycle1.size() != cycle2.size()) {
            return false;
        }

        int commonVertexIndexInCycle2 = findCommonVertexIndex(cycle1, cycle2);

        if (commonVertexIndexInCycle2 < 0) {
            return false;
        }

        int cyclesSize = cycle1.size();
        int endIndex = commonVertexIndexInCycle2;
        int i = 0;

        do {
            if (cycle1.get(i).intValue()
                    != cycle2.get(commonVertexIndexInCycle2)) {
                return false;
            }
            ++commonVertexIndexInCycle2;
            commonVertexIndexInCycle2 %= cyclesSize;
            i++;
        } while (commonVertexIndexInCycle2 != endIndex);

        return true;
    }

    /**
     * Finds common vertex for two lists
     *
     * @param list1 list of vertices
     * @param list2 list of vertices
     * @return common vertex number
     */
    private int findCommonVertexIndex(List<Integer> list1, List<Integer> list2) {
        int commonVertex = list1.get(0);
        int commonVertexIndexInCycle2 = -1;
        for (int i = 0; i < list2.size(); i++) {
            if (list2.get(i) == commonVertex) {
                commonVertexIndexInCycle2 = i;
            }
        }

        return commonVertexIndexInCycle2;
    }

    /**
     * Checks if cycle(sequence of vertices) is in list of cycles
     *
     * @param cyclesList list of cycles for comparison
     * @param cycle sequence of vertices for comparison
     * @return true if cycle is inside cyclesList, otherwise false
     */
    private boolean checkIfCycleIsInList(List<List<Integer>> cyclesList,
            List<Integer> cycle) {

        for (List<Integer> c : cyclesList) {
            if (checkIfCyclesAreEqual(c, cycle)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Finds cycles inside graph and returns number of their occurrences
     *
     * @param g graph that will be searched
     * @return number of cycles inside given graph
     */
    @Override
    public int getNumberOfCycles(IGraphRemote g) {
        Map<Integer, List<Integer>> edges = g.getEdges();
        Map<Integer, Boolean> visitedVertices = new HashMap<>();
        List<List<Integer>> discoveredCycles = new LinkedList<>();
        List<Integer> cycle = new LinkedList<>();

        for (Map.Entry<Integer, List<Integer>> entry : edges.entrySet()) {
            for (Map.Entry<Integer, List<Integer>> e : edges.entrySet()) {
                visitedVertices.put(e.getKey(), Boolean.FALSE);
                for (Integer i : e.getValue()) {
                    visitedVertices.put(i, Boolean.FALSE);
                }
            }

            if (findCycleDFS(g, entry.getKey(), entry.getKey(), cycle,
                    visitedVertices, discoveredCycles)) {
                discoveredCycles.add(new ArrayList<>(cycle));
                cycle.clear();
            }
        }

        return discoveredCycles.size();
    }
}
