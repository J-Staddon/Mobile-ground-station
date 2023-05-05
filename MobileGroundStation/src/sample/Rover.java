package sample;

import java.io.PrintWriter;

/**
 * Rover class
 *
 * @author Jay Staddon
 * @version 4th May 2023
 */
public class Rover {

    private RoverData[] roverData = new RoverData[100];
    private int dataPosition = -1;
    private String name;
    private String ID;

    /**
     * Saves to file
     *
     * @param pw Writes to file
     */
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

    /**
     * Creates rover
     *
     * @param sentName Rover name
     * @param id Rover ID
     */
    public void roverInsulator(String sentName, String id) {
        name = sentName;
        ID = id;
    }

    /**
     * Updates the data array
     *
     * @param data new data
     */
    public void updateValues(RoverData data) {
        RoverData[] tempRoverData;
        tempRoverData = roverData;
        boolean dataPlaced = false;
        boolean front = true;
        int dataPositionNum = dataPosition;
        int tempDataPosition;
        int newTempDataPosition;
        if (dataPosition == -1){
            tempRoverData[0] = data;
            dataPosition = 0;
            dataPlaced = true;
        }
        else {
            //Reorders the array into the correct order
            for (int i = 0; i < 100; i++) {
                tempDataPosition = dataPositionNum- i;
                newTempDataPosition = tempDataPosition + 1;

                if (tempDataPosition < 0 || tempDataPosition == 99) {
                    tempDataPosition = 99;
                    newTempDataPosition = 0;
                }

                if (roverData[tempDataPosition] == null) {
                    if (!dataPlaced){
                        tempRoverData[newTempDataPosition] = data;
                        dataPosition++;
                        dataPlaced = true;
                    }
                    break;
                }

                if (dataPlaced) {
                    tempRoverData[tempDataPosition] = roverData[tempDataPosition];
                }
                else if (data.getDate().compareTo(roverData[tempDataPosition].getDate()) == 0){
                    if (data.getTime().compareTo(roverData[tempDataPosition].getTime()) >= 0) {
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
                else if (data.getDate().compareTo(roverData[tempDataPosition].getDate()) > 0) {
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
        if(!dataPlaced){
            dataPosition++;
        }

        if(dataPosition == 100){
            dataPosition = 0;
        }
        roverData = tempRoverData;
    }

    public RoverData[] getRoverData() {
        return roverData;
    }

    public int getDataPosition() {
        return dataPosition;
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
