package fr.insa.lyon.ifa1.view;

import fr.insa.lyon.ifa1.controller.GeoMapController;
import fr.insa.lyon.ifa1.controller.PlanningRequestController;
import fr.insa.lyon.ifa1.controller.ViewController;
import fr.insa.lyon.ifa1.models.map.Intersection;
import fr.insa.lyon.ifa1.models.request.PassagePointType;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.FileChooser;

import javafx.event.ActionEvent;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.Math.min;

public class MainView implements ViewInterface {

    private static final ViewController VIEW_CONTROLLER = new ViewController();

    private static final double MAP_SEGMENTS_WIDTH = 1.0;
    private static final double DELIVERY_MEN_PATHS_WIDTH = 2.0;
    private static final double DEPOT_WIDTH = 16.0;
    private static final double PASSAGE_POINTS_WIDTH = 8.0;
    private static final double OVERED_PASSAGE_POINTS_WIDTH = 16.0;

    private static final Color MAP_SEGMENTS_COLOR = Color.GRAY;
    private static final Color[] DELIVERY_MEN_PATHS_COLORS = new Color[] {
            Color.RED, Color.GREEN, Color.ORANGE, Color.PINK, Color.YELLOW, Color.BLUE, Color.PURPLE
    };
    private static final Color DEPOT_COLOR = Color.BLACK;
    private static final Color[] PASSAGE_POINTS_COLORS = new Color[] {
            Color.GREEN, Color.ORANGE, Color.PINK, Color.YELLOW, Color.BLUE, Color.PURPLE, Color.CRIMSON,
            Color.BROWN, Color.CYAN, Color.FUCHSIA, Color.LEMONCHIFFON
    };
    private static final Color OVERED_PASSAGE_POINTS_COLOR = Color.RED;

    protected static final Scene SCENE = VIEW_CONTROLLER.loadScene(ViewController.View.MAIN_VIEW);

    private static final Tooltip tooltip = new Tooltip();

    private static Map<String, Double> canvasOrigin;
    private static Map<String, Double> mapOrigin;
    private static Double ratio;

    private static StateMainView state;
    private static Map<String, Object> closestPassagePoint = null;

    public void show() {

        Canvas map = (Canvas) SCENE.lookup("#map");
        Canvas overEffects = (Canvas) SCENE.lookup("#overEffects");

        overEffects.setOnMouseMoved(onMouseMove());

        tooltip.setShowDelay(Duration.ZERO);
        tooltip.setPrefWidth(125);
        tooltip.setWrapText(true);

        setState(new MainViewWaitingState());

        VIEW_CONTROLLER.showScene(SCENE);

        SCENE.widthProperty().addListener(onWidthResize());
        SCENE.heightProperty().addListener(onHeightResize());
        SCENE.getWindow().setHeight(SCENE.getWindow().getHeight() + 1);
        SCENE.getWindow().setWidth(SCENE.getWindow().getWidth() + 1);

        //contextMenu
        ContextMenu contextMenu = new ContextMenu();

        MenuItem itemDelete = new MenuItem("Supprimer");
        itemDelete.setOnAction(onDeletePassagePoint());
        MenuItem itemMove = new MenuItem("Déplacer");
        itemMove.setOnAction(e -> { int x = 0; });
        MenuItem itemChangeOrder = new MenuItem("Modifier l'ordre");
        itemChangeOrder.setOnAction(onChangeOrder());

        // Add MenuItem to ContextMenu
        contextMenu.getItems().addAll(itemDelete, itemMove, itemChangeOrder);

        overEffects.setOnContextMenuRequested(e -> contextMenu.show(map.getScene().getWindow(), e.getScreenX(), e.getScreenY()));

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

        setCanvasOrigin();

    }

    private void setCanvasOrigin() {

        Canvas map = (Canvas) SCENE.lookup("#map");
        Map<String, Map<String, Double>> range = GeoMapController.getRange();

        double x = (map.getWidth() - (range.get("x").get("max") - range.get("x").get("min")) * ratio) / 2;
        double y = (map.getHeight() - (range.get("y").get("max") - range.get("y").get("min")) * ratio) / 2;

        canvasOrigin = Map.ofEntries(
                Map.entry("x", x),
                Map.entry("y", y)
        );

    }

