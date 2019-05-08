package jthreads.practice.chapter2thread_creation_and_management.self_study;

/**
 * static void	sleep(long millis) / sleep(long millis, int nanos)
 * Causes the currently executing thread to sleep...
 * <p>
 * static !
 * the currently executing thread !
 * <p>
 * 本thread内部 ! 而非对其他thread !
 * <p>
 * Strictly speaking, sleeping is not the same thing as thread suspension.
 * One important difference is that with true thread suspension, one thread would suspend (and later resume) another thread.
 * Conversely, the sleep() method affects only the thread that executes it; it’s not possible to tell another thread to go to sleep.
 */
public class InterruptionExercise {
    private static long startTime = 0;
    private static long endTime = 0;
    private static long finalTime = 0;

    /**
     * public void interrupt()
     * Interrupts this thread.
     * <p>
     * Unless the current thread is interrupting itself, which is always permitted,
     * the checkAccess method of this thread is invoked, which may cause a SecurityException to be thrown.
     * <p>
     * If this thread is blocked in an invocation of the wait(), wait(long), or wait(long, int) methods of the Object class, or
     * ------------------------------------------ of the join(), join(long), join(long, int),
     * ------------------------------------------------- sleep(long), or sleep(long, int), methods of this class,
     * then its interrupt status will be cleared and it will receive an InterruptedException.
     * <p>
     * If this thread is blocked in an I/O operation upon an InterruptibleChannel
     * then the channel will be closed, the thread's interrupt status will be set, and the thread will receive a ClosedByInterruptException.
     * <p>
     * If this thread is blocked in a Selector
     * then the thread's interrupt status will be set and it will return immediately from the selection operation, possibly with a non-zero value, just as if the selector's wakeup method were invoked.
     * <p>
     * If none of the previous conditions hold
     * then this thread's interrupt status will be set.
     * <p>
     * Interrupting a thread that is not alive need not have any effect.
     * <p>
     * Throws:
     * SecurityException - if the current thread cannot modify this thread
     */
    static class TaskInterruptedInSleeping implements Runnable {
        @Override
        public void run() {
            Thread curThread = Thread.currentThread();
            try {
                startTime = System.currentTimeMillis();
                System.out.printf("1. %1s is running... plan to sleeping for 5s...", curThread);
                System.out.println();
                Thread.sleep(5000);
            } catch (InterruptedException excp) {
                //                                                                           true                false
                System.out.printf("3. %1s is interrupted, and now in catch block,   isAlive: %2b, isInterrupted: %3b", curThread, curThread.isAlive(), curThread.isInterrupted());
                System.out.println();
            } finally {
                endTime = System.currentTimeMillis();
                //                                                                           true                false
                System.out.printf("   %1s is interrupted, and now in finally block, isAlive: %2b, isInterrupted: %3b", curThread, curThread.isAlive(), curThread.isInterrupted());
                System.out.println();
            }

            System.out.printf("4. %1s is terminating... %2d milli seconds cost", curThread, endTime - startTime);
            System.out.println();
        }
    }

    static class TaskInterruptedInJoining implements Runnable {
        @Override
        public void run() {
            // todo
        }
    }

    static class TaskInterruptedInWaitting implements Runnable {
        @Override
        public void run() {
            // todo
        }
    }

    static class TaskInterruptedWhenBlockedInIo implements Runnable {
        @Override
        public void run() {
            // todo
        }
    }

    static class TaskInterruptedWhenBlockedInSelector implements Runnable {
        @Override
        public void run() {
            // todo
        }
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new TaskInterruptedInSleeping(), "test_task_1");
        //                                                                           false               false
        System.out.printf("   %1s created, before start,                    isAlive: %2b, isInterrupted: %3b", thread, thread.isAlive(), thread.isInterrupted());
        System.out.println();
        thread.start();
        //                                                                           true                false
        System.out.printf("   %1s, after start,                             isAlive: %2b, isInterrupted: %3b", thread, thread.isAlive(), thread.isInterrupted());
        System.out.println();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException excp) {
            excp.printStackTrace();
        }

        System.out.printf("2. %1s(main thread) is interrupting thread %2s after 2s...", Thread.currentThread(), thread);
        System.out.println();
        // Interrupts this thread. 立刻返回，不等被interrupt的thread结束，那就是说本方法non-blocking喽...
        thread.interrupt();
        //                                                                           true                true
        System.out.printf("   %1s, after interrupt,                         isAlive: %2b, isInterrupted: %3b", thread, thread.isAlive(), thread.isInterrupted());
        System.out.println();

        try {
            Thread.sleep(1000);
            thread.join();  // blocking
        } catch (InterruptedException excp) {
            excp.printStackTrace();
        }
        finalTime = System.currentTimeMillis();
        //                                                                              false               false
        System.out.printf("5. %1s, after join, %2d milli seconds cost          isAlive: %3b, isInterrupted: %4b", thread, finalTime - startTime, thread.isAlive(), thread.isInterrupted());
        System.out.println();

    }
}
/*
output:



 */