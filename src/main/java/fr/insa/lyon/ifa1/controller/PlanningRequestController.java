package fr.insa.lyon.ifa1.controller;

import fr.insa.lyon.ifa1.algo.*;
import fr.insa.lyon.ifa1.models.map.GeoMap;
import fr.insa.lyon.ifa1.models.map.Intersection;
import fr.insa.lyon.ifa1.models.request.*;
import fr.insa.lyon.ifa1.models.map.Segment;
import fr.insa.lyon.ifa1.xml.XMLDeserialization;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlanningRequestController {

    private static final Logger LOGGER = Logger.getLogger(GeoMapController.class.getName());

    private static final PlanningRequest PLANNING_REQUEST = new PlanningRequest();
    private static final Dijkstra DIJKSTRA = new Dijkstra();
    private static FindShortestHamiltonianCircuit hamiltonianCircuitFinder;
    private static final AddPickupAndDeliveryImpl ADD_PICKUP_AND_DELIVERY = new AddPickupAndDeliveryImpl();

    private static Map<String, Map<String, FindShortestRoutes.Route>> dijkstraRoutes;
    private static List<PassagePoint> hamiltonianCircuit = new ArrayList<>();

    public static PlanningRequest getModel() { return PLANNING_REQUEST; }

    private static Request tmpRequest = null;

    private static int deliveryMenSpeed;
    
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

    public static boolean isCalculated() { return !hamiltonianCircuit.isEmpty(); }

    public static Map<String, Double> getDepot() {

        Intersection depotAddress = PLANNING_REQUEST.getDepot().getAddress();

        return Map.ofEntries(
                Map.entry("x", depotAddress.getLongitude()),
                Map.entry("y", depotAddress.getLatitude())
        );

    }

    public static Depot getRealDepot() { return PLANNING_REQUEST.getDepot(); }

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

    public static Map<String, Object> getClosestPassagePoint(Map<String, Double> coordinates) {

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

                  Map.entry("type", closestPassagePoint.getType()),
                  Map.entry("address", closestPassagePoint.getAddress().getId()),
                  Map.entry("duration", closestPassagePoint.getDuration()),

                  Map.entry("x", closestPassagePoint.getAddress().getLongitude()),
                  Map.entry("y", closestPassagePoint.getAddress().getLatitude()),

                  Map.entry("order", getPassagePointOrder(closestPassagePoint))

        );

    }

    private static int getPassagePointOrder(PassagePoint passagePoint) {

      int order = 0;

      for(int i = 1; i <= hamiltonianCircuit.size(); i++) {

        if(passagePoint == hamiltonianCircuit.get(i))
        { order = i; break; }

      }

      return order;

    }

  public static PassagePoint getRealClosestPassagePoint(Map<String, Double> coordinates) {

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

    return closestPassagePoint;

  }

    public static void addPickupPoint(Intersection intersection, int duration) {
        tmpRequest.setPickup(new DurationPassagePoint(intersection, duration, PassagePointType.PICKUP));
    }

    public static void addDeliveryPoint(Intersection intersection, int duration) {
        tmpRequest.setDelivery(new DurationPassagePoint(intersection, duration, PassagePointType.DELIVERY));
    }

    public static boolean commit() {
        if(tmpRequest != null) {
            if(!hamiltonianCircuit.isEmpty()) {
                //TODO : crochet
              final GeoMap geoMap = GeoMapController.getModel();
              ADD_PICKUP_AND_DELIVERY.solve(geoMap, PLANNING_REQUEST, tmpRequest, hamiltonianCircuit, dijkstraRoutes);
              //hamiltonianCircuit.add()
            } else {
              PLANNING_REQUEST.addRequest(tmpRequest);
            }
            return true;
        }
       return false;
    }

    public static void undo() {
        tmpRequest = null;
    }

    public static void begin() {
        tmpRequest = new Request();
    }

    public static void calculateDeliveryMenPaths(int deliveryMenNumber, int dmSpeed, int computationTime) {

        final GeoMap geoMap = GeoMapController.getModel();

        deliveryMenSpeed = dmSpeed;
        hamiltonianCircuitFinder = new OptimalHamiltonianCircuit(computationTime * 1000);
        dijkstraRoutes = DIJKSTRA.solve(geoMap, PLANNING_REQUEST.getPassagePoints());
        hamiltonianCircuit = hamiltonianCircuitFinder.solve(geoMap, dijkstraRoutes, PLANNING_REQUEST);

    }

    public static List<PassagePoint> getHamiltonianCircuit() {
        return hamiltonianCircuit;
    }

    public static List<String> getDeliveryTableViewAdress() {
        List<String> adressList = new ArrayList<>();
        String tmpSegmentName;

        if(hamiltonianCircuit.size() > 1) {
            for (int i = 0; i < hamiltonianCircuit.size() - 1; i++) {

                String origin = hamiltonianCircuit.get(i).getAddress().getId();
                String destination = hamiltonianCircuit.get(i + 1).getAddress().getId();
                FindShortestRoutes.Route route = dijkstraRoutes.get(origin).get(destination);

                if(route.getItinerary().size() > 0) {
                    tmpSegmentName = route.getItinerary().get(0).getName();
                    adressList.add(tmpSegmentName);
                    for (int j = 1; j < route.getItinerary().size() - 1; j++) {
                        if(!tmpSegmentName.equals(route.getItinerary().get(j).getName())) {
                            adressList.add(route.getItinerary().get(j).getName());
                            tmpSegmentName = route.getItinerary().get(j).getName();
                        }
                    }
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

    private static Request getRequest(Map<String, Object> passagePointData) {

      Request result = null;

      for(Request request : PLANNING_REQUEST.getRequests()) {

        double x = (double) passagePointData.get("x");
        double y = (double) passagePointData.get("y");

        boolean passagePointIsPickup = passagePointData.get("type").equals(PassagePointType.PICKUP);
        boolean passagePointIsDelivery = passagePointData.get("type").equals(PassagePointType.DELIVERY);

        double pickupX = request.getPickup().getAddress().getLongitude();
        double pickupY = request.getPickup().getAddress().getLatitude();
        double deliveryX = request.getDelivery().getAddress().getLongitude();
        double deliveryY = request.getDelivery().getAddress().getLatitude();

        if(passagePointIsPickup && x == pickupX && y == pickupY ||
           passagePointIsDelivery && x == deliveryX && y == deliveryY)
        { result = request; break; }

      }

      return result;

    }

    public static Map<PassagePointType, Map<String, Double>> getCouplePassagePoints(Map<String, Object> passagePointData) {

        Request request = getRequest(passagePointData);

        Intersection pickupAddress = request.getPickup().getAddress();
        Intersection deliveryAddress = request.getDelivery().getAddress();

        return Map.ofEntries(

          Map.entry(PassagePointType.PICKUP, Map.ofEntries(
            Map.entry("x", pickupAddress.getLongitude()),
            Map.entry("y", pickupAddress.getLatitude())
          )),

          Map.entry(PassagePointType.DELIVERY, Map.ofEntries(
            Map.entry("x", deliveryAddress.getLongitude()),
            Map.entry("y", deliveryAddress.getLatitude())
          ))

        );

    }

     public static void deleteOneRequest(Map<String, Object> passagePoint) {

        // suppression de la course
        Map<String, Double> coordinates = Map.ofEntries(
          Map.entry("x", (double) passagePoint.get("x")),
          Map.entry("y", (double) passagePoint.get("y"))
        );
        List<Request> requests = PLANNING_REQUEST.getRequests();

        for(Request request : requests) {

            double x = coordinates.get("x");
            double y = coordinates.get("y");

            Intersection pickupAddress = request.getPickup().getAddress();
            Intersection deliveryAddress = request.getDelivery().getAddress();

            if(passagePoint.get("type").equals(PassagePointType.PICKUP) && x == pickupAddress.getLongitude() && y == pickupAddress.getLatitude() ||
              passagePoint.get("type").equals(PassagePointType.DELIVERY) && x == deliveryAddress.getLongitude() && y == deliveryAddress.getLatitude()) {

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

    public static boolean changeOrder(Map<String, Object> passagePointData, int order) {

      PassagePoint passagePoint;
      Request request = getRequest(passagePointData);

      if (passagePointData.get("type") == PassagePointType.PICKUP) {

        passagePoint = request.getPickup();
        if (order >= getPassagePointOrder(request.getDelivery())) {
          return false;
        }

      } else {

        passagePoint = request.getDelivery();
        if (order <= getPassagePointOrder(request.getPickup())) {
          return false;
        }

      }

      hamiltonianCircuit.remove(passagePoint);
      hamiltonianCircuit.add(order, passagePoint);

      return true;

    }

    public static void deplacerPoint(PassagePoint pointToMove, Intersection intersection, int deliveryMenSpeed, int computationTime) {
      pointToMove.setAddress(intersection);

      // maj du circuit hamiltonien
      if(PlanningRequestController.isCalculated()){
        //@TODO: remplacer la maj du circuit hamiltonien par traitement avec algo plus léger
        calculateDeliveryMenPaths(1, deliveryMenSpeed, computationTime * 1000);
      }


    }

    public static double getTravelDuration(PassagePoint origin, PassagePoint destination) {

      return dijkstraRoutes.get(origin.getAddress().getId()).get(destination.getAddress().getId()).getLength() / (deliveryMenSpeed / 3.6);

    }

}
