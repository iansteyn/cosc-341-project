/*
 * WHAT IS THIS?
 * This file just mocks the Log method d so that it can pretend
 * to be used in unit tests.
 */
package android.util;

public class Log {
    public static int d(String tag, String msg) {
        System.out.println("DEBUG: " + tag + ": " + msg);
        return 0;
    }
}
