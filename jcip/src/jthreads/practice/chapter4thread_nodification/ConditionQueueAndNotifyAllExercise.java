package jthreads.practice.chapter4thread_nodification;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * what happens to the condition queue when notifyAll is called
 */
public class ConditionQueueAndNotifyAllExercise {
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
                printlnToStdOut("get notified and(!) now is back to life (lock acquired)...");
            }
            Object task = taskFIFO.pollFirst();
//            notifyAll(); attn !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            return task;
        }

        protected synchronized final void doPut(List<Object> task) throws InterruptedException {
            printlnToStdOut("try to produce a task...");
            while (isFull()) {//  attn - wait should always be used in a loop ...
                printlnToStdOut("fifo is full, WAIT to produce");
                wait();
                printlnToStdOut("get notified and(!) now is back to life (lock acquired)...");
            }
            taskFIFO.addAll(task);
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
        boolean infinity;
        float produceInterval;
        int productionSpeed;
        static AtomicInteger taskId = new AtomicInteger(0);

        public TaskProducer(BoundedTaskFIFO fifo, boolean infinity, float produceInterval, int productionSpeed) {
            this.fifo = fifo;
            this.infinity = infinity;
            this.produceInterval = produceInterval;
            this.productionSpeed = productionSpeed;
        }

        @Override
        public void run() {
            do {
                try {
                    int taskIdBase = taskId.getAndAdd(productionSpeed); // bulk apply task id
                    List<Object> taskList = new ArrayList<>(productionSpeed);
                    for (int i = 1; i <= productionSpeed; i++) {
                        taskList.add(taskIdBase + i);
                    }
                    fifo.doPut(taskList);
                    printlnToStdOut("produced task" + taskList + "");
                    Thread.sleep((int) (1000 * produceInterval));
                } catch (InterruptedException ex) {
                }

            } while (infinity);
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
            new Thread(new TaskConsumer(fifo, .2f), "consumer" + i).start();
        }

        new Thread(new TaskProducer(fifo, false, .3f, 5), "producer" + " singleton").start();
    }
}
