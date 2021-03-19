package fr.insa.lyon.ifa1.models.map;

public class Segment {
  private double length;
  private String name;
  private Intersection origin;
  private Intersection dest;

  public Segment(double length, String name, Intersection origin, Intersection dest) {
    this.length = length;
    this.name = name;
    this.origin = origin;
    this.dest = dest;
  }

  public double getLength() {
    return length;
  }

  public String getName() {
    return name;
  }

  public Intersection getOrigin() {
    return origin;
  }

  public Intersection getDest() {
    return dest;
  }

  @Override
  public String toString() {
    return "{"
        + "length="
        + this.length
        + ", name="
        + this.name
        + ", origin="
        + this.origin.toString()
        + ", dest="
        + this.dest.toString()
        + "}";
  }
}
