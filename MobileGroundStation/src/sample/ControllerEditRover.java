package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;


public class ControllerEditRover {
    @FXML
    public Button saveButton;
    public TextField nameTextField;
    public TextField idTextField;
    public Label errorMessage;
    public AnchorPane anchorPane;
    private Controller controller;

    public void initialize(){

        idTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                idTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public void save(ActionEvent actionEvent) throws IOException {
        if (!nameTextField.getText().trim().isEmpty() && !idTextField.getText().trim().isEmpty()) {
            if(nameTextField.getText().length() < 10) {
                if(idTextField.getText().length() == 4) {
                    boolean IDinUse = false;
                    for (int i = 0; i < controller.rovers.size(); i++) {

                        if (controller.rovers.get(i).getID().equals(idTextField.getText())) {
                            if(!controller.rovers.get(i).getID().equals(controller.rovers.get(controller.selectedRoverPos).getID())){
                                errorMessage.setText("ID already in use");
                                IDinUse = true;
                                break;
                            }
                        }
                    }
                    if(!IDinUse) {
                        controller.roverUpdater(nameTextField.getText(), idTextField.getText());
                        Node n = (Node) actionEvent.getSource();
                        Stage stage = (Stage) n.getScene().getWindow();
                        stage.close();
                    }
                }
                else {errorMessage.setText("ID must be exactly 4 characters long");}
            }
            else{errorMessage.setText("Name must be less than 10 characters");}
        }
        else{errorMessage.setText("Both boxes require text");}
    }

    public void handleDeleteButton(ActionEvent actionEvent){
        controller.roverDeleter(controller.selectedRoverPos);
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }

    public void setParentController(Controller controller){
        this.controller = controller;
    }
}
