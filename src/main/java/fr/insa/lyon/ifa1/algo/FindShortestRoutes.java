package fr.insa.lyon.ifa1.algo;

import fr.insa.lyon.ifa1.models.map.GeoMap;
import fr.insa.lyon.ifa1.models.map.Segment;
import fr.insa.lyon.ifa1.models.request.PassagePoint;

import java.util.List;
import java.util.Map;

public interface FindShortestRoutes {


  /**
   * Find all shortest routes between every given passage points
   *
   * @param gm The GeoMap model
   * @param pps The passage points which will be used to compute the shortest route.
   * @return A map of key=(originId, destinationId) and value=ShortestRoute
   */
  Map<String, Map<String, Route>> solve(GeoMap gm, PassagePoint[] pps);

  /**
   * Describe a shortest route between an origin and a destination with its itinerary and its length
   */
  class Route {
    private String origin;
    private String destination;
    private List<Segment> itinerary;
    private double length;

    public Route(String origin, String destination, List<Segment> itinerary, double length) {
      this.origin = origin;
      this.destination = destination;
      this.itinerary = itinerary;
      this.length = length;
    }

    public String getOrigin() {
      return origin;
    }

    public String getDestination() {
      return destination;
    }

    public List<Segment> getItinerary() {
      return itinerary;
    }

    public double getLength() {
      return length;
    }
  }


}
