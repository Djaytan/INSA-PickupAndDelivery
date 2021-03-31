package fr.insa.lyon.ifa1.models.request;

import fr.insa.lyon.ifa1.models.map.Intersection;

public class Depot extends PassagePoint {

  private Time departureTime;

  public Depot(Intersection address, Time departureTime) {
    super(address);
    this.departureTime = departureTime;
  }

  public Time getDepartureTime() {
    return departureTime;
  }

  public static class Time {
    private int hours;
    private int minutes;
    private int seconds;

    public Time(int hours, int minutes, int seconds) {
      this.hours = hours;
      this.minutes = minutes;
      this.seconds = seconds;
    }

    public int getHours() {
      return hours;
    }

    public int getMinutes() {
      return minutes;
    }

    public int getSeconds() {
      return seconds;
    }

    public PassagePointType getType() {
      return PassagePointType.DEPOT;
    }
  }
}
