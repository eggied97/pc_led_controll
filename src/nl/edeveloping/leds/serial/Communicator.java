package nl.edeveloping.leds.serial;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import gnu.io.*;
import nl.edeveloping.leds.Log;
import nl.edeveloping.leds.model.Port;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Created by Egbert Dijkstra on 13-10-2016.
 */



public class Communicator implements SerialPortEventListener {

    //map the port names to CommPortIdentifiers
    private HashMap<String, CommPortIdentifier> portMap = new HashMap();

    //this is the object that contains the opened port
    private SerialPort serialPort = null;

    //input and output streams for sending and receiving data
    private InputStream input = null;
    private OutputStream output = null;

    //just a boolean flag that i use for enabling
//and disabling buttons depending on whether the program
//is connected to a serial port or not
    private boolean bConnected = false;

    //the timeout value for connecting with the port
    private final static int TIMEOUT = 2000;

    //some ascii values for for certain things
    private final static int SPACE_ASCII = 32;
    private final static int DASH_ASCII = 45;
    private final static int NEW_LINE_ASCII = 10;

    private static final int BAUDRATE = 115200;



    public void searchForPorts(){
        Enumeration ports = CommPortIdentifier.getPortIdentifiers();

        while (ports.hasMoreElements()){
            CommPortIdentifier curPort = (CommPortIdentifier)ports.nextElement();

            if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL){
                Log.debug(curPort.getName() + " : " + curPort.getPortType());
                portMap.put(curPort.getName(), curPort);
            }
        }
    }

    public ArrayList<Port> getOpenPorts(){
        ArrayList<Port> result = new ArrayList<>();

        for(Map.Entry<String, CommPortIdentifier> e : portMap.entrySet()){
            result.add(new Port(e.getKey(), e.getValue()));
        }

        return result;
    }


    public boolean connect(Port p){
        CommPortIdentifier selectedPortIdentifier = p.getComPort();

        CommPort commPort = null;

        try{
            commPort = selectedPortIdentifier.open("TigerControlPanel", TIMEOUT);
            Log.debug(p.getName() + " opened successfully.");

            this.serialPort = (SerialPort)commPort;
            this.serialPort.setSerialPortParams( BAUDRATE,
                                                SerialPort.DATABITS_8,
                                                SerialPort.STOPBITS_1,
                                                SerialPort.PARITY_NONE );

            return true;

        }catch (PortInUseException e){
            Log.error(p.getName() + " is in use. (" + e.toString() + ")");
            return false;
        }catch (Exception e){
            Log.error("Failed to open " + p.getName() + "(" + e.toString() + ")");
            return false;
        }
    }

    //open the input and output streams
    //pre style="font-size: 11px;": an open port
    //post: initialized input and output streams for use to communicate data
    public boolean initIOStream(){

        try {
            input = this.serialPort.getInputStream();
            output = this.serialPort.getOutputStream();
            //writeData(0, 0);
            return true;
        }catch (IOException e) {
            Log.error("I/O Streams failed to open. (" + e.toString() + ")");

            return false;
        }
    }

    //starts the event listener that knows whenever data is available to be read
    //pre style="font-size: 11px;": an open serial port
    //post: an event listener for the serial port that knows when data is received
    public boolean initListener(){
        if(serialPort == null){return false;}

        try{
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);

            return true;
        }catch (TooManyListenersException e){
            Log.error("Too many listeners. (" + e.toString() + ")");

            return false;
        }
    }

    //disconnect the serial port
    //pre style="font-size: 11px;": an open serial port
    //post: closed serial port
    public void disconnect(){
        //close the serial port
        if(serialPort == null){
            return;
        }

        try{
            //writeData(0, 0);

            serialPort.removeEventListener();
            serialPort.close();
            input.close();
            output.close();

            Log.debug("Disconnected.");

        }catch (IOException e){
            Log.error("Failed to close " + serialPort.getName() + "(" + e.toString() + ")");
        }
    }

    //what happens when data is received
    //pre style="font-size: 11px;": serial event is triggered
    //post: processing on the data it reads
    @Override
    public void serialEvent(SerialPortEvent evt) {
        if (evt.getEventType() == SerialPortEvent.DATA_AVAILABLE){
            try{
                byte singleData = (byte)input.read();

                if (singleData != NEW_LINE_ASCII){
                    Log.debug("INPUT:");
                    Log.debug(new String(new byte[] {singleData}));
                }else{
                    Log.debug(">NewLine<");
                }

            }catch (IOException e){
                Log.error("Failed to read data. (" + e.toString() + ")");
            }
        }
    }

    public void writeColorWithPos(int pos, int r, int g, int b){

        String toSend = pos + " " + r + " " + g + " " + b + "\n";

        try{
            output.write(toSend.getBytes());
            output.flush();
        }catch (IOException e){
            e.printStackTrace();
        }

        /*try {
            output.write((byte)checkInput(pos, 0, 72));
            output.flush();
            output.write((byte)checkInput(r, 0, 255));
            output.flush();
            output.write((byte)checkInput(g, 0, 255));
            output.flush();
            output.write((byte)checkInput(b, 0, 255));
            output.flush();
            output.write("\n".getBytes());
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    private int checkInput(int input, int min, int max){
        int result = input;
        if(input > max){result = max;}
        if(input < min){result = min;}

        return result;
    }

    //method that can be called to send data
    //pre style="font-size: 11px;": open serial port
    //post: data sent to the other device
    /*public void writeData(int leftThrottle, int rightThrottle){
        try{
            output.write(leftThrottle);
            output.flush();
            //this is a delimiter for the data
            output.write(DASH_ASCII);
            output.flush();

            output.write(rightThrottle);
            output.flush();
            //will be read as a byte so it is a space key
            output.write(SPACE_ASCII);
            output.flush();
        }catch (Exception e){
            Log.error("Failed to write data. (" + e.toString() + ")");
        }
    }*/

}
