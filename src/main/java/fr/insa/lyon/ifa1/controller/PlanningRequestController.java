package fr.insa.lyon.ifa1.controller;

import fr.insa.lyon.ifa1.models.map.Intersection;
import fr.insa.lyon.ifa1.models.request.PassagePoint;
import fr.insa.lyon.ifa1.models.request.PlanningRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlanningRequestController {

    private static PlanningRequestController instance;
    private static PlanningRequest model;

    public static PlanningRequestController getInstance() { return instance; }

    public static PlanningRequest getModel() { return model; }

    public PlanningRequestController() {

        instance = this;
        model = new PlanningRequest();

    }

    public Map<String, Double> getDepot() {

        Intersection depotAddress = model.getDepot().getAddress();

        return Map.ofEntries(
                Map.entry("x", depotAddress.getLongitude()),
                Map.entry("y", depotAddress.getLatitude())
        );

    }

    public List<Map<String, Map<String, Double>>> getPassagePoints() {

        PassagePoint[] passagePoints = model.getPassagePoints();
        List<Map<String, Map<String, Double>>> passagePointsData = new ArrayList<>();

        for(int i = 1; i < passagePoints.length - 1; i += 2) {

            Intersection pickupAddress = passagePoints[i].getAddress();
            Intersection deliveryAddress = passagePoints[i+1].getAddress();

            passagePointsData.add(Map.ofEntries(
                    Map.entry("pickup", Map.ofEntries(
                            Map.entry("x", pickupAddress.getLongitude()),
                            Map.entry("y", pickupAddress.getLatitude())
                    )),
                    Map.entry("delivery", Map.ofEntries(
                            Map.entry("x", deliveryAddress.getLongitude()),
                            Map.entry("y", deliveryAddress.getLatitude())
                    ))
            ));

        }

        return passagePointsData;

    }

}
