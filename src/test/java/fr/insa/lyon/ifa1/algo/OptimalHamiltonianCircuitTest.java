package fr.insa.lyon.ifa1.algo;

import fr.insa.lyon.ifa1.models.map.GeoMap;
import fr.insa.lyon.ifa1.models.map.Intersection;
import fr.insa.lyon.ifa1.models.map.Segment;
import fr.insa.lyon.ifa1.models.request.*;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class OptimalHamiltonianCircuitTest {


    @Test
    public void solveTest(){

        Map<String, Intersection> intersections = new HashMap<>();
        intersections.put("Depot", new Intersection("Depot", 0, 0));
        intersections.put("A", new Intersection("A", 0, 0));
        intersections.put("B", new Intersection("B", 0, 0));
        intersections.put("C", new Intersection("C", 0, 0));
        intersections.put("D", new Intersection("D", 0, 0));
        intersections.put("E", new Intersection("E", 0, 0));
        intersections.put("F", new Intersection("F", 0, 0));
        intersections.put("G", new Intersection("G", 0, 0));

        Map<String, Map<String, Segment>> segments = new HashMap<>();
        Map<String, Segment> destDeDepot = new HashMap<>();
        Map<String, Segment> destDeA = new HashMap<>();
        Map<String, Segment> destDeB = new HashMap<>();
        Map<String, Segment> destDeC = new HashMap<>();
        Map<String, Segment> destDeD = new HashMap<>();
        Map<String, Segment> destDeE = new HashMap<>();
        Map<String, Segment> destDeF = new HashMap<>();
        Map<String, Segment> destDeG = new HashMap<>();

        destDeDepot.put("A", new Segment(2, "rue", intersections.get("Depot"), intersections.get("A")));
        destDeDepot.put("G", new Segment(2, "rue", intersections.get("DepotG"), intersections.get("G")));
        destDeDepot.put("D", new Segment(20, "rue", intersections.get("Depot"), intersections.get("D")));
        destDeA.put("Depot", new Segment(2, "rue", intersections.get("A"), intersections.get("Depot")));
        destDeA.put("B", new Segment(3, "rue", intersections.get("A"), intersections.get("B")));
        destDeA.put("F", new Segment(8, "rue", intersections.get("A"), intersections.get("F")));
        destDeA.put("G", new Segment(8, "rue", intersections.get("A"), intersections.get("G")));
        destDeB.put("A", new Segment(3, "rue", intersections.get("B"), intersections.get("A")));
        destDeB.put("C", new Segment(3, "rue", intersections.get("B"), intersections.get("C")));
        destDeB.put("E", new Segment(8, "rue", intersections.get("B"), intersections.get("E")));
        destDeB.put("G", new Segment(8, "rue", intersections.get("B"), intersections.get("G")));
        destDeC.put("B", new Segment(3, "rue", intersections.get("C"), intersections.get("B")));
        destDeC.put("D", new Segment(2, "rue", intersections.get("C"), intersections.get("D")));
        destDeC.put("E", new Segment(5, "rue", intersections.get("C"), intersections.get("E")));
        destDeC.put("F", new Segment(8, "rue", intersections.get("C"), intersections.get("F")));
        destDeD.put("C", new Segment(2, "rue", intersections.get("D"), intersections.get("C")));
        destDeD.put("E", new Segment(2, "rue", intersections.get("D"), intersections.get("E")));
        destDeD.put("Depot", new Segment(20, "rue", intersections.get("D"), intersections.get("Depot")));
        destDeE.put("B", new Segment(8, "rue", intersections.get("E"), intersections.get("B")));
        destDeE.put("C", new Segment(5, "rue", intersections.get("E"), intersections.get("C")));
        destDeE.put("D", new Segment(2, "rue", intersections.get("E"), intersections.get("D")));
        destDeE.put("F", new Segment(3, "rue", intersections.get("E"), intersections.get("F")));
        destDeF.put("A", new Segment(8, "rue", intersections.get("F"), intersections.get("A")));
        destDeF.put("C", new Segment(8, "rue", intersections.get("F"), intersections.get("C")));
        destDeF.put("E", new Segment(3, "rue", intersections.get("F"), intersections.get("E")));
        destDeF.put("G", new Segment(3, "rue", intersections.get("F"), intersections.get("G")));
        destDeG.put("B", new Segment(8, "rue", intersections.get("G"), intersections.get("B")));
        destDeG.put("F", new Segment(3, "rue", intersections.get("G"), intersections.get("F")));
        destDeG.put("Depot", new Segment(1, "rue", intersections.get("G"), intersections.get("Depot")));

        segments.put("Depot", destDeA);
        segments.put("A", destDeA);
        segments.put("B", destDeB);
        segments.put("C", destDeC);
        segments.put("D", destDeD);
        segments.put("E", destDeE);
        segments.put("F", destDeF);
        segments.put("G", destDeG);

        GeoMap gM = new GeoMap(intersections, segments);

        Depot depot = new Depot(new Intersection("depot", 0, 0), new Depot.Time(7, 0, 0));

        DurationPassagePoint P1 = new DurationPassagePoint(intersections.get("A"),2,"pickup");
        DurationPassagePoint P2 = new DurationPassagePoint(intersections.get("B"),2,"pickup");
        DurationPassagePoint P3 = new DurationPassagePoint(intersections.get("C"),2,"pickup");
        DurationPassagePoint P4 = new DurationPassagePoint(intersections.get("D"),2,"pickup");

        DurationPassagePoint D1 = new DurationPassagePoint(intersections.get("F"),2,"delivery");
        DurationPassagePoint D2 = new DurationPassagePoint(intersections.get("F"),2,"delivery");
        DurationPassagePoint D3 = new DurationPassagePoint(intersections.get("E"),2,"delivery");
        DurationPassagePoint D4 = new DurationPassagePoint(intersections.get("G"),2,"delivery");

        Request[] requests = new Request[]{
                new Request(P1,D1),
                new Request(P2,D2),
                new Request(P3,D3),
                new Request(P4,D4),
        };

        PlanningRequest pr = new PlanningRequest(requests,depot);


        Map<String, Map<String, FindShortestRoutes.Route>> routes = new HashMap<>();
        Map<String, FindShortestRoutes.Route> routeDeDepot = new HashMap<>();
        Map<String, FindShortestRoutes.Route> routeDeA = new HashMap<>();
        Map<String, FindShortestRoutes.Route> routeDeB = new HashMap<>();
        Map<String, FindShortestRoutes.Route> routeDeC = new HashMap<>();
        Map<String, FindShortestRoutes.Route> routeDeD = new HashMap<>();
        Map<String, FindShortestRoutes.Route> routeDeE = new HashMap<>();
        Map<String, FindShortestRoutes.Route> routeDeF = new HashMap<>();
        Map<String, FindShortestRoutes.Route> routeDeG = new HashMap<>();

        routeDeDepot.put("Depot", new FindShortestRoutes.Route("Depot","A", Collections.singletonList(segments.get("Depot").get("A")),2));
        routeDeDepot.put("Depot", new FindShortestRoutes.Route("Depot", "D", Collections.singletonList(segments.get("Depot").get("D")), 20));
        routeDeDepot.put("Depot", new FindShortestRoutes.Route("Depot", "G", Collections.singletonList(segments.get("Depot").get("G")), 1));
        routeDeA.put("A", new FindShortestRoutes.Route("A", "Depot", Collections.singletonList(segments.get("A").get("Depot")), 2));
        routeDeA.put("A", new FindShortestRoutes.Route("A", "B", Collections.singletonList(segments.get("A").get("B")), 3));
        routeDeA.put("A", new FindShortestRoutes.Route("A", "F", Collections.singletonList(segments.get("A").get("F")), 8));
        routeDeB.put("B", new FindShortestRoutes.Route("B", "A", Collections.singletonList(segments.get("B").get("A")), 3));
        routeDeB.put("B", new FindShortestRoutes.Route("B", "C", Collections.singletonList(segments.get("B").get("C")), 3));
        routeDeB.put("B", new FindShortestRoutes.Route("B", "E", Collections.singletonList(segments.get("B").get("E")), 8));
        routeDeB.put("B", new FindShortestRoutes.Route("B", "G", Collections.singletonList(segments.get("B").get("G")), 8));
        routeDeC.put("C", new FindShortestRoutes.Route("C", "B", Collections.singletonList(segments.get("C").get("B")), 3));
        routeDeC.put("C", new FindShortestRoutes.Route("C", "D", Collections.singletonList(segments.get("C").get("D")), 2));
        routeDeC.put("C", new FindShortestRoutes.Route("C", "E", Collections.singletonList(segments.get("C").get("E")), 5));
        routeDeC.put("C", new FindShortestRoutes.Route("C", "F", Collections.singletonList(segments.get("C").get("F")), 8));
        routeDeD.put("D", new FindShortestRoutes.Route("D", "Depot", Collections.singletonList(segments.get("D").get("Depot")), 20));
        routeDeD.put("D", new FindShortestRoutes.Route("D", "C", Collections.singletonList(segments.get("D").get("C")), 2));
        routeDeD.put("D", new FindShortestRoutes.Route("D", "E", Collections.singletonList(segments.get("D").get("E")), 2));
        routeDeE.put("E", new FindShortestRoutes.Route("E", "B", Collections.singletonList(segments.get("E").get("B")), 8));
        routeDeE.put("E", new FindShortestRoutes.Route("E", "C", Collections.singletonList(segments.get("E").get("C")), 5));
        routeDeE.put("E", new FindShortestRoutes.Route("E", "D", Collections.singletonList(segments.get("E").get("D")), 2));
        routeDeE.put("E", new FindShortestRoutes.Route("E", "F", Collections.singletonList(segments.get("E").get("F")), 3));
        routeDeF.put("F", new FindShortestRoutes.Route("F", "A", Collections.singletonList(segments.get("F").get("A")), 8));
        routeDeF.put("F", new FindShortestRoutes.Route("F", "C", Collections.singletonList(segments.get("F").get("C")), 8));
        routeDeF.put("F", new FindShortestRoutes.Route("F", "E", Collections.singletonList(segments.get("F").get("E")), 3));
        routeDeF.put("F", new FindShortestRoutes.Route("F", "G", Collections.singletonList(segments.get("F").get("G")), 3));
        routeDeG.put("G", new FindShortestRoutes.Route("G", "Depot", Collections.singletonList(segments.get("G").get("Depot")), 1));
        routeDeG.put("G", new FindShortestRoutes.Route("G", "B", Collections.singletonList(segments.get("G").get("B")), 8));
        routeDeG.put("G", new FindShortestRoutes.Route("G", "F", Collections.singletonList(segments.get("G").get("F")), 3));






        routes.put("Depot", routeDeDepot);
        routes.put("A", routeDeA);
        routes.put("B", routeDeB);
        routes.put("C", routeDeC);
        routes.put("D", routeDeD);
        routes.put("E", routeDeE);
        routes.put("F", routeDeF);
        routes.put("G", routeDeG);


        OptimalHamiltonianCircuit algoHamilton = new OptimalHamiltonianCircuit();
        List<PassagePoint> result = algoHamilton.solve(gM,routes,pr);

        assertEquals(depot, result.get(0));
        assertEquals(P1, result.get(1));
        assertEquals(P2, result.get(2));
        assertEquals(P3, result.get(3));
        assertEquals(P4, result.get(4));
        assertEquals(D3, result.get(5));
        assertEquals(D1, result.get(6));
        assertEquals(D2, result.get(7));
        assertEquals(D4, result.get(8));
        assertEquals(depot, result.get(9));
    }

}