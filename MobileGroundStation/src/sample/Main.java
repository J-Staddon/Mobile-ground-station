package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


public class Main extends Application {

    Button button;
    //Controller controller;
    String infileName = "files/saveData";

/*
    private void loader() throws IOException, FileNotFoundException {
        System.out.println(new File("saveData").getAbsolutePath());
        try (Scanner infile = new Scanner(new FileReader(infileName));) {
            infile.useDelimiter("\r?\n|\r");

            int numOfRovers = infile.nextInt();
            for (int i = 0; numOfRovers > i; i++){
                String name = infile.next();
                String fileName = infile.next();
                controller.roverMaker(name, fileName);
            }
        }
        catch(Exception e){
            System.out.println("File not found");
        }
    }
*/
    private void updater(){

    }



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //controller = new Controller();

        //FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        //loader.setController(controller);
        //loader.setLocation(getClass().getResource("sample.fxml"));
        //Parent root = loader.load();

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        primaryStage.setTitle("Mobile Ground Station");
        primaryStage.setScene(new Scene(root, 1080, 720));
        primaryStage.show();
        //loader();
        //controller.loader(infileName);
    }

}
