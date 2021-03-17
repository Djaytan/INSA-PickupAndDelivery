package fr.insa.lyon.ifa1.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;

public class MainViewController {
    public Label helloWorld;

    public void sayHelloWorld(ActionEvent actionEvent) {
        helloWorld.setText("Hello World!");
    }
}
