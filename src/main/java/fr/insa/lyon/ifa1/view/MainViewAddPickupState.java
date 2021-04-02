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
    public void canvaClick(MainView context) {
        Button btnPickup = (Button) context.getScene().lookup("#btnAddPickup");
        Button btnFileChooser = (Button) context.getScene().lookup("#btnFileChooser");
        Button btnPath = (Button) context.getScene().lookup("#btnPath");
        TextField input = (TextField) context.getScene().lookup("#nbLivreurs");
        Text stateTxt = (Text) context.getScene().lookup("#stateTxt");
        Canvas map = (Canvas) context.getScene().lookup("#overEffects");

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
