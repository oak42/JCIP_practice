package jcip.practice.chapter1;

import net.jcip.examples.Sequence;
import net.jcip.examples.UnsafeSequence;

/**
 * Created by ackerley on 2019/4/4.
 */
public class FirstBlood {
    public static void main(String[] args) {
        testNonThreadSafeSequenceGenerator();
        testThreadSafeSequenceGenerator();
    }

    private static void testNonThreadSafeSequenceGenerator() {
        UnsafeSequence unsafeSequenceSvc = new UnsafeSequence();
        for (int i = 0; i < 100; i++) {
            String indexStr = Integer.toString(i + 1) + ".: ";
            new Thread(() -> {
                System.out.println(indexStr + Integer.toString(unsafeSequenceSvc.getNext()));
            }).start();
        }
    }

    private static void testThreadSafeSequenceGenerator() {
        Sequence sequenceSvc = new Sequence();
        for (int i = 0; i < 100; i++) {
            String indexStr = Integer.toString(i + 1) + ".: ";
            new Thread(() -> {
                System.out.println(indexStr + Integer.toString(sequenceSvc.getNext()));
            }).start();
        }
    }
}
