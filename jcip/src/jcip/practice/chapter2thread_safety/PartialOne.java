package jcip.practice.chapter2thread_safety;

import net.jcip.examples.NonreentrantDeadlock;
import net.jcip.examples.NonreentrantDeadlock.LoggingWidget;
import net.jcip.examples.NonreentrantDeadlock.YetAnotherWidget;
import net.jcip.examples.NonreentrantDeadlock.YetAnotherWidgetRecursive;

/**
 * Created by ackerley on 2019/4/5.
 * todo 现在intrinsic lock都是reentrant的了？？？还是自己理解有问题？
 */
public class PartialOne {
    public static void main(String[] args) {
        new LoggingWidget().doSomething();
        new YetAnotherWidget().doSomething1();
        new YetAnotherWidgetRecursive().recur();
    }
}
