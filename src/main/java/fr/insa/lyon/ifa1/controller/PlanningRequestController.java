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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlanningRequestController {

    private static final Logger LOGGER = Logger.getLogger(GeoMapController.class.getName());

    private static final PlanningRequest PLANNING_REQUEST = new PlanningRequest();
    private static final Dijkstra DIJKSTRA = new Dijkstra();
    private static final FindShortestHamiltonianCircuit HAMILTONIAN_CIRCUIT_FINDER = new OptimalHamiltonianCircuit(5000);

    private static Map<String, Map<String, FindShortestRoutes.Route>> dijkstraRoutes;
    private static List<PassagePoint> hamiltonianCircuit = new ArrayList<>();

    public static PlanningRequest getModel() { return PLANNING_REQUEST; }

    private static Request tmpRequest = null;
    
    public static void importPlanningRequest(File file) {

        try { XMLDeserialization.deserializeRequests(file); }
        catch (SAXException e)
        { LOGGER.log(Level.SEVERE, "Error during XML map file content reading", e); }
        catch (ParserConfigurationException e)
        { LOGGER.log(Level.SEVERE, "Something went wrong in map XML parser configuration", e); }
        catch (IOException e)
        { LOGGER.log(Level.SEVERE, "Error during XML map file manipulation", e); }

    }

    public static boolean isEmpty() { return PLANNING_REQUEST.getRequests().isEmpty(); }

    public static Map<String, Double> getDepot() {

        Intersection depotAddress = PLANNING_REQUEST.getDepot().getAddress();

        return Map.ofEntries(
                Map.entry("x", depotAddress.getLongitude()),
                Map.entry("y", depotAddress.getLatitude())
        );

    }

    public static List<Map<PassagePointType, Map<String, Double>>> getPassagePoints() {

        PassagePoint[] passagePoints = PLANNING_REQUEST.getPassagePoints();
        List<Map<PassagePointType, Map<String, Double>>> passagePointsData = new ArrayList<>();

        if(PLANNING_REQUEST.getDepot() != null) {

            passagePointsData.add(Map.ofEntries(
                    Map.entry(PassagePointType.DEPOT, PlanningRequestController.getDepot())
            ));

            for (int i = 1; i < passagePoints.length - 1; i += 2) {

                Intersection pickupAddress = passagePoints[i].getAddress();
                Intersection deliveryAddress = passagePoints[i + 1].getAddress();

                passagePointsData.add(Map.ofEntries(
                        Map.entry(PassagePointType.PICKUP, Map.ofEntries(
                                Map.entry("x", pickupAddress.getLongitude()),
                                Map.entry("y", pickupAddress.getLatitude())
                        )),
                        Map.entry(PassagePointType.DELIVERY, Map.ofEntries(
                                Map.entry("x", deliveryAddress.getLongitude()),
                                Map.entry("y", deliveryAddress.getLatitude())
                        ))
                ));

            }

        }

        return passagePointsData;

    }

    public static Map<PassagePointType, Map<String, Double>> getClosestPassagePoint(Map<String, Double> coordinates) {

        DurationPassagePoint closestPassagePoint = null;
        double distance = Double.MAX_VALUE;
        double tmpDistance;
        double x = coordinates.get("x");
        double y = coordinates.get("y");

        //loop on all passage point to find the closest one to x,y
        for(Request request : PLANNING_REQUEST.getRequests()) {

            DurationPassagePoint[] passagePoints = new DurationPassagePoint[] {
                    request.getPickup(), request.getDelivery()
            };

            for(DurationPassagePoint passagePoint : passagePoints) {

                Intersection address = passagePoint.getAddress();

                tmpDistance = Math.sqrt(Math.pow(x - address.getLongitude(), 2) + Math.pow(y - address.getLatitude(), 2));

                if (tmpDistance < distance) {
                    closestPassagePoint = passagePoint;
                    distance = tmpDistance;
                }

            }

        }

        return Map.ofEntries(
                Map.entry(closestPassagePoint.getType(), Map.ofEntries(
                    Map.entry("x", closestPassagePoint.getAddress().getLongitude()),
                    Map.entry("y", closestPassagePoint.getAddress().getLatitude())
                ))
        );

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

    public static void calculateDeliveryMenPaths(int deliveryMenNumber) {

        final GeoMap geoMap = GeoMapController.getModel();

        dijkstraRoutes = DIJKSTRA.solve(geoMap, PLANNING_REQUEST.getPassagePoints());
        hamiltonianCircuit = HAMILTONIAN_CIRCUIT_FINDER.solve(geoMap, dijkstraRoutes, PLANNING_REQUEST);

    }

    public static List<PassagePoint> getHamiltonianCircuit() {
        return hamiltonianCircuit;
    }

    public static List<String> getDeliveryTableViewAdress() {
        List<String> adressList = new ArrayList<>();

        if(hamiltonianCircuit.size() > 1) {
            for (int i = 0; i < hamiltonianCircuit.size() - 1; i++) {

                String origin = hamiltonianCircuit.get(i).getAddress().getId();
                String destination = hamiltonianCircuit.get(i + 1).getAddress().getId();
                FindShortestRoutes.Route route = dijkstraRoutes.get(origin).get(destination);

                for (Segment segment : route.getItinerary()) {
                    adressList.add(segment.getName());
                }
            }
        }

        return adressList;
    }

    public static List<List<Map<String, Map<String, Double>>>> getDeliveryMenPaths() {

        final List<List<PassagePoint>> passagePointsList = new ArrayList<>() {{ add(hamiltonianCircuit); }};
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

    public static Map<PassagePointType, Map<String, Double>> getCouplePassagePoints(Map<PassagePointType, Map<String, Double>> passagePoint) {

        Map<PassagePointType, Map<String, Double>> couple = null;
        Optional<PassagePointType> passagePointType = passagePoint.keySet().stream().findFirst();

        if(passagePointType.isEmpty()) { return null; }

        Map<String, Double> coordinates = passagePoint.get(passagePointType.get());

        for(Request request : PLANNING_REQUEST.getRequests()) {

            double x = coordinates.get("x");
            double y = coordinates.get("y");

            Intersection pickupAddress = request.getPickup().getAddress();
            Intersection deliveryAddress = request.getDelivery().getAddress();

            if(passagePointType.get().equals(PassagePointType.PICKUP) && x == pickupAddress.getLongitude() && y == pickupAddress.getLatitude()) {

                couple = Map.ofEntries(
                        Map.entry(PassagePointType.PICKUP, coordinates),
                        Map.entry(PassagePointType.DELIVERY, Map.ofEntries(
                                Map.entry("x", deliveryAddress.getLongitude()),
                                Map.entry("y", deliveryAddress.getLatitude())
                        ))
                );

                break;

            }

            else if(passagePointType.get().equals(PassagePointType.DELIVERY) && x == deliveryAddress.getLongitude() && y == deliveryAddress.getLatitude()) {

                couple = Map.ofEntries(
                        Map.entry(PassagePointType.PICKUP, Map.ofEntries(
                                Map.entry("x", pickupAddress.getLongitude()),
                                Map.entry("y", pickupAddress.getLatitude())
                        )),
                        Map.entry(PassagePointType.DELIVERY, coordinates)
                );

                break;

            }

        }

        return couple;

    }

     public static void deleteOneRequest(Map<PassagePointType, Map<String,Double>> passagePoint) {

        // suppression de la course
        Optional<PassagePointType> passagePointType = passagePoint.keySet().stream().findFirst();

        if(passagePointType.isEmpty()) { return; }

        Map<String, Double> coordinates = passagePoint.get(passagePointType.get());
        List<Request> requests = PLANNING_REQUEST.getRequests();

        for(Request request : requests) {

            double x = coordinates.get("x");
            double y = coordinates.get("y");

            Intersection pickupAddress = request.getPickup().getAddress();
            Intersection deliveryAddress = request.getDelivery().getAddress();

            if(passagePointType.get().equals(PassagePointType.PICKUP) && x == pickupAddress.getLongitude() && y == pickupAddress.getLatitude() ||
               passagePointType.get().equals(PassagePointType.DELIVERY) && x == deliveryAddress.getLongitude() && y == deliveryAddress.getLatitude()) {

                if(!hamiltonianCircuit.isEmpty()) {
                    hamiltonianCircuit.remove(request.getPickup());
                    hamiltonianCircuit.remove(request.getDelivery());
                }

                requests.remove(request);

                break;

            }

        }

        PLANNING_REQUEST.setRequests(requests);

    }


}
