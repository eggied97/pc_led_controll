package nl.edeveloping.leds.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import nl.edeveloping.leds.MainApp;
import nl.edeveloping.leds.model.Port;

/**
 * Created by Egbert Dijkstra on 22-12-2016.
 */
public class ConnectController {

    @FXML
    private Button connectButton;

    @FXML
    private ChoiceBox<Port> connectChoiceBox;


    // Reference to the main application.
    private MainApp mainApp;


    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public ConnectController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {

    }

    @FXML
    private void handleConnect(){
        Port connectTo = connectChoiceBox.getSelectionModel().getSelectedItem();

        if(connectTo.getComPort() != null){
            this.mainApp.setupCommunicationWithPort(connectTo);
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No communication port selected");
            alert.setContentText("Please select a communication port in the dropdown menu.");

            alert.showAndWait();
        }
    }


    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        connectChoiceBox.setItems(mainApp.getSerialComPorts());
        connectChoiceBox.getSelectionModel().selectFirst();
        //personTable.setItems(mainApp.getPersonData());
    }




}
