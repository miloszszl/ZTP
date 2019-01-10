
import javax.ejb.Remote;

/**
 *
 * @author milosz
 */
@Remote
public interface ICycleRemote {
    
    /**
     * Finds cycles inside graph and returns number of their occurrences
     * @param g graph that will be searched
     * @return number of cycles inside given graph
     */
    public int getNumberOfCycles(IGraphRemote g);
}
