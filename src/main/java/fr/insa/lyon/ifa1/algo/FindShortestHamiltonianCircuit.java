package fr.insa.lyon.ifa1.algo;

import fr.insa.lyon.ifa1.models.map.GeoMap;
import fr.insa.lyon.ifa1.models.request.PassagePoint;
import fr.insa.lyon.ifa1.models.request.PlanningRequest;

import java.util.List;
import java.util.Map;

public interface FindShortestHamiltonianCircuit {

    List<PassagePoint> solve(GeoMap gm, Map<String, Map<String, FindShortestRoutes.Route>> routes, PlanningRequest pr);
}
