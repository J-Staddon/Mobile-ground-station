package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Controller {
    Image map = new Image(getClass().getResourceAsStream("images/testMap.jpg"));
    Image roverIcon = new Image(getClass().getResourceAsStream("images/roverIcon.png"));

    @FXML
    public AnchorPane anchorPane;
    public AnchorPane rightAnchorPane;
    public ListView<Rover> leftListView;
    public ListView<String> rightListView;
    public ListView<String> rightListView1;
    public ImageView mapImageView;
    public Label dataLabel;
    public Label missingDataLabel;

    public int selectedRoverPos = -1;
    String infileName = "files/saveData.txt";
    public boolean deleting = false;
    public Rover currentView;
    ArrayList<Rover> rovers = new ArrayList<Rover>();
    ArrayList<Button> roverButtons = new ArrayList<Button>();
    public float mapScale = 1;


    public void initialize() throws IOException {
        loader();
        setMap();
        rightListView1.setMouseTransparent(true);
        rightListView1.setFocusTraversable(false);
        roverDataUpdaterV2();
        roverPositionUpdater();
    }

    private void loader() throws IOException, FileNotFoundException {
        System.out.println(new File("saveData").getAbsolutePath());
        try (Scanner infile = new Scanner(new FileReader(infileName));) {
            infile.useDelimiter("\r?\n|\r");

            int numOfRoversToAdd = infile.nextInt();
            for (int i = 0; numOfRoversToAdd > i; i++){
                String name = infile.next();
                String ID = infile.next();
                roverMaker(name, ID);
                for(int x = 0; x < 100; x++){
                    RoverData data = new RoverData();
                    if (!data.addData(infile.next())) {
                        rovers.get(findRoverPos(ID)).updateValues(data);
                    }
                }
            }
        }
        catch(Exception e){
            System.err.println("Load Error");
        }
    }

    public void saver() throws IOException {
        try (FileWriter fw = new FileWriter(infileName);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter outfile = new PrintWriter(bw);) {
            outfile.println(rovers.size());
            for (Rover rover : rovers) {
                rover.saver(outfile);
            }
        }
    }

    private int findRoverPos(String ID){

        for(int i = 0; 0 < rovers.size(); i++){
            if (rovers.get(i).getID().equals(ID)){
                return i;
            }
        }
        return -1;
    }

    public void handleButtonClick(int position){

        if (selectedRoverPos < rovers.size() && selectedRoverPos != -1) {
            System.out.println(selectedRoverPos);
            roverButtons.get(selectedRoverPos).setBorder(Border.EMPTY);
        }
        selectedRoverPos = position;
        roverButtons.get(selectedRoverPos).setBorder(Border.stroke(Paint.valueOf("#000000")));
        roverButtons.get(selectedRoverPos).toFront();
        dataDisplayWindow(selectedRoverPos);
    }

    public void handleNewRoverButton() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sampleNewRover.fxml"));
            Parent root = loader.load();
            ControllerNewRover controllerNewRover = loader.getController();
            controllerNewRover.setParentController(this);

            Stage newRoverPage = new Stage();
            newRoverPage.setTitle("Create rover");
            newRoverPage.setScene(new Scene(root));
            newRoverPage.setAlwaysOnTop(true);
            newRoverPage.setResizable(false);

            newRoverPage.show();
        }
        catch (Exception e){
            System.err.println("New Rover Error");
        }
    }

    public void handleChangeMapButton(){
        final FileChooser fileChooser = new FileChooser();
        try {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            File file = fileChooser.showOpenDialog(stage);
            map = new Image(file.toURI().toString());
            setMap();
        }
        catch (Exception e){
            System.err.println("Map Error");
        }
    }

    public void setMap(){
        mapImageView.setImage(map);
        System.out.println(map.getHeight());
        System.out.println(map.getWidth());
        mapScale = 1;
        mapImageView.setFitHeight(map.getHeight()*mapScale);
        mapImageView.setFitWidth(map.getWidth()*mapScale);
    }

    public void handleZoomIn(){
        if(mapScale < 0.5f) {
            mapScale = mapScale + 0.1f;
            mapImageView.setFitHeight(map.getHeight() * mapScale);
            mapImageView.setFitWidth(map.getWidth() * mapScale);
        }
        else if(mapScale < 5) {
            mapScale = mapScale + 0.5f;
            mapImageView.setFitHeight(map.getHeight() * mapScale);
            mapImageView.setFitWidth(map.getWidth() * mapScale);
        }
        else{
            System.out.println("Max zoom reached");
        }
        roverPositionUpdater();
    }

    public void handleZoomOut(){
        if(mapScale > 0.5f) {
            mapScale = mapScale - 0.5f;
            mapImageView.setFitHeight(map.getHeight() * mapScale);
            mapImageView.setFitWidth(map.getWidth() * mapScale);
        }
        else if(mapScale > 0.2f) {
            mapScale = mapScale - 0.1f;
            mapImageView.setFitHeight(map.getHeight() * mapScale);
            mapImageView.setFitWidth(map.getWidth() * mapScale);
        }
        else{
            System.out.println("Minimum zoom reached");
        }
        roverPositionUpdater();
    }

    public void handleEditButton(){
        int tempSelectedRoverPos = selectedRoverPos;
        //rovers[tempSelectedRoverPos]
    }

    public void roverSelectPanel(int position){
        leftListView.getItems().add(rovers.get(position));
        leftListView.setCellFactory(lv -> new ListCell<Rover>(){
             @Override
             public void updateItem(Rover rover, boolean empty) {
                   super.updateItem(rover, empty) ;
                   setText(empty ? null : rover.getName());
             }
        });

        leftListView.getSelectionModel().selectedItemProperty().addListener((observableValue, rover, target) -> {
            if (!deleting) {
                if (currentView != target) {
                    handleButtonClick(findRoverPos(leftListView.getSelectionModel().getSelectedItem().getID()));
                    currentView = leftListView.getSelectionModel().getSelectedItem();
                }
            }
        });
    }

    public void dataDisplayWindow(int position){
        Rover tempRover = rovers.get(position);
        rightListView1.getItems().clear();
        rightListView.getItems().clear();
        try {
            missingDataLabel.setVisible(false);
            rightListView.setVisible(true);
            rightListView1.setVisible(true);
            RoverData tempRoverData = tempRover.roverData[rovers.get(position).dataPosition];
            rightListView1.getItems().addAll("ID", "Battery", "Date", "Time", "X", "Y");
            for (int i = 0; i < tempRoverData.sensors.size(); i++) {
                rightListView1.getItems().add("Sensor " + (i + 1));
            }

            char[] arr = tempRoverData.date.toCharArray();
            char[] arr2 = {arr[6], arr[7], '/', arr[4], arr[5], '/', arr[0], arr[1], arr[2], arr[3]};
            String date = String.valueOf(arr2);

            arr = tempRoverData.time.toCharArray();
            char[] arr3 = {arr[0], arr[1], ':', arr[2], arr[3], ':', arr[4], arr[5]};
            String time = String.valueOf(arr3);

            rightListView.getItems().addAll(tempRover.ID,
                    tempRoverData.battery + "%", date, time, String.valueOf(tempRoverData.locationX), String.valueOf(tempRoverData.locationY));

            for (int i = 0; i < tempRoverData.sensors.size(); i++) {
                rightListView.getItems().add(tempRoverData.sensors.get(i));
            }
        }
        catch (Exception e){
            System.err.println("Rover Display Data Error");
            rightListView.setVisible(false);
            rightListView1.setVisible(false);
            missingDataLabel.setVisible(true);
        }
        dataLabel.setText(tempRover.name);
        rightAnchorPane.setVisible(true);
        rightListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        rightListView1.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }



    public void roverDeleter(){
        deleting = true;
        int tempSelectedRoverPos = selectedRoverPos;
        leftListView.getSelectionModel().clearSelection();
        rightAnchorPane.setVisible(false);
        leftListView.getItems().remove(tempSelectedRoverPos);
        anchorPane.getChildren().remove(roverButtons.get(tempSelectedRoverPos));
        roverButtons.remove(tempSelectedRoverPos);
        rovers.remove(tempSelectedRoverPos);
        deleting = false;
    }


    public void roverMaker(String name, String ID) throws IOException {

        Rover rover = new Rover();
        //numOfRovers++;
        rover.roverInsulator(name, ID);
        //roverDataUpdater(rover, file, false);
        //roverDataUpdaterV2();
        rovers.add(rover);
        roverButtonMaker(name, ID);
        roverSelectPanel(findRoverPos(ID));

    }

    private void roverButtonMaker(String name, String ID){
        Button button;
        button = new Button(name);
        button.setId(ID);
        button.setOnAction(e -> handleButtonClick(findRoverPos(button.getId())));

        button.setPrefWidth(50);
        button.setPrefHeight(50);
        ImageView view = new ImageView(roverIcon);
        view.setFitWidth(button.getPrefWidth());
        view.setFitHeight(button.getPrefHeight());

        button.setGraphic(view);
        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        button.setBackground(null);

        anchorPane.getChildren().add(button);

        button.setLayoutX(100);
        button.setLayoutY(200);
        roverButtons.add(button);
    }

    public void roverDataUpdaterV2() throws IOException {
        RoverData data = new RoverData();
        try (Scanner infile = new Scanner(new FileReader("files/rover1TestData.txt"));) {
            data.addData(infile.nextLine());
            int pos = findRoverPos(data.getID());
            rovers.get(pos).updateValues(data);
        }
        catch (Exception e){
            System.err.println("Update Rover Data Error");
        }
    }


    public void roverPositionUpdater() {

        for (int i = 0; i < rovers.size(); i++) {
            double topLeftX = 52.408774623329776;
            double topLeftY = -4.0501845529945575;
            double bottomRightX = 52.40623515020268;
            double bottomRightY = -4.044841592659181;
            try {
                double roverX = rovers.get(i).roverData[rovers.get(i).dataPosition].locationX;
                double roverY = rovers.get(i).roverData[rovers.get(i).dataPosition].locationY;


                double diff;
                double roverPos;
                if (topLeftX > bottomRightX) {
                    diff = topLeftX - bottomRightX;
                    roverPos = topLeftX - roverX;
                } else {
                    diff = bottomRightX - topLeftX;
                    roverPos = bottomRightX - roverX;
                }

                double mapX = map.getWidth();

                double scale = mapX / diff;
                roverButtons.get(i).setLayoutX(((roverPos * scale) * mapScale) - 25);


                if (topLeftY > bottomRightY) {
                    diff = topLeftY - bottomRightY;
                    roverPos = topLeftY - roverY;
                } else {
                    diff = bottomRightY - topLeftY;
                    roverPos = bottomRightY - roverY;
                }

                double mapY = map.getHeight();

                scale = mapY / diff;
                roverButtons.get(i).setLayoutY(((roverPos * scale) * mapScale) - 25);
            }
            catch (Exception e){
                System.err.println("No location found for " + rovers.get(i).getName());
            }
        }
    }
}

