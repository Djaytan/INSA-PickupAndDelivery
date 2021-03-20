package fr.insa.lyon.ifa1.models.map;

import java.util.Collection;
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

  /**
   * Dans Map<String, Intersection>: String correspond à l'identifiant de l'intersection
   *
   * <p>Dans Map<String, Map<String, Segment>>: Le premier String correspond à l'identifaint de
   * l'intersection d'origine Le second String correspond à l'identifaint de l'intersection de
   * destination
   *
   * @param intersections
   * @param segments
   */
  public GeoMap(Map<String, Intersection> intersections, Map<String, Map<String, Segment>> segments) {
    this.intersections = intersections;
    this.segments = segments;
  }

  public Collection<Intersection> getIntersections() {
    return intersections.values();
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
    return this.intersections.get(idIntersection);
  }

  /**
   * Adds a {@link Segment} to the {@link GeoMap}.
   *
   * @param segment The {@link Segment} to add.
   */
  public void addSegment(Segment segment) {
    Map<String, Segment> dest;
    if (!this.segments.containsKey(segment.getOrigin().getId())) {
      dest = new HashMap<>();
      this.segments.put(segment.getOrigin().getId(), dest);
    } else {
      dest = segments.get(segment.getOrigin().getId());
    }
    dest.put(segment.getDest().getId(), segment);
  }

  public Segment getSegment(String origin, String dest) {
    if (!segments.containsKey(origin)) return null;
    return segments.get(origin).get(dest);
  }

  public List<Segment> getSegments() {
    return segments.values().stream()
        .flatMap(e -> e.values().stream())
        .collect(Collectors.toList());
  }

  @Override
  public String toString() {
    return "{" + "intersections=" + this.intersections + ", segments=" + this.segments + "}";
  }
}
