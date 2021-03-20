package fr.insa.lyon.ifa1.view;

import fr.insa.lyon.ifa1.controller.GeoMapController;
import fr.insa.lyon.ifa1.controller.ViewController;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;

import java.io.File;

public class ImportView implements View {

    private ViewController viewController;
    private GeoMapController geoMapController;

    public ImportView() {
        this.viewController = ViewController.getInstance();
        this.geoMapController = GeoMapController.getInstance();
    }

    public void show() {

        Scene scene = this.viewController.loadScene("/view/importView.fxml");
        this.viewController.showScene(scene);

    }

    public void openFileChooser(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir une carte au format XML");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml file", "*.xml"));

        File file = fileChooser.showOpenDialog(null);

        if(file != null && file.getName().endsWith(".xml")) {

            this.geoMapController.importGeoMap(file);
            this.viewController.goToView(ViewController.MAIN_VIEW);

        }

    }

}
