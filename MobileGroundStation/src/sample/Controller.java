package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Controller {

   // File imageFile = new File("C:/Users/jayst/Documents/Uni/CS39440_Major_Project/Program/MobileGroundStation/src/sample/images/testMap.jpg");
    //Image map = new Image(imageFile.toURI().toString());
    //Image map = new Image("C:/Users/jayst/Documents/Uni/CS39440_Major_Project/Program/MobileGroundStation/src/sample/images/testMap.jpg");
    Image map = new Image(getClass().getResourceAsStream("images/testMap.jpg"));
    Image roverIcon = new Image(getClass().getResourceAsStream("images/roverIcon.png"));
    @FXML
    public AnchorPane anchorPane;
    public AnchorPane rightAnchorPane;
    public ListView<Rover> leftListView;
    public ListView<String> rightListView;
    public ListView<String> rightListView1;
    public ImageView mapImageView;
    //public TableView<Rover> rightTableView;
    public Label dataLabel;
    public Label missingDataLabel;
   // public TableColumn<String, String> rightTableViewColumn1;
   // public TableColumn<Rover, String> rightTableViewColumn2;

    //public String selectedRover;
    public int selectedRoverPos = -1;

    int numOfRovers = 0;

    String infileName = "files/saveData.txt";

    int arraySize = 10;

    Button[] roverButtons = new Button[arraySize];
    Rover[] rovers = new Rover[arraySize];

    public void initialize() throws IOException {
        loader();
        mapImageView.setImage(map);
        System.out.println(map.getHeight());
        System.out.println(map.getWidth());
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
                    roverDataUpdater(rovers[numOfRovers-1], infile.next(), true);
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
        if (selectedRoverPos != -1) {
            System.out.println(selectedRoverPos);
            //selectedRover = rovers[num].name;
            roverButtons[selectedRoverPos].setBorder(Border.EMPTY);
        }
        selectedRoverPos = position;
        roverButtons[selectedRoverPos].setLayoutX(roverButtons[selectedRoverPos].getLayoutX()-1);
        roverButtons[selectedRoverPos].setBorder(Border.stroke(Paint.valueOf("#000000")));
        roverButtons[selectedRoverPos].toFront();
        dataDisplayWindow(position);
    }

    public void handleNewRoverButton() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("newRoverMaker.fxml"));
            Parent root = loader.load();
            ControllerNewRover controllerNewRover = loader.getController();
            controllerNewRover.setParentController(this);

            Stage newRoverPage = new Stage();
            newRoverPage.setTitle("Create rover");
            newRoverPage.setScene(new Scene(root));

            newRoverPage.show();
        }
        catch (Exception e){
            System.out.println("New Rover Error");
        }
    }

//    public void handleNewRoverButton() throws IOException {
//        roverMaker("bob", "rover3TestData");
//        roverDataUpdater(rovers[numOfRovers-1], rovers[numOfRovers-1].getDataFile(), false);
//    }

    public void handleChangeMapButton(){
        final FileChooser fileChooser = new FileChooser();
//        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "gif");
        try {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            File file = fileChooser.showOpenDialog(stage);
            //String foundImage = .toString();
            //map = new Image(getClass().getResourceAsStream(foundImage));
            Image tempImage = new Image(file.toURI().toString());
            map = tempImage;
            mapImageView.setImage(map);


            //System.out.println(mapImageView.get);
            //System.out.println(tempImage.getWidth());
        }
        catch (Exception e){
            System.err.println("Map Error");
        }
    }

    public void roverSelectPanel(int position){
        leftListView.getItems().add(rovers[position]);
        leftListView.setCellFactory(lv -> new ListCell<Rover>(){
             @Override
             public void updateItem(Rover rover, boolean empty) {
                   super.updateItem(rover, empty) ;
                   setText(empty ? null : rover.getName());
             }
        });
        //Rover tempRover = new Rover();
        leftListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Rover>() {

            @Override
            public void changed(ObservableValue<? extends Rover> observableValue, Rover rover, Rover t1) {
                if(selectedRoverPos != t1.positionInArray) {
                    int position = leftListView.getSelectionModel().getSelectedItem().positionInArray;
                    handleButtonClick(position);
                }

            }
        });
    }

    public void dataDisplayWindow(int position){
        Rover tempRover = rovers[position];
        rightListView1.getItems().clear();
        rightListView.getItems().clear();
        try {
            missingDataLabel.setVisible(false);
            rightListView.setVisible(true);
            rightListView1.setVisible(true);
            RoverData tempRoverData = tempRover.roverData[rovers[position].dataPosition];
            rightListView1.getItems().addAll("ID", "Battery", "Date", "Time", "X", "Y");
            for (int i = 0; i < tempRoverData.numSensors; i++) {
                rightListView1.getItems().add("Sensor " + (i + 1));
            }
            rightListView1.setMouseTransparent(true);
            rightListView1.setFocusTraversable(false);

            char[] arr = tempRoverData.date.toCharArray();
            char[] arr2 = {arr[6], arr[7], '/', arr[4], arr[5], '/', arr[0], arr[1], arr[2], arr[3]};
            String date = String.valueOf(arr2);

            arr = tempRoverData.time.toCharArray();
            char[] arr3 = {arr[0], arr[1], ':', arr[2], arr[3], ':', arr[4], arr[5]};
            String time = String.valueOf(arr3);

            rightListView.getItems().addAll(tempRoverData.ID,
                    tempRoverData.battery + "%", date, time,
                    String.valueOf(tempRoverData.locationX), String.valueOf(tempRoverData.locationY));

            for (int i = 0; i < tempRoverData.numSensors; i++) {
                rightListView.getItems().add(tempRoverData.sensors[i]);
            }
        }
        catch (Exception e){
            System.out.println("Rover Data Error");
            rightListView.setVisible(false);
            rightListView1.setVisible(false);
            missingDataLabel.setVisible(true);
        }
        dataLabel.setText(tempRover.name);
        rightAnchorPane.setVisible(true);
        rightListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        rightListView1.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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
        roverDataUpdater(rover, file, false);
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

    public void roverDataUpdater(Rover rover, String dataLine, Boolean loadData) throws IOException {
        if(loadData){
            rover.updateValues(dataLine);
        }
        else{
            try {
                String contents = new String(Files.readAllBytes(Paths.get(rover.getDataFile())));
                rover.updateValues(contents);
            }
            catch (Exception e){
                System.out.println("File error");
            }
        }
    }

    public void roverPositionUpdater(){
        double topLeftX = 52.408774623329776;
        double topLeftY = -4.0501845529945575;
        double bottomRightX = 52.40623515020268;
        double bottomRightY = -4.044841592659181;
        double roverX = rovers[0].roverData[rovers[0].dataPosition].locationX;
        double roverY = rovers[0].roverData[rovers[0].dataPosition].locationY;


        double mapX = mapImageView.getFitWidth();
        double mapY = mapImageView.getFitHeight();

        double latLeft = (topLeftX * mapX)/360;
        double logLeft = (topLeftY * mapY)/180;
        double latRight = (bottomRightX * mapX)/360;
        double logRight = (bottomRightY * mapY)/180;
        System.out.println(latRight);
        System.out.println(logRight);
        roverButtons[0].setLayoutX(latRight);
        roverButtons[0].setLayoutY(logRight);

    }
}

