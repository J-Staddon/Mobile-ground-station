package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Rover {


    String dataFile;
    RoverData[] roverData = new RoverData[100];
    int dataPosition = -1;
    String name;
    String ID;
    int positionInArray;



    public void roverInsulator(String sentName, String fileName, int position) {
        name = sentName;
        dataFile = fileName;
        positionInArray = position;
    }

    public void loadValues(String line) throws IOException {
        RoverData data = new RoverData();
        data.addData(line);

        roverData[dataPosition] = data;
        if(dataPosition < 100) {
            dataPosition++;
        }
        else{
            dataPosition = 0;
        }
    }

    public void updateValues(String newData) throws IOException {
        RoverData data = new RoverData();
        boolean nullData = data.addData(newData);

        if(!nullData) {
            if (dataPosition < 100) {
                dataPosition++;
            } else {
                dataPosition = 0;
            }
            roverData[dataPosition] = data;
        }
    }



    public String getDataFile() {
        return dataFile;
    }

    public void setDataFile(String dataFile) {
        this.dataFile = dataFile;
    }

    public RoverData[] getRoverData() {
        return roverData;
    }

    public void setRoverData(RoverData[] roverData) {
        this.roverData = roverData;
    }

    public int getDataPosition() {
        return dataPosition;
    }

    public void setDataPosition(int dataPosition) {
        this.dataPosition = dataPosition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getPositionInArray() {
        return positionInArray;
    }

    public void setPositionInArray(int positionInArray) {
        this.positionInArray = positionInArray;
    }
}
