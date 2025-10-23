package multipleProcess;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * The  PlayerClient class represents a client process (player)
 * that connects to a server using a socket and communicates by sending
 * and receiving serialized ProcessMessage objects.
 * This class implements the Runnable interface, allowing
 * instances to run on separate threads. Each client listens for incoming
 * messages, prints them, and replies to the server up to a certain limit.
 * The client will send and receive messages up to a maximum of 10 exchanges.
 *
 * @author Zahra Gholinia
 */
public class PlayerClient implements Runnable {

    private static final int MAX_MESSAGES = 10;
    private static final int SERVER_PORT = 8082;
    private static final String SERVER_URL = "localhost";
    private final Socket socket;
    private final ObjectInputStream in;
    ObjectOutputStream out;
    private int messageCount = 0;

    public PlayerClient(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());

    }

    public static void main(String[] args) throws IOException {
        PlayerClient playerClient = new PlayerClient(SERVER_URL, SERVER_PORT);
        Thread thread = new Thread(playerClient);
        thread.start();
        playerClient.sendMessage(new ProcessMessage("this is a message number:", 0));
    }

    /**
     * Continuously listens for incoming messages and responds until
     * the message limit is reached (10 messages), then closes the connection.
     */
    @Override
    public void run() {
        while (messageCount < MAX_MESSAGES) {
            try {
                try {
                    receiveMessage();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } catch (IOException e) {
                try {
                    close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                throw new RuntimeException(e);
            }
        }
        try {
            close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void close() throws IOException {
        socket.close();
    }

    /**
     * Sends a {@link ProcessMessage} object to the server.
     * Each time this method is called, it increments the message's counter.
     *
     * @param message the message to send
     * @throws IOException if an I/O error occurs during writing
     */
    private void sendMessage(ProcessMessage message) throws IOException {
        if (messageCount < MAX_MESSAGES) {
            message.setCounter(message.getCounter() + 1);
            out.writeObject(message);
        }
    }

    /**
     * Receives a {@link ProcessMessage} object from the server.
     * If the message limit is not yet reached, it prints the message,
     * increments the local counter, and sends the message back.
     *
     * @throws IOException            if an I/O error occurs during reading
     * @throws ClassNotFoundException if the received object type is unknown
     */
    private void receiveMessage() throws IOException, ClassNotFoundException {
        ProcessMessage message = (ProcessMessage) in.readObject();
        if (message != null) {
            if (messageCount < MAX_MESSAGES) {
                System.out.println(message.getMessage() + message.getCounter());
                messageCount++;
                sendMessage(message);
            } else {
                close();
            }
        }
    }

}
