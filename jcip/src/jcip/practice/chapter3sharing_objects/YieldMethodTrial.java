package jcip.practice.chapter3sharing_objects;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * todo 线程优先级？似乎也并不保证严格先后？
 *
 * @author ackerley
 * @date 2019-04-06 10:15
 */
public class YieldMethodTrial {
    private static CountDownLatch startGate = new CountDownLatch(3);
    private static AtomicInteger all3RunnerReachedHalfGate = new AtomicInteger(3);

    static class RankedRunner extends Thread {
        public RankedRunner(String name) {
            super(name);
        }

        @Override
        public void run() {
            startGate.countDown();
            try {
                startGate.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i = 1; i <= 7; i++) {
                System.out.println(getName() + " runs the " + i + " time.");
                if (4 == i) {
                    while (all3RunnerReachedHalfGate.decrementAndGet() > 0) {
                        yield();    //Thread.yield()

                    }
                }
            }
        }
    }

    public static void main(String[] args) {

        RankedRunner rank1 = new RankedRunner("Rank1 thread");
        rank1.setPriority(Thread.MAX_PRIORITY);
        rank1.start();

        RankedRunner rank2 = new RankedRunner("Rank2 thread");
        rank2.setPriority(Thread.NORM_PRIORITY);
        rank2.start();

        RankedRunner rank3 = new RankedRunner("Rank3 thread");
        rank3.setPriority(Thread.MIN_PRIORITY);
        rank3.start();

    }
}
