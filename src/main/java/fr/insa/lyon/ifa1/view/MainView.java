package fr.insa.lyon.ifa1.view;

import fr.insa.lyon.ifa1.controller.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.net.URL;
import java.util.List;
import java.util.Map;

public class MainView extends Application {

    private static Color mapSegmentsColor = Color.BLACK;

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
        drawSegments(map, mapSegmentsColor);
        primaryStage.setScene(sc);
        primaryStage.show();

    }

    private void drawSegments(Canvas map, Color color) {

        GraphicsContext gc = map.getGraphicsContext2D();
        List<Map<String, Map<String, Double>>> segments = this.controller.getSegments();

        gc.setFill(color);
        gc.setLineWidth(1.0);

        for(Map<String, Map<String, Double>> segment : segments) {

            Integer x1 = (int) ((segment.get("origin").get("longitude") - 4.8) * 10000) - 300;
            Integer y1 = (int) ((segment.get("origin").get("latitude") - 45.7) * 10000) - 200;
            Integer x2 = (int) ((segment.get("destination").get("longitude") - 4.8) * 10000) - 300;
            Integer y2 = (int) ((segment.get("destination").get("latitude") - 45.7) * 10000) - 200;

            System.out.println("Drawing segment from " + x1 + " " + y1 + " to " + x2 + " " + y2);

            gc.strokeLine(x1, y1, x2, y2);

        }

    }

    private void drawPoints(Canvas map, Color color) {

        GraphicsContext gc = map.getGraphicsContext2D();
        List<List<Map<String, Map<String, Double>>>> passagePoints = this.controller.getPassagePoints();

        gc.setFill(color);
        gc.setLineWidth(6.0);

        for(List<Map<String, Map<String, Double>>> group : passagePoints) {

            for(Map<String, Map<String, Double>> passagePoint : group) {

                Integer x1 = (int) ((passagePoint.get("pickup").get("longitude") - 4.8) * 10000) - 800;
                Integer y1 = (int) ((passagePoint.get("pickup").get("latitude") - 45.7) * 10000) - 750;
                Integer x2 = (int) ((passagePoint.get("delivery").get("longitude") - 4.8) * 10000) - 800;
                Integer y2 = (int) ((passagePoint.get("delivery").get("latitude") - 45.7) * 10000) - 750;

                System.out.println("Drawing point from " + x1 + " " + y1 + " to " + x1 + " " + y1);

                gc.setFill(color);
                gc.strokeOval(x1, y1, x1, y1);

                System.out.println("Drawing point from " + x2 + " " + y2 + " to " + x2 + " " + y2);

                gc.setFill(color);
                gc.strokeOval(x2, y2, x2, y2);

            }

        }


    }

}
