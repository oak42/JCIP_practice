package jcip.practice.chapter2thread_safety;

import net.jcip.examples.NonreentrantDeadlock;
import net.jcip.examples.NonreentrantDeadlock.LoggingWidget;
import net.jcip.examples.NonreentrantDeadlock.YetAnotherWidget;
import net.jcip.examples.NonreentrantDeadlock.YetAnotherWidgetRecursive;

/**
 * Created by ackerley on 2019/4/5.
 * Reentrancy facilitates encapsulation of locking behavior, and thus simplifies the development of object-oriented concurrent code.
 * Without reentrant locks,
 * the very natural-looking code in Listing 2.7 , in which a subclass overrides a synchronized method and then calls the superclass method,
 * would deadlock.
 * Because the doSomething methods in Widget and LoggingWidget are both synchronized, each tries to acquire the lock on the Widget before proceeding.
 * But if intrinsic locks were not reentrant,
 *     the call to super.doSomething would never be able to acquire the lock because it would be considered already held, and
 *     the thread would permanently stall waiting for a lock it can never acquire.
 * Reentrancy saves us from deadlock in situations like this.
 */
public class PartialOne {
    public static void main(String[] args) {
        new LoggingWidget().doSomething();
        new YetAnotherWidget().doSomething1();
        new YetAnotherWidgetRecursive().recur();
    }
}
