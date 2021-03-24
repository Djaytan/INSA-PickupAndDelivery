package fr.insa.lyon.ifa1.view;

import fr.insa.lyon.ifa1.controller.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.net.URL;
import java.util.List;
import java.util.Map;

public class MainView extends Application {

    private MainViewController controller;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        this.controller = new MainViewController();

        primaryStage.setTitle("Pick'Up");

        String fxml = "/view/mainView.fxml";
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

        Scene sc = new Scene(root, 750, 600);
        Canvas map = (Canvas) sc.lookup("#map");
        drawSegments(map);
        primaryStage.setScene(sc);
        primaryStage.show();

    }

    private void drawSegments(Canvas map) {

        GraphicsContext gc = map.getGraphicsContext2D();
        List<Map<String, Map<String, Double>>> segments = this.controller.getSegments();

        gc.setFill(Color.BLACK);
        gc.setLineWidth(1.0);

        for(Map<String, Map<String, Double>> segment : segments) {

            Integer x1 = (int)((segment.get("origin").get("latitude") - 45.7) * 10000) - 200;
            Integer y1 = (int)((segment.get("origin").get("longitude") - 4.8) * 10000) - 300;
            Integer x2 = (int)((segment.get("destination").get("latitude") - 45.7) * 10000) - 200;
            Integer y2 = (int)((segment.get("destination").get("longitude") - 4.8) * 10000) - 300;

            System.out.println("Drawing segment from " + x1 + " " + y1 + " to " + x2 + " " + y2);

            gc.strokeLine(x1, y1, x2, y2);

        }

    }

}
