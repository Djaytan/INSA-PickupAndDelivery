package fr.insa.lyon.ifa1.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

public class ViewController extends Application implements Observer {
  private Stage stage;

  private static ViewController instance;

  public ViewController() {
    instance = this;
  }

  public static ViewController getInstance() {
    return instance;
  }

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    try {
      this.stage = primaryStage;
      goToImportView();
      primaryStage.show();
    } catch (Exception ex) {
      System.out.println("start");
    }
  }

  private void goToImportView()
  {
    try {
      replaceView("/view/importView.fxml");
    }
    catch(Exception ex) {
      System.out.println("Erreur importView.fxml");
      stage.setTitle("Import");
    }
  }

  private void goToMainView() {
    try {
      replaceView("/view/mainView.fxml");
      stage.setTitle("Main page");
    }
    catch(Exception ex) {
      System.out.println("Erreur mainView.fxml");
    }
  }

  private void goToDeliveryView() {
    try {
      replaceView("/view/deliveryView.fxml");
      stage.setTitle("Delivery");
    }
    catch(Exception ex) {
      System.out.println("Erreur deliveryView.fxml");
    }
  }

  private void replaceView(String fxml) throws Exception{
    Parent root = null;
    URL url = null;
    try
    {
      url  = getClass().getResource( fxml );
      root = FXMLLoader.load( url );
      System.out.println( "  fxmlResource = " + fxml );
    }
    catch ( Exception ex )
    {
      System.out.println( "Exception on FXMLLoader.load()" );
      System.out.println( "  * url: " + url );
      System.out.println( "  * " + ex );
      System.out.println( "    ----------------------------------------\n" );
    }

    stage.setScene(new Scene(root, 800, 600));
  }

  @Override
  public void update(Observable o, Object arg) {
    System.out.println("j'ai été notifié");
    int index = (int)arg;
    switch (index) {
      case 1:
        goToMainView();
        break;
      case 2:
        goToDeliveryView();
        break;
      default:
        break;
    }
  }
}
