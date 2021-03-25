package fr.insa.lyon.ifa1.controller;

import fr.insa.lyon.ifa1.models.map.Intersection;
import fr.insa.lyon.ifa1.models.request.DurationPassagePoint;
import fr.insa.lyon.ifa1.models.request.PassagePoint;
import fr.insa.lyon.ifa1.models.request.PlanningRequest;
import fr.insa.lyon.ifa1.models.request.Request;
import fr.insa.lyon.ifa1.xml.XMLDeserialization;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlanningRequestController {

    private static final Logger LOGGER = Logger.getLogger(GeoMapController.class.getName());

    private static final PlanningRequest MODEL = new PlanningRequest();

    public static PlanningRequest getModel() { return MODEL; }

    public static Request tmpRequest = null;

    public Map<String, Double> getDepot() {

        Intersection depotAddress = MODEL.getDepot().getAddress();

        return Map.ofEntries(
                Map.entry("x", depotAddress.getLongitude()),
                Map.entry("y", depotAddress.getLatitude())
        );

    }

    public List<Map<String, Map<String, Double>>> getPassagePoints() {

        PassagePoint[] passagePoints = MODEL.getPassagePoints();
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

    public void addPickupPoint(Intersection intersection) {
        tmpRequest.setPickup(new DurationPassagePoint(intersection, 5, "pickup"));
    }

    public void addDeliveryPoint(Intersection intersection) {
        tmpRequest.setDelivery(new DurationPassagePoint(intersection, 5, "delivery"));
    }

    public boolean commit() {
        if(tmpRequest != null) {
            MODEL.addRequest(tmpRequest);
            return true;
        }
       return false;
    }

    public void undo() {
        tmpRequest = null;
    }

    public void begin() {
        if(tmpRequest == null) {
            tmpRequest = new Request();
        }
    }

    public void importPlanningRequest(File file) {

        try { XMLDeserialization.deserializeRequests(file); }
        catch (SAXException e)
        { LOGGER.log(Level.SEVERE, "Error during XML map file content reading", e); }
        catch (ParserConfigurationException e)
        { LOGGER.log(Level.SEVERE, "Something went wrong in map XML parser configuration", e); }
        catch (IOException e)
        { LOGGER.log(Level.SEVERE, "Error during XML map file manipulation", e); }

    }

}
