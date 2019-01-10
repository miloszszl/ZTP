
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;

/**
 *
 * @author milosz
 */
@Stateless
public class Graph implements IGraphRemote {

    private Map<Integer, List<Integer>> edges;

    /**
     * Adds edge to graph.
     *
     * @param u beginning of edge (vertex)
     * @param v end of edge (vertex)
     */
    @Override
    public void addEdge(int u, int v) {
        if (edges.get(u) == null) {
            edges.put(u, new ArrayList<Integer>());
        }
        edges.get(u).add(v);
    }

    /**
     * Initializes structure of edges inside graph
     */
    @Override
    public void initGraph() {
        if (edges == null) {
            edges = new HashMap<>();
        }
    }

    /**
     * Gives list of vertices that are at the end of the edges with beginning in
     * specified vertex
     *
     * @param vertex vertex that neighbors will be searched for
     * @return list of vertices that are neighbors for specified vertex
     */
    @Override
    public List<Integer> getVertexAdjacencyList(int vertex) {
        return edges.get(vertex);
    }

    /**
     * Returns map of edges
     *
     * @return map of edges
     */
    @Override
    public Map<Integer, List<Integer>> getEdges() {
        return edges;
    }
}
