import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Player player = new Player();
        Player player2 = new Player();
        Thread thread = new Thread(player);
        Thread thread2 = new Thread(player2);

        thread.start();
        thread2.start();

        player.sendMessage("this is a message number: ", Arrays.asList(player2));

        thread.join();
        thread2.join();

    }
}