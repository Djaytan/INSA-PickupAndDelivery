package fr.insa.lyon.ifa1.view;

import fr.insa.lyon.ifa1.controller.GeoMapController;
import fr.insa.lyon.ifa1.controller.PlanningRequestController;
import fr.insa.lyon.ifa1.controller.ViewController;
import fr.insa.lyon.ifa1.models.map.Intersection;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.FileChooser;

import javafx.event.ActionEvent;
import java.io.File;
import java.util.Collection;
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

    private static StateMainView state;


    public void show() {

        Scene scene = VIEW_CONTROLLER.loadScene(ViewController.View.MAIN_VIEW);

        Canvas map = (Canvas) scene.lookup("#map");
        setMapParameters(map);
        drawSegments(map, MAP_SEGMENTS_COLOR);
        drawPoints(map, Color.BLUE);

        setState(new MainViewWaitingState());

        VIEW_CONTROLLER.showScene(scene);

        //contextMenu
        ContextMenu contextMenu = new ContextMenu();

        MenuItem itemDelete = new MenuItem("Supprimer");
        itemDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int x = 0;
            }
        });
        MenuItem itemMove = new MenuItem("Déplacer");
        itemMove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int x = 0;
            }
        });

        // Add MenuItem to ContextMenu
        contextMenu.getItems().addAll(itemDelete, itemMove);

        map.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                contextMenu.show(map.getScene().getWindow(), event.getScreenX(), event.getScreenY());
            }
        });
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

    private void userDrawPoint(Canvas map, Color color, double mapX, double mapY) {
        //get closest intersect position
        int closestX = 0;
        int closestY = 0;
        Intersection closestIntersection = null;
        double distance = Double.MAX_VALUE;
        double tmpDistance;

        for (Intersection intersection : GEO_MAP_CONTROLLER.getIntersections()) {
            int x = (int) ((intersection.getLongitude() - mapOrigin.get("x")) * ratio);
            int y = (int) ((intersection.getLatitude() - mapOrigin.get("y")) * ratio);

            tmpDistance = Math.sqrt((mapY - y) * (mapY - y) + (mapX - x) * (mapX - x));
            if(tmpDistance < distance) {
                closestIntersection = intersection;
                closestX = x;
                closestY = y;
                distance = tmpDistance;
            }
        }

        //insert value in data structure temporary
        if(state instanceof MainViewAddPickupState) {
            PLANNING_REQUEST_CONTROLLER.addPickupPoint(closestIntersection);
        } else if(state instanceof MainViewAddDeliveryState) {
            PLANNING_REQUEST_CONTROLLER.addDeliveryPoint(closestIntersection);
            PLANNING_REQUEST_CONTROLLER.commit();
        }

        //draw
        GraphicsContext gc = map.getGraphicsContext2D();
        gc.setFill(color);
        gc.strokeOval(closestX, closestY, 5, 5);
        gc.fillOval(closestX, closestY, 5, 5);

        //map.setOnContextMenuRequested(e -> menu.show(map.getScene().getWindow(), e.getScreenX(), e.getScreenY()));
    }

    public void openFileChooser() {
        List<Map<String, Map<String, Double>>> passagePoints = PLANNING_REQUEST_CONTROLLER.getPassagePoints();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importer des points relais au format XML");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml file", "*.xml"));

        File file = fileChooser.showOpenDialog(null);

        if(file != null && file.getName().endsWith(".xml")) {

            PLANNING_REQUEST_CONTROLLER.importPlanningRequest(file);
            //TODO : call drawing
        }

    }

    public void quit() {
        System.exit(0);
    }

    public void calculatePath() {
        //TODO call calculatePath;
    }

    public void setState(StateMainView state) {
        this.state = state;
    }

    public void addPickup() {
        if(state instanceof MainViewWaitingState) {
            setState(new MainViewAddPickupState());
            PLANNING_REQUEST_CONTROLLER.begin();
        } else {
            setState(new MainViewWaitingState());
            PLANNING_REQUEST_CONTROLLER.undo();
        }

        if(state != null) {
            state.canvaClick(this);
        }
    }

    @FXML
    public void canvaClick(MouseEvent event) {
        if(state instanceof MainViewAddPickupState) {
            userDrawPoint((Canvas)getScene().lookup("#map"), Color.RED, event.getX(), event.getY());
            setState(new MainViewAddDeliveryState());
        } else if(state instanceof MainViewAddDeliveryState) {
            userDrawPoint((Canvas)getScene().lookup("#map"), Color.BLUE, event.getX(), event.getY());
            setState(new MainViewWaitingState());
        }

        if(state != null) {
            state.canvaClick(this);
        }
    }

    public Scene getScene() {
        return VIEW_CONTROLLER.getScene();
    }

}
