package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ControllerCompareData {
    @FXML
    public AnchorPane anchorPane;
    public MenuButton roverMenuLeft;
    public MenuButton roverMenuRight;
    public TableView<String[]> tableOfData;
    public TableView<String[]> tableOfDataLeft;
    public TableView<String[]> tableOfDataRight;
    private Controller controller;

    @FXML
    private void initialize() {
    }


    public void tableUpdater(Rover rover, TableView<String[]> table) {
        table.getColumns().clear();
        table.getItems().clear();

        String[][] data = new String[100][];
        int arrayDataPosition = 0;
        int arrayPosition;

        int sensorTableSize = 0;
        int amountOfData = 0;

        for (int i = 0; i < 100; i++) {
            if (rover.roverData[i] != null) {
                if (rover.roverData[i].sensors.size() > sensorTableSize) {
                    sensorTableSize = rover.roverData[i].sensors.size();
                }
                amountOfData++;
            }
        }

        for (arrayPosition = 0; arrayPosition < sensorTableSize; arrayPosition++) {
            TableColumn<String[],String> sensorColumn = new TableColumn("Sensor " + (arrayPosition + 1));
            table.getColumns().add(sensorColumn);
            int finalI = arrayPosition;
            sensorColumn.setCellValueFactory((p)->{
                String[] x = p.getValue();
                return new SimpleStringProperty(x != null && x.length>1 ? x[finalI] : "Null");
            });
        }

        int tempDataPosition;
        for (int i = 0; i < amountOfData; i++) {
            String[] dataArray = new String[100];
            tempDataPosition = rover.dataPosition - i;
            if (tempDataPosition < 0) {
                tempDataPosition = 99 + tempDataPosition + 1;
            }
            int x;
            for (x = 0; x < rover.roverData[tempDataPosition].sensors.size(); x++) {
                dataArray[x] = rover.roverData[tempDataPosition].sensors.get(x);
            }

            dataArray[sensorTableSize+1] = rover.ID;
            dataArray[sensorTableSize + 2] = rover.name;
            dataArray[sensorTableSize + 3] = rover.roverData[tempDataPosition].battery;
            dataArray[sensorTableSize + 4] = rover.roverData[tempDataPosition].getDateFormatted();
            dataArray[sensorTableSize + 5] = rover.roverData[tempDataPosition].getTimeFormatted();
            dataArray[sensorTableSize + 6] = String.valueOf(rover.roverData[tempDataPosition].locationX);
            dataArray[sensorTableSize + 7] = String.valueOf(rover.roverData[tempDataPosition].locationY);
            data[arrayDataPosition] = dataArray;
            arrayDataPosition++;
        }



    TableColumn<String[],String> IDColumn = new TableColumn("ID");
        TableColumn<String[],String> nameColumn = new TableColumn("Name");
        TableColumn<String[],String> batteryColumn = new TableColumn("Battery");
        TableColumn<String[],String> dateColumn = new TableColumn("Date");
        TableColumn<String[],String> timeColumn = new TableColumn("Time");
        TableColumn<String[],String> locationXColumn = new TableColumn("LocationX");
        TableColumn<String[],String> locationYColumn = new TableColumn("LocationY");

        table.getColumns().addAll(IDColumn, nameColumn, batteryColumn, dateColumn, timeColumn, locationXColumn, locationYColumn);


        int finalArrayPosition = arrayPosition;
        IDColumn.setCellValueFactory((p)->{
            String[] x = p.getValue();
            return new SimpleStringProperty(x != null && x.length>1 ? x[finalArrayPosition+1] : "Missing ID");
        });

        nameColumn.setCellValueFactory((p)->{
            String[] x = p.getValue();
            return new SimpleStringProperty(x != null && x.length>1 ? x[finalArrayPosition+2] : "Missing Name");
        });

        batteryColumn.setCellValueFactory((p)->{
            String[] x = p.getValue();
            return new SimpleStringProperty(x != null && x.length>1 ? x[finalArrayPosition+3] : "Missing Battery");
        });

        dateColumn.setCellValueFactory((p)->{
            String[] x = p.getValue();
            return new SimpleStringProperty(x != null && x.length>1 ? x[finalArrayPosition+4] : "Missing Date");
        });

        timeColumn.setCellValueFactory((p)->{
            String[] x = p.getValue();
            return new SimpleStringProperty(x != null && x.length>1 ? x[finalArrayPosition+5] : "Missing Time");
        });

        locationXColumn.setCellValueFactory((p)->{
            String[] x = p.getValue();
            return new SimpleStringProperty(x != null && x.length>1 ? x[finalArrayPosition+6] : "Missing X Coordinate");
        });

        locationYColumn.setCellValueFactory((p)->{
            String[] x = p.getValue();
            return new SimpleStringProperty(x != null && x.length>1 ? x[finalArrayPosition+7] : "Missing Y Coordinate");
        });

        table.getItems().addAll(Arrays.asList(data));
    }


    public void viewOneRover(){
        menuSetter(roverMenuLeft, tableOfDataLeft);
        roverMenuRight.setVisible(false);
        roverMenuLeft.setVisible(true);
        tableOfData.setVisible(true);
    }

    public void viewTwoRovers(){
        menuSetter(roverMenuLeft, tableOfDataLeft);
        menuSetter(roverMenuRight, tableOfDataRight);
        tableOfData.setVisible(false);
        roverMenuLeft.setVisible(true);
        tableOfDataLeft.setVisible(true);
        roverMenuRight.setVisible(true);
        tableOfDataRight.setVisible(true);
    }

    public void menuSetter(MenuButton roverMenu, TableView<String[]> table){
        roverMenu.getItems().clear();
        List<MenuItem> menuItemList = new ArrayList<>();
        for(int i = 0; i < controller.rovers.size(); i++) {
            MenuItem menuItem = new MenuItem(controller.rovers.get(i).name + " #" + controller.rovers.get(i).ID);
            int finalI = i;
            menuItem.setOnAction(event -> {
                tableUpdater(controller.rovers.get(finalI), table);
                if (table == tableOfDataLeft){
                    tableUpdater(controller.rovers.get(finalI), tableOfData);
                }
                roverMenu.setText(controller.rovers.get(finalI).name);
            });
            menuItemList.add(menuItem);
        }
        roverMenu.getItems().addAll(menuItemList);
    }



    public void setParentController(Controller controller){
        this.controller = controller;
    }
}
