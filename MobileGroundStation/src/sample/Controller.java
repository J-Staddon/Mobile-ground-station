package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Controller {
    Image roverIcon = new Image(getClass().getResourceAsStream("images/roverIcon.png"));
    Image map = new Image(getClass().getResourceAsStream("images/testMap.jpg"));
    Image tempMap;
    String mapFileLocation;
    String tempMapFileLocation;

    @FXML
    public AnchorPane anchorPane;
    public AnchorPane leftAnchorPane;
    public AnchorPane rightAnchorPane;
    public ScrollPane scrollPane;
    public SplitPane splitPane;
    public ListView<Rover> leftListView;
    public ListView<String> rightListView;
    public ListView<String> rightListView1;
    public ImageView mapImageView;
    public ImageView batteryImageView;
    public Label dataLabel;
    public Label missingDataLabel;
    public Label dateLabel;
    public Label timeLabel;
    public Label batteryLabel;
    public Label IDLabel;
    public CheckBox showLatestDataLabel;
    public Button forwardDataButton;
    public Button backDataButton;

    double topLeftX;
    double topLeftY;
    double bottomRightX;
    double bottomRightY;

    public int selectedRoverPos = -1;
    private int selectedRoverDataPos = -1;
    private float mapScale = 1;
    private String foundID = "";
    private final String infileName = "files/saveData.txt";
    private boolean deleting = false;
    private boolean showLatestData = true;
    private boolean pause = false;
    public boolean zoomKey = false;
    private Rover currentView;
    ArrayList<Rover> rovers = new ArrayList<Rover>();
    ArrayList<Button> roverButtons = new ArrayList<Button>();
    ArrayList<String> ignoreIDList = new ArrayList<String>();
    List<Line> lines = new ArrayList<>();

    private StringProperty valueProperty = new SimpleStringProperty("");

    public StringProperty valueProperty() { return this.valueProperty; }

    public void initialize() {
        loader();
        setMap(topLeftX, topLeftY, bottomRightX, bottomRightY);
        allRoverRotationUpdater();

        resizeRightAnchorPane(0, 0);

        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (zoomKey) {
                double deltaY = event.getDeltaY();
                if (deltaY < 0) {
                    handleZoomOut();
                } else {
                    handleZoomIn();
                }
                event.consume();
            }
                });

        valueProperty.addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                try {roverDataUpdater(newValue);}
                catch (IOException e) {throw new RuntimeException(e);}
            }
        });
    }

    private void loader(){
        System.out.println(new File("saveData").getAbsolutePath());
        try (Scanner infile = new Scanner(new FileReader(infileName));) {
            infile.useDelimiter("\r?\n|\r");
            try{
                tempMapFileLocation = infile.next();
                tempMap = new Image(tempMapFileLocation);
            } catch (Exception e){
                tempMap = new Image(getClass().getResourceAsStream("images/testMap.jpg"));
                System.err.println("Could not find a saved map");
            }
            topLeftX = infile.nextDouble();
            topLeftY = infile.nextDouble();
            bottomRightX = infile.nextDouble();
            bottomRightY = infile.nextDouble();
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
            outfile.println(mapFileLocation);
            outfile.println(topLeftX);
            outfile.println(topLeftY);
            outfile.println(bottomRightX);
            outfile.println(bottomRightY);
            outfile.println(rovers.size());
            for (Rover rover : rovers) {
                rover.saver(outfile);
            }
        }
    }

    private int findRoverPos(String ID){

        for(int i = 0; 0 < rovers.size(); i++){
            try {
                if (rovers.get(i).getID().equals(ID)) {
                    return i;
                }
            }catch (Exception e){
                for (int x = 0; x < ignoreIDList.size(); x++){
                    if (ignoreIDList.get(x).equals(ID)){
                        return -2;
                    }
                }
                return -1;
            }
        }
        return -1;
    }

    public void handleButtonClick(int pos) {
        if (!pause) {
            if (selectedRoverPos < rovers.size() && selectedRoverPos != -1) {
                roverButtons.get(selectedRoverPos).setBorder(Border.EMPTY);
            }
            selectedRoverPos = pos;
            selectedRoverDataPos = rovers.get(selectedRoverPos).getDataPosition();
            roverButtons.get(selectedRoverPos).setBorder(Border.stroke(Paint.valueOf("#000000")));
            roverButtons.get(selectedRoverPos).toFront();
            linePath(pos);
            dataDisplayWindow(selectedRoverPos, rovers.get(selectedRoverPos).getDataPosition());
        }
    }

    private void stageSetter(Parent root, Stage newStage) {
        newStage.setScene(new Scene(root));
        newStage.toFront();

        Stage window = (Stage) anchorPane.getScene().getWindow();
        newStage.initOwner(window);
        newStage.show();
        newStage.setX(window.getX()+((window.getWidth() - newStage.getWidth()) / 2));
        newStage.setY(window.getY()+((window.getHeight() - newStage.getHeight()) / 2));

        Stage stage;
        stage = newStage;
        newStage.close();
        stage.showAndWait();
    }

    public void alertSetter(Alert alert) {
        Stage window = (Stage) anchorPane.getScene().getWindow();
        alert.initOwner(window);
        alert.setX(window.getX()+((window.getWidth() - alert.getWidth()) / 2));
        alert.setY(window.getY()+((window.getHeight() - alert.getHeight()) / 2));
        alert.showAndWait();
    }

    public void handleNewRoverButton(){
        if (!pause) {
            pause = true;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("fxmlFiles/sampleNewRover.fxml"));
                Parent root = loader.load();
                ControllerNewRover controllerNewRover = loader.getController();
                controllerNewRover.setParentController(this);
                controllerNewRover.idTextField.setText(foundID);

                Stage newRoverPage = new Stage();
                newRoverPage.setTitle("Create rover");
                newRoverPage.setResizable(false);
                stageSetter(root, newRoverPage);
                foundID = "";

            } catch (Exception e) {
                System.err.println("New Rover Error");
            }
            pause = false;
        }
    }

    public void handleEditButton() {
        if (!pause) {
            pause = true;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("fxmlFiles/sampleEditRover.fxml"));
                Parent root = loader.load();
                ControllerEditRover controllerEditRover = loader.getController();
                controllerEditRover.setParentController(this);
                controllerEditRover.nameTextField.setText(rovers.get(selectedRoverPos).getName());
                controllerEditRover.idTextField.setText(rovers.get(selectedRoverPos).getID());

                Stage editRoverPage = new Stage();
                editRoverPage.setTitle("Edit rover");
                editRoverPage.setResizable(false);
                stageSetter(root, editRoverPage);
            } catch (Exception e) {
                System.err.println("Edit Rover Error");
            }
            pause = false;
        }
    }

    public void handleChangeMapButton(){

        if (!pause) {
            final FileChooser fileChooser = new FileChooser();
            try {
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
                Stage stage = (Stage) anchorPane.getScene().getWindow();
                File file = fileChooser.showOpenDialog(stage);
                tempMapFileLocation = file.getPath();
                tempMap = new Image(file.toURI().toString());

                pause = true;
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("fxmlFiles/sampleMapCoordinates.fxml"));
                    Parent root = loader.load();
                    ControllerMapCoordinates controllerMapCoordinates = loader.getController();
                    controllerMapCoordinates.setParentController(this);

                    Stage changeMapPage = new Stage();
                    changeMapPage.setTitle("Map Coordinates");
                    changeMapPage.setResizable(false);
                    stageSetter(root, changeMapPage);

                } catch (Exception e) {
                    System.err.println("Map Coordinates Error");
                }

            } catch (Exception e) {
                System.err.println("Map Error");
            }
        pause = false;


        }
    }

    public void handleCompareDataButton() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxmlFiles/sampleCompareData.fxml"));
            Parent root = loader.load();
            ControllerCompareData controllerCompareData = loader.getController();
            controllerCompareData.setParentController(this);

            Stage compareDataPage = new Stage();
            compareDataPage.setTitle("Compare Data");
            compareDataPage.setMinHeight(300);
            compareDataPage.setMinWidth(600);
            stageSetter(root, compareDataPage);

        } catch (Exception e) {
            System.err.println("Compare Data Error");

        }
    }

    public void setMap(double TLX, double TLY, double BRX, double BRY){
        map = tempMap;
        mapFileLocation = tempMapFileLocation;
        topLeftX = TLX;
        topLeftY = TLY;
        bottomRightX = BRX;
        bottomRightY = BRY;
        mapImageView.setImage(map);
        System.out.println(map.getHeight());
        System.out.println(map.getWidth());
        mapScale = 1;
        mapImageView.setFitHeight(map.getHeight()*mapScale);
        mapImageView.setFitWidth(map.getWidth()*mapScale);
        allRoverPositionUpdater();
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
        allRoverPositionUpdater();
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
        allRoverPositionUpdater();
    }

    public void handleBackDataButton(){
        if (!showLatestData) {
            int tempSelectedRoverDataPos = selectedRoverDataPos;
            if (selectedRoverDataPos == 0) {
                tempSelectedRoverDataPos = 99;
            } else {
                tempSelectedRoverDataPos--;
            }
            if (rovers.get(selectedRoverPos).getRoverData()[tempSelectedRoverDataPos] != null && rovers.get(selectedRoverPos).getDataPosition() != tempSelectedRoverDataPos) {
                selectedRoverDataPos = tempSelectedRoverDataPos;
                dataDisplayWindow(selectedRoverPos, selectedRoverDataPos);
            }
        }
    }

    public void handleForwardDataButton() {
        if (!showLatestData) {
            if (selectedRoverDataPos != rovers.get(selectedRoverPos).getDataPosition()) {
                int tempSelectedRoverDataPos = selectedRoverDataPos;
                if (selectedRoverDataPos == 99) {
                    tempSelectedRoverDataPos = 0;
                } else {
                    tempSelectedRoverDataPos++;
                }
                selectedRoverDataPos = tempSelectedRoverDataPos;
                dataDisplayWindow(selectedRoverPos, selectedRoverDataPos);
            }
        }
    }

    public void handleShowLatestData(){
        if(showLatestData){
            showLatestData = false;
        }
        else{
            showLatestData = true;
            selectedRoverDataPos = rovers.get(selectedRoverPos).getDataPosition();
            dataDisplayWindow(selectedRoverPos, selectedRoverDataPos);
        }
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
                    if (!pause) {
                        handleButtonClick(findRoverPos(leftListView.getSelectionModel().getSelectedItem().getID()));
                        currentView = leftListView.getSelectionModel().getSelectedItem();
                    }
                }
            }
        });
    }

    private void resizeRightAnchorPane(int min, int max){
        rightAnchorPane.setMinWidth(min);
        rightAnchorPane.setMaxWidth(max);
    }

    public void dataDisplayWindow(int pos, int dataPos){
        ScrollBar scrollBar1 = (ScrollBar) rightListView.lookup(".scroll-bar:vertical");
        ScrollBar scrollBar2 = (ScrollBar) rightListView1.lookup(".scroll-bar:vertical");
        scrollBar1.valueProperty().bindBidirectional(scrollBar2.valueProperty());

        Rover tempRover = rovers.get(pos);
        leftListView.scrollTo(tempRover);
        leftListView.getSelectionModel().select(tempRover);
        rightListView1.getItems().clear();
        rightListView.getItems().clear();
        resizeRightAnchorPane(250, 250);
        try {
            missingDataLabel.setVisible(false);
            rightListView.setVisible(true);
            rightListView1.setVisible(true);
            dateLabel.setVisible(true);
            timeLabel.setVisible(true);
            batteryLabel.setVisible(true);
            forwardDataButton.setVisible(true);
            backDataButton.setVisible(true);
            batteryImageView.setVisible(true);
            showLatestDataLabel.setVisible(true);
            RoverData tempRoverData = tempRover.getRoverData()[dataPos];
            rightListView1.getItems().addAll("ID", "Battery", "Date", "Time", "Latitude", "Longitude", "Direction");
            for (int i = 0; i < tempRoverData.getSensors().size(); i++) {
                rightListView1.getItems().add("Sensor " + (i + 1));
            }


            String date = tempRoverData.getDateFormatted();
            String time = tempRoverData.getTimeFormatted();

            IDLabel.setText(tempRover.getID());
            dateLabel.setText(date);
            timeLabel.setText(time);
            batteryLabel.setText(tempRoverData.getBattery() + "%");

            rightListView.getItems().addAll(tempRover.getID(),
                    tempRoverData.getBattery() + "%", date, time, String.valueOf(tempRoverData.getLocationX()), String.valueOf(tempRoverData.getLocationY()), String.valueOf(tempRoverData.getRotation()));

            for (int i = 0; i < tempRoverData.getSensors().size(); i++) {
                rightListView.getItems().add(tempRoverData.getSensors().get(i));
            }

            if(!tempRoverData.getMessage().equals("")) {
                rightListView1.getItems().add("Message");
                rightListView.getItems().add(tempRoverData.getMessage());
            }
        }
        catch (Exception e){
            System.err.println("Rover Display Data Error");
            rightListView.setVisible(false);
            rightListView1.setVisible(false);
            dateLabel.setVisible(false);
            timeLabel.setVisible(false);
            batteryLabel.setVisible(false);
            forwardDataButton.setVisible(false);
            backDataButton.setVisible(false);
            batteryImageView.setVisible(false);
            showLatestDataLabel.setVisible(false);
            missingDataLabel.setVisible(true);
        }
        dataLabel.setText(tempRover.getName());
        IDLabel.setText(tempRover.getID());
        rightAnchorPane.setVisible(true);
        rightListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        rightListView1.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void roverDeleter(int tempSelectedRoverPos){
        deleting = true;
        leftListView.getSelectionModel().clearSelection();
        rightAnchorPane.setVisible(false);
        leftListView.getItems().remove(tempSelectedRoverPos);
        anchorPane.getChildren().remove(roverButtons.get(tempSelectedRoverPos));
        roverButtons.remove(tempSelectedRoverPos);
        rovers.remove(tempSelectedRoverPos);
        anchorPane.getChildren().removeAll(lines);
        lines.clear();
        resizeRightAnchorPane(0, 0);
        deleting = false;
    }

    public void roverEditor(String name, String ID){
        rovers.get(selectedRoverPos).setName(name);
        rovers.get(selectedRoverPos).setID(ID);
        roverButtons.get(selectedRoverPos).setId(ID);
        dataDisplayWindow(selectedRoverPos, selectedRoverDataPos);
        leftListView.refresh();

    }

    public void roverMaker(String name, String ID) {
        Rover rover = new Rover();
        rover.roverInsulator(name, ID);
        rovers.add(rover);
        roverButtonMaker(ID);
        roverSelectPanel(findRoverPos(ID));
    }

    private void roverButtonMaker(String ID){
        Button button;
        button = new Button();
        button.setId(ID);
        button.setOnAction(e -> handleButtonClick(findRoverPos(button.getId())));

        button.setPrefWidth(30);
        button.setPrefHeight(30);
        ImageView view = new ImageView(roverIcon);
        view.setFitWidth(button.getPrefWidth());
        view.setFitHeight(button.getPrefHeight());

        button.setGraphic(view);
        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        button.setBackground(null);

        anchorPane.getChildren().add(button);

        button.setLayoutX(-100);
        button.setLayoutY(-100);
        roverButtons.add(button);
    }

    private void roverDataUpdater(String data) throws IOException {
        RoverData roverData = new RoverData();
        try{
            roverData.addData(data);
            int pos = findRoverPos(roverData.getID());
            if (pos == -1){
                ButtonType yes = new ButtonType("Yes");
                ButtonType no = new ButtonType("No");
                Alert alert = new Alert(Alert.AlertType.NONE, "", yes, no);
                alert.setTitle("New Rover Found!");
                alert.setHeaderText("A new rover has been discovered: #" + roverData.getID());
                alert.setContentText("Do you want to add it?\n\nIf you press no this rover will be ignored unless manually added");
                alertSetter(alert);
                if (Objects.equals(alert.getResult().getText(), "Yes")){
                    foundID = roverData.getID();
                    handleNewRoverButton();
                    roverDataUpdater(data);
                }
                if (Objects.equals(alert.getResult().getText(), "No")){
                    ignoreIDList.add(roverData.getID());
                }

            }
            else {
                if ((showLatestData && selectedRoverPos == pos) || rovers.get(pos).getDataPosition() == -1) {
                    rovers.get(pos).updateValues(roverData);
                    selectedRoverDataPos = rovers.get(pos).getDataPosition();
                    handleButtonClick(pos);
                }
                else{
                    rovers.get(pos).updateValues(roverData);
                }
                roverPositionUpdater(pos);
                roverRotationUpdater(pos);
            }
        }
        catch (Exception e){
            System.err.println("Update Rover Data Error");
        }
    }

    private void allRoverRotationUpdater(){
        for(int i = 0; i < rovers.size(); i++){
            roverRotationUpdater(i);
        }
    }

    private void roverRotationUpdater(int pos){
        ImageView view = new ImageView(roverIcon);
        view.setFitWidth(roverButtons.get(pos).getPrefWidth());
        view.setFitHeight(roverButtons.get(pos).getPrefHeight());
        view.setRotate(rovers.get(pos).getRoverData()[rovers.get(pos).getDataPosition()].getRotation());
        roverButtons.get(pos).setGraphic(view);
    }


    private void allRoverPositionUpdater(){
        for(int i = 0; i < rovers.size(); i++){
            roverPositionUpdater(i);
        }
    }

    private void roverPositionUpdater(int pos) {
        try {
            roverButtons.get(pos).setLayoutX((positionFinderX(pos, -1)) - 24);
            roverButtons.get(pos).setLayoutY((positionFinderY(pos, -1)) - 23);
        } catch (Exception e){
            System.err.println("No location found for " + rovers.get(pos).getName());
        }

        if (selectedRoverPos == pos){
            linePath(pos);
        }
    }

    private double positionFinderX(int pos, int tempDataPosition) {
        double roverX;
        if (tempDataPosition == -1) {
            roverX = rovers.get(pos).getRoverData()[rovers.get(pos).getDataPosition()].getLocationX();
        } else {
            roverX = rovers.get(pos).getRoverData()[tempDataPosition].getLocationX();
        }
        double diff;
        double roverPos;
        if (topLeftX < bottomRightX) {
            diff = topLeftX - bottomRightX;
            roverPos = topLeftX - roverX;
        } else {
            diff = bottomRightX - topLeftX;
            roverPos = bottomRightX - roverX;
        }
        double mapX = map.getWidth();
        double scale = mapX / diff;
        return (roverPos * scale) * mapScale;
    }

    private double positionFinderY(int pos, int tempDataPosition){
        double roverY;
        if (tempDataPosition == -1) {
            roverY = rovers.get(pos).getRoverData()[rovers.get(pos).getDataPosition()].getLocationY();
        }
        else{
            roverY = rovers.get(pos).getRoverData()[tempDataPosition].getLocationY();
        }
        double diff;
        double roverPos;
        if (topLeftY > bottomRightY) {
            diff = topLeftY - bottomRightY;
            roverPos = topLeftY - roverY;
        } else {
            diff = bottomRightY - topLeftY;
            roverPos = bottomRightY - roverY;
        }
        double mapY = map.getHeight();
        double scale = mapY / diff;
        return (roverPos * scale) * mapScale;
    }

    public void linePath(int pos) {
        int tempDataPosition = rovers.get(pos).getDataPosition();
        anchorPane.getChildren().removeAll(lines);
        lines.clear();
        if(tempDataPosition != -1) {
            double newValueX;
            double newValueY;
            double oldValueX = positionFinderX(pos, tempDataPosition);
            double oldValueY = positionFinderY(pos, tempDataPosition);

            for (int i = 0; i < 99; i++) {

                tempDataPosition--;
                if (tempDataPosition < 0) {
                    tempDataPosition = 99;
                } else if (tempDataPosition > 99) {
                    tempDataPosition = 0;
                }

                if (rovers.get(pos).getRoverData()[tempDataPosition] == null) {
                    break;
                }

                newValueX = positionFinderX(pos, tempDataPosition);
                newValueY = positionFinderY(pos, tempDataPosition);

                Line line = new Line(oldValueX, oldValueY, newValueX, newValueY);
                line.getStrokeDashArray().addAll(1d, 4d);
                line.setStrokeWidth(2);
                lines.add(line);

                oldValueX = newValueX;
                oldValueY = newValueY;
            }
            anchorPane.getChildren().addAll(lines);
        }
    }
}

