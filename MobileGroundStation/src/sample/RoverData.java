package sample;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class RoverData {

    String date;
    String time;
    double locationX;
    double locationY;
    String battery;
    int numSensors = 0;
    String[] sensors = new String[10];
    String ID;

    public boolean addData(String file) throws IOException, FileNotFoundException {
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
                        sensors[numSensors] = data;
                        numSensors++;
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

    public int getNumSensors() {
        return numSensors;
    }

    public String[] getSensors() {
        return sensors;
    }

    public String getID() {
        return ID;
    }
}

