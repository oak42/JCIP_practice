package jthreads.practice.chapter4thread_nodification;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Threads can use the wait and notify mechanism discussed in Chapter 4
 * to achieve the functionality of thread suspension and resumption.
 * The difference is that the threads must be coded to use that technique
 * (rather than a generic suspend/resume mechanism that could be imposed from other threads).
 */
public class WaitAndNotifyMechanismExercise {
    static class BoundedTaskFIFO {
        private int capacity;
        private LinkedList<Object> taskFIFO;

        protected BoundedTaskFIFO(int capacity) {
            this.capacity = capacity;
            taskFIFO = new LinkedList<>();
        }

        protected synchronized final Object doTake() throws InterruptedException {
            printlnToStdOut("try to consume a task...");
            while (isEmpty()) {// attn - wait should always be used in a loop ...
                printlnToStdOut("fifo is empty, WAIT to consume");
                wait();
            }
            Object task = taskFIFO.pollFirst();
            notifyAll();
            return task;
        }

        protected synchronized final void doPut(Object task) throws InterruptedException {
            printlnToStdOut("try to produce a task...");
            while (isFull()) {// attn - wait should always be used in a loop ...
                printlnToStdOut("fifo is full, WAIT to produce");
                wait();
            }
            taskFIFO.offerLast(task);
            notifyAll();
        }

        public synchronized final boolean isFull() {
            return taskFIFO.size() == capacity;
        }

        public synchronized final boolean isEmpty() {
            return taskFIFO.size() == 0;
        }
    }


    static class TaskConsumer implements Runnable {
        BoundedTaskFIFO fifo;
        float consumInterval;

        public TaskConsumer(BoundedTaskFIFO fifo, float consumInterval) {
            this.fifo = fifo;
            this.consumInterval = consumInterval;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Object task = fifo.doTake();
                    printlnToStdOut("consumed task" + task);
                    Thread.sleep((int) (1000 * consumInterval));
                } catch (InterruptedException ex) {
                }

            }
        }
    }

    static class TaskProducer implements Runnable {
        BoundedTaskFIFO fifo;
        float produceInterval;
        static AtomicInteger taskId = new AtomicInteger(0);

        public TaskProducer(BoundedTaskFIFO fifo, float produceInterval) {
            this.fifo = fifo;
            this.produceInterval = produceInterval;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    int task = taskId.addAndGet(1);
                    fifo.doPut(task);
                    printlnToStdOut("produced task" + task);
                    Thread.sleep((int) (1000 * produceInterval));
                } catch (InterruptedException ex) {
                }

            }
        }
    }

    public static void printlnToStdOut(Object content) {
        System.out.println(thisThreadName() + content);
    }

    public static String thisThreadName() {
        return "[" + Thread.currentThread().getName() + "] ";
    }

    public static void main(String[] args) {
        BoundedTaskFIFO fifo = new BoundedTaskFIFO(7);
        for (int i = 0; i < 5; i++) {
            new Thread(new TaskProducer(fifo, .3f), "producer" + i).start();
            new Thread(new TaskConsumer(fifo, .2f), "consumer" + i).start();
        }
    }
}
