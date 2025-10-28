/**
 * Enumerates the possible types of messages that can be exchanged between processes.
 * Each message type defines specific handling behavior in the messaging system.
 *
 * @author Zahra GHolinia
 */
public enum MessageType {
    /**
     * Standard message type for regular communication.
     */
    NORMAL,

    /**
     * Special message type indicating termination request.
     * Stop messages signal that processing should cease.
     */
    STOP
}
