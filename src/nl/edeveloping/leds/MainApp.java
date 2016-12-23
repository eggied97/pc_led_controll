package nl.edeveloping.leds;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javafx.stage.WindowEvent;
import nl.edeveloping.leds.controller.ConnectController;
import nl.edeveloping.leds.controller.SetLedController;
import nl.edeveloping.leds.model.Port;
import nl.edeveloping.leds.serial.Communicator;

import java.io.IOException;

/**
 * Created by Egbert Dijkstra on 9-10-2016.
 */
public class MainApp extends Application{

    static Communicator communicator;

    private Stage primaryStage;
    private BorderPane rootLayout;

    private ObservableList<Port> comPorts = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("PCLed V1.0");

        this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                Log.debug("Stage is closing");
                communicator.disconnect();
            }
        });

        communicator = new Communicator();
        communicator.searchForPorts();

        comPorts.add(new Port("Select port:", null));

        comPorts.addAll(communicator.getOpenPorts());

        initRootLayout();

        showConnectScreen();
    }

    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showConnectScreen() {
        try {
            // Load connect screen.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/connectStage.fxml"));
            AnchorPane connectScreen = (AnchorPane) loader.load();

            // Set connect screen into the center of root layout.
            rootLayout.setCenter(connectScreen);

            ConnectController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showChangeLedScreen(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/setLedText.fxml"));
            TabPane changeLedScreen = (TabPane) loader.load();

            rootLayout.setCenter(changeLedScreen);

            SetLedController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean setupCommunicationWithPort(Port port){

        if(communicator.connect(port)){
            if(communicator.initIOStream()){
                if(communicator.initListener()){
                    showChangeLedScreen();
                    return true;
                }else{
                    return false;
                }

            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    public void setLedColor(int pos, int r, int g, int b){
        communicator.writeColorWithPos(pos, r, g, b);
    }

    public static void main(String[] args) {
        launch(args);
    }


    public ObservableList<Port> getSerialComPorts(){
        return comPorts;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}