package net.jcip.examples;

/**
 * NonreentrantDeadlock
 * <p/>
 * Code that would deadlock if intrinsic locks were not reentrant
 * ★★★★★★★没有发生deadlock啊...现在intrinsic lock都是reentrant的了？？？★★★★★★★
 *
 * @author Brian Goetz and Tim Peierls
 */
public class NonreentrantDeadlock {
    public static class Widget {
        public synchronized void doSomething() {
            System.out.println("doSomething in Widget gets called...");
        }
    }

    public static class LoggingWidget extends Widget {
        public synchronized void doSomething() {
            System.out.println(toString() + ": calling doSomething");
            super.doSomething();
        }
    }

    public static class YetAnotherWidget {
        public synchronized void doSomething1() {
            System.out.println("doSomething1 in YetAnotherWidget gets called...");
            doSomething2();
        }

        public synchronized void doSomething2() {
            System.out.println("doSomething2 in YetAnotherWidget gets called...");
        }
    }

    public static class YetAnotherWidgetRecursive {
        private int counter = 7;

        public synchronized void recur() {
            System.out.println("recur gets called the " + (7 - counter-- + 1) + "time");
            if (counter > 0) {
                recur();
            }

        }

    }
}


