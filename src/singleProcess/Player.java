package singleProcess;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The singleProcess.Player class represents a player in the messaging system.
 * Each player can send and receive messages from other players.
 * The system ensures that each player can send a maximum of 10 messages
 * before the process terminates.
 * <p>
 * Responsibilities:
 * 1. Send messages to a list of other players.
 * 2. Receive messages from other players and process them.
 * 3. Maintain the message count and ensure the stop condition (10 messages) is met.
 * <p>
 * Author: Zahra Gholinia
 */
public class Player implements Runnable {
    /**
     * A blocking deque to store messages for each player.
     * Each player has its own queue to maintain thread safety , message order.
     * The `BlockingDeque` is used because it provides thread-safe operations for adding and removing messages
     * from both ends of the deque.This guarantees that no messages are lost and ensures proper synchronization between
     * threads.
     */
    private final BlockingDeque<Message> queue = new LinkedBlockingDeque<>();
    /**
     * An atomic counter that tracks the number of messages sent by the player.
     * Ensures thread-safe increments.
     */
    private final AtomicInteger messagesCount = new AtomicInteger(0);
    private static final int MAX_MESSAGES = 10;



    /**
     * Sends a message to a list of recipient players.
     * The message includes the player's message count to keep track of message exchanges.
     * The `put()` method is used to safely add messages to a player's queue in a thread-safe manner.
     * It ensures that the current thread is blocked if the queue is full, which avoids the risk of overflowing
     * @param message   The message content to send.
     * @param recipient A list of players to send the message to.
     * @throws InterruptedException If the thread is interrupted while waiting to put the message in the queue.
     */
    public void sendMessage(String message, List<Player> recipient) throws InterruptedException {
        for (Player p : recipient) {
            Message message1 = new Message(message + (messagesCount.addAndGet(1)), this);
            p.queue.put(message1);
        }

    }


    /**
     * Receives a message from another player and processes it.
     * Increments the message count and sends the received message to the sender.
     *
     * @param message The received message.
     * @throws InterruptedException If the thread is interrupted while waiting to process the message.
     */
    public synchronized void receiveMessage(Message message) throws InterruptedException {
        messagesCount.incrementAndGet();
        System.out.println(message.getMessage());
        sendMessage(message.getMessage(), Collections.singletonList(message.getFromPlayer()));

    }


    /**
     * The main execution thread for the player. Continuously processes received messages
     * until the player reaches the maximum message count.
     * The `take()` method is used to safely retrieve and remove messages from a player's queue in a thread-safe
     * manner. It blocks the calling thread if the queue is empty, ensuring that the player waits for a message
     * to be available before processing it. This behavior ensures that players do not waste CPU resources by
     * repeatedly checking for messages in a busy-wait loop. It also guarantees that players process messages
     * in the order they are added.
     */

    @Override
    public void run() {
        while (messagesCount.get() < MAX_MESSAGES) {
            try {
                Message message = queue.take();
                receiveMessage(message);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
