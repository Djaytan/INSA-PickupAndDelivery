package fr.insa.lyon.ifa1.models.request;

import fr.insa.lyon.ifa1.models.map.Intersection;
import java.util.ArrayList;
import java.util.List;

public class PlanningRequest {
  private List<Request> requests;
  private Depot depot;

  public PlanningRequest() {
      this.requests = new ArrayList<>();
      this.requests.add(new Request(
              new DurationPassagePoint(new Intersection("239601996",45.748684, 4.902288), 30, "pickup"),
              new DurationPassagePoint(new Intersection("3095792647",45.74138, 4.841897), 15, "delivery")
      ));
      this.depot = new Depot(new Intersection("2292223595", 45.73208, 4.902046), new Depot.Time(12, 36, 00));
  }

  /*public PlanningRequest() {
    this.requests = new ArrayList<>();
    this.depot = null;
  }*/

  public PlanningRequest(List<Request> requests, Depot depot) {
    this.requests = requests;
    this.depot = depot;
  }

  public List<Request> getRequests() {
    return requests;
  }

  /**
   * Adds a {@link Request} into the {@link PlanningRequest}.
   *
   * @param request The {@link Request} to add into the {@link PlanningRequest}.
   */
  public void addRequest(Request request) {
    this.requests.add(request);
  }

  public Depot getDepot() {
    return depot;
  }

  public PassagePoint[] getPassagePoints() {
    int nbPassagePoints = getRequests().size() * 2 + 1;
    PassagePoint[] pps = new PassagePoint[nbPassagePoints];
    pps[0] = getDepot();
    for (int i = 0; i < getRequests().size(); i++) {
      pps[i * 2 + 1] = getRequests().get(i).getPickup();
      pps[i * 2 + 2] = getRequests().get(i).getDelivery();
    }
    return pps;
  }

  public void changeDepot(Depot depot) { this.depot = depot; }

}
