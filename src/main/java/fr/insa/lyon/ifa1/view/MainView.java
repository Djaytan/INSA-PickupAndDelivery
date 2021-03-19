package fr.insa.lyon.ifa1.view;

import fr.insa.lyon.ifa1.controller.MainViewController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

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

        BorderPane pane = new BorderPane();

        // Top Pane
        BorderPane topPane = new BorderPane();

        Text title = new Text("Pick'Up");
        title.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        topPane.setCenter(title);

        HBox topBottomPane = new HBox();
        topBottomPane.setAlignment(Pos.BOTTOM_RIGHT);

        Button addBtn = new Button();
        addBtn.setText("Ajouter une livraison");
        topBottomPane.getChildren().add(addBtn);

        topPane.setBottom(topBottomPane);

        pane.setTop(topPane);

        // Center Pane
        Canvas map = new Canvas(750, 300);
        drawSegments(map);
        pane.setCenter(map);

        // Bottom Pane
        HBox bottomPane = new HBox();
        bottomPane.setAlignment(Pos.BASELINE_LEFT);

        Text text = new Text("Importer des points relais à partir d'un fichier XML");
        text.setFont(Font.font("Tahoma", FontWeight.NORMAL, 10));
        bottomPane.getChildren().add(text);

        Button importButton = new Button();
        importButton.setText("Sélectionner un fichier");
        bottomPane.getChildren().add(importButton);

        pane.setBottom(bottomPane);

        primaryStage.setScene(new Scene(pane, 750, 400));
        primaryStage.show();

    }

    private void drawSegments(Canvas map) {

        GraphicsContext gc = map.getGraphicsContext2D();
        List<Map<String, Map<String, Double>>> segments = this.controller.getSegments();

        gc.setFill(Color.BLACK);
        gc.setLineWidth(1.0);

        for(Map<String, Map<String, Double>> segment : segments) {

            Integer x1 = (int)((segment.get("origin").get("latitude") - 45.7) * 10000) - 750;
            Integer y1 = (int)((segment.get("origin").get("longitude") - 4.8) * 10000) - 800;
            Integer x2 = (int)((segment.get("destination").get("latitude") - 45.7) * 10000) - 750;
            Integer y2 = (int)((segment.get("destination").get("longitude") - 4.8) * 10000) - 800;

            System.out.println("Drawing segment from " + x1 + " " + y1 + " to " + x2 + " " + y2);

            gc.strokeLine(x1, y1, x2, y2);

        }

    }

}
