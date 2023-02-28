package sample;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class RoverData {

    int date;
    int time;
    double locationX;
    double locationY;
    float battery;
    int numSensors = 0;
    float[] sensors = new float[10];
    String ID;

    public void addData(String file) throws IOException, FileNotFoundException {
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
                        date = Integer.parseInt(data);
                        break;
                    case 'T':
                        time = Integer.parseInt(data);
                        break;
                    case 'X':
                        locationX = Double.parseDouble(data);
                        break;
                    case 'Y':
                        locationY = Double.parseDouble(data);
                        break;
                    case 'S':
                        sensors[numSensors] = Float.parseFloat(data);
                        numSensors++;
                        break;
                    case 'I':
                        ID = data;
                        break;
                    case 'B':
                        battery = Float.parseFloat(data);
                        break;
                }
            }
        }
    }
}

