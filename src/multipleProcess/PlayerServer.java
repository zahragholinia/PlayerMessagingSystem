package multipleProcess;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The  PlayerServer class represents a simple server that accepts a
 * single client connection and exchanges ProcessMessage objects with it.
 * The server listens on a specified port, receives messages from the client,
 * prints them to the console, increments a message counter, and sends them back
 * to the client until a maximum of 10 messages are exchanged.
 * After the client connects, the server echoes messages back and forth until
 * the exchange limit is reached.
 *
 * @author Zahra Gholinia
 */
public class PlayerServer {
    private static final int MAX_MESSAGES = 10;
    private static final int SERVER_PORT = 8082;
    private final ServerSocket serverSocket;
    ObjectOutputStream out;
    Socket socket;
    private ObjectInputStream in;
    private int messageCount = 0;

    public PlayerServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        PlayerServer playerServer = new PlayerServer(SERVER_PORT);
        playerServer.start();
    }

    public void start() throws IOException, ClassNotFoundException {

        socket = serverSocket.accept();
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
        while (messageCount < MAX_MESSAGES) {
            receiveMessage();
        }
        socket.close();
        serverSocket.close();
    }


    /**
     * Sends a {@link ProcessMessage} to the connected client.
     * The method increments the message counter in the message
     * before sending it.
     *
     * @param message the message object to send
     * @throws IOException if an error occurs while writing to the output stream
     */
    private void sendMessage(ProcessMessage message) throws IOException {
        message.setCounter(message.getCounter() + 1);
        out.writeObject(message);
    }

    /**
     * Receives a {@link ProcessMessage} from the client,
     * prints it to the console, increments the message count,
     * and sends the updated message back to the client.
     *
     * @throws IOException            if an error occurs while reading from the input stream
     * @throws ClassNotFoundException if the received object type is unknown
     */
    private void receiveMessage() throws IOException, ClassNotFoundException {
        ProcessMessage message = (ProcessMessage) in.readObject();
        if (message != null) {
            System.out.println(message.getMessage() + message.getCounter());
            messageCount++;
            sendMessage(message);
        }
    }
}
