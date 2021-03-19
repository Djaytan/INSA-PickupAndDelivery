package fr.insa.lyon.ifa1.controller;

import fr.insa.lyon.ifa1.App;
import fr.insa.lyon.ifa1.cache.GeoMapRegistry;
import fr.insa.lyon.ifa1.xml.XMLDeserialization;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Observable;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImportViewController extends Observable  {
    private static final Logger LOGGER = Logger.getLogger(ImportViewController.class.getName());

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
                importGeoMap(file.getName());
                //redirection nvl fenetre
                System.out.println("notify observer");
                setChanged();
                notifyObservers(1);
            }
        }
    }

    public void importGeoMap(String filename) {
        try {
            XMLDeserialization.deserializeMap(filename);
            LOGGER.info(GeoMapRegistry.getGeoMap().toString());
        } catch (SAXException e) {
            LOGGER.log(Level.SEVERE, "Error during XML map file content reading", e);
        } catch (ParserConfigurationException e) {
            LOGGER.log(Level.SEVERE, "Something went wrong in map XML parser configuration", e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error during XML map file manipulation", e);
        }
    }


}
