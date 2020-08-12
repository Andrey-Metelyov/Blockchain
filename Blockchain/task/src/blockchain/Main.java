package blockchain;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Blockchain blockchain = Blockchain.getInstance();
        int poolSize = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        ExecutorService sender = Executors.newFixedThreadPool(poolSize);
        for (int i = 0; i < poolSize; i++) {
            executor.submit(new BlockGenerator());
        }
        sender.submit(new MessageSender());
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (blockchain.size() < 15);
//        System.out.println("----- SHUTDOWN!!! -----");
        try {
            executor.shutdownNow();
            sender.shutdownNow();
            sender.awaitTermination(100, TimeUnit.MILLISECONDS);
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        executor.shutdownNow();
//        System.out.println("----- SHUTDOWN DONE!!! -----");
    }
}
