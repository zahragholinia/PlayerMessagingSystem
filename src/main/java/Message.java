import java.io.Serializable;

/**
 * Represents a message entity that can be transmitted between players.
 * Implements Serializable to enable message passing across process boundaries.
 *
 * @author Zahra GHolinia
 */
public class Message implements Serializable {
    private String message;
    private final MessageType messageType;
    private int counter;


    public Message(String message, MessageType messageType, int counter) {
        this.message = message;
        this.messageType = messageType;
        this.counter = counter;
    }


    /**
     * Creates a message just with type, typically used for stop messages and notify others to shut down.
     *
     * @param messageType
     */
    public Message(MessageType messageType) {
        this.messageType = messageType;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getMessage() {
        return message;
    }

    public int getCounter() {
        return counter;
    }

}
