package fr.insa.lyon.ifa1.controller;

import fr.insa.lyon.ifa1.view.DeliveryView;
import fr.insa.lyon.ifa1.view.ImportView;
import fr.insa.lyon.ifa1.view.MainView;
import fr.insa.lyon.ifa1.view.ViewInterface;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class ViewController extends Application {

  public enum View { IMPORT_VIEW, MAIN_VIEW, DELIVERY_VIEW }

  private static final Map<View, String> VIEWS_FXML = Map.ofEntries(
          Map.entry(View.IMPORT_VIEW, "importView.fxml"),
          Map.entry(View.MAIN_VIEW,"mainView.fxml"),
          Map.entry(View.DELIVERY_VIEW, "deliveryView.fxml")
  );

  private static final Map<View, Class<? extends ViewInterface>> VIEWS_CLASS = Map.ofEntries(
          Map.entry(View.IMPORT_VIEW, ImportView.class),
          Map.entry(View.MAIN_VIEW, MainView.class),
          Map.entry(View.DELIVERY_VIEW, DeliveryView.class)
  );

  private static final String FXML_REPOSITORY = "/view/";

  private static Stage stage;

  public void run() { launch(); }

  @Override
  public void start(Stage primaryStage) {

    try {
      stage = primaryStage;
      stage.setTitle("Pick'Up");
      goToView(View.IMPORT_VIEW);
      stage.show();
    }
    catch (Exception ex) { System.out.println("Failed to start"); }

  }

  public void goToView(View view) {

    try { VIEWS_CLASS.get(view).getDeclaredConstructor().newInstance().show(); }
    catch(NoSuchMethodException ex) { System.out.println("Missing constructor in view " + VIEWS_CLASS.get(view).getName()); }
    catch(Exception ex) {System.out.println("Failed to initialize controller " + VIEWS_CLASS.get(view).getName() + ex); }

  }

  public Scene loadScene(View view) {

    String fxml = FXML_REPOSITORY + VIEWS_FXML.get(view);
    URL url = null;
    Scene scene = null;

    try {
      url = getClass().getResource( fxml );
      Parent root = FXMLLoader.load( url );
      scene = new Scene(root, 750, 600);
      System.out.println( "  fxmlResource = " + fxml );
    }
    catch(IOException ex) {
      System.out.println( "Exception on ViewController.loadScene()" );
      System.out.println( "  * url: " + url );
      System.out.println( "  * " + ex );
      System.out.println( "    ----------------------------------------\n" );
    }

    return scene;

  }

  public void showScene(Scene scene) {

    stage.setScene(scene);
    stage.show();

  }

}
