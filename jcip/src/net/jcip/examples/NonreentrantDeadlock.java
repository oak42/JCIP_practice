package net.jcip.examples;

/**
 * NonreentrantDeadlock
 * <p/>
 * Code that would deadlock if intrinsic locks were not reentrant
 *
 * Reentrancy facilitates encapsulation of locking behavior, and thus
 *            simplifies the development of object-oriented concurrent code.
 * Without reentrant locks,
 * the very natural-looking code in Listing 2.7 ,
 * in which a subclass overrides a synchronized method and then calls the superclass method,
 * would deadlock.
 * Because the doSomething methods in Widget and LoggingWidget are both synchronized,
 *         each tries to acquire the lock on the Widget before proceeding.
 * But if intrinsic locks were not reentrant,
 *     the call to super.doSomething would never be able to acquire the lock because it would be considered already held, and
 *     the thread would permanently stall waiting for a lock it can never acquire.
 * Reentrancy saves us from deadlock in situations like this.
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


