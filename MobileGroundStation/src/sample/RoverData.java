package sample;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Rover data class
 *
 * @author Jay Staddon
 * @version 4th May 2023
 */
public class RoverData {

    private String date;
    private String time;
    private double locationX;
    private double locationY;
    private ArrayList<String> sensors = new ArrayList<>();
    private String ID;
    private String battery;
    private double rotation;
    private String message = "";

    /**
     * Saves to file
     *
     * @param pw Writes to file
     */
    public void saver(PrintWriter pw){
        pw.print('D');
        pw.print(date);
        pw.print('#');
        pw.print('T');
        pw.print(time);
        pw.print('#');
        pw.print('X');
        pw.print(locationX);
        pw.print('#');
        pw.print('Y');
        pw.print(locationY);
        pw.print('#');
        for (String sensor : sensors){
            pw.print('S');
            pw.print(sensor);
            pw.print('#');
        }
        pw.print('I');
        pw.print(ID);
        pw.print('#');
        pw.print('B');
        pw.print(battery);
        pw.print('#');
        pw.print('R');
        pw.print(rotation);
        pw.print('#');
        pw.print('M');
        pw.println(message);
    }

    /**
     * Creates new piece of data
     *
     * @param receivedData New data string
     * @return If successful return true
     */
    public boolean addData(String receivedData){
        try (Scanner infile = new Scanner(receivedData);) {
            infile.useDelimiter("\r?#|\r");
            char pointer;
            String data;
            while(infile.hasNext()) {
                data = infile.next();
                pointer = data.charAt(0);
                data = data.substring(1);
                switch (pointer) {
                    case 'D':
                        date = data;
                        break;
                    case 'T':
                        time = data;
                        break;
                    case 'X':
                        locationX = Double.parseDouble(data);
                        break;
                    case 'Y':
                        locationY = Double.parseDouble(data);
                        break;
                    case 'S':
                        sensors.add(data);
                        break;
                    case 'I':
                        ID = data;
                        break;
                    case 'B':
                        battery = data;
                        break;
                    case 'R':
                        rotation = Double.parseDouble(data);
                        break;
                    case 'M':
                        message = data;
                        break;
                    default:
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * Formats date
     *
     * @return Formatted data
     */
    public String getDateFormatted(){
        char[] arr = date.toCharArray();
        char[] arr2 = {arr[6], arr[7], '/', arr[4], arr[5], '/', arr[0], arr[1], arr[2], arr[3]};
        return String.valueOf(arr2);
    }

    /**
     * Formats time
     *
     * @return Formatted time
     */
    public String getTimeFormatted() {
        char[] arr = time.toCharArray();
        char[] arr2 = {arr[0], arr[1], ':', arr[2], arr[3], ':', arr[4], arr[5]};
        return String.valueOf(arr2);
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public double getLocationX() {
        return locationX;
    }

    public double getLocationY() {
        return locationY;
    }

    public ArrayList<String> getSensors() {
        return sensors;
    }

    public String getID() {
        return ID;
    }

    public String getBattery() {
        return battery;
    }

    public double getRotation() {
        return rotation;
    }

    public String getMessage() {
        return message;
    }
}

