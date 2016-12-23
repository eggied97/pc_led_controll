package nl.edeveloping.leds.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import nl.edeveloping.leds.Log;
import nl.edeveloping.leds.MainApp;


/**
 * Created by Egbert Dijkstra on 22-12-2016.
 */
public class SetLedController {
    @FXML
    private TextField tfPos;

    @FXML
    private TextField tfRed;
    @FXML
    private TextField tfGreen;
    @FXML
    private TextField tfBlue;

    private MainApp mainApp;

    @FXML
    private void changeLed(){

        int pos = Integer.valueOf(tfPos.getCharacters().toString());

        int red = Integer.valueOf(tfRed.getCharacters().toString());
        int green = Integer.valueOf(tfGreen.getCharacters().toString());
        int blue = Integer.valueOf(tfBlue.getCharacters().toString());

        Log.debug("pos:"+pos+", ("+red+", "+green+", "+blue+")");

        this.mainApp.setLedColor(pos, red, green, blue);

    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

}
