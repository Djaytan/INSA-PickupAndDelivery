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

import static java.lang.Math.min;

public class MainView extends Application {

    private static final Color mapSegmentsColor = Color.BLACK;

    private MainViewController controller;
    private Map<String, Double> mapOrigin;
    private Double ratio;

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
        setMapParameters(map);
        drawSegments(map, mapSegmentsColor);
        primaryStage.setScene(sc);
        primaryStage.show();

    }

    private void setMapParameters(Canvas map) {

        Map<String, Map<String, Double>> range = this.controller.getRange();

        this.mapOrigin = Map.ofEntries(
                Map.entry("x", range.get("longitude").get("min")),
                Map.entry("y", range.get("latitude").get("min"))
        );

        System.out.println("Canvas size : width = " + map.getWidth() + ", height = " + map.getHeight());
        System.out.println("Max values : x = " + range.get("longitude").get("max") + ", y = " + range.get("latitude").get("max"));
        this.ratio = min(
                map.getHeight() / (range.get("latitude").get("max") - range.get("latitude").get("min")),
                map.getWidth() / (range.get("longitude").get("max") - range.get("longitude").get("min"))
        );

        System.out.println("Map origin : x = " + this.mapOrigin.get("x") + ", y = " + this.mapOrigin.get("y"));
        System.out.println("Map ratio : " + this.ratio);

    }

    private void drawSegments(Canvas map, Color color) {

        GraphicsContext gc = map.getGraphicsContext2D();
        List<Map<String, Map<String, Double>>> segments = this.controller.getSegments();

        gc.setFill(color);
        gc.setLineWidth(1.0);

        for(Map<String, Map<String, Double>> segment : segments) {

            Integer x1 = (int) ((segment.get("origin").get("longitude") - this.mapOrigin.get("x")) * this.ratio);
            Integer y1 = (int) ((segment.get("origin").get("latitude") - this.mapOrigin.get("y")) * this.ratio);
            Integer x2 = (int) ((segment.get("destination").get("longitude") - this.mapOrigin.get("x")) * this.ratio);
            Integer y2 = (int) ((segment.get("destination").get("latitude") - this.mapOrigin.get("y")) * this.ratio);

            // System.out.println("Drawing segment from " + x1 + " " + y1 + " to " + x2 + " " + y2);

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

                Integer x1 = (int) ((passagePoint.get("pickup").get("longitude") - 4.88) * 10000);
                Integer y1 = (int) ((passagePoint.get("pickup").get("latitude") - 45.775) * 10000);
                Integer x2 = (int) ((passagePoint.get("delivery").get("longitude") - 4.88) * 10000);
                Integer y2 = (int) ((passagePoint.get("delivery").get("latitude") - 45.775) * 10000);

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
