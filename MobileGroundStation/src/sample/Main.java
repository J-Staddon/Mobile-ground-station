package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {


    public static void main(String[] args) {
        launch(args);
    }


    public void close(Stage stage, Controller controller) throws IOException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.NO);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", yes, no);

        alert.setTitle("Quitting");
        alert.setHeaderText("Closing Program");
        alert.setContentText("Do you want to save before exiting?");

        if (alert.showAndWait().orElse(yes) == yes){
           // controller.saver();
            System.out.println("Data has been saved");
            stage.close();
        }
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
//        Receiver receiver = new Receiver();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Controller controller = loader.getController();
        primaryStage.setTitle("Mobile Ground Station");
        primaryStage.setScene(new Scene(root, 1080, 720));
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            try {
                close(primaryStage, controller);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
