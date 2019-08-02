package ua.skillsup.concurrency_practice;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class NumberGenerator implements Runnable {
    private final AtomicBoolean deadPool;
    private final BlockingQueue<Integer> queue;
    private final CountDownLatch downLatch;
    private final CountDownLatch mainLatch;

    public NumberGenerator(AtomicBoolean deadPool,
                           BlockingQueue<Integer> queue,
                           CountDownLatch downLatch,
                           CountDownLatch mainLatch) {
        this.deadPool = deadPool;
        this.queue = queue;
        this.downLatch = downLatch;
        this.mainLatch = mainLatch;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() +
                " started");
        downLatch.countDown();
        try {
            mainLatch.await();

            Random random = new Random();
            while (!deadPool.get()) {
                int num = random.nextInt(5) + 1;
                System.out.println(num +
                        " (" + Thread.currentThread().getName() + ")");
                queue.put(num);
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
