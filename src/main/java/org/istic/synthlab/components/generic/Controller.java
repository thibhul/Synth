package org.istic.synthlab.components.generic;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import org.istic.synthlab.ui.ConnectionManager;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by seb on 04/02/16.
 */
public class Controller implements Initializable {

    @FXML
    Circle input;
    @FXML
    Circle output;
    @FXML
    Circle circleEvent;

    private static int numInstance = 0;
    private generic generic = new generic("Generic"+numInstance++);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        input.addEventHandler(MouseEvent.MOUSE_CLICKED, new GetIdWithClick());
        output.addEventHandler(MouseEvent.MOUSE_CLICKED, new GetIdWithClick());

    }
    @FXML
    public void connectOutput() {
        ConnectionManager.makeOrigin(circleEvent, generic.getOutput());
    }

    @FXML
    public void connectInput() {
        ConnectionManager.makeDestination(circleEvent, generic.getInput());
    }

    private class GetIdWithClick implements EventHandler<Event> {
        @Override
        public void handle(Event event) {
            circleEvent = (Circle)event.getSource();
        }
    }


}
