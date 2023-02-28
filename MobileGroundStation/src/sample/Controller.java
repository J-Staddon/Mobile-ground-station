package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Controller {

    Image map = new Image(getClass().getResourceAsStream("images/testMap.jpg"));
    Image roverIcon = new Image(getClass().getResourceAsStream("images/roverIcon.png"));
    @FXML
    public AnchorPane anchorPane;
    public ListView<Rover> leftListView;
    public ListView<Rover> rightListView;

    //public String selectedRover;
    public int selectedRoverPos;

    int numOfRovers = 0;

    String infileName = "files/saveData";

    int arraySize = 10;

    Button[] roverButtons = new Button[arraySize];
    Rover[] rovers = new Rover[arraySize];

    public void initialize() throws IOException {
        loader();
        //roverSelectPanel();

    }

    private void loader() throws IOException, FileNotFoundException {
        System.out.println(new File("saveData").getAbsolutePath());
        try (Scanner infile = new Scanner(new FileReader(infileName));) {
            infile.useDelimiter("\r?\n|\r");

            int numOfRoversToAdd = infile.nextInt();
            for (int i = 0; numOfRoversToAdd > i; i++){
                String name = infile.next();
                String fileName = infile.next();
                roverMaker(name, fileName);
                for(int x = 0; x < 100; x++){
                    roverPositionUpdater(rovers[numOfRovers-1], infile.next(), true);
                    //rovers[numOfRovers-1].loadValues(infile.next());
                }
            }
        }
        catch(Exception e){
            System.out.println("Error");
        }
    }

    private void saver(){

    }

    public void handleButtonClick(int position){

        //int num = Integer.parseInt(position);
        //roverButtons[num].setPadding(Insets.EMPTY);
        System.out.println(selectedRoverPos);
        //selectedRover = rovers[num].name;
        roverButtons[selectedRoverPos].setBorder(Border.EMPTY);
        selectedRoverPos = position;
        roverButtons[selectedRoverPos].setLayoutX(roverButtons[selectedRoverPos].getLayoutX()-1);
        roverButtons[selectedRoverPos].setBorder(Border.stroke(Paint.valueOf("#77C2BB")));
    }

    public void handleNewRoverButton() throws IOException {
        roverMaker("bob", "rover3TestData");
        roverPositionUpdater(rovers[numOfRovers-1], rovers[numOfRovers-1].getDataFile(), false);
    }

    public void roverSelectPanel(int position){

       //leftListView.getItems().add(rovers[roverPosition].name);
        //ListView<Rover> roverListView = new ListView<>();
        leftListView.getItems().addAll(rovers[position]);
        leftListView.setCellFactory(lv -> new ListCell<Rover>(){
             @Override
             public void updateItem(Rover rover, boolean empty) {
                   super.updateItem(rover, empty) ;
                   setText(empty ? null : rover.getName());
             }
        });

        leftListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Rover>() {
            @Override
            public void changed(ObservableValue<? extends Rover> observableValue, Rover rover, Rover t1) {
                int position = leftListView.getSelectionModel().getSelectedItem().positionInArray;
                handleButtonClick(position);
            }
        });
    }
/*
    public void dataDisplayWindow(){
        rightListView.getItems().addAll(rovers[roverPosition]);
        rightListView.setCellFactory(lv -> new ListCell<Rover>(){
            @Override
            public void updateItem(Rover rover, boolean empty) {
                super.updateItem(rover, empty) ;
                setText(empty ? null : rover.getName());
            }
        });

        rightListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Rover>() {
            @Override
            public void changed(ObservableValue<? extends Rover> observableValue, Rover rover, Rover t1) {
                selectedRoverPos = rightListView.getSelectionModel().getSelectedItem().positionInArray;
                handleButtonClick(selectedRoverPos);
            }
        });
    }
*/


    public void deleteRover(){

    }


    public void roverMaker(String name, String file) throws IOException {

        Rover rover = new Rover();
        int position = -1;
        for(int i = 0; i < arraySize; i++){
            if (rovers[i] == null){
                position = i;
                break;
            }
        }
        if(position == -1){
            System.out.println("Too many rovers");
            return;
        }
        numOfRovers++;
        rover.roverInsulator(name, file, position);
        rovers[position] = rover;
        roverButtonMaker(name, position);
        roverSelectPanel(position);

    }

    private void roverButtonMaker(String name, int position){
        Button button;
        button = new Button(name);
        button.setId(Integer.toString(position));
        button.setOnAction(e -> handleButtonClick(position));

        button.setPrefWidth(50);
        button.setPrefHeight(50);
        ImageView view = new ImageView(roverIcon);
        view.setFitWidth(button.getPrefWidth());
        view.setFitHeight(button.getPrefHeight());

        button.setGraphic(view);
        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        button.setBackground(null);

        //this.anchorPane = anchorPane;
        anchorPane.getChildren().add(button);

        button.setLayoutX(100);
        button.setLayoutY(200);
        roverButtons[position] = button;
    }





    public void roverPositionUpdater(Rover rover, String dataLine, Boolean loadData) throws IOException {
        if(loadData){
            rover.updateValues(dataLine);
        }
        else{
            String contents = new String(Files.readAllBytes(Paths.get("files/" + rover.getDataFile())));
            rover.updateValues(contents);
        }
    }
}

