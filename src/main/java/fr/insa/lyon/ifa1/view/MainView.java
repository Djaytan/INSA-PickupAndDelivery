package fr.insa.lyon.ifa1.view;

import fr.insa.lyon.ifa1.controller.GeoMapController;
import fr.insa.lyon.ifa1.controller.PlanningRequestController;
import fr.insa.lyon.ifa1.controller.ViewController;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.List;
import java.util.Map;

import static java.lang.Math.min;

public class MainView implements View {

    private static final Color mapSegmentsColor = Color.BLACK;

    private final ViewController VIEW_CONTROLLER;
    private final GeoMapController GEO_MAP_CONTROLLER;
    private final PlanningRequestController PLANNING_REQUEST_CONTROLLER;

    private Map<String, Double> mapOrigin;
    private Double ratio;

    public MainView() {

        VIEW_CONTROLLER = ViewController.getInstance();
        GEO_MAP_CONTROLLER = GeoMapController.getInstance();
        PLANNING_REQUEST_CONTROLLER = PlanningRequestController.getInstance();

    }

    public void show() {

        Scene scene = VIEW_CONTROLLER.loadScene("/view/mainView.fxml");

        Canvas map = (Canvas) scene.lookup("#map");
        setMapParameters(map);
        drawSegments(map, mapSegmentsColor);
        drawPoints(map, Color.BLUE);

        VIEW_CONTROLLER.showScene(scene);

    }

    private void setMapParameters(Canvas map) {

        Map<String, Map<String, Double>> range = GEO_MAP_CONTROLLER.getRange();

        this.mapOrigin = Map.ofEntries(
                Map.entry("x", range.get("x").get("min")),
                Map.entry("y", range.get("y").get("min"))
        );

        this.ratio = min(
                map.getWidth() / (range.get("x").get("max") - range.get("x").get("min")),
                map.getHeight() / (range.get("y").get("max") - range.get("y").get("min"))
        );

    }

    private void drawSegments(Canvas map, Color color) {

        GraphicsContext gc = map.getGraphicsContext2D();
        List<Map<String, Map<String, Double>>> segments = GEO_MAP_CONTROLLER.getSegments();

        gc.setFill(color);
        gc.setLineWidth(1.0);

        for(Map<String, Map<String, Double>> segment : segments) {

            int x1 = (int) ((segment.get("origin").get("x") - this.mapOrigin.get("x")) * this.ratio);
            int y1 = (int) ((segment.get("origin").get("y") - this.mapOrigin.get("y")) * this.ratio);
            int x2 = (int) ((segment.get("destination").get("x") - this.mapOrigin.get("x")) * this.ratio);
            int y2 = (int) ((segment.get("destination").get("y") - this.mapOrigin.get("y")) * this.ratio);

            gc.strokeLine(x1, y1, x2, y2);

        }

    }

    private void drawPoints(Canvas map, Color color) {

        GraphicsContext gc = map.getGraphicsContext2D();
        List<Map<String, Map<String, Double>>> passagePoints = PLANNING_REQUEST_CONTROLLER.getPassagePoints();

        gc.setFill(color);
        gc.setLineWidth(6.0);

        for(Map<String, Map<String, Double>> group : passagePoints) {

            int x1 = (int) ((group.get("pickup").get("x") - this.mapOrigin.get("x")) * this.ratio);
            int y1 = (int) ((group.get("pickup").get("y") - this.mapOrigin.get("y")) * this.ratio);
            int x2 = (int) ((group.get("delivery").get("x") - this.mapOrigin.get("x")) * this.ratio);
            int y2 = (int) ((group.get("delivery").get("y") - this.mapOrigin.get("y")) * this.ratio);

            System.out.println("Drawing point at " + x1 + " " + y1);

            gc.setFill(color);
            gc.strokeOval(x1, y1, 5, 5);

            System.out.println("Drawing point at " + x2 + " " + y2);

            gc.setFill(color);
            gc.strokeOval(x2, y2, 5, 5);

        }

    }

}
