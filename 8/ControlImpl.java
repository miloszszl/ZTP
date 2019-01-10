
import javax.ejb.Stateful;

/**
 *
 * @author milosz
 */
@Stateful
public class ControlImpl implements IControlRemote {

    private boolean active;
    private int counter;
    private int errorCounter;

    public ControlImpl() {
        active = false;
        counter = errorCounter = 0;
    }

    @Override
    public void start() {
        if (active) {
            errorCounter++;
        } else {
            active = true;
        }
    }

    @Override
    public void stop() {
        if (active) {
            active = false;
        } else {
            errorCounter++;
        }
    }

    @Override
    public void increment(int i) {
        if (active) {
            counter += i;
        } else {
            errorCounter++;
        }
    }

    @Override
    public int counter() {
        return counter;
    }

    @Override
    public int errors() {
        return errorCounter;
    }

}
