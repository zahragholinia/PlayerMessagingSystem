package multipleProcess;

import java.io.Serializable;
/**
 * The  ProcessMessage class represents a simple data transfer object (DTO)
 * used for communication between the  PlayerClient and PlayerServer.
 * @author Zahra Gholinia
 */
public class ProcessMessage implements Serializable {
    private int counter;
    private String message;

    public ProcessMessage(String message, int counter) {
        this.message = message;
        this.counter = counter;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
