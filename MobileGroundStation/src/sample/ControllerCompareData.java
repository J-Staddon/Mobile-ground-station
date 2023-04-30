package sample;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ControllerCompareData {
    @FXML
    public AnchorPane anchorPane;
    public MenuButton roverMenuLeft;
    public MenuButton roverMenuRight;
    public Button exportLeftButton;
    public Button exportRightButton;
    public TableView<String[]> tableOfData;
    public TableView<String[]> tableOfDataLeft;
    public TableView<String[]> tableOfDataRight;
    private Controller controller;


    public void tableUpdater(Rover rover, TableView<String[]> table) {
        table.getColumns().clear();
        table.getItems().clear();

        String[][] data = new String[100][];
        int arrayDataPosition = 0;
        int arrayPosition;

        int sensorTableSize = 0;
        int amountOfData = 0;

        for (int i = 0; i < 100; i++) {
            if (rover.getRoverData()[i] != null) {
                if (rover.getRoverData()[i].getSensors().size() > sensorTableSize) {
                    sensorTableSize = rover.getRoverData()[i].getSensors().size();
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
                return new SimpleStringProperty(x != null && x.length>1 ? x[finalI] : "");
            });
        }

        int tempDataPosition;
        for (int i = 0; i < amountOfData; i++) {
            String[] dataArray = new String[100];
            tempDataPosition = rover.getDataPosition() - i;
            if (tempDataPosition < 0) {
                tempDataPosition = 99 + tempDataPosition + 1;
            }
            int x;
            for (x = 0; x < rover.getRoverData()[tempDataPosition].getSensors().size(); x++) {
                dataArray[x] = rover.getRoverData()[tempDataPosition].getSensors().get(x);
            }

            dataArray[sensorTableSize+1] = rover.getID();
            dataArray[sensorTableSize + 2] = rover.getName();
            dataArray[sensorTableSize + 3] = rover.getRoverData()[tempDataPosition].getBattery();
            dataArray[sensorTableSize + 4] = rover.getRoverData()[tempDataPosition].getDateFormatted();
            dataArray[sensorTableSize + 5] = rover.getRoverData()[tempDataPosition].getTimeFormatted();
            dataArray[sensorTableSize + 6] = String.valueOf(rover.getRoverData()[tempDataPosition].getLocationX());
            dataArray[sensorTableSize + 7] = String.valueOf(rover.getRoverData()[tempDataPosition].getLocationY());
            dataArray[sensorTableSize + 8] = String.valueOf(rover.getRoverData()[tempDataPosition].getRotation());
            dataArray[sensorTableSize + 9] = String.valueOf(rover.getRoverData()[tempDataPosition].getMessage());
            data[arrayDataPosition] = dataArray;
            arrayDataPosition++;
        }


    TableColumn<String[],String> IDColumn = new TableColumn("ID");
        TableColumn<String[],String> nameColumn = new TableColumn("Name");
        TableColumn<String[],String> batteryColumn = new TableColumn("Battery");
        TableColumn<String[],String> dateColumn = new TableColumn("Date");
        TableColumn<String[],String> timeColumn = new TableColumn("Time");
        TableColumn<String[],String> locationXColumn = new TableColumn("Latitude");
        TableColumn<String[],String> locationYColumn = new TableColumn("Longitude");
        TableColumn<String[],String> rotationColumn = new TableColumn("Direction");
        TableColumn<String[],String> messageColumn = new TableColumn("Message");

        table.getColumns().addAll(IDColumn, nameColumn, batteryColumn, dateColumn, timeColumn, locationXColumn, locationYColumn, rotationColumn, messageColumn);


        int finalArrayPosition = arrayPosition;
        IDColumn.setCellValueFactory((p)->{
            String[] x = p.getValue();
            return new SimpleStringProperty(x != null && x.length>1 ? x[finalArrayPosition+1] : "");
        });

        nameColumn.setCellValueFactory((p)->{
            String[] x = p.getValue();
            return new SimpleStringProperty(x != null && x.length>1 ? x[finalArrayPosition+2] : "");
        });

        batteryColumn.setCellValueFactory((p)->{
            String[] x = p.getValue();
            return new SimpleStringProperty(x != null && x.length>1 ? x[finalArrayPosition+3] : "");
        });

        dateColumn.setCellValueFactory((p)->{
            String[] x = p.getValue();
            return new SimpleStringProperty(x != null && x.length>1 ? x[finalArrayPosition+4] : "");
        });

        timeColumn.setCellValueFactory((p)->{
            String[] x = p.getValue();
            return new SimpleStringProperty(x != null && x.length>1 ? x[finalArrayPosition+5] : "");
        });

        locationXColumn.setCellValueFactory((p)->{
            String[] x = p.getValue();
            return new SimpleStringProperty(x != null && x.length>1 ? x[finalArrayPosition+6] : "");
        });

        locationYColumn.setCellValueFactory((p)->{
            String[] x = p.getValue();
            return new SimpleStringProperty(x != null && x.length>1 ? x[finalArrayPosition+7] : "");
        });

        rotationColumn.setCellValueFactory((p)->{
            String[] x = p.getValue();
            return new SimpleStringProperty(x != null && x.length>1 ? x[finalArrayPosition+8] : "");
        });

        messageColumn.setCellValueFactory((p)->{
            String[] x = p.getValue();
            return new SimpleStringProperty(x != null && x.length>1 ? x[finalArrayPosition+9] : "");
        });

        table.getItems().addAll(Arrays.asList(data));
    }


    public void viewOneRover(){
        menuSetter(roverMenuLeft, tableOfDataLeft);
        roverMenuRight.setVisible(false);
        roverMenuLeft.setVisible(true);
        tableOfData.setVisible(true);
//        exportLeftButton.setVisible(true);
        exportRightButton.setVisible(false);

    }

    public void viewTwoRovers(){
        menuSetter(roverMenuLeft, tableOfDataLeft);
        menuSetter(roverMenuRight, tableOfDataRight);
        tableOfData.setVisible(false);
        roverMenuLeft.setVisible(true);
        tableOfDataLeft.setVisible(true);
        roverMenuRight.setVisible(true);
        tableOfDataRight.setVisible(true);
        if(!roverMenuRight.getText().equals("Select Rover")){
            exportRightButton.setVisible(true);
        }
    }

    public void menuSetter(MenuButton roverMenu, TableView<String[]> table){
        roverMenu.getItems().clear();
        List<MenuItem> menuItemList = new ArrayList<>();
        for(int i = 0; i < controller.rovers.size(); i++) {
            MenuItem menuItem = new MenuItem(controller.rovers.get(i).getName() + " #" + controller.rovers.get(i).getID());
            int finalI = i;
            menuItem.setOnAction(event -> {
                tableUpdater(controller.rovers.get(finalI), table);
                if (table == tableOfDataLeft){
                    tableUpdater(controller.rovers.get(finalI), tableOfData);
                    exportLeftButton.setVisible(true);
                }
                else{
                    exportRightButton.setVisible(true);
                }
                roverMenu.setText(controller.rovers.get(finalI).getName());
            });
            menuItemList.add(menuItem);
        }
        roverMenu.getItems().addAll(menuItemList);
    }

    public void exportToExcel(ActionEvent event) {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Row row = sheet.createRow(0);
        Button button = (Button) event.getSource();
        TableView<String[]> table;
        MenuButton menu;
        if (button.getId().equals("exportLeftButton")) {
            table = tableOfDataLeft;
            menu = roverMenuLeft;
        } else {
            table = tableOfDataRight;
            menu = roverMenuRight;
        }

        for (int i = 0; i < table.getColumns().size(); i++) {
            row.createCell(i).setCellValue(table.getColumns().get(i).getText());
        }

        for (int i = 0; i < table.getItems().size(); i++) {
            row = sheet.createRow(i + 1);
            for (int j = 0; j < table.getColumns().size(); j++) {
                if (table.getColumns().get(j).getCellData(i) != null) {
                    row.createCell(j).setCellValue(table.getColumns().get(j).getCellData(i).toString());
                } else {
                    row.createCell(j).setCellValue("");
                }
            }
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Files", "*.xls"));
        fileChooser.setInitialFileName(menu.getText() + " Data.xls");
        File roverDataSheet = fileChooser.showSaveDialog(anchorPane.getScene().getWindow());

        try {
            roverDataSheet.createNewFile();
            FileOutputStream saveFile = new FileOutputStream(roverDataSheet, false);
            workbook.write(saveFile);
            saveFile.close();
            Desktop.getDesktop().open(roverDataSheet);
        }catch (Exception e){
            System.err.println("Export Error");
        }
    }

    public void setParentController(Controller controller){
        this.controller = controller;
    }
}
