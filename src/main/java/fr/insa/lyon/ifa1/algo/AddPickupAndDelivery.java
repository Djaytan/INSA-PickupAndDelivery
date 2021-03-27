package fr.insa.lyon.ifa1.algo;

import fr.insa.lyon.ifa1.models.map.GeoMap;
import fr.insa.lyon.ifa1.models.request.PassagePoint;
import fr.insa.lyon.ifa1.models.request.PlanningRequest;
import fr.insa.lyon.ifa1.models.request.Request;

import java.util.List;
import java.util.Map;

public interface AddPickupAndDelivery {

    void solve(GeoMap gm, PlanningRequest pr, Request r, List<PassagePoint> circuit, Map<String, Map<String, FindShortestRoutes.Route>> routes);


}
