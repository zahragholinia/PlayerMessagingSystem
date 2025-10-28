import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

/**
 * NetworkPlayer extends Player to provide network communication capabilities.
 * Handles both server and client roles for message exchange between players.
 * Uses object serialization for message transmission over TCP sockets.
 *
 * @author Zahra GHolinia
 */
public class NetworkPlayer extends Player {
    private static final String host = "localhost";
    private static final int port = 8085;
    private static final int LINGER = 1000;
    private static final int TIMEOUT = 5000;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private volatile boolean streamsOpen = true;


    /**
     * Constructs a NetworkPlayer instance with network communication capabilities.
     *
     * @param isInitiator true if this player should act as initiator
     * @param latch       CountDownLatch for synchronization between players
     */
    public NetworkPlayer(boolean isInitiator, CountDownLatch latch) {
        super(isInitiator);
        try {
            setupConnection(isInitiator, latch);
        } catch (IOException | InterruptedException e) {
            closeConnections();
        }
    }

    private void setupConnection(boolean isInitiator, CountDownLatch latch)
            throws IOException, InterruptedException {
        if (isInitiator) {
            serverSocket = new ServerSocket(port);
            latch.countDown();
            clientSocket = serverSocket.accept();
        } else {
            latch.await();
            clientSocket = new Socket(host, port);
        }

        configureSocket();

        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new ObjectInputStream(clientSocket.getInputStream());
    }

    private void configureSocket() throws IOException {
        clientSocket.setKeepAlive(true);
        clientSocket.setSoLinger(true, LINGER);
        clientSocket.setSoTimeout(TIMEOUT);
    }

    /**
     * Main execution loop for network message handling.
     * Continuously receives and processes messages until shutdown(initiator receive and send max messages).
     */
    protected void run() {
        try {
            while (running.get()) {
                if (initiator && sentMessages.get() == MAX_MESSAGES && receivedMessages.get() == MAX_MESSAGES) {
                    sendStopMessage();
                    shutdown();
                    break;
                }
                Message message = (Message) in.readObject();
                receiveMessage(message);
            }
        } catch (Exception e) {
            shutdown();
        } finally {
            shutdown();
        }
    }


    /**
     * Sends a message over the network connection.
     *
     * @param message the message to be sent
     */
    public synchronized void sendMessage(Message message) {
        try {
            out.writeObject(message);
            out.flush();
            sentMessages.incrementAndGet();
        } catch (IOException e) {
            shutdown();
        }
    }


    /**
     * Sends a first message with NORMAL type and initial counter value.
     *
     * @param content the message content to be sent
     */
    @Override
    public void sendMessage(String content) {
        Message message = new Message(content, MessageType.NORMAL, START_COUNT);
        sendMessage(message);
    }

    /**
     * Processes incoming messages and handles STOP signals.
     *
     * @param message the received message
     */
    public synchronized void receiveMessage(Message message) {
        if (message.getMessageType() == MessageType.STOP) {
            shutdown();
        }
        System.out.println(message.getMessage() + message.getCounter());
        receivedMessages.incrementAndGet();
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
     * Initiates shutdown sequence and closes all network connections.
     */
    public synchronized void shutdown() {
        running.set(false);
        closeConnections();
    }

    private void closeConnections() {
        try {
            if (in != null && streamsOpen) {
                in.close();
                streamsOpen = false;
            }
            if (out != null && streamsOpen) {
                out.close();
                streamsOpen = false;
            }
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing connections: " + e.getMessage());
        }
    }

}