package fr.insa.lyon.ifa1.algo;

import fr.insa.lyon.ifa1.models.map.GeoMap;
import fr.insa.lyon.ifa1.models.request.*;

import java.util.*;

public class OptimalHamiltonianCircuit implements FindShortestHamiltonianCircuit {

    private double optimalLength;
    private List<PassagePoint> optimalRoute;
    private Map<String, Map<String, FindShortestRoutes.Route>> routes;
    private PlanningRequest pr;

    @Override
    public List<PassagePoint> solve(GeoMap gm, Map<String, Map<String, FindShortestRoutes.Route>> routes, PlanningRequest pr) {

        this.optimalLength = Double.POSITIVE_INFINITY;
        this.optimalRoute = null;
        this.routes = routes;
        this.pr = pr;

        PassagePoint[] passagePoints = pr.getPassagePoints();

        Map<PassagePoint, DurationPassagePoint> ppToDurationPp = new HashMap<>();

        for (Request r : pr.getRequests()) {
            ppToDurationPp.put(Arrays.stream(passagePoints)
                    .filter(pp -> pp.equals(r.getPickup()))
                    .findFirst()
                    .get(), r.getPickup());


            ppToDurationPp.put(Arrays.stream(passagePoints)
                    .filter(pp -> pp.equals(r.getDelivery()))
                    .findFirst()
                    .get(), r.getDelivery());

        }


        // init
        Set<PassagePoint> ppNotVisited = new HashSet<>(Arrays.asList(passagePoints));

        ppNotVisited.remove(pr.getDepot());

        List<PassagePoint> ppVisited = new ArrayList<>();
        ppVisited.add(pr.getDepot());

        visitePoint(pr.getDepot(), ppVisited, ppNotVisited, 0d, pr.getDepot(), ppToDurationPp);

        return this.optimalRoute;

    }

    private void visitePoint(PassagePoint currentPoint, List<PassagePoint> ppVisited, Set<PassagePoint> ppNotVisited, double lengthCircuit, PassagePoint ppDepot, Map<PassagePoint, DurationPassagePoint> ppToDurationPp) {

        if (ppNotVisited.isEmpty()) {
            // Calcule la valeur definif  = lengthCircuit + distance vers depot
            lengthCircuit += this.routes.get(currentPoint.getAddress().getId()).get(ppDepot.getAddress().getId()).getLength();

            if (lengthCircuit < optimalLength) {
                optimalLength = lengthCircuit;

                List<PassagePoint> clonePpVisited = new ArrayList<>(ppVisited);
                clonePpVisited.add(ppDepot);
                optimalRoute = clonePpVisited;
            }
            // finish
            // verifier si c'est chemin le plus court
        }

        Set<PassagePoint> clonePpNotVisited = new HashSet<>(ppNotVisited);

        for (PassagePoint pp : clonePpNotVisited) {

            if (ppToDurationPp.get(pp).getType().equals(PassagePointType.DELIVERY)) {

                // vérifier si on est passé au pick up correspondant
                Request r = Arrays.stream(this.pr.getRequests()).filter(request -> request.getDelivery().equals(pp)).findFirst().get();
                if (!ppVisited.contains(r.getPickup())) continue;
            }
            ppNotVisited.remove(pp);
            ppVisited.add(pp);
            visitePoint(pp, ppVisited, ppNotVisited, lengthCircuit + routes.get(currentPoint.getAddress().getId()).get(pp.getAddress().getId()).getLength(), ppDepot, ppToDurationPp);

            ppNotVisited.add(pp);
            ppVisited.remove(pp);
        }
    }
}
