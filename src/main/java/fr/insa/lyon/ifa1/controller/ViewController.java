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
  private final Map<String, View> VIEWS;

  private static ViewController instance;
  private static Stage stage;

  public static ViewController getInstance() {
    return instance;
  }

  public ViewController() {

    ViewController.instance = this;

    VIEWS = Map.ofEntries(
            Map.entry(IMPORT_VIEW, new ImportView()),
            Map.entry(MAIN_VIEW, new MainView())
    );

  }

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

  public void goToView(String view) { VIEWS.get(view).show(); }

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
