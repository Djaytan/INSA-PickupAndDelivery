package fr.insa.lyon.ifa1.models.request;

import fr.insa.lyon.ifa1.models.map.Intersection;

public class DurationPassagePoint extends PassagePoint {

  private int duration;
  private String type;

  public DurationPassagePoint(Intersection intersection, int duration, String type) {
    super(intersection);
    this.duration = duration;
    this.type = type;
  }

  public int getDuration() {
    return duration;
  }

  public String getType() {
    return type;
  }
}
