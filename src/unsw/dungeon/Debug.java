package unsw.dungeon;

/**
 * Suite of debug utilities
 */
public class Debug {
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public static void printC(String text) {
        System.out.println(GREEN + text + RESET);
    }

    public static void printC(String text, String colour) {
        System.out.println(colour + text + RESET);
    }
}
