package fr.insa.lyon.ifa1.controller;

import javafx.fxml.FXML;
import java.util.Observable;
import fr.insa.lyon.ifa1.models.map.GeoMap;
import fr.insa.lyon.ifa1.models.map.Intersection;
import fr.insa.lyon.ifa1.models.map.Segment;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainViewController extends Observable {

    private ViewController vc;
    private GeoMap model;

    public MainViewController() {
        this.model = new GeoMap();
    }

    public MainViewController(Map<String, Intersection> intersections, Map<String, Map<String, Segment>> segments) {
        this.model = new GeoMap(intersections, segments);
    }

    @FXML
    public void initialize() {
        vc = ViewController.getInstance();
        this.addObserver(vc);
    }

    public List<Map<String, Map<String, Double>>> getSegments() {

        return this.model.getSegments().stream().map(segment -> Map.ofEntries(
                Map.entry("origin", Map.ofEntries(
                        Map.entry("latitude", segment.getOrigin().getLatitude()),
                        Map.entry("longitude", segment.getOrigin().getLongitude())
                )),
                Map.entry("destination", Map.ofEntries(
                        Map.entry("latitude", segment.getDest().getLatitude()),
                        Map.entry("longitude", segment.getDest().getLongitude())
                ))
        )).collect(Collectors.toList());

    }

    public Map<String, Map<String, Double>> getRange() {

        Double minLatitude = -180., maxLatitude = 180., minLongitude = -180., maxLongitude = 180.;

        for (Intersection intersection : this.model.getIntersections().values()) {

            Double latitude = intersection.getLatitude();
            Double longitude = intersection.getLongitude();

            if (latitude < minLatitude) {
                minLatitude = latitude;
            } else if (latitude > maxLatitude) {
                maxLatitude = latitude;
            }

            if (longitude < minLongitude) {
                minLongitude = longitude;
            } else if (longitude > maxLongitude) {
                maxLongitude = longitude;
            }

        }

        return Map.ofEntries(
                Map.entry("latitude", Map.ofEntries(
                        Map.entry("min", minLatitude),
                        Map.entry("max", maxLatitude)
                )),
                Map.entry("longitude", Map.ofEntries(
                        Map.entry("min", minLongitude),
                        Map.entry("max", maxLongitude)
                ))
        );

    }

}