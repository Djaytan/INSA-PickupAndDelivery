package fr.insa.lyon.ifa1.models.map;

public class Intersection {

  private String id;
  private double latitude;
  private double longitude;

  public Intersection(String id, double latitude, double longitude) {
    this.id = id;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public String getId() {
    return id;
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  @Override
  public String toString() {
    return "{"
        + "id="
        + this.id
        + ", latitude="
        + this.latitude
        + ", longitude="
        + this.longitude
        + "}";
  }
}
