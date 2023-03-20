package sample;

//import sun.management.Sensor;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class RoverData {

    String date;
    String time;
    double locationX;
    double locationY;
    //int numSensors = 0;
    //String[] sensors = new String[10];
    ArrayList<String> sensors = new ArrayList<>();
    String ID;
    String battery;


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
        pw.println(battery);
    }

    public boolean addData(String file){
        try (Scanner infile = new Scanner(file);) {
            infile.useDelimiter("\r?#|\r");
            char pointer = 'a';
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
                        //sensors[numSensors] = data;
                        sensors.add(data);
//                        numSensors++;
                        break;
                    case 'I':
                        ID = data;
                        break;
                    case 'B':
                        battery = data;
                        break;
                    default:
                        return true;
                }
            }
        }
        return false;
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

    public String getBattery() {
        return battery;
    }

//    public int getNumSensors() {
//        return numSensors;
//    }

    public ArrayList<String> getSensors() {
        return sensors;
    }

    public String getID() {
        return ID;
    }
}

