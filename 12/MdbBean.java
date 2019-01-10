
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author milosz
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(
            propertyName = "destinationLookup",
            propertyValue = "jms/MyQueue")
    ,
    @ActivationConfigProperty(
            propertyName = "destinationType",
            propertyValue = "javax.jms.Queue")
    ,
    @ActivationConfigProperty(propertyName = "acknowledgeMode",
            propertyValue = "Auto-acknowledge")})
public class MdbBean implements MessageListener {

    /**
     * Represents states of counter
     */
    enum State {
        COUNTING,
        STOPPED
    }

    private static final String ALBUM = "114014";
    private static final String CON_FACTORY = "jms/MyConnectionFactory";
    private State state;
    private int errorCounter;
    private double counter;

    /**
     * Constructor, initializes state, counter and error counter
     */
    public MdbBean() {
        state = State.STOPPED;
        counter = 0.0;
        errorCounter = 0;
    }

    /**
     * Changes state of counter from STOPPED to COUNTING. If previous state was
     * not STOPPED then errorCounter is incremented
     */
    private void start() {
        if (state == State.COUNTING) {
            errorCounter++;
        } else {
            state = State.COUNTING;
        }
    }

    /**
     * Changes state of counter from COUNTING to STOPPED. If previous state was
     * not COUNTING then errorCounter is incremented
     */
    private void stop() {
        if (state == State.COUNTING) {
            state = State.STOPPED;
        } else {
            errorCounter++;
        }
    }

    /**
     * Increments counter by given integer value
     *
     * @param i counter is incremented by this value
     */
    private void increment(double i) {
        if (state == State.COUNTING) {
            counter += i;
        } else {
            errorCounter++;
        }
    }

    /**
     * In given string removes all characters that are not a digit, "-", "." and
     * transforms result to double number
     *
     * @param str String to convert
     * @return integer number extracted from string (positive, 0, negative)
     */
    private double getDoubleFromString(String str) {
        return Double.parseDouble(str.replaceAll("[^\\d-.]", ""));
    }

    /**
     * Sends message to Topic
     *
     * @param message String message to send
     */
    private void sendMsg(String message) {
        try {
            Context ctx = new InitialContext();
            TopicConnectionFactory factory
                    = (TopicConnectionFactory) ctx.lookup(CON_FACTORY);
            try (TopicConnection topicConnection
                    = factory.createTopicConnection();
                    TopicSession topicSession
                    = topicConnection.createTopicSession(
                            false, Session.AUTO_ACKNOWLEDGE)) {

                Topic topic = (Topic) ctx.lookup("jms/MyTopic");
                TopicPublisher publisher = topicSession.createPublisher(topic);

                topicConnection.start();

                message = ALBUM + "/" + message;
                TextMessage textMessage = topicSession.createTextMessage();
                textMessage.setText(message);

                publisher.send(textMessage);
            }
        } catch (NamingException | JMSException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Handle message. Performs suitable actions for certain message content
     *
     * @param message Received message
     */
    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                String msg = ((TextMessage) message).getText();
                if (msg.equals("start")) {
                    start();
                } else if (msg.equals("stop")) {
                    stop();
                } else if (msg.equals("counter")) {
                    sendMsg(counter + "");
                } else if (msg.equals("error")) {
                    sendMsg(errorCounter + "");
                } else if (msg.equals("increment")) {
                    increment(1.0);
                } else if (msg.matches("^increment/-?[0-9]+(\\.[0-9]+)?$")) {
                    increment(getDoubleFromString(msg));
                } else {
                    ++errorCounter;
                }
            } catch (JMSException ex) {
                System.out.println(ex);
            }
        }
    }
}
