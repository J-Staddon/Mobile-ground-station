package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ControllerNewRover {

    @FXML
    public Button fileButton;
    public Button createButton;
    public TextField fileTextField;
    public TextField nameTextField;
    public AnchorPane anchorPane;

    private Controller controller;

    public void initialize(){

        fileButton.setOnAction(e -> findFile());
    }

    public void create(ActionEvent actionEvent) throws IOException {
        //if (fileTextField)
        if (!nameTextField.getText().trim().isEmpty() && !fileTextField.getText().trim().isEmpty()) {
            controller.roverMaker(nameTextField.getText(), fileTextField.getText());
            Node n = (Node) actionEvent.getSource();
            Stage stage = (Stage) n.getScene().getWindow();
            stage.close();
        }
        //System.out.println("lose");

       // return null;
    }

    @FXML
    private void findFile(){
        File file = fileBrowser();
        if(file != null){
            fileTextField.setText(file.getAbsolutePath());
        }
    }

    public File fileBrowser(){
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("files"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        return fileChooser.showOpenDialog(stage);
    }

    public void setParentController(Controller controller){
        this.controller = controller;
    }

}
