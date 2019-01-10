
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author milosz
 */
public class AppClient {

    @EJB
    private static IGraphRemote graph;

    @EJB
    private static ICycleRemote cycle;

    /**
     * Gives list of vertices. Vertices are extracted from String line
     *
     * @param str String that will be parsed for vertices
     * @return list of vertices
     */
    private static List<Integer> getVerticesFromString(String str) {
        String[] stringVertices = str.trim().split("\\s+");
        List<Integer> intVerticesList = new LinkedList<>();

        for (String s : stringVertices) {
            int vertex;

            try {
                vertex = Integer.parseInt(s);
                if (vertex > 0) {
                    intVerticesList.add(vertex);
                }
            } catch (NumberFormatException e) {}
        }
        return intVerticesList;
    }

    /**
     * Reads file line by line and extracts from each line vertices
     *
     * @param fileName path to file that will be read
     * @return list of vertices that are extracted from file. If file can not be
     * found then returns null
     */
    private static List<Integer> getVerticesFromFile(String fileName) {
        List<Integer> verticesList = new LinkedList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                verticesList.addAll(getVerticesFromString(line));
            }
        } catch (IOException e) {
            return null;
        }
        return verticesList;
    }

    /**
     * Initializes structure inside graph that will store edges and then adds
     * edges to this structure. If number of vertices is odd then last vertex is
     * not taken.
     *
     * @param verticesList list of vertices
     */
    private static void buildGraph(List<Integer> verticesList) {
        if(graph==null){
            graph = new Graph();
        }

        graph.initGraph();

        int length = verticesList.size();

        if (length % 2 != 0) {
            length--;
        }

        for (int i = 0; i < length; i += 2) {
            graph.addEdge(verticesList.get(i), verticesList.get(i + 1));
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if(args.length<=0)
            return; 
        
        String fileName = args[0];

        if (fileName == null) {
            return;
        }

        List<Integer> verticesList = getVerticesFromFile(fileName);
        if (verticesList != null) {
            buildGraph(verticesList);
            if(cycle==null){
                cycle = new Cycle();
            }
            int numOfCycles = cycle.getNumberOfCycles(graph);
            System.out.println("Ilość cykli : " + numOfCycles);
        }
    }

}
