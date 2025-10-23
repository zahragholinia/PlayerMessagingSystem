package singleProcess;

/**
 * The singleProcess.Message class represents a message sent from one player to another.
 * It contains the message content and the player who sent the message.
 * Responsibilities:
 * 1. Hold the message content.
 * 2. Track the sender of the message.
 * This class is used to pass messages between players in the system.
 * Author: Zahra Gholinia
 */
public class Message {
    private Player fromPlayer;
    private String message;

    public Message( String message,Player fomPlayer) {
        this.message = message;
        this.fromPlayer = fomPlayer;
    }

    public Player getFromPlayer() {
        return fromPlayer;
    }


    public String getMessage() {
        return message;
    }

}
