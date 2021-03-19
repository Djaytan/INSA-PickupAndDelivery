package fr.insa.lyon.ifa1.algo;

import fr.insa.lyon.ifa1.models.map.GeoMap;
import fr.insa.lyon.ifa1.models.request.PassagePoint;
import fr.insa.lyon.ifa1.models.request.PlanningRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OptimalHamiltonianCircuit implements FindShortestHamiltonianCircuit {
    @Override
    public List<PassagePoint> solve(GeoMap gm, Map<String, Map<String, FindShortestRoutes.Route>> routes, PlanningRequest pr) {

        Set<String> ppNotVisited = new HashSet<>();

        for (PassagePoint pp : pr.getPassagePoints()) {
            ppNotVisited.add(pp.getAddress().getId());
        }
        ppNotVisited.remove(pr.getDepot().getAddress().getId());


    }

    private void visitePoint(Set<String> ppNotVisited, Map<String, Map<String, FindShortestRoutes.Route>> routes, Double lengthCircuit ) {

        if(ppNotVisited.isEmpty()){
            // Calcule la valeur definif  = lengthCircuit + distance vers depot
            // finish
            // verifier si c'est chemin le plus court
        }

        for(String ppId : ppNotVisited) {


        }
    }
}
