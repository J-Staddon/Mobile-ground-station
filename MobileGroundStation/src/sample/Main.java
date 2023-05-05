package sample;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Objects;

/**
 * To receive and send strings of data to the controller
 *
 * @author Jay Staddon
 * @version 4th May 2023
 */

class DataReceiver extends Task<String> {
    @Override

    /**
     * Reads data and sends it to rover
     */
    protected String call() throws Exception {
        try {
            DatagramSocket datasoc = new DatagramSocket(3000);
            byte[] buff = new byte[1024];
            DatagramPacket dpac = new DatagramPacket(buff, 1024);
            datasoc.receive(dpac); //Wait to receive data packet on port 3000
            System.out.println("Data Received");
            String strn = new String(dpac.getData(), 0, dpac.getLength()); //Sets variable to data
            System.out.println("Data: " + strn);
            datasoc.close();
            return strn; //Returns gathered data
        }
        catch (Exception e){}
        System.out.println("Closing Thread");
        return null;
    }
}

/**
 * Launches the program
 *
 * @author Jay Staddon
 * @version 4th May 2023
 */
public class Main extends Application {

    private Controller controller = new Controller();

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Provides a close menu to the user
     * @param stage The main stage open
     * @throws IOException
     */
    private void close(Stage stage) throws IOException {
        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType("No");
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.NONE, "", yes, no, cancel);

        alert.setTitle("Quitting");
        alert.setHeaderText("Closing Program");
        alert.setContentText("Do you want to save before exiting?");
        controller.alertSetter(alert);
        if (Objects.equals(alert.getResult().getText(), "Yes")){
            controller.saver();
            System.out.println("Data has been saved");
            stage.close();
        }
        else if (Objects.equals(alert.getResult().getText(), "No")){
            System.out.println("Data not saved");
            stage.close();
        }
    }

    /**
     * Starts the main stage#
     * @param primaryStage Receives the existing stage
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        DataService dataService = new DataService();
        dataService.start();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxmlFiles/sample.fxml")); //Sets stage
        Parent root = loader.load();
        controller = loader.getController();

        controller.valueProperty().bind(dataService.valueProperty()); //Connects controllers valueProperty variable and dataService valueProperty variable

        primaryStage.setTitle("Mobile Ground Station");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("images/programIcon.png")));
        primaryStage.setScene(new Scene(root, 1080, 720));
        primaryStage.setMinHeight(300);
        primaryStage.setMinWidth(400);

        //Listens if CTRL is pressed down
        primaryStage.getScene().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.CONTROL) {
                controller.zoomKey = true;
            }
        });

        primaryStage.getScene().setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.CONTROL) {
                controller.zoomKey = false;
            }
        });

        primaryStage.show();

        //When closing call 'close'
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            try {
                close(primaryStage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Returns the controller to the program
     * @return controller
     */
    public Controller getController() {
        return controller;
    }
}
