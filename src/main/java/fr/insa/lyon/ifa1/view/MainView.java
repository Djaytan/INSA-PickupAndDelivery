package fr.insa.lyon.ifa1.view;

import fr.insa.lyon.ifa1.controller.GeoMapController;
import fr.insa.lyon.ifa1.controller.PlanningRequestController;
import fr.insa.lyon.ifa1.controller.ViewController;
import fr.insa.lyon.ifa1.models.map.Intersection;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.FileChooser;

import javafx.event.ActionEvent;

import java.io.File;
import java.util.List;
import java.util.Map;

import static java.lang.Math.min;

public class MainView implements ViewInterface {

    private static final ViewController VIEW_CONTROLLER = new ViewController();

    private static final Color MAP_SEGMENTS_COLOR = Color.GRAY;
    private static final Color[] DELIVERY_MEN_PATHS_COLORS = new Color[] {
            Color.RED, Color.GREEN, Color.ORANGE, Color.PINK, Color.YELLOW, Color.BLUE, Color.PURPLE
    };

    private static final Scene SCENE = VIEW_CONTROLLER.loadScene(ViewController.View.MAIN_VIEW);

    private static Map<String, Double> mapOrigin;
    private static Double ratio;

    private static StateMainView state;
    private static boolean chargedPoints = false;
    private static Map<String, Double> closestPassagePoint;

