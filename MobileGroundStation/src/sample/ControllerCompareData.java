package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

public class ControllerCompareData {
    @FXML
    public Button saveButton;
    public TextField nameTextField;
    public TextField idTextField;
    public Label errorMessage;
    public AnchorPane anchorPane;
    public MenuButton roverMenu;
    public TableView tableOfData;
    private Controller controller;

    public void initialize(){
        List<MenuItem> menuItemList = new ArrayList<>();
        for(int i = 0; i < controller.rovers.size(); i++) {
            MenuItem menuItem = new MenuItem(controller.rovers.get(i).name);
            menuItemList.add(menuItem);
        }
       // MenuItem menuItem = new MenuItem();
        roverMenu.getItems().addAll(menuItemList);
    }



    public void setParentController(Controller controller){
        this.controller = controller;
    }
}
