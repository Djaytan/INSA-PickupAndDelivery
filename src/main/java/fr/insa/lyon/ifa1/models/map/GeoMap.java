package fr.insa.lyon.ifa1.models.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GeoMap {
    private Map<String, Intersection> intersections;
    private Map<String, Map<String, Segment>> segments;

    public GeoMap(Map<String, Intersection> intersections, Map<String, Map<String, Segment>>  segments) {
        this.intersections = intersections;
        this.segments = segments;
    }

    public Collection<Intersection> getIntersections() {
        return intersections.values();
    }

    public Intersection getIntersection(String id) {
        return intersections.get(id);
    }

    public Collection<Segment> getSegments() {
        return segments.values().stream().flatMap(e -> e.values().stream()).collect(Collectors.toList());
    }

    public Segment getSegment(String origin, String dest) {
        if (!segments.containsKey(origin)) return null;
        return segments.get(origin).get(dest);
    }
}