    public void show() {

        Canvas map = (Canvas) SCENE.lookup("#map");
        Canvas overEffects = (Canvas) SCENE.lookup("#overEffects");
        setMapParameters(map);
        drawSegments(GeoMapController.getSegments(), map, MAP_SEGMENTS_COLOR);

        setState(new MainViewWaitingState());

        overEffects.setOnMouseMoved(onMouseMove());

        VIEW_CONTROLLER.showScene(SCENE);

        //contextMenu
        ContextMenu contextMenu = new ContextMenu();

        MenuItem itemDelete = new MenuItem("Supprimer");
        itemDelete.setOnAction(onDeletePassagePoint());
        MenuItem itemMove = new MenuItem("Déplacer");
        itemMove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int x = 0;
            }
        });

        // Add MenuItem to ContextMenu
        contextMenu.getItems().addAll(itemDelete, itemMove);

        overEffects.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                contextMenu.show(map.getScene().getWindow(), event.getScreenX(), event.getScreenY());
            }
        });
    }

    private void setMapParameters(Canvas map) {

        Map<String, Map<String, Double>> range = GeoMapController.getRange();

        mapOrigin = Map.ofEntries(
                Map.entry("x", range.get("x").get("min")),
                Map.entry("y", range.get("y").get("min"))
        );

        ratio = min(
                map.getWidth() / (range.get("x").get("max") - range.get("x").get("min")),
                map.getHeight() / (range.get("y").get("max") - range.get("y").get("min"))
        );

    }

    private void drawSegments(List<Map<String, Map<String, Double>>> segments, Canvas map, Color color) {

        GraphicsContext gc = map.getGraphicsContext2D();

        gc.setStroke(color);
        gc.setLineWidth(1.0);
System.out.println(segments.size() + " segments to draw in " + color.toString());
        for(Map<String, Map<String, Double>> segment : segments) {

            int x1 = (int) ((segment.get("origin").get("x") - mapOrigin.get("x")) * ratio);
            int y1 = (int) ((segment.get("origin").get("y") - mapOrigin.get("y")) * ratio);
            int x2 = (int) ((segment.get("destination").get("x") - mapOrigin.get("x")) * ratio);
            int y2 = (int) ((segment.get("destination").get("y") - mapOrigin.get("y")) * ratio);

            gc.strokeLine(x1, y1, x2, y2);

        }

    }

    private void drawPoints(List<Map<String, Map<String, Double>>> points, Canvas map, Color color) {

        GraphicsContext gc = map.getGraphicsContext2D();

        gc.setLineWidth(5.0);
System.out.println(points.size() + " points to draw");
        for(Map<String, Map<String, Double>> group : points) {

            for(Map.Entry<String, Map<String, Double>> passagePoint : group.entrySet()) {

                int x = (int) ((passagePoint.getValue().get("x") - mapOrigin.get("x")) * ratio);
                int y = (int) ((passagePoint.getValue().get("y") - mapOrigin.get("y")) * ratio);

                gc.setStroke(color);
                gc.strokeOval(x, y, 5, 5);

            }
        }
    }

    private void drawOverPassagePoint(Map<String, Double> passagePoint, Canvas canvas) {

        GraphicsContext gc = canvas.getGraphicsContext2D();

        int x = (int) ((passagePoint.get("x") - mapOrigin.get("x")) * ratio);
        int y = (int) ((passagePoint.get("y") - mapOrigin.get("y")) * ratio);

        gc.setLineWidth(2.0);
        gc.setStroke(Color.RED);
        gc.strokeOval(x, y, 10, 5);

    }

    private void userDrawPoint(Canvas map, Color color, double mapX, double mapY) {
        //get closest intersect position
        Intersection closestIntersection = GeoMapController.getClosestIntersection(getWorldCoordinatesFromMapCoordinates(mapX, mapY).get("x"), getWorldCoordinatesFromMapCoordinates(mapX, mapY).get("y"));

        //insert value in data structure temporary
        if(state instanceof MainViewAddPickupState) {
            PlanningRequestController.addPickupPoint(closestIntersection);
        } else if(state instanceof MainViewAddDeliveryState) {
            PlanningRequestController.addDeliveryPoint(closestIntersection);
            PlanningRequestController.commit();
            Button btnPath = (Button) SCENE.lookup("#btnPath");
            if(btnPath != null) {
                btnPath.setDisable(false);
            }
        }

        //draw
        int x = (int) ((closestIntersection.getLongitude() - mapOrigin.get("x")) * ratio);
        int y = (int) ((closestIntersection.getLatitude() - mapOrigin.get("y")) * ratio);
        GraphicsContext gc = map.getGraphicsContext2D();
        gc.setFill(color);
        gc.strokeOval(x, y, 5, 5);
        gc.fillOval(x, y, 5, 5);

        //map.setOnContextMenuRequested(e -> menu.show(map.getScene().getWindow(), e.getScreenX(), e.getScreenY()));
    }

    public void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importer des points relais au format XML");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml file", "*.xml"));

        File file = fileChooser.showOpenDialog(null);

        if(file != null && file.getName().endsWith(".xml")) {
            Canvas canvas = (Canvas) SCENE.lookup("#passagePoints");

            PlanningRequestController.importPlanningRequest(file);
            chargedPoints = true;
            System.out.println("Start drawing P&D points");
            drawPoints(PlanningRequestController.getPassagePoints(), canvas, Color.BLUE);

            Button btnPath = (Button) SCENE.lookup("#btnPath");
            if(btnPath != null) {
                btnPath.setDisable(false);
            }
        }

    }

    public void quit() {
        System.exit(0);
    }

    public void calculatePath() {
        TextField input = (TextField) SCENE.lookup("#nbLivreurs");
        Canvas canvas = (Canvas) SCENE.lookup("#deliverymenPaths");
        if(input != null && canvas != null) {
            int nbLivreurs = Integer.parseInt(input.getText());
            System.out.println("nb de livreurs :" + nbLivreurs);
            System.out.println("Start calculating deliverymen paths");
            List<List<Map<String, Map<String, Double>>>> deliveryMenPaths = PlanningRequestController.getDeliveryMenPaths();
            clearCanvas(canvas);
            System.out.println("Start drawing deliverymen paths");
            for(int i = 0; i < deliveryMenPaths.size(); i++) {
                drawSegments(deliveryMenPaths.get(i), canvas, DELIVERY_MEN_PATHS_COLORS[i % DELIVERY_MEN_PATHS_COLORS.length]);
            }
        }
    }

    private Map<String, Double> getWorldCoordinatesFromMapCoordinates(double x, double y) {
        return Map.ofEntries(
                Map.entry("x", x/ratio + mapOrigin.get("x")),
                Map.entry("y", y/ratio + mapOrigin.get("y"))
        );
    }

    private Map<String, Integer> getMapCoordinatesFromWorldCoordinates(double x, double y) {
        return Map.ofEntries(
                Map.entry("x", (int)((x - mapOrigin.get("x")) * ratio)),
                Map.entry("y", (int)((y - mapOrigin.get("y")) * ratio))
        );
    }

    public void setState(StateMainView state) {
        MainView.state = state;
    }

    public void addPickup() {
        if(state instanceof MainViewWaitingState) {
            setState(new MainViewAddPickupState());
            PlanningRequestController.begin();
        } else {
            setState(new MainViewWaitingState());
            PlanningRequestController.undo();
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

    private EventHandler<ActionEvent> onDeletePassagePoint() {

        return e -> {

            PlanningRequestController.deleteOneRequest(closestPassagePoint);

            Canvas canvas = (Canvas) SCENE.lookup("#passagePoints");
            clearCanvas(canvas);
            drawPoints(PlanningRequestController.getPassagePoints(), canvas, Color.BLUE);

            calculatePath();

        };

    }

    private void clearCanvas(Canvas canvas) {
System.out.println("Clearing " + canvas.getId());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

    }

    private EventHandler<? super MouseEvent> onMouseMove() {

        return e -> {

            if(chargedPoints) {

                closestPassagePoint = PlanningRequestController.getClosestPassagePoint(getWorldCoordinatesFromMapCoordinates(e.getX(), e.getY()));

                Canvas canvas = (Canvas) SCENE.lookup("#overEffects");
                clearCanvas(canvas);
                drawOverPassagePoint(closestPassagePoint, canvas);

            }

        };

    }

}
