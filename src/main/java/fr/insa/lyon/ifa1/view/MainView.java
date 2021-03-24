package fr.insa.lyon.ifa1.view;

import fr.insa.lyon.ifa1.controller.GeoMapController;
import fr.insa.lyon.ifa1.controller.PlanningRequestController;
import fr.insa.lyon.ifa1.controller.ViewController;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;
import java.util.Map;

import static java.lang.Math.min;

public class MainView implements ViewInterface {

    private static final ViewController VIEW_CONTROLLER = new ViewController();
    private static final GeoMapController GEO_MAP_CONTROLLER = new GeoMapController();
    private static final PlanningRequestController PLANNING_REQUEST_CONTROLLER = new PlanningRequestController();

    private static final Color MAP_SEGMENTS_COLOR = Color.BLACK;

    private static Map<String, Double> mapOrigin;
    private static Double ratio;

    public void show() {

        Scene scene = VIEW_CONTROLLER.loadScene(ViewController.View.MAIN_VIEW);

        Canvas map = (Canvas) scene.lookup("#map");
        setMapParameters(map);
        drawSegments(map, MAP_SEGMENTS_COLOR);
        drawPoints(map, Color.BLUE);

        VIEW_CONTROLLER.showScene(scene);

    }

    private void setMapParameters(Canvas map) {

        Map<String, Map<String, Double>> range = GEO_MAP_CONTROLLER.getRange();

        mapOrigin = Map.ofEntries(
                Map.entry("x", range.get("x").get("min")),
                Map.entry("y", range.get("y").get("min"))
        );

        ratio = min(
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

            int x1 = (int) ((segment.get("origin").get("x") - mapOrigin.get("x")) * ratio);
            int y1 = (int) ((segment.get("origin").get("y") - mapOrigin.get("y")) * ratio);
            int x2 = (int) ((segment.get("destination").get("x") - mapOrigin.get("x")) * ratio);
            int y2 = (int) ((segment.get("destination").get("y") - mapOrigin.get("y")) * ratio);

            gc.strokeLine(x1, y1, x2, y2);

        }

    }

    private void drawPoints(Canvas map, Color color) {

        GraphicsContext gc = map.getGraphicsContext2D();
        List<Map<String, Map<String, Double>>> passagePoints = PLANNING_REQUEST_CONTROLLER.getPassagePoints();

        gc.setLineWidth(5.0);

        for(Map<String, Map<String, Double>> group : passagePoints) {

            for(Map.Entry<String, Map<String, Double>> passagePoint : group.entrySet()) {

                int x = (int) ((passagePoint.getValue().get("x") - mapOrigin.get("x")) * ratio);
                int y = (int) ((passagePoint.getValue().get("y") - mapOrigin.get("y")) * ratio);

                gc.setFill(color);
                gc.strokeOval(x, y, 5, 5);

            }

        }

    }

    public void openFileChooser() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importer des points relais au format XML");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml file", "*.xml"));

        File file = fileChooser.showOpenDialog(null);

        if(file != null && file.getName().endsWith(".xml")) {

            PLANNING_REQUEST_CONTROLLER.importPlanningRequest(file);
            //TODO : call drawing
        }

    }

}
