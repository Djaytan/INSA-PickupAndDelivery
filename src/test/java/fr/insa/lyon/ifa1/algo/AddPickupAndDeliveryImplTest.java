package fr.insa.lyon.ifa1.algo;

import fr.insa.lyon.ifa1.models.map.GeoMap;
import fr.insa.lyon.ifa1.models.map.Intersection;
import fr.insa.lyon.ifa1.models.map.Segment;
import fr.insa.lyon.ifa1.models.request.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * AddPickupAndDeliveryImpl Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>mars 31, 2021</pre>
 */
public class AddPickupAndDeliveryImplTest {

    @Before
    public void before() throws Exception {

    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: solve(GeoMap gm, PlanningRequest pr, Request r, List<PassagePoint> circuit, Map<String, Map<String, FindShortestRoutes.Route>> routes)
     */
    @Test
    public void testSolve() throws Exception {

        Map<String, Intersection> intersections = new HashMap<>();
        intersections.put("Depot", new Intersection("Depot", 0, 0));
        intersections.put("A", new Intersection("A", 0, 0));
        intersections.put("B", new Intersection("B", 0, 0));
        intersections.put("C", new Intersection("C", 0, 0));
        intersections.put("D", new Intersection("D", 0, 0));

        intersections.put("X", new Intersection("X", 0, 0));
        intersections.put("Y", new Intersection("Y", 0, 0));


        Map<String, Map<String, Segment>> segments = new HashMap<>();
        Map<String, Segment> destDeDepot = new HashMap<>();
        Map<String, Segment> destDeA = new HashMap<>();
        Map<String, Segment> destDeB = new HashMap<>();
        Map<String, Segment> destDeC = new HashMap<>();
        Map<String, Segment> destDeD = new HashMap<>();
        Map<String, Segment> destDeX = new HashMap<>();
        Map<String, Segment> destDeY = new HashMap<>();


        destDeDepot.put("A", new Segment(5, "rue", intersections.get("Depot"), intersections.get("A")));
        destDeDepot.put("D", new Segment(2, "rue", intersections.get("Depot"), intersections.get("D")));
        destDeDepot.put("Y", new Segment(1, "rue", intersections.get("Depot"), intersections.get("Y")));

        destDeA.put("Depot", new Segment(5, "rue", intersections.get("A"), intersections.get("Depot")));
        destDeA.put("B", new Segment(2, "rue", intersections.get("A"), intersections.get("B")));
        destDeA.put("X", new Segment(2, "rue", intersections.get("A"), intersections.get("X")));

        destDeB.put("A", new Segment(2, "rue", intersections.get("B"), intersections.get("A")));
        destDeB.put("C", new Segment(4, "rue", intersections.get("B"), intersections.get("C")));
        destDeB.put("X", new Segment(1, "rue", intersections.get("B"), intersections.get("X")));

        destDeC.put("B", new Segment(4, "rue", intersections.get("C"), intersections.get("B")));
        destDeC.put("D", new Segment(3, "rue", intersections.get("C"), intersections.get("D")));
        destDeC.put("X", new Segment(3, "rue", intersections.get("C"), intersections.get("X")));

        destDeD.put("C", new Segment(3, "rue", intersections.get("D"), intersections.get("C")));
        destDeD.put("Depot", new Segment(2, "rue", intersections.get("D"), intersections.get("Depot")));
        destDeD.put("Y", new Segment(2, "rue", intersections.get("D"), intersections.get("Y")));

        destDeX.put("A", new Segment(2, "rue", intersections.get("X"), intersections.get("A")));
        destDeX.put("B", new Segment(1, "rue", intersections.get("X"), intersections.get("B")));
        destDeX.put("C", new Segment(3, "rue", intersections.get("X"), intersections.get("C")));

        destDeY.put("D", new Segment(2, "rue", intersections.get("Y"), intersections.get("D")));
        destDeY.put("Depot", new Segment(1, "rue", intersections.get("Y"), intersections.get("Depot")));

        segments.put("Depot", destDeDepot);
        segments.put("A", destDeA);
        segments.put("B", destDeB);
        segments.put("C", destDeC);
        segments.put("D", destDeD);
        segments.put("X", destDeX);
        segments.put("Y", destDeY);

        GeoMap gM = new GeoMap(intersections, segments);

        Depot depot = new Depot(new Intersection("Depot", 0, 0), new Depot.Time(7, 0, 0));

        DurationPassagePoint P1 = new DurationPassagePoint(intersections.get("A"), 2, PassagePointType.PICKUP);
        DurationPassagePoint P2 = new DurationPassagePoint(intersections.get("B"), 2, PassagePointType.PICKUP);

        DurationPassagePoint D1 = new DurationPassagePoint(intersections.get("D"), 2, PassagePointType.DELIVERY);
        DurationPassagePoint D2 = new DurationPassagePoint(intersections.get("C"), 2, PassagePointType.DELIVERY);

        Request[] requests = new Request[]{
                new Request(P1, D1),
                new Request(P2, D2),
        };

        List<Request> requestList = new ArrayList<>(Arrays.asList(requests));

        PlanningRequest pr = new PlanningRequest(requestList, depot);


        Dijkstra dd = new Dijkstra();
        PassagePoint[] pps = new PassagePoint[]{
                P1,
                P2,
                D1,
                D2,
                depot,
        };
        Map<String, Map<String, FindShortestRoutes.Route>> routes = dd.solve(gM, pps);

        OptimalHamiltonianCircuit algoHamilton = new OptimalHamiltonianCircuit(5000);
        List<PassagePoint> result = algoHamilton.solve(gM, routes, pr);


        // 1er cas


        DurationPassagePoint cP1 = new DurationPassagePoint(intersections.get("X"), 2, PassagePointType.PICKUP);
        DurationPassagePoint cD1 = new DurationPassagePoint(intersections.get("Y"), 2, PassagePointType.DELIVERY);

        Request r1 = new Request(cP1, cD1);

        AddPickupAndDeliveryImpl addPickupAndDelivery = new AddPickupAndDeliveryImpl();
        addPickupAndDelivery.solve(gM, pr, r1, result, routes);

        assertEquals(depot, result.get(0));
        assertEquals(P1, result.get(1));
        assertEquals(cP1, result.get(2));
        assertEquals(P2, result.get(3));
        assertEquals(D2, result.get(4));
        assertEquals(D1, result.get(5));
        assertEquals(cD1, result.get(6));

    }

} 
