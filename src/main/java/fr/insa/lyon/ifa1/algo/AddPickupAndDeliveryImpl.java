package fr.insa.lyon.ifa1.algo;

import fr.insa.lyon.ifa1.models.map.GeoMap;
import fr.insa.lyon.ifa1.models.request.PassagePoint;
import fr.insa.lyon.ifa1.models.request.PassagePointType;
import fr.insa.lyon.ifa1.models.request.PlanningRequest;
import fr.insa.lyon.ifa1.models.request.Request;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AddPickupAndDeliveryImpl implements AddPickupAndDelivery {

    @Override
    public void solve(GeoMap gm, PlanningRequest pr, Request r, List<PassagePoint> circuit, Map<String, Map<String, FindShortestRoutes.Route>> routes) {
        // première étape : ajout du pickup
        // - utiliser Dijkstra.solveOne pour calculer les chemins les plus courts entre ce point et les autres
        // - trouver le point de passage le plus proche
        // - insérer le pickup après ce point (sauf si dépot fin) -> calculer la longueur du circuit
        // - insérer le pickup avant ce point (sauf si dépot début) -> calculer la logueur du circuit
        // - comparer puis insérer avant ou après selon la longueur minimale
        // deuxième étape : ajout du delivery
        // - même chose que pour l'étape une mais en ne prenant pas en compte les points avant le pickup

        //List<PassagePoint> pps = new ArrayList<>(Arrays.asList(pr.getPassagePoints()));
        addPoint(gm, r, circuit, 0, routes, PassagePointType.PICKUP);
       // pps.add(r.getPickup());
        addPoint(gm, r, circuit, circuit.indexOf(r.getPickup()), routes, PassagePointType.DELIVERY);
        pr.addRequest(r);
    }

    private void addPoint(
            GeoMap gm,
            //List<PassagePoint> pps,
            Request r,
            List<PassagePoint> circuit,
            int startPoint,
            Map<String, Map<String, FindShortestRoutes.Route>> routes,
            PassagePointType ppType
    ) {
        Dijkstra dijkstra = new Dijkstra();
        PassagePoint[] searchPps = circuit.subList(startPoint, circuit.size()).toArray(new PassagePoint[0]);
        PassagePoint ppToAdd = ppType.equals(PassagePointType.PICKUP) ? r.getPickup() : r.getDelivery();
        Map<String, FindShortestRoutes.Route> routesToNewPickup = dijkstra.solveOne(gm, ppToAdd, searchPps, dijkstra.getSuccesors(gm));

        Map.Entry<String, FindShortestRoutes.Route> nearestPoint = routesToNewPickup.entrySet().stream().min(
                Comparator.comparingDouble(r2 -> r2.getValue().getLength())
        ).get();

        List<Integer> nearestPpsIndices = IntStream.range(startPoint, circuit.size())
                .filter(i -> circuit.get(i).getAddress().getId().equals(nearestPoint.getKey()))
                .boxed()
                .collect(Collectors.toList());

        int bestPpIndex = -1;
        double bestDistance = Double.POSITIVE_INFINITY;

        for (int ppIndex : nearestPpsIndices)
        {
            // test insertion après
            if (ppIndex != circuit.size() - 1)
            {
                PassagePoint first = circuit.get(ppIndex);
                PassagePoint last = circuit.get(ppIndex + 1);
                double length = computeTripletLength(first, last, routesToNewPickup);
                if (length < bestDistance)
                {
                    bestDistance = length;
                    bestPpIndex = ppIndex + 1;
                }
            }

            // test insertion avant
            if (ppIndex != startPoint)
            {
                PassagePoint first = circuit.get(ppIndex - 1);
                PassagePoint last = circuit.get(ppIndex);
                double length = computeTripletLength(first, last, routesToNewPickup);
                if (length < bestDistance)
                {
                    bestDistance = length;
                    bestPpIndex = ppIndex;
                }
            }
        }



        circuit.add(bestPpIndex, ppToAdd);

        routes.put(ppToAdd.getAddress().getId(), new HashMap<>());
        for (Map.Entry<String, FindShortestRoutes.Route> route : routesToNewPickup.entrySet())
        {
            routes.get(route.getKey()).put(ppToAdd.getAddress().getId(), route.getValue());
            routes.get(ppToAdd.getAddress().getId()).put(route.getKey(), route.getValue());
        }
    }

    private double computeTripletLength(
            PassagePoint first,
            PassagePoint last,
            Map<String, FindShortestRoutes.Route> routes)
    {
        return routes.get(first.getAddress().getId()).getLength() + routes.get(last.getAddress().getId()).getLength();
    }

}
