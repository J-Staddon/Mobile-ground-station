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
            if(roverData != null) {
                roverData.saver(pw);
            }
            else{
                pw.println("missing");
            }
        }
    }

    public void roverInsulator(String sentName, String id) {
        name = sentName;
        ID = id;
    }

    public void updateValues(RoverData data) {
        RoverData[] tempRoverData = new RoverData[100];
        tempRoverData = roverData;
        boolean dataPlaced = false;
        boolean front = true;
        int tempDataPosition = dataPosition;
        int newTempDataPosition;
        if (dataPosition == -1){
            tempRoverData[0] = data;
            dataPosition = 0;
        }
        else {
            for (int i = 0; i < 100; i++) {
                tempDataPosition = tempDataPosition - i;
                newTempDataPosition = tempDataPosition + 1;

                if (tempDataPosition < 0) {
                    tempDataPosition = 99;
                    newTempDataPosition = 0;
                }
                if (roverData[tempDataPosition] == null) {
                    if (!dataPlaced){
                        tempRoverData[newTempDataPosition] = data;
                        dataPosition++;
                    }
                    break;
                }

                if (dataPlaced) {
                    tempRoverData[tempDataPosition] = roverData[tempDataPosition];
                }
                else if (data.getDate().compareTo(roverData[tempDataPosition].getDate()) == 0){
                    if (data.getTime().compareTo(roverData[tempDataPosition].getTime()) >= 0) { //If newData >= roverData
                        tempRoverData[newTempDataPosition] = data;
                        dataPosition++;
                        dataPlaced = true;
                        if (front){
                            break;
                        }
                    }
                    else {
                        tempRoverData[newTempDataPosition] = roverData[tempDataPosition];
                    }
                }
                else if (data.getDate().compareTo(roverData[tempDataPosition].getDate()) > 0) { //If newData > roverData
                    tempRoverData[newTempDataPosition] = data;
                    dataPosition++;
                    dataPlaced = true;
                    if (front){
                        break;
                    }
                }
                else {
                    tempRoverData[newTempDataPosition] = roverData[tempDataPosition];
                }
                front = false;
            }
        }
        roverData = tempRoverData;
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
}
