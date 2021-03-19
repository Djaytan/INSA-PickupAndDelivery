package fr.insa.lyon.ifa1.algo;

import fr.insa.lyon.ifa1.models.map.GeoMap;
import fr.insa.lyon.ifa1.models.map.Intersection;
import fr.insa.lyon.ifa1.models.map.Segment;
import fr.insa.lyon.ifa1.models.request.PassagePoint;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Dijkstra implements FindShortestRoutes {

    @Override
    public Map<String, Map<String, Route>> solve(GeoMap gm, PassagePoint[] pps) {
        Map<String, Map<String, Route>> solution = new HashMap<>();

        // calcul de tous les voisins
        Map<String, Set<String>> succesors = new HashMap<>();
        for (Intersection i : gm.getIntersections()) {
            succesors.put(i.getId(), new HashSet<>());
        }

        for (Segment s : gm.getSegments()) {
            succesors.get(s.getOrigin().getId()).add(s.getDest().getId());
        }

        for (PassagePoint ppSource : pps) {
            solution.put(ppSource.getAddress().getId(), solveOne(gm, ppSource, pps, succesors));
        }

        return solution;
    }

    private Map<String, Route> solveOne(GeoMap gm, PassagePoint ppOrigin, PassagePoint[] ppDests, Map<String, Set<String>> successors) {
        Map<String, Double> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        PriorityQueue<String> q = new PriorityQueue<>(Comparator.comparingDouble(dist::get));
        Set<String> unvisited = Arrays.stream(ppDests).map(pp -> pp.getAddress().getId()).collect(Collectors.toSet());

        // initialisation
        String origin = ppOrigin.getAddress().getId();
        initialize(gm, dist, prev, q, unvisited, origin);

        // calcul des chemins les plus courts
        computeShortestRoutes(gm, successors, dist, prev, q, unvisited);

        // construction des chemins
        return extractRoutes(gm, ppDests, prev, origin);
    }

    private void initialize(
            GeoMap gm,
            Map<String, Double> dist,
            Map<String, String> prev,
            PriorityQueue<String> q,
            Set<String> unvisited,
            String origin
    ) {
        dist.put(origin, 0.0d);
        unvisited.remove(origin);
        for (Intersection i : gm.getIntersections()) {
            String v = i.getId();
            if (!v.equals(origin)) {
                dist.put(v, Double.POSITIVE_INFINITY);
                prev.put(v, null);
            }
            q.add(v);
        }
    }

    private void computeShortestRoutes(
            GeoMap gm, Map<String,
            Set<String>> successors,
            Map<String, Double> dist,
            Map<String, String> prev,
            PriorityQueue<String> q,
            Set<String> unvisited
    ) {
        while (!q.isEmpty()) {
            String u = q.poll();
            Set<String> neighbours = successors.get(u);
            unvisited.remove(u);
            if (unvisited.isEmpty()) break; // tous les noeuds intéressants ont été explorés
            for (String neighbour : neighbours) {
                if (!q.contains(neighbour)) continue; // noeud déjà exploré
                double alt = dist.get(u) + gm.getSegment(u, neighbour).getLength();
                if (alt < dist.get(neighbour)) {
                    dist.put(neighbour, alt);
                    prev.put(neighbour, u);
                    q.remove(neighbour);
                    q.add(neighbour);
                }
            }
        }
    }

    private Map<String, Route> extractRoutes(
            GeoMap gm,
            PassagePoint[] ppDests,
            Map<String, String> prev,
            String origin
    ) {
        return Arrays.stream(ppDests)
                .map(pp -> extractRoute(gm, prev, origin, pp))
                .collect(Collectors.toMap(Route::getDestination, r -> r));
    }

    private Route extractRoute(GeoMap gm, Map<String, String> prev, String origin, PassagePoint pp) {
        String dest = pp.getAddress().getId();
        LinkedList<Segment> itinerary = new LinkedList<>();
        double length = 0.0d;
        String currentNode = dest;
        while (prev.get(currentNode) != null) {
            Segment s = gm.getSegment(prev.get(currentNode), currentNode);
            itinerary.addFirst(s);
            currentNode = prev.get(currentNode);
            length += s.getLength();
        }
        return new Route(origin, dest, itinerary, length);
    }
}
