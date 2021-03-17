package fr.insa.lyon.ifa1.algo;

import fr.insa.lyon.ifa1.models.map.GeoMap;
import fr.insa.lyon.ifa1.models.map.Intersection;
import fr.insa.lyon.ifa1.models.map.Segment;
import fr.insa.lyon.ifa1.models.request.PassagePoint;

import java.util.*;

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

    private Map<String, Route> solveOne(GeoMap gm, PassagePoint ppSource, PassagePoint[] ppDests, Map<String, Set<String>> successors) {
        Map<String, Double> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        PriorityQueue<String> q = new PriorityQueue<>(Comparator.comparingDouble(dist::get));

        // initialisation
        String source = ppSource.getAddress().getId();
        dist.put(source, 0.0d);
        for (Intersection i : gm.getIntersections()) {
            String v = i.getId();
            if (!v.equals(source)) {
                dist.put(v, Double.POSITIVE_INFINITY);
                prev.put(v, null);
            }
            q.add(v);
        }

        // calcul des chemins les plus courts
        while (!q.isEmpty()) {
            String u = q.poll();
            Set<String> neighbours = successors.get(u);
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

        // construction des chemins
        Map<String, Route> route = new HashMap<>();
        for (PassagePoint pp : ppDests) {
            String dest = pp.getAddress().getId();
            if (dest.equals(source)) {
                route.put(dest, new Route(source, dest, new LinkedList<>(), 0.0d));
            } else {
                LinkedList<Segment> itinerary = new LinkedList<>();
                double length = 0.0d;
                String currentNode = dest;
                while (prev.get(currentNode) != null) {
                    Segment s = gm.getSegment(prev.get(currentNode), currentNode);
                    itinerary.addFirst(s);
                    currentNode = prev.get(currentNode);
                    length += s.getLength();
                }
                route.put(dest, new Route(source, dest, itinerary, length));
            }
        }

        return route;
    }
}
