
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author milosz
 */
@Remote
public interface IGraphRemote {

    /**
     * Adds edge to graph.
     *
     * @param u beginning of edge (vertex)
     * @param v end of edge (vertex)
     */
    public void addEdge(int u, int v);

    /**
     * Gives list of vertices that are at the end of the edges with beginning in
     * specified vertex
     *
     * @param vertex vertex that neighbors will be searched for
     * @return list of vertices that are neighbors for specified vertex
     */
    public List<Integer> getVertexAdjacencyList(int vertex);

    /**
     * Initializes structure of edges inside graph
     */
    public void initGraph();

    /**
     * Returns map of edges
     *
     * @return map of edges
     */
    public Map<Integer, List<Integer>> getEdges();
}
