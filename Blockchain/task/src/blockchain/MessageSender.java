package blockchain;

import java.util.Random;

public class MessageSender implements Runnable {
    MessageSender() {}

    @Override
    public void run() {
        Blockchain blockchain = Blockchain.getInstance();
        blockchain.sendMessage(generateMessage());
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String generateMessage() {
        String[] names = {"Tom", "Sarah", "Nick"};
        String[] messages = {
                "Hey, I'm first!",
                "It's not fair!",
                "You always will be first because it is your blockchain!",
                "Anyway, thank you for this amazing chat.",
                "You're welcome :)",
                "Hey Tom, nice chat"
        };
        Random random = new Random();
        return names[random.nextInt(names.length)] + ": " + messages[random.nextInt(messages.length)];
    }
}
