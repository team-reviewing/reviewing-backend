package project.reviewing.concurrency;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrencyRunner {

    private final ExecutorService executorService;
    private final CountDownLatch latch;
    private final CyclicBarrier barrier;

    public ConcurrencyRunner(int threadCnt) {
        this.executorService = Executors.newFixedThreadPool(threadCnt);
        this.latch = new CountDownLatch(threadCnt);
        this.barrier = new CyclicBarrier(threadCnt);
    }

    public void execute(ConcurrencyRunnable func, long milliseconds) {
        executorService.execute(() -> {
            try {
                barrier.await();
                Thread.sleep(milliseconds);
                func.run();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        });
    }

    public void await(long milliseconds) {
        try {
            latch.await();
            Thread.sleep(milliseconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}