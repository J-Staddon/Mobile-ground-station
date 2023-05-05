package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Controls new rover stage
 *
 * @author Jay Staddon
 * @version 4th May 2023
 */
public class ControllerNewRover {

    @FXML
    public Button createButton;
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
     * Creates a new rover
     *
     * @param actionEvent Action
     */
    public void create(ActionEvent actionEvent) {
        //Error checks
        if (!nameTextField.getText().trim().isEmpty() && !idTextField.getText().trim().isEmpty()) {
            if(nameTextField.getText().length() < 10) {
                if(idTextField.getText().length() == 4) {
                    boolean IDinUse = false;
                    for (int i = 0; i < controller.rovers.size(); i++) {
                        if (controller.rovers.get(i).getID().equals(idTextField.getText())) {
                            errorMessage.setText("ID already in use");
                            IDinUse = true;
                            break;
                        }
                    }
                    if (!IDinUse) {
                        controller.roverMaker(nameTextField.getText(), idTextField.getText()); //Make new rover
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
     * Sets parent controller
     *
     * @param controller Parent controller
     */
    public void setParentController(Controller controller){
        this.controller = controller;
    }
}
