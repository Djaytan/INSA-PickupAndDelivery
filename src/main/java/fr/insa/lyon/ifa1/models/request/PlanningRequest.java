package fr.insa.lyon.ifa1.models.request;

import java.util.ArrayList;
import java.util.List;

public class PlanningRequest {
  private List<Request> requests;
  private Depot depot;

  public PlanningRequest(Depot depot) {
    this(new ArrayList<>(), depot);
  }

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
}
