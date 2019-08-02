package ua.skillsup.concurrency_practice;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class App {
    public static void main(String[] args) throws InterruptedException {
        AtomicBoolean deadPool = new AtomicBoolean(false);
        BlockingQueue<Integer> queue =
                new ArrayBlockingQueue<>(100);
        CountDownLatch downLatch = new CountDownLatch(9);
        CountDownLatch mainLatch = new CountDownLatch(1);
        NumberGenerator generator =
                new NumberGenerator(deadPool, queue, downLatch, mainLatch);

        ExecutorService threadPool =
                Executors.newFixedThreadPool(9);
        for (int i = 0; i < 7; i++) {
            threadPool.execute(generator);
        }
        for (int i = 0; i < 2; i++) {
            threadPool.submit(new Listener(deadPool, queue, downLatch, mainLatch));
        }
        downLatch.await();
        mainLatch.countDown();

        threadPool.shutdown();
    }
}

