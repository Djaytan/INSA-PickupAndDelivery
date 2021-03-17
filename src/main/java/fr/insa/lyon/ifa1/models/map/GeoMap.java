package fr.insa.lyon.ifa1.models.map;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GeoMap {
    private Map<String, Intersection> intersections;
    private Map<String, Map<String, Segment>> segments;

    /**
     * Dans Map<String, Intersection>:
     * String correspond à l'identifiant de l'intersection
     *
     * Dans Map<String, Map<String, Segment>>:
     * Le premier String correspond à l'identifaint de l'intersection d'origine
     * Le second String correspond à l'identifaint de l'intersection de destination
     *
     * @param intersections
     * @param segments
     */
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
