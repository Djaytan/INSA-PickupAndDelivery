package fr.insa.lyon.ifa1.algo;

import fr.insa.lyon.ifa1.models.map.GeoMap;
import fr.insa.lyon.ifa1.models.request.PassagePoint;
import fr.insa.lyon.ifa1.models.request.PlanningRequest;

import java.util.*;

public class OptimalHamiltonianCircuit implements FindShortestHamiltonianCircuit {

    private double optimalLength;
    private List<String> optimalRoute;

    @Override
    public List<PassagePoint> solve(GeoMap gm, Map<String, Map<String, FindShortestRoutes.Route>> routes, PlanningRequest pr) {

        this.optimalLength = Double.POSITIVE_INFINITY;
        this.optimalRoute = null;

        Set<String> ppNotVisited = new HashSet<>();

        PassagePoint[] passagePoints = pr.getPassagePoints();
        // init
        for (PassagePoint pp : passagePoints) {
            ppNotVisited.add(pp.getAddress().getId());
        }

        String idDepot = pr.getDepot().getAddress().getId();
        ppNotVisited.remove(idDepot);

        List<String> ppVisited = new ArrayList<>();
        ppVisited.add(idDepot);

        visitePoint(idDepot, ppVisited, ppNotVisited, routes, 0d, idDepot);

        return this.optimalRoute.stream().map(
                p -> Arrays.stream(passagePoints)
                        .filter(pp -> pp.getAddress().getId().equals(p))
                        .findFirst()
                        .get()
        ).toList();
    }

    private void visitePoint(String currentPoint, List<String> ppVisited, Set<String> ppNotVisited, Map<String, Map<String, FindShortestRoutes.Route>> routes, double lengthCircuit, String idDepot) {

        if (ppNotVisited.isEmpty()) {
            // Calcule la valeur definif  = lengthCircuit + distance vers depot
            lengthCircuit += routes.get(currentPoint).get(idDepot).getLength();

            if (lengthCircuit < optimalLength) {
                optimalLength = lengthCircuit;

                List<String> clonePpVisited = new ArrayList<>(ppVisited);
                clonePpVisited.add(idDepot);
                optimalRoute = clonePpVisited;
            }
            // finish
            // verifier si c'est chemin le plus court
        }

        Set<String> clonePpNotVisited = new HashSet<>(ppNotVisited);

        for (String ppId : clonePpNotVisited) {
            ppNotVisited.remove(ppId);
            ppVisited.add(ppId);
            visitePoint(ppId, ppVisited, ppNotVisited, routes, lengthCircuit + routes.get(currentPoint).get(ppId).getLength(), idDepot);

            ppNotVisited.add(ppId);
            ppVisited.remove(ppId);
        }
    }
}
