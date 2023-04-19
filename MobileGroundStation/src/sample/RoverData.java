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

    public String getDateFormatted(){
        char[] arr = date.toCharArray();
        char[] arr2 = {arr[6], arr[7], '/', arr[4], arr[5], '/', arr[0], arr[1], arr[2], arr[3]};
        return String.valueOf(arr2);
    }

    public String getTime() {
        return time;
    }

    public String getTimeFormatted() {
        char[] arr = time.toCharArray();
        char[] arr2 = {arr[0], arr[1], ':', arr[2], arr[3], ':', arr[4], arr[5]};
        return String.valueOf(arr2);
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

    public ArrayList<String> getSensors() {
        return sensors;
    }

    public String getID() {
        return ID;
    }

    @Override
    public String toString() {
        return "RoverData{" + "sensors" + sensors + ", battery='" + battery +  "date='" + date + '\'' + ", time='" + time + '\'' + ", locationX=" + locationX + ", locationY=" + locationY + '\'' + '}';
    }
}

