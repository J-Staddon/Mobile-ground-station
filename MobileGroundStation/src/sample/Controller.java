package sample;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

public class Controller {

    Image map = new Image(getClass().getResourceAsStream("images/testMap.jpg"));
    Image roverIcon = new Image(getClass().getResourceAsStream("images/roverIcon.png"));
    @FXML
    public AnchorPane anchorPane;
    int numOfRovers = 0;

    String infileName = "files/saveData";

    Button[] roverButtons = new Button[10];
    Rover[] rovers = new Rover[10];

    public void initialize() throws IOException {
        loader();
    }

    private void loader() throws IOException, FileNotFoundException {
        System.out.println(new File("saveData").getAbsolutePath());
        try (Scanner infile = new Scanner(new FileReader(infileName));) {
            infile.useDelimiter("\r?\n|\r");

            int numOfRovers = infile.nextInt();
            for (int i = 0; numOfRovers > i; i++){
                String name = infile.next();
                String fileName = infile.next();
                roverMaker(name, fileName);
                for(int x = 0; i < 100; i++){
                    roverPositionUpdater(rovers[numOfRovers-1], );
                    rovers[numOfRovers-1].loadValues(infile.next());
                }
            }
        }
        catch(Exception e){
            System.out.println("Error");
        }
    }

    private void saver(){

    }

    public void handleButtonClick(){

        //buttons[0] = rover2;
        roverButtons[0].setPadding(Insets.EMPTY);
        System.out.println("Yo");
        roverButtons[0].setLayoutX(roverButtons[0].getLayoutX()-1);
    }

    public void handleNewRoverButton() throws IOException {
        roverMaker("bob", "rover3TestData");
    }

    public void roverMaker(String name, String file) throws IOException {

        Rover rover = new Rover();
        numOfRovers++;
        rover.roverInsulator(name, file);
        //roverPositionUpdater(rover);
        rovers[numOfRovers-1] = rover;


        Button button;
        button = new Button(name);
        button.setOnAction(e -> handleButtonClick());

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
        roverButtons[numOfRovers-1] = button;

        //rovers[numOfRovers-1].roverInsulator();

    }

    public void roverPositionUpdater(Rover rover, String dataLine, Boolean loadData) throws IOException {
        if(loadData){
            rover.updateValues(dataLine);
        }
        else{
            FileReader hi = new FileReader(rover.getDataFile());
            //String hiii = readFile
            String hii = Files.readString("files/" + rover.getDataFile());

        }
        rover.updateValues();
    }

}

