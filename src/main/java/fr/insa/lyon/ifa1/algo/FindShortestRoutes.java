package fr.insa.lyon.ifa1.algo;

import fr.insa.lyon.ifa1.models.map.GeoMap;
import fr.insa.lyon.ifa1.models.map.Segment;
import fr.insa.lyon.ifa1.models.request.PassagePoint;

import java.util.List;
import java.util.Map;

public interface FindShortestRoutes {

    Map<String, Map<String, Route>> solve(GeoMap gm, PassagePoint[] pps);

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
