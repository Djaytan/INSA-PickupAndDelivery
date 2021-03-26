package fr.insa.lyon.ifa1.controller;

import fr.insa.lyon.ifa1.algo.Dijkstra;
import fr.insa.lyon.ifa1.algo.FindShortestHamiltonianCircuit;
import fr.insa.lyon.ifa1.algo.FindShortestRoutes;
import fr.insa.lyon.ifa1.algo.OptimalHamiltonianCircuit;
import fr.insa.lyon.ifa1.models.map.GeoMap;
import fr.insa.lyon.ifa1.models.map.Intersection;
import fr.insa.lyon.ifa1.models.request.*;
import fr.insa.lyon.ifa1.models.map.Segment;
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

    private static final PlanningRequest PLANNING_REQUEST = new PlanningRequest();
    private static final Dijkstra DIJKSTRA = new Dijkstra();
    private static final FindShortestHamiltonianCircuit HAMILTONIAN_CIRCUIT = new OptimalHamiltonianCircuit(5000);

    public static PlanningRequest getModel() { return PLANNING_REQUEST; }

    public static Request tmpRequest = null;
    
    public static void importPlanningRequest(File file) {

        try { XMLDeserialization.deserializeRequests(file); }
        catch (SAXException e)
        { LOGGER.log(Level.SEVERE, "Error during XML map file content reading", e); }
        catch (ParserConfigurationException e)
        { LOGGER.log(Level.SEVERE, "Something went wrong in map XML parser configuration", e); }
        catch (IOException e)
        { LOGGER.log(Level.SEVERE, "Error during XML map file manipulation", e); }

    }

    public static Map<String, Double> getDepot() {

        Intersection depotAddress = PLANNING_REQUEST.getDepot().getAddress();

        return Map.ofEntries(
                Map.entry("x", depotAddress.getLongitude()),
                Map.entry("y", depotAddress.getLatitude())
        );

    }

    public static List<Map<String, Map<String, Double>>> getPassagePoints() {

        PassagePoint[] passagePoints = PLANNING_REQUEST.getPassagePoints();
        List<Map<String, Map<String, Double>>> passagePointsData = new ArrayList<>() {{
            add(Map.ofEntries(Map.entry("depot", PlanningRequestController.getDepot())));
        }};

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

    public static PassagePoint getClosestPassagePoint(double x, double y) {
        PassagePoint closestPassagePoint = null;
        double distance = Double.MAX_VALUE;
        double tmpDistance;

        PassagePoint[] passagePoints = PLANNING_REQUEST.getPassagePoints();

        //loop on all passage point to find the closest one to x,y
        for (PassagePoint passagePoint : passagePoints) {

            tmpDistance = Math.sqrt((y - passagePoint.getAddress().getLatitude()) * (y - passagePoint.getAddress().getLatitude()) + (x - passagePoint.getAddress().getLongitude()) * (x - passagePoint.getAddress().getLongitude()));
            if(tmpDistance < distance) {
                closestPassagePoint = passagePoint;
                distance = tmpDistance;
            }
        }

        return closestPassagePoint;
    }

    public static void addPickupPoint(Intersection intersection) {
        tmpRequest.setPickup(new DurationPassagePoint(intersection, 5, PassagePointType.PICKUP));
    }

    public static void addDeliveryPoint(Intersection intersection) {
        tmpRequest.setDelivery(new DurationPassagePoint(intersection, 5, PassagePointType.DELIVERY));
    }

    public static boolean commit() {
        if(tmpRequest != null) {
            PLANNING_REQUEST.addRequest(tmpRequest);
            return true;
        }
       return false;
    }

    public static void undo() {
        tmpRequest = null;
    }

    public static void begin() {
        if(tmpRequest == null) {
            tmpRequest = new Request();
        }
    }

    public static List<List<Map<String, Map<String, Double>>>> getDeliveryMenPaths() {

        final GeoMap geoMap = GeoMapController.getModel();
        final Map<String, Map<String, FindShortestRoutes.Route>> dijkstraRoutes = DIJKSTRA.solve(geoMap, PLANNING_REQUEST.getPassagePoints());
        final List<List<PassagePoint>> passagePointsList = new ArrayList<>() {{
            add(HAMILTONIAN_CIRCUIT.solve(geoMap, dijkstraRoutes, PLANNING_REQUEST));
        }};

        List<List<Map<String, Map<String, Double>>>> segmentsList = new ArrayList<>();

        for (List<PassagePoint> passagePoints : passagePointsList) {

            List<Map<String, Map<String, Double>>> segments = new ArrayList<>();

            for (int i = 0; i < passagePoints.size() - 1; i++) {

                String origin = passagePoints.get(i).getAddress().getId();
                String destination = passagePoints.get(i + 1).getAddress().getId();
                FindShortestRoutes.Route route = dijkstraRoutes.get(origin).get(destination);

                for (Segment segment : route.getItinerary()) {

                    segments.add(Map.ofEntries(
                            Map.entry("origin", Map.ofEntries(
                                    Map.entry("x", segment.getOrigin().getLongitude()),
                                    Map.entry("y", segment.getOrigin().getLatitude())
                            )),
                            Map.entry("destination", Map.ofEntries(
                                    Map.entry("x", segment.getDest().getLongitude()),
                                    Map.entry("y", segment.getDest().getLatitude())
                            ))
                    ));

                }

            }

            segmentsList.add(segments);

        }

        return segmentsList;

    }


    static public void deleteOneRequest(Map<String,Double> point){
        final GeoMap geoMap = GeoMapController.getModel();

        // suppression de la course
        List<Request> requests = PLANNING_REQUEST.getRequests();
        for (Request request : PLANNING_REQUEST.getRequests()) {
            if (point.get("x").equals(request.getPickup().getAddress().getLongitude())
                    && point.get("y").equals(request.getDelivery().getAddress().getLatitude())){
                requests.remove(request);
                break;
            }
        }
        PLANNING_REQUEST.setRequests(requests);

        // maj du circuit hamiltonien
        final Map<String, Map<String, FindShortestRoutes.Route>> dijkstraRoutes = DIJKSTRA.solve(geoMap, PLANNING_REQUEST.getPassagePoints());
        HAMILTONIAN_CIRCUIT.solve(geoMap, dijkstraRoutes, PLANNING_REQUEST);
    }


}
