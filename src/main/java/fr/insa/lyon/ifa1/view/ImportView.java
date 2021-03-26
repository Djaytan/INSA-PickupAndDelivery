package fr.insa.lyon.ifa1.view;

import fr.insa.lyon.ifa1.controller.GeoMapController;
import fr.insa.lyon.ifa1.controller.ViewController;
import javafx.scene.Scene;
import javafx.stage.FileChooser;

import java.io.File;

public class ImportView implements ViewInterface {

    private static final ViewController VIEW_CONTROLLER = new ViewController();

    private static final Scene SCENE = VIEW_CONTROLLER.loadScene(ViewController.View.IMPORT_VIEW);

    public void show() { VIEW_CONTROLLER.showScene(SCENE); }

    public void openFileChooser() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir une carte au format XML");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml file", "*.xml"));

        File file = fileChooser.showOpenDialog(null);

        if(file != null && file.getName().endsWith(".xml")) {

            GeoMapController.importGeoMap(file);
            VIEW_CONTROLLER.goToView(ViewController.View.MAIN_VIEW);

        }
    }

    public void quit() {
        System.exit(0);
    }

}
