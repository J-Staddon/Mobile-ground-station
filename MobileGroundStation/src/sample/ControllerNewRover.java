package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;


public class ControllerNewRover {

    @FXML
    public Button createButton;
    public TextField nameTextField;
    public TextField idTextField;
    public AnchorPane anchorPane;
    private Controller controller;

    public void create(ActionEvent actionEvent) throws IOException {
        if (!nameTextField.getText().trim().isEmpty() && !idTextField.getText().trim().isEmpty()) {
            if(nameTextField.getText().length() < 10 && idTextField.getText().length() == 4) {
                for (int i = 0; i < controller.rovers.size(); i++){
                    if(controller.rovers.get(i).getID().equals(idTextField.getText())){
                        break;
                    }
                    else if(i == controller.rovers.size() - 1){
                        controller.roverMaker(nameTextField.getText(), idTextField.getText());
                        Node n = (Node) actionEvent.getSource();
                        Stage stage = (Stage) n.getScene().getWindow();
                        stage.close();
                    }
                }
            }
        }
    }

    public void setParentController(Controller controller){
        this.controller = controller;
    }
}