    private Map<String, Double> getWorldCoordinatesFromMapCoordinates(double x, double y) {

        return Map.ofEntries(
                Map.entry("x", (x - canvasOrigin.get("x")) / ratio + mapOrigin.get("x")),
                Map.entry("y", (y - canvasOrigin.get("y")) / ratio + mapOrigin.get("y"))
        );

    }

    private Map<String, Integer> getMapCoordinatesFromWorldCoordinates(Map<String, Double> coordinates) {

        return Map.ofEntries(
                Map.entry("x", (int) ((coordinates.get("x") - mapOrigin.get("x")) * ratio + canvasOrigin.get("x"))),
                Map.entry("y", (int) ((coordinates.get("y") - mapOrigin.get("y")) * ratio + canvasOrigin.get("y")))
        );

    }

    private void drawSegments(List<Map<String, Map<String, Double>>> segments, Canvas map, double width, Color color) {

        GraphicsContext gc = map.getGraphicsContext2D();

        gc.setStroke(color);
        gc.setLineWidth(width);

        for(Map<String, Map<String, Double>> segment : segments) {

            Map<String, Integer> coordinates1 = getMapCoordinatesFromWorldCoordinates(segment.get("origin"));
            Map<String, Integer> coordinates2 = getMapCoordinatesFromWorldCoordinates(segment.get("destination"));

            gc.strokeLine(coordinates1.get("x"), coordinates1.get("y"), coordinates2.get("x"), coordinates2.get("y"));

        }

    }

    private void drawPoints(List<Map<PassagePointType, Map<String, Double>>> points, Canvas map, double width, Color[] colors) {

        GraphicsContext gc = map.getGraphicsContext2D();

        int i = 0;
        for(Map<PassagePointType, Map<String, Double>> group : points) {

            gc.setFill(colors[i % colors.length]);

            for(Map.Entry<PassagePointType, Map<String, Double>> passagePoint : group.entrySet()) {

                Map<String, Integer> coordinates = getMapCoordinatesFromWorldCoordinates(passagePoint.getValue());

                int x = (int) (coordinates.get("x") - width / 2);
                int y = (int) (coordinates.get("y") - width / 2);

                if (passagePoint.getKey() == PassagePointType.PICKUP) { gc.fillRect(x, y, width, width); }
                else { gc.fillOval(x, y, width, width); }

            }

            i++;

        }

    }

    private void drawMap() {

        Canvas canvas = (Canvas) SCENE.lookup("#map");

        clearCanvas(canvas);
        drawSegments(GeoMapController.getSegments(), canvas, MAP_SEGMENTS_WIDTH, MAP_SEGMENTS_COLOR);

    }

    private void drawPassagePoints() {

        Canvas canvas = (Canvas) SCENE.lookup("#passagePoints");
        List<Map<PassagePointType, Map<String, Double>>> passagePoints = PlanningRequestController.getPassagePoints();

        clearCanvas(canvas);

        if(!passagePoints.isEmpty()) {

            drawPoints(new ArrayList<>() {{ add(passagePoints.get(0)); }}, canvas, DEPOT_WIDTH, new Color[]{DEPOT_COLOR});
            passagePoints.remove(0);
            drawPoints(passagePoints, canvas, PASSAGE_POINTS_WIDTH, PASSAGE_POINTS_COLORS);

        }

    }

    private void drawDeliveryMenPaths() {

        Canvas canvas = (Canvas) SCENE.lookup("#deliverymenPaths");
        List<List<Map<String, Map<String, Double>>>> deliveryMenPaths = PlanningRequestController.getDeliveryMenPaths();

        clearCanvas(canvas);

        for(int i = 0; i < deliveryMenPaths.size(); i++)
        { drawSegments(deliveryMenPaths.get(i), canvas, DELIVERY_MEN_PATHS_WIDTH, DELIVERY_MEN_PATHS_COLORS[i % DELIVERY_MEN_PATHS_COLORS.length]); }

    }

