import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Abstract base class representing a player in the network and single process communication system.
 * Provides common functionality for message handling and synchronization.
 * Implements Serializable for network transmission support.
 *
 * @author Zahra GHolinia
 */
public abstract class Player implements Serializable {
    /**
     * Maximum number of messages that can be sent/received before automatic shutdown.
     */
    protected static final int MAX_MESSAGES = 10;
    /**
     * Flag indicating if this player acts as the initiator in the communication.
     */
    protected final boolean initiator;
    /**
     * Atomic flag tracking whether the player is currently running.
     */
    protected final AtomicBoolean running = new AtomicBoolean(true);
    /**
     * Counter for messages received by this player.
     */
    protected AtomicInteger receivedMessages = new AtomicInteger(0);
    /**
     * Counter for messages sent by this player.
     */
    protected AtomicInteger sentMessages = new AtomicInteger(0);
    /**
     * Starting value for message counters.
     */
    protected static final int START_COUNT = 1;

    /**
     * Constructs a new Player instance with the specified initiator status.
     *
     * @param initiator true if this player should act as the initiator
     */
    public Player(boolean initiator) {
        this.initiator = initiator;
    }

    /**
     * Sends first message with the specified content.
     *
     * @param content the message content to be sent
     */
    public abstract void sendMessage(String content);

    /**
     * Sends  message.
     *
     * @param message
     */
    public abstract void sendMessage(Message message);

    /**
     * Processes an incoming message.
     *
     * @param message the message to be processed
     */
    public abstract void receiveMessage(Message message);

    /**
     * terminate program and stop communication.
     */
    public abstract void shutdown();

    /**
     * Sends a stop message to signal termination.
     */
    public abstract void sendStopMessage();


}
