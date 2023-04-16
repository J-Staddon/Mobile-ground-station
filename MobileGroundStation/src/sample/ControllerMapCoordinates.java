package sample;

import javafx.fxml.FXML;

public class ControllerMapCoordinates {

    @FXML
    private Controller controller;


    public void initialize(){
//        idTextField.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (!newValue.matches("\\d*")) {
//                idTextField.setText(newValue.replaceAll("[^\\d]", ""));
//            }
//        });
    }


    public void setParentController(Controller controller){
        this.controller = controller;
    }
}
