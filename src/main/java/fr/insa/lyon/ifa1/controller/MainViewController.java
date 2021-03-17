package fr.insa.lyon.ifa1.controller;

import javafx.fxml.FXML;
import java.util.Observable;


public class MainViewController extends Observable {
    private ViewController vc;

    @FXML
    public void initialize() {
        vc = ViewController.getInstance();
        this.addObserver(vc);
    }
}
