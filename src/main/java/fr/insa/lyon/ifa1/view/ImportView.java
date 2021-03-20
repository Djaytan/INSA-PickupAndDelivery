package fr.insa.lyon.ifa1.view;

import fr.insa.lyon.ifa1.controller.GeoMapController;
import fr.insa.lyon.ifa1.controller.ViewController;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;

import java.io.File;

public class ImportView implements View {

    private static final ViewController VIEW_CONTROLLER = new ViewController();
    private static final GeoMapController GEO_MAP_CONTROLLER = new GeoMapController();

    public void show() {

        Scene scene = VIEW_CONTROLLER.loadScene(ViewController.IMPORT_VIEW);
        VIEW_CONTROLLER.showScene(scene);

    }

    public void openFileChooser() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir une carte au format XML");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml file", "*.xml"));

        File file = fileChooser.showOpenDialog(null);

        if(file != null && file.getName().endsWith(".xml")) {

            GEO_MAP_CONTROLLER.importGeoMap(file);
            VIEW_CONTROLLER.goToView(ViewController.MAIN_VIEW);

        }

    }

}
