package sample;

import java.io.PrintWriter;

public class Rover {

    RoverData[] roverData = new RoverData[100];
    int dataPosition = -1;
    String name;
    String ID;


    public void saver(PrintWriter pw){
        pw.println(name);
        pw.println(ID);
        for (RoverData roverData : roverData) {
            roverData.saver(pw);
        }
    }

    public void roverInsulator(String sentName,/* String fileName, */String id) {
        name = sentName;
        ID = id;
    }

    public void updateValues(RoverData data){
        if (dataPosition < 99) {
            dataPosition++;
        } else {
            dataPosition = 0;
        }
        roverData[dataPosition] = data;
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

//    public int getPositionInArray() {
//        return positionInArray;
//    }
//
//    public void setPositionInArray(int positionInArray) {
//        this.positionInArray = positionInArray;
//    }
}
