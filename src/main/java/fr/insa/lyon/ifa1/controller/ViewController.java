package fr.insa.lyon.ifa1.controller;

import fr.insa.lyon.ifa1.view.ImportView;
import fr.insa.lyon.ifa1.view.MainView;
import fr.insa.lyon.ifa1.view.View;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class ViewController extends Application {

  public static final String IMPORT_VIEW = "ImportView";
  public static final String MAIN_VIEW = "MainView";
  private static final Map<String, Class<? extends View>> VIEWS = Map.ofEntries(
          Map.entry(IMPORT_VIEW, ImportView.class),
          Map.entry(MAIN_VIEW, MainView.class)
  );

  private static Stage stage;

  public void run() { launch(); }

  @Override
  public void start(Stage primaryStage) {

    try {
      stage = primaryStage;
      stage.setTitle("Pick'Up");
      goToView(IMPORT_VIEW);
      stage.show();
    }
    catch (Exception ex) { System.out.println("Failed to start"); }

  }

  public void goToView(String view) {

    try { VIEWS.get(view).getDeclaredConstructor().newInstance().show(); }
    catch(NoSuchMethodException ex) { System.out.println("Missing constructor in view " + VIEWS.get(view).getName()); }
    catch(Exception ex) {System.out.println("Failed to initialize controller " + VIEWS.get(view).getName()); }

  }

  public Scene loadScene(String fxml) {

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
