import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Implementation of Player for single-process communication.
 * Uses a thread-safe BlockingQueue for message handling, providing:
 * - Atomic operations for thread safety
 * - Automatic blocking for empty/full queue conditions
 * - Efficient producer-consumer pattern implementation
 * - Built-in synchronization for concurrent access
 * - Memory-safe null value handling
 *
 * @author Zahra GHolinia
 */
public class SingleProcessPlayer extends Player implements Runnable, Serializable {
    /**
     * Thread-safe queue for storing incoming messages.
     * Provides atomic operations and automatic blocking for thread synchronization.
     */
    private final BlockingQueue<Message> queue = new LinkedBlockingQueue<>();
    /**
     * Reference to the recipient player for message forwarding.
     */
    private SingleProcessPlayer recipient;

    /**
     * Sets the recipient player for message forwarding.
     *
     * @param recipient the player that will receive messages
     */
    public void setRecipient(SingleProcessPlayer recipient) {
        this.recipient = recipient;
    }

    public SingleProcessPlayer(boolean initiator) {
        super(initiator);
    }


    /**
     * Sends a message to the recipient player using thread-safe queue operations.
     *
     * @param message the message to be sent
     */
    public void sendMessage(Message message) {
        if (recipient == null)
            shutdown();
        try {
            // BlockingQueue automatically handles thread synchronization
            recipient.queue.put(message);
            sentMessages.incrementAndGet();
        } catch (InterruptedException e) {
            shutdown();
        }
    }

    /**
     * Sends first message with NORMAL type and initial counter value.
     *
     * @param content the message content to be sent
     */
    public void sendMessage(String content) {
        Message message = new Message(content, MessageType.NORMAL, START_COUNT);
        sendMessage(message);
    }

    /**
     * Processes an incoming message and handles message forwarding.
     *
     * @param message the message to be processed
     */
    public synchronized void receiveMessage(Message message) {
        System.out.println(message.getMessage() + message.getCounter());
        if (!running.get())
            return;
        receivedMessages.incrementAndGet();

        if (initiator && sentMessages.get() >= MAX_MESSAGES && receivedMessages.get() >= MAX_MESSAGES) {
            sendStopMessage();
            shutdown();
        }
        sendMessage(new Message(message.getMessage(), MessageType.NORMAL, message.getCounter() + 1));

    }

    /**
     * Sends a STOP message when initiator receive and send max messages to signal termination.
     */
    public void sendStopMessage() {
        Message stopMessage = new Message(MessageType.STOP);
        sendMessage(stopMessage);
    }

    /**
     * Initiates shutdown sequence signal to thread.
     */
    public void shutdown() {
        running.set(false);
        Thread.currentThread().interrupt();
    }

    /**
     * Main execution loop for message processing.
     * Uses BlockingQueue's take() method for thread-safe message retrieval.
     */
    @Override
    public void run() {
        while (running.get()) {
            try {
                Message message = queue.take();
                if (message.getMessageType() == MessageType.STOP) {
                    running.set(false);
                    break;
                }
                receiveMessage(message);
            } catch (InterruptedException e) {
                shutdown();
            }
        }
    }
}
