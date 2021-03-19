package fr.insa.lyon.ifa1.controller;

import fr.insa.lyon.ifa1.models.request.PassagePoint;
import fr.insa.lyon.ifa1.models.request.PlanningRequest;
import fr.insa.lyon.ifa1.cache.GeoMapRegistry;
import javafx.fxml.FXML;

import java.util.*;

import fr.insa.lyon.ifa1.models.map.GeoMap;
import fr.insa.lyon.ifa1.models.map.Intersection;
import fr.insa.lyon.ifa1.models.map.Segment;

import java.util.stream.Collectors;

public class MainViewController extends Observable {

    private ViewController vc;
    private GeoMap geoMap;
    private PlanningRequest planningRequest;

    public MainViewController() { this.geoMap = GeoMapRegistry.getGeoMap(); }

    @FXML
    public void initialize() {
        vc = ViewController.getInstance();
        this.addObserver(vc);
    }

    public List<Map<String, Map<String, Double>>> getSegments() {

        return this.geoMap.getSegments().stream().map(segment -> Map.ofEntries(
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

    public List<Map<String, Double>> getDepot() {

        Intersection depotAddress = this.planningRequest.getDepot().getAddress();

        return Arrays.asList(Map.ofEntries(
                Map.entry("latitude", depotAddress.getLatitude()),
                Map.entry("longitude", depotAddress.getLongitude())
        ));

    }

    public List<List<Map<String, Map<String, Double>>>> getPassagePoints() {

        PassagePoint[] passagePoints = this.planningRequest.getPassagePoints();
        List<List<Map<String, Map<String, Double>>>> passagePointsData = new ArrayList<>();

        for(int i = 1; i < passagePoints.length - 1; i += 2) {

            Intersection pickupAddress = passagePoints[i].getAddress();
            Intersection deliveryAddress = passagePoints[i+1].getAddress();

            passagePointsData.add(new ArrayList<>() {{
                add(Map.ofEntries(
                        Map.entry("pickup", Map.ofEntries(
                                Map.entry("latitude", pickupAddress.getLatitude()),
                                Map.entry("longitude", pickupAddress.getLongitude())
                        ))
                ));
                add(Map.ofEntries(
                        Map.entry("delivery", Map.ofEntries(
                                Map.entry("latitude", deliveryAddress.getLatitude()),
                                Map.entry("longitude", deliveryAddress.getLongitude())
                        ))
                ));
            }});

        }

        return passagePointsData;

    }

    public Map<String, Map<String, Double>> getRange() {

        Double minLatitude = -180., maxLatitude = 180., minLongitude = -180., maxLongitude = 180.;

        for (Intersection intersection : this.geoMap.getIntersections()) {

            Double latitude = intersection.getLatitude();
            Double longitude = intersection.getLongitude();

            if (latitude < minLatitude) { minLatitude = latitude; }
            else if (latitude > maxLatitude) { maxLatitude = latitude; }

            if (longitude < minLongitude) { minLongitude = longitude; }
            else if (longitude > maxLongitude) { maxLongitude = longitude; }

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