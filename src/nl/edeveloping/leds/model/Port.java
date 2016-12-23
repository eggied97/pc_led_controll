package nl.edeveloping.leds.model;

import gnu.io.CommPortIdentifier;

/**
 * Created by Egbert Dijkstra on 22-12-2016.
 */
public class Port {
    private String name;
    private CommPortIdentifier comPort;

    public Port(String name, CommPortIdentifier comPort) {
        this.name = name;
        this.comPort = comPort;
    }

    public String getName() {
        return name;
    }

    public CommPortIdentifier getComPort() {
        return comPort;
    }

    public String toString(){
        return this.name;
    }
}
