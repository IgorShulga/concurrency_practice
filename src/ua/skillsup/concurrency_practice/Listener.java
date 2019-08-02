package ua.skillsup.concurrency_practice;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Listener implements Callable<String> {
    private final AtomicBoolean deadPool;
    private final BlockingQueue<Integer> queue;
    private final CountDownLatch downLatch;
    private final CountDownLatch mainLatch;

    public Listener(AtomicBoolean deadPool,
                    BlockingQueue<Integer> queue,
                    CountDownLatch downLatch,
                    CountDownLatch mainLatch) {
        this.deadPool = deadPool;
        this.queue = queue;
        this.downLatch = downLatch;
        this.mainLatch = mainLatch;
    }

    @Override
    public String call() throws Exception {
        System.out.println(Thread.currentThread().getName() +
                " started");
        downLatch.countDown();
        mainLatch.await();
        int sum = 0;
        while (sum < 100) {
            if(deadPool.get()) {
                return "deadPool!";
            }
                sum += queue.poll(100, TimeUnit.MILLISECONDS);
                System.out.println("sum " +
                        Thread.currentThread().getName() +
                        " = " + sum);

        }
        deadPool.set(true);
        return sum + " (" + Thread.currentThread().getName() + ")";
    }
}
