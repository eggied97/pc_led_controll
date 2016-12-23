package nl.edeveloping.leds;

/**
 * Created by Egbert Dijkstra on 22-12-2016.
 */
public class Log {
    private static final boolean DEBUG = true;

    public static void debug(String message){
        if(DEBUG){
            System.out.println(message);
        }
    }

    public static void error(String message){
        System.out.println("ERROR >>> "+message);
    }

}
