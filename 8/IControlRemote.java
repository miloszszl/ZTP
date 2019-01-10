
import javax.ejb.Remote;

/**
 *
 * @author milosz
 */
@Remote
public interface IControlRemote {

    /**
     * Changes state counting state
     */
    public void start();

    /**
     * Changes state to suspension state
     */
    public void stop();

    /**
     * If ejb is in counting state then increments counter by i, otherwise 
     * increments error counter by 1
     * 
     * @param i int value that counter will be incremented by
     */
    public void increment(int i);

    /**
     * Returns counter
     * @return counter value
     */
    public int counter();

    /**
     * Returns error counter
     * @return error counter value
     */
    public int errors();
}
