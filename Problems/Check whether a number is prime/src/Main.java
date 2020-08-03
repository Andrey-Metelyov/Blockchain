import java.util.Scanner;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExecutorService executor; // assign an object to it
        int poolSize = Runtime.getRuntime().availableProcessors();
        executor = Executors.newFixedThreadPool(poolSize);
        while (scanner.hasNext()) {
            int number = scanner.nextInt();
            // create and submit tasks
            executor.submit(new PrintIfPrimeTask(number));
        }
        executor.shutdown();
    }
}

class PrintIfPrimeTask implements Runnable {
    private final int number;

    public PrintIfPrimeTask(int number) {
        this.number = number;
    }

    @Override
    public void run() {
        // write code of task here
        if (number <= 1) {
            return;
        }
        for (int i = 2; i < number; i++) {
            if (number % i == 0) {
                return;
            }
        }
        System.out.println(number);
    }
}