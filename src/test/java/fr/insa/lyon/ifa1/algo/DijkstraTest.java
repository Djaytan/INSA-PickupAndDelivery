package fr.insa.lyon.ifa1.algo;

import fr.insa.lyon.ifa1.models.map.GeoMap;
import fr.insa.lyon.ifa1.models.map.Intersection;
import fr.insa.lyon.ifa1.models.map.Segment;
import fr.insa.lyon.ifa1.models.request.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class DijkstraTest {

    @Test
    public void solve() {

        Map<String, Intersection> intersections = new HashMap<>();
        intersections.put("A", new Intersection("A", 0, 0));
        intersections.put("B", new Intersection("B", 0, 0));
        intersections.put("C", new Intersection("C", 0, 0));
        intersections.put("D", new Intersection("D", 0, 0));
        intersections.put("s", new Intersection("s", 0, 0));
        intersections.put("t", new Intersection("t", 0, 0));
        intersections.put("u", new Intersection("u", 0, 0));
        intersections.put("v", new Intersection("v", 0, 0));
        intersections.put("w", new Intersection("w", 0, 0));
        intersections.put("x", new Intersection("x", 0, 0));
        intersections.put("y", new Intersection("y", 0, 0));

        Map<String, Map<String, Segment>> segments = new HashMap<>();
        Map<String, Segment> destDeA = new HashMap<>();
        Map<String, Segment> destDeB = new HashMap<>();
        Map<String, Segment> destDeC = new HashMap<>();
        Map<String, Segment> destDeD = new HashMap<>();
        Map<String, Segment> destDes = new HashMap<>();
        Map<String, Segment> destDet = new HashMap<>();
        Map<String, Segment> destDeu = new HashMap<>();
        Map<String, Segment> destDev = new HashMap<>();
        Map<String, Segment> destDew = new HashMap<>();
        Map<String, Segment> destDex = new HashMap<>();
        Map<String, Segment> destDey = new HashMap<>();
        destDeA.put("t", new Segment(3, "rue", intersections.get("A"), intersections.get("t")));
        destDeA.put("y", new Segment(6, "rue", intersections.get("A"), intersections.get("y")));
        destDeA.put("s", new Segment(2, "rue", intersections.get("A"), intersections.get("s")));
        destDeB.put("t", new Segment(1, "rue", intersections.get("B"), intersections.get("t")));
        destDeB.put("v", new Segment(2, "rue", intersections.get("B"), intersections.get("v")));
        destDeB.put("w", new Segment(4, "rue", intersections.get("B"), intersections.get("w")));
        destDeC.put("s", new Segment(3, "rue", intersections.get("C"), intersections.get("s")));
        destDeC.put("u", new Segment(1, "rue", intersections.get("C"), intersections.get("u")));
        destDeC.put("y", new Segment(3, "rue", intersections.get("C"), intersections.get("y")));
        destDeD.put("w", new Segment(4, "rue", intersections.get("D"), intersections.get("w")));
        destDeD.put("y", new Segment(2, "rue", intersections.get("D"), intersections.get("y")));
        destDes.put("A", new Segment(2, "rue", intersections.get("s"), intersections.get("A")));
        destDes.put("C", new Segment(3, "rue", intersections.get("s"), intersections.get("C")));
        destDet.put("A", new Segment(3, "rue", intersections.get("t"), intersections.get("A")));
        destDet.put("B", new Segment(1, "rue", intersections.get("t"), intersections.get("B")));
        destDet.put("u", new Segment(2, "rue", intersections.get("t"), intersections.get("u")));
        destDeu.put("v", new Segment(4, "rue", intersections.get("u"), intersections.get("v")));
        destDeu.put("C", new Segment(1, "rue", intersections.get("u"), intersections.get("C")));
        destDeu.put("t", new Segment(2, "rue", intersections.get("u"), intersections.get("t")));
        destDev.put("B", new Segment(2, "rue", intersections.get("v"), intersections.get("B")));
        destDev.put("u", new Segment(4, "rue", intersections.get("v"), intersections.get("u")));
        destDev.put("x", new Segment(3, "rue", intersections.get("v"), intersections.get("x")));
        destDew.put("B", new Segment(4, "rue", intersections.get("w"), intersections.get("B")));
        destDew.put("C", new Segment(2, "rue", intersections.get("w"), intersections.get("C")));
        destDew.put("D", new Segment(4, "rue", intersections.get("w"), intersections.get("D")));
        destDew.put("x", new Segment(1, "rue", intersections.get("w"), intersections.get("x")));
        destDex.put("v", new Segment(3, "rue", intersections.get("x"), intersections.get("v")));
        destDex.put("w", new Segment(1, "rue", intersections.get("x"), intersections.get("w")));
        destDex.put("y", new Segment(2, "rue", intersections.get("x"), intersections.get("y")));
        destDey.put("A", new Segment(6, "rue", intersections.get("y"), intersections.get("A")));
        destDey.put("C", new Segment(3, "rue", intersections.get("y"), intersections.get("C")));
        destDey.put("D", new Segment(2, "rue", intersections.get("y"), intersections.get("D")));
        destDey.put("x", new Segment(2, "rue", intersections.get("y"), intersections.get("x")));

        segments.put("A", destDeA);
        segments.put("B", destDeB);
        segments.put("C", destDeC);
        segments.put("D", destDeD);
        segments.put("s", destDes);
        segments.put("t", destDet);
        segments.put("u", destDeu);
        segments.put("v", destDev);
        segments.put("w", destDew);
        segments.put("x", destDex);
        segments.put("y", destDey);

        GeoMap gM = new GeoMap(intersections, segments);

        PassagePoint[] pps = new PassagePoint[]{
                new Depot(intersections.get("A"), new Depot.Time(7, 0, 0)),
                new DurationPassagePoint(intersections.get("B"), 4, PassagePointType.PICKUP),
                new DurationPassagePoint(intersections.get("C"), 3, PassagePointType.PICKUP),
                new DurationPassagePoint(intersections.get("D"), 4, PassagePointType.PICKUP)
        };

        Dijkstra algoD = new Dijkstra();
        Map<String, Map<String, FindShortestRoutes.Route>> result = algoD.solve(gM, pps);

        assertEquals(result.get("A").get("C").getLength(), 5, 0.00001);
        assertEquals(result.get("A").get("B").getLength(), 4, 0.00001);
        assertEquals(result.get("A").get("D").getLength(), 8, 0.00001);

        assertEquals(result.get("B").get("A").getLength(), 4, 0.00001);
        assertEquals(result.get("B").get("C").getLength(), 4, 0.00001);
        assertEquals(result.get("B").get("D").getLength(), 8, 0.00001);

        assertEquals(result.get("C").get("A").getLength(), 5, 0.00001);
        assertEquals(result.get("C").get("B").getLength(), 4, 0.00001);
        assertEquals(result.get("C").get("D").getLength(), 5, 0.00001);

        assertEquals(result.get("D").get("A").getLength(), 8, 0.00001);
        assertEquals(result.get("D").get("B").getLength(), 8, 0.00001);
        assertEquals(result.get("D").get("C").getLength(), 5, 0.00001);

        List<Segment> AtoB = result.get("A").get("B").getItinerary();
        assertEquals("A", AtoB.get(0).getOrigin().getId());
        assertEquals("t", AtoB.get(0).getDest().getId());
        assertEquals("t", AtoB.get(1).getOrigin().getId());
        assertEquals("B", AtoB.get(1).getDest().getId());

        List<Segment> AtoC = result.get("A").get("C").getItinerary();
        assertEquals("A", AtoC.get(0).getOrigin().getId());
        assertEquals("s", AtoC.get(0).getDest().getId());
        assertEquals("s", AtoC.get(1).getOrigin().getId());
        assertEquals("C", AtoC.get(1).getDest().getId());

        List<Segment> AtoD = result.get("A").get("D").getItinerary();
        assertEquals("A", AtoD.get(0).getOrigin().getId());
        assertEquals("y", AtoD.get(0).getDest().getId());
        assertEquals("y", AtoD.get(1).getOrigin().getId());
        assertEquals("D", AtoD.get(1).getDest().getId());


        List<Segment> BtoA = result.get("B").get("A").getItinerary();
        assertEquals("B", BtoA.get(0).getOrigin().getId());
        assertEquals("t", BtoA.get(0).getDest().getId());
        assertEquals("t", BtoA.get(1).getOrigin().getId());
        assertEquals("A", BtoA.get(1).getDest().getId());

        List<Segment> BtoC = result.get("B").get("C").getItinerary();
        assertEquals("B", BtoC.get(0).getOrigin().getId());
        assertEquals("t", BtoC.get(0).getDest().getId());
        assertEquals("t", BtoC.get(1).getOrigin().getId());
        assertEquals("u", BtoC.get(1).getDest().getId());
        assertEquals("u", BtoC.get(2).getOrigin().getId());
        assertEquals("C", BtoC.get(2).getDest().getId());

        List<Segment> BtoD = result.get("B").get("D").getItinerary();
        assertEquals("B", BtoD.get(0).getOrigin().getId());
        assertEquals("w", BtoD.get(0).getDest().getId());
        assertEquals("w", BtoD.get(1).getOrigin().getId());
        assertEquals("D", BtoD.get(1).getDest().getId());



        List<Segment> CtoA = result.get("C").get("A").getItinerary();
        assertEquals("C", CtoA.get(0).getOrigin().getId());
        assertEquals("s", CtoA.get(0).getDest().getId());
        assertEquals("s", CtoA.get(1).getOrigin().getId());
        assertEquals("A", CtoA.get(1).getDest().getId());

        List<Segment> CtoB = result.get("C").get("B").getItinerary();
        assertEquals("C", CtoB.get(0).getOrigin().getId());
        assertEquals("u", CtoB.get(0).getDest().getId());
        assertEquals("u", CtoB.get(1).getOrigin().getId());
        assertEquals("t", CtoB.get(1).getDest().getId());
        assertEquals("t", CtoB.get(2).getOrigin().getId());
        assertEquals("B", CtoB.get(2).getDest().getId());

        List<Segment> CtoD = result.get("C").get("D").getItinerary();
        assertEquals("C", CtoD.get(0).getOrigin().getId());
        assertEquals("y", CtoD.get(0).getDest().getId());
        assertEquals("y", CtoD.get(1).getOrigin().getId());
        assertEquals("D", CtoD.get(1).getDest().getId());



        List<Segment> DtoA = result.get("D").get("A").getItinerary();
        assertEquals("D", DtoA.get(0).getOrigin().getId());
        assertEquals("y", DtoA.get(0).getDest().getId());
        assertEquals("y", DtoA.get(1).getOrigin().getId());
        assertEquals("A", DtoA.get(1).getDest().getId());

        List<Segment> DtoB = result.get("D").get("B").getItinerary();
        assertEquals("D", DtoB.get(0).getOrigin().getId());
        assertEquals("w", DtoB.get(0).getDest().getId());
        assertEquals("w", DtoB.get(1).getOrigin().getId());
        assertEquals("B", DtoB.get(1).getDest().getId());

        List<Segment> DtoC = result.get("D").get("C").getItinerary();
        assertEquals("D", DtoC.get(0).getOrigin().getId());
        assertEquals("y", DtoC.get(0).getDest().getId());
        assertEquals("y", DtoC.get(1).getOrigin().getId());
        assertEquals("C", DtoC.get(1).getDest().getId());
    }
}