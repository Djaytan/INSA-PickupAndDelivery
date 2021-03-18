package fr.insa.lyon.ifa1.models.map;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GeoMap {
    private Map<String, Intersection> intersections;
    private Map<String, Map<String, Segment>> segments;

    public GeoMap() {
        this.intersections = new HashMap<>();
        this.segments = new HashMap<>();
    }

    public GeoMap(Map<String, Intersection> intersections, Map<String, Map<String, Segment>>  segments) {
        this.intersections = intersections;
        this.segments = segments;
    }

    public Map<String, Intersection> getIntersections() {
        return intersections;
    }

    /**
     * Adds an {@link Intersection} to the {@link GeoMap}.
     * 
     * @param intersection The {@link Intersection} to add.
     */
    public void addIntersection(Intersection intersection) {
        this.intersections.put(intersection.getId(), intersection);
    }

    /**
     * Searches and returns the {@link Intersection} corresponding to the specified id.
     * 
     * @param idIntersection The id of the targeted {@link Intersection}.
     * @return The {@link Intersection} found with the specified id, or null.
     */
    public Intersection getIntersection(String idIntersection) {
        Intersection intersection = null;
        if (this.intersections.containsKey(idIntersection)) {
            intersection = this.intersections.get(idIntersection);
        }
        return intersection;
    }

    public List<Segment> getSegments() {
        return segments.values().stream().flatMap(e -> e.values().stream()).collect(Collectors.toList());
    }

    /**
     * Adds a {@link Segment} to the {@link GeoMap}.
     * 
     * @param segment The {@link Segment} to add.
     */
    public void addSegment(Segment segment) {
        Map<String, Segment> map = new HashMap<>();
        map.put(segment.getDest().getId(), segment);
        this.segments.put(segment.getOrigin().getId(), map);
    }

    public Segment getSegment(String origin, String dest) {
        if (!segments.containsKey(origin)) return null;
        return segments.get(origin).get(dest);
    }
}
