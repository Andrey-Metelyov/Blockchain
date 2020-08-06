package blockchain;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.*;

public class Main {
    public static void main(String[] args) {
        Blockchain blockchain = Blockchain.getInstance();
        int poolSize = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        do {
            for (int i = 0; i < poolSize; i++) {
//            System.out.println("get block");
//            executor.submit(new BlockGenerator(parameters.id, parameters.prevHash, parameters.zeroesNeeded));
                executor.submit(new BlockGenerator());
//            executor.shutdown();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (blockchain.size() < 5);
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        executor.shutdown();
//        System.out.println("wait");
    }
}
