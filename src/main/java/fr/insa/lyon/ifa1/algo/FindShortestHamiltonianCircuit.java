package fr.insa.lyon.ifa1.algo;

import fr.insa.lyon.ifa1.models.map.GeoMap;
import fr.insa.lyon.ifa1.models.request.PassagePoint;
import fr.insa.lyon.ifa1.models.request.PlanningRequest;

import java.util.List;
import java.util.Map;

public interface FindShortestHamiltonianCircuit {

  /**
   * Find the shortest hamiltonian circuit, given a planning request
   * @param gm The Geomap model
   * @param routes The shortest routes between every passage point
   * @param pr The planning request
   * @return The shortest hamiltonian circuit (or an heuristic)
   */
    List<PassagePoint> solve(GeoMap gm, Map<String, Map<String, FindShortestRoutes.Route>> routes, PlanningRequest pr);
}
