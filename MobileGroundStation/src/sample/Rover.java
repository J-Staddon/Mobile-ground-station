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
    int dataPosition = 0;
    String name;



    public void roverInsulator(String sentName, String fileName) {
        name = sentName;
        dataFile = fileName;
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

    public void updateValues() throws IOException {
        RoverData data = new RoverData();
        data.addData("files/" + dataFile);

        roverData[dataPosition] = data;
        if(dataPosition < 100) {
            dataPosition++;
        }
        else{
            dataPosition = 0;
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
}
