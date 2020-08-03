package blockchain;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Blockchain blockchain = Blockchain.getInstance();
        int poolSize = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        for (int i = 0; i < 5; i++) {
//            System.out.println("get block");
            Blockchain.BlockParameters parameters = blockchain.getNextBlockParameters();
            executor.submit(new BlockGenerator(parameters.id, parameters.prevHash, parameters.zeroesNeeded));
//            executor.shutdown();
            try {
                executor.awaitTermination(90, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        executor.shutdown();
//        System.out.println("wait");
    }
}
