package fr.insa.lyon.ifa1.models.map;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GeoMap {
    private Map<String, Intersection> intersections;
    private Map<String, Map<String, Segment>> segments;

    public GeoMap() {

        this.intersections = Map.ofEntries(
            Map.entry("2509481775", new Intersection("2509481775", 45.775345, 4.8870163)),
            Map.entry("2684668925", new Intersection("2684668925", 45.775486, 4.888253)),
            Map.entry("190823183", new Intersection("190823183", 45.774925, 4.8882933))
        );

        this.segments = Map.ofEntries(
                Map.entry("2684668925", Map.ofEntries(
                        Map.entry("2509481775", new Segment(97.249695, "Rue Château-Gaillard", this.intersections.get("2684668925"), this.intersections.get("2509481775"))),
                        Map.entry("190823183", new Segment(62.401424, "Rue Château-Gaillard", this.intersections.get("2684668925"), this.intersections.get("190823183")))
                ))
        );

    }

    public GeoMap(Map<String, Intersection> intersections, Map<String, Map<String, Segment>>  segments) {
        this.intersections = intersections;
        this.segments = segments;
    }

    public Map<String, Intersection> getIntersections() {
        return intersections;
    }

    public List<Segment> getSegments() {
        return segments.values().stream().flatMap(e -> e.values().stream()).collect(Collectors.toList());
    }

    public Segment getSegment(String origin, String dest) {
        if (!segments.containsKey(origin)) return null;
        return segments.get(origin).get(dest);
    }
}
