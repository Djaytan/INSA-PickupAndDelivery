package fr.insa.lyon.ifa1.view;

import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Text;

public class MainViewAddPickupState implements StateMainView {
    @Override
    public void canvasClick(MainView context) {
        Button btnPickup = (Button) MainView.SCENE.lookup("#btnAddPickup");
        Button btnFileChooser = (Button) MainView.SCENE.lookup("#btnFileChooser");
        Button btnPath = (Button) MainView.SCENE.lookup("#btnPath");
        TextField input = (TextField) MainView.SCENE.lookup("#nbLivreurs");
        Text stateTxt = (Text) MainView.SCENE.lookup("#stateTxt");
        Canvas map = (Canvas) MainView.SCENE.lookup("#overEffects");

        if(btnPickup != null && btnFileChooser != null && btnPath != null && input != null && stateTxt != null && map != null) {
            btnPickup.setText("Annuler l'ajout");
            stateTxt.setText("Édition : Ajout d'un pickup");
            btnFileChooser.setDisable(true);
            btnPath.setDisable(true);
            input.setDisable(true);

            //Image image = new Image(getClass().getResource( "/images/mapPicker.png" ).toString());  //pass in the image path
            //map.setCursor(new ImageCursor(image));
            map.setCursor(Cursor.CROSSHAIR);
        }
    }
}
