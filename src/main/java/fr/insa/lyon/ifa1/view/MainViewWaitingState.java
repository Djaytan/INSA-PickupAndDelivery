package fr.insa.lyon.ifa1.view;

import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Text;

public class MainViewWaitingState implements StateMainView {
    @Override
    public void canvaClick(MainView context) {
        Button btnPickup = (Button) context.getScene().lookup("#btnAddPickup");
        Button btnFileChooser = (Button) context.getScene().lookup("#btnFileChooser");
        Button btnPath = (Button) context.getScene().lookup("#btnPath");
        TextField input = (TextField) context.getScene().lookup("#nbLivreurs");
        Text stateTxt = (Text) context.getScene().lookup("#stateTxt");
        Canvas map = (Canvas) context.getScene().lookup("#map");

        if(btnPickup != null && btnFileChooser != null && btnPath != null && input != null && stateTxt != null && map != null) {
            btnPickup.setText("Ajouter une livraison");
            stateTxt.setText("");
            btnFileChooser.setDisable(false);
            btnPath.setDisable(false);
            input.setDisable(false);

            map.setCursor(Cursor.DEFAULT);
        }
    }
}
