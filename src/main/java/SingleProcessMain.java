/**
 * Main class for demonstrating single-process communication between two players.
 * Uses threading for concurrent message handling within the same process.
 *
 * @author Zahra GHolinia
 */
public class SingleProcessMain {
    public static void main(String[] args) {
        SingleProcessPlayer singleProcessPlayer = new SingleProcessPlayer(true);
        SingleProcessPlayer singleProcessPlayer2 = new SingleProcessPlayer(false);
        singleProcessPlayer.setRecipient(singleProcessPlayer2);
        singleProcessPlayer2.setRecipient(singleProcessPlayer);
        Thread thread = new Thread(singleProcessPlayer);
        Thread thread2 = new Thread(singleProcessPlayer2);

        thread.start();
        thread2.start();

        singleProcessPlayer.sendMessage("this is a message number: ");

        try {
            thread.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}