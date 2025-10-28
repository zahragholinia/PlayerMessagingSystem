import java.util.concurrent.CountDownLatch;
/**
 * Main class for demonstrating network communication between two players.
 * Uses CountDownLatch for thread synchronization during connection setup.
 *
 * @author Zahra GHolinia
 */
public class NetworkPlayerMain {
    public static void main(String[] args) {
        try {
            CountDownLatch latch = new CountDownLatch(1);
            Thread player1Thread = new Thread(() -> {
                NetworkPlayer player1;
                try {
                    player1 = new NetworkPlayer(true, latch);
                    player1.run();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            player1Thread.start();

            NetworkPlayer player2 = new NetworkPlayer(false, latch);
            player2.sendMessage("this is a message number: ");
            player2.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
