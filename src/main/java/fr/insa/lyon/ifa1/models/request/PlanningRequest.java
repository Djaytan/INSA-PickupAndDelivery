package fr.insa.lyon.ifa1.models.request;

import fr.insa.lyon.ifa1.models.map.Intersection;

public class PlanningRequest {
    private Request[] requests;
    private Depot depot;

    public PlanningRequest() {
        this.requests[0] = new Request(
                new DurationPassagePoint(new Intersection("239601996",45.748684, 4.902288), 30, "pickup"),
                new DurationPassagePoint(new Intersection("3095792647",45.74138, 4.841897), 15, "delivery")
        );
        this.depot = new Depot(new Intersection("2292223595", 45.73208, 4.902046), new Depot.Time(12, 36, 00));
    }

    public PlanningRequest(Request[] requests, Depot depot) {
        this.requests = requests;
        this.depot = depot;
    }

    public Request[] getRequests() {
        return requests;
    }

    public Depot getDepot() {
        return depot;
    }

    public PassagePoint[] getPassagePoints() {
        int nbPassagePoints = getRequests().length * 2 + 1;
        PassagePoint[] pps = new PassagePoint[nbPassagePoints];
        pps[0] = getDepot();
        for (int i = 0; i < getRequests().length; i++) {
            pps[i * 2 + 1] = getRequests()[i].getPickup();
            pps[i * 2 + 2] = getRequests()[i].getDelivery();
        }
        return pps;
    }
}
