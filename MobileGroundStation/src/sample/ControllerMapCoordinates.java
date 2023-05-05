package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


/**
 * Controls change map stage
 *
 * @author Jay Staddon
 * @version 4th May 2023
 */
public class ControllerMapCoordinates {

    @FXML
    public TextField TLX;
    public TextField TLY;
    public TextField BRX;
    public TextField BRY;
    public Label errorMessage;
    private Controller controller;

    /**
     * Runs when controller is made
     */
    public void initialize(){
        TLX.textProperty().addListener(new boxListener(TLX));
        TLY.textProperty().addListener(new boxListener(TLY));
        BRX.textProperty().addListener(new boxListener(BRX));
        BRY.textProperty().addListener(new boxListener(BRY));
    }

    /**
     * Sets new GPS coordinates
     *
     * @param actionEvent Action
     */
    public void confirmButton(ActionEvent actionEvent){
        //Error checks
        if (!TLX.getText().trim().isEmpty() && !TLY.getText().trim().isEmpty() && !BRX.getText().trim().isEmpty() && !BRY.getText().trim().isEmpty()) {

            try{
                double TLXtemp = Double.parseDouble(TLX.getText());
                double TLYtemp = Double.parseDouble(TLY.getText());
                double BRXtemp = Double.parseDouble(BRX.getText());
                double BRYtemp = Double.parseDouble(BRY.getText());
                controller.setMap(TLXtemp, TLYtemp, BRXtemp, BRYtemp);
                Node n = (Node) actionEvent.getSource();
                Stage stage = (Stage) n.getScene().getWindow();
                stage.close();

            } catch (Exception e){
                errorMessage.setText("Unable to use assigned values");
            }
        }
        else{errorMessage.setText("All boxes require a number");}
    }

    /**
     * Sets to the coordinates to the existing ones
     */
    public void useCurrentCoordinates(){
        TLX.setText(String.valueOf(controller.topLeftX));
        TLY.setText(String.valueOf(controller.topLeftY));
        BRX.setText(String.valueOf(controller.bottomRightX));
        BRY.setText(String.valueOf(controller.bottomRightY));
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


/**
 * Listener for text fields
 *
 * @author Jay Staddon
 * @version 4th May 2023
 */
class boxListener implements ChangeListener<String> {

    private final TextField textField;

    /**
     * Restricts inputs to numbers, dots and dashes
     *
     * @param textField The text field
     */
    public boxListener(TextField textField) {
        this.textField = textField;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^0-9.-]", ""));
            }
    }
}