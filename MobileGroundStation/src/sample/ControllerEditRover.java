package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.util.Objects;

/**
 * Controls edit rover stage
 *
 * @author Jay Staddon
 * @version 4th May 2023
 */
public class ControllerEditRover {
    @FXML
    public Button saveButton;
    public TextField nameTextField;
    public TextField idTextField;
    public Label errorMessage;
    public AnchorPane anchorPane;
    private Controller controller;

    /**
     * Runs when controller is made
     */
    public void initialize(){
        //Only allow numbers to be inputted int ID
        idTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                idTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    /**
     * Saves new rover name and ID
     *
     * @param actionEvent Action
     */
    public void save(ActionEvent actionEvent) {
        //Error checks
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
                        controller.roverEditor(nameTextField.getText(), idTextField.getText()); //Updates rover name and  ID
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

    /**
     * Deletes a rover
     *
     * @param actionEvent Action
     */
    public void handleDeleteButton(ActionEvent actionEvent) {
        //Warns user they are deleting a rover
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deleting Rover");
        alert.setHeaderText("Deleting Rover");
        alert.setContentText("Are you sure you want to delete this rover and all it's data?");
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        alert.initOwner(stage);
        alert.showAndWait();
        if (Objects.equals(alert.getResult().getText(), "OK")) {
            controller.roverDeleter(controller.selectedRoverPos);
            stage.close();
        }
    }

    /**
     * Sets parent controller
     *
     * @param controller Parent controller
     */
    public void setParentController(Controller controller){
        this.controller = controller;
    }
}
