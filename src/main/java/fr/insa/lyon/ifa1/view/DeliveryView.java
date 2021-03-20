package fr.insa.lyon.ifa1.view;

import fr.insa.lyon.ifa1.controller.ViewController;
import javafx.scene.Scene;

public class DeliveryView implements View {

    private static final ViewController VIEW_CONTROLLER = new ViewController();

    public void show() {

        Scene scene = VIEW_CONTROLLER.loadScene(ViewController.DELIVERY_VIEW);
        VIEW_CONTROLLER.showScene(scene);

    }

}