    private void drawOveredPassagePoints() {

        Canvas canvas = (Canvas) SCENE.lookup("#overEffects");

        if(closestPassagePoint != null) {

            List<Map<PassagePointType, Map<String, Double>>> closestPassagePoints = new ArrayList<>() {{
                add(PlanningRequestController.getCouplePassagePoints(closestPassagePoint));
            }};

            clearCanvas(canvas);
            drawPoints(closestPassagePoints, canvas, OVERED_PASSAGE_POINTS_WIDTH, new Color[] { OVERED_PASSAGE_POINTS_COLOR });
            setToolTip();

        }

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
        int x = (int) ((closestIntersection.getLongitude() - mapOrigin.get("x")) * ratio + canvasOrigin.get("x"));
        int y = (int) ((closestIntersection.getLatitude() - mapOrigin.get("y")) * ratio + canvasOrigin.get("y"));
        GraphicsContext gc = map.getGraphicsContext2D();
        gc.setFill(color);
        gc.strokeOval(x, y, 5, 5);
        gc.fillOval(x, y, 5, 5);

        //map.setOnContextMenuRequested(e -> menu.show(map.getScene().getWindow(), e.getScreenX(), e.getScreenY()));
    }

    private void drawAll() {

        drawMap();
        drawPassagePoints();
        drawDeliveryMenPaths();
        drawOveredPassagePoints();

    }

    private void clearCanvas(Canvas canvas) {

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

    }

    private void setToolTip() {

      Canvas overEffects = (Canvas) SCENE.lookup("#overEffects");
      Bounds bounds = overEffects.localToScreen(overEffects.getBoundsInLocal());
      Map<String, Integer> coordinates = getMapCoordinatesFromWorldCoordinates(Map.ofEntries(
        Map.entry("x", (double) closestPassagePoint.get("x")),
        Map.entry("y", (double) closestPassagePoint.get("y") )
      ));

      String type = closestPassagePoint.get("type") == PassagePointType.PICKUP ? "Pickup" : "Delivery";
      int order = (int) closestPassagePoint.get("order");
      String address = (String) closestPassagePoint.get("address");
      double latitude = (double) closestPassagePoint.get("x");
      double longitude = (double) closestPassagePoint.get("y");
      int minutes = (int) closestPassagePoint.get("duration") / 60;
      int seconds = (int) closestPassagePoint.get("duration") - minutes * 60;

      tooltip.setX(coordinates.get("x") + bounds.getMinX());
      tooltip.setY(coordinates.get("y")+ bounds.getMinY());
      tooltip.setText(
        type + " (" + order + ")                 " +
        " Adresse : " + address +
        " Latitude : " + latitude +
        " Longitude : " + longitude +
        " Durée : " + minutes + "mins " + seconds + "secs"
      );

    }

