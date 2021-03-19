package fr.insa.lyon.ifa1.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.util.Observable;

import java.io.File;

public class ImportViewController extends Observable  {
    public Label labelImport;
    private ViewController vc;

    @FXML
    public void initialize() {
        vc = ViewController.getInstance();
        this.addObserver(vc);
    }

    public void openFileChooser(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir une carte au format XML");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml file", "*.xml"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            System.out.println(file.getAbsoluteFile());
            if(file.getName().endsWith(".xml"))
            {
                //appelle de la function de volta => charger les données
                //redirection nvl fenetre
                System.out.println("notify observer");
                setChanged();
                notifyObservers(1);
            }
        }
    }


}