    public void openFileChooser() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importer des points relais au format XML");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml file", "*.xml"));

        File file = fileChooser.showOpenDialog(null);

        if(file != null && file.getName().endsWith(".xml")) {

            Canvas overEffects = (Canvas) SCENE.lookup("#overEffects");

            PlanningRequestController.importPlanningRequest(file);
            System.out.println("Start drawing P&D points");
            drawPassagePoints();

            Button btnPath = (Button) SCENE.lookup("#btnPath");
            if(btnPath != null) {
                btnPath.setDisable(false);
            }

            Tooltip.install(overEffects, tooltip);

        }

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
            state.canvasClick(this);
        }
    }

    private ChangeListener<? super Number> onWidthResize() {

        return (obs, oldVal, newVal) -> {

            Canvas map = (Canvas) SCENE.lookup("#map");
            Canvas deliverymenPaths = (Canvas) SCENE.lookup("#deliverymenPaths");
            Canvas passagePoints = (Canvas) SCENE.lookup("#passagePoints");
            Canvas overEffects = (Canvas) SCENE.lookup("#overEffects");

            map.setWidth(newVal.doubleValue());
            deliverymenPaths.setWidth(newVal.doubleValue());
            passagePoints.setWidth(newVal.doubleValue());
            overEffects.setWidth(newVal.doubleValue());

            setMapParameters(map);
            drawAll();

        };

    }

    private ChangeListener<? super Number> onHeightResize() {

        return (obs, oldVal, newVal) -> {

            VBox borderPaneTop = (VBox) SCENE.lookup("#borderPaneTop");
            HBox borderPaneBottom = (HBox) SCENE.lookup("#borderPaneBottom");

            Canvas map = (Canvas) SCENE.lookup("#map");
            Canvas deliverymenPaths = (Canvas) SCENE.lookup("#deliverymenPaths");
            Canvas passagePoints = (Canvas) SCENE.lookup("#passagePoints");
            Canvas overEffects = (Canvas) SCENE.lookup("#overEffects");

            double height = newVal.doubleValue() - borderPaneTop.getHeight() - borderPaneBottom.getHeight();

            map.setHeight(height);
            deliverymenPaths.setHeight(height);
            passagePoints.setHeight(height);
            overEffects.setHeight(height);

            setMapParameters(map);
            drawAll();

        };

    }

    private EventHandler<? super MouseEvent> onMouseMove() {

        return e -> {

            if(!PlanningRequestController.isEmpty()) {

                Map<String, Double> coordinates = getWorldCoordinatesFromMapCoordinates(e.getX(), e.getY());
                closestPassagePoint = PlanningRequestController.getClosestPassagePoint(coordinates);

                drawOveredPassagePoints();

            }

        };

    }

    @FXML
    public void onCanvasClick(MouseEvent event) {

        if(state instanceof MainViewAddPickupState) {
            userDrawPoint((Canvas)SCENE.lookup("#map"), Color.RED, event.getX(), event.getY());
            setState(new MainViewAddDeliveryState());
        } else if(state instanceof MainViewAddDeliveryState) {
            userDrawPoint((Canvas)SCENE.lookup("#map"), Color.BLUE, event.getX(), event.getY());
            setState(new MainViewWaitingState());
        }

        if(state != null) {
            state.canvasClick(this);
        }

    }

    private EventHandler<ActionEvent> onDeletePassagePoint() {

        return e -> {

            PlanningRequestController.deleteOneRequest(closestPassagePoint);
            drawPassagePoints();
            drawDeliveryMenPaths();
            clearCanvas((Canvas) SCENE.lookup("#overEffects"));

        };

    }

    private EventHandler<ActionEvent> onChangeOrder() {

        return e -> {

          if(PlanningRequestController.isCalculated()) {

            TextInputDialog textInputDialog = new TextInputDialog(closestPassagePoint.get("order").toString());

            textInputDialog.setHeaderText("Ordre de passage");
            textInputDialog.setContentText("Ordre : ");

            Optional<String> order = textInputDialog.showAndWait();

            order.ifPresent(this::onCloseChangeOrder);

          }

        };

    }

    private void onCloseChangeOrder(String stringOrder) {

      int order = Integer.parseInt(stringOrder);

      if(PlanningRequestController.changeOrder(closestPassagePoint, order)) {

        closestPassagePoint = Map.ofEntries(

          Map.entry("type", closestPassagePoint.get(("type"))),

          Map.entry("x", closestPassagePoint.get(("x"))),
          Map.entry("y", closestPassagePoint.get(("y"))),

          Map.entry("order", order)

        );

        drawDeliveryMenPaths();
        drawPassagePoints();

      }

    }

    public void onCalculate() {

        int deliveryMenNumber = Integer.parseInt(((TextField) SCENE.lookup("#nbLivreurs")).getText());
        System.out.println("nb de livreurs :" + deliveryMenNumber);

        System.out.println("Start calculating deliverymen paths");
        PlanningRequestController.calculateDeliveryMenPaths(deliveryMenNumber);

        drawDeliveryMenPaths();

    }

    public void quit() {
        System.exit(0);
    }

}
