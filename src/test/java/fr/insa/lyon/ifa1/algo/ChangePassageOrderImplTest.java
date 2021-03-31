package fr.insa.lyon.ifa1.algo;

import fr.insa.lyon.ifa1.models.request.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ChangePassageOrderImplTest {

    @Test
    public void testSolveTargetBeforeNew() {
        List<Request> requests = Arrays.asList(
                new Request(
                        new DurationPassagePoint(null, 0, PassagePointType.PICKUP),
                        new DurationPassagePoint(null, 0, PassagePointType.DELIVERY)
                ),
                new Request(
                        new DurationPassagePoint(null, 0, PassagePointType.PICKUP),
                        new DurationPassagePoint(null, 0, PassagePointType.DELIVERY)
                )
        );

        Depot d = new Depot(null, new Depot.Time(0, 0, 0));

        PlanningRequest pr = new PlanningRequest(requests, d);

        List<PassagePoint> circuit = Arrays.asList(
                d,
                requests.get(0).getPickup(),
                requests.get(1).getPickup(),
                requests.get(1).getDelivery(),
                requests.get(0).getDelivery(),
                d
        );

        ChangePassageOrder cpo = new ChangePassageOrderImpl();

        try {
            List<PassagePoint> newCircuit = cpo.solve(pr, circuit, requests.get(0).getPickup(), requests.get(1).getPickup());
            assertEquals(d, newCircuit.get(0));
            assertEquals(requests.get(1).getPickup(), newCircuit.get(1));
            assertEquals(requests.get(0).getPickup(), newCircuit.get(2));
            assertEquals(requests.get(1).getDelivery(), newCircuit.get(3));
            assertEquals(requests.get(0).getDelivery(), newCircuit.get(4));
            assertEquals(d, newCircuit.get(5));
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testTargetAfterNew() {
        List<Request> requests = Arrays.asList(
                new Request(
                        new DurationPassagePoint(null, 0, PassagePointType.PICKUP),
                        new DurationPassagePoint(null, 0, PassagePointType.DELIVERY)
                ),
                new Request(
                        new DurationPassagePoint(null, 0, PassagePointType.PICKUP),
                        new DurationPassagePoint(null, 0, PassagePointType.DELIVERY)
                )
        );

        Depot d = new Depot(null, new Depot.Time(0, 0, 0));

        PlanningRequest pr = new PlanningRequest(requests, d);

        List<PassagePoint> circuit = Arrays.asList(
                d,
                requests.get(0).getPickup(),
                requests.get(1).getPickup(),
                requests.get(1).getDelivery(),
                requests.get(0).getDelivery(),
                d
        );

        ChangePassageOrder cpo = new ChangePassageOrderImpl();

        try {
            List<PassagePoint> newCircuit = cpo.solve(pr, circuit, requests.get(0).getDelivery(), requests.get(0).getPickup());
            assertEquals(d, newCircuit.get(0));
            assertEquals(requests.get(0).getPickup(), newCircuit.get(1));
            assertEquals(requests.get(0).getDelivery(), newCircuit.get(2));
            assertEquals(requests.get(1).getPickup(), newCircuit.get(3));
            assertEquals(requests.get(1).getDelivery(), newCircuit.get(4));
            assertEquals(d, newCircuit.get(5));
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testTargetDepotShouldFail() {
        List<Request> requests = Arrays.asList(
                new Request(
                        new DurationPassagePoint(null, 0, PassagePointType.PICKUP),
                        new DurationPassagePoint(null, 0, PassagePointType.DELIVERY)
                ),
                new Request(
                        new DurationPassagePoint(null, 0, PassagePointType.PICKUP),
                        new DurationPassagePoint(null, 0, PassagePointType.DELIVERY)
                )
        );

        Depot d = new Depot(null, new Depot.Time(0, 0, 0));

        PlanningRequest pr = new PlanningRequest(requests, d);

        List<PassagePoint> circuit = Arrays.asList(
                d,
                requests.get(0).getPickup(),
                requests.get(1).getPickup(),
                requests.get(1).getDelivery(),
                requests.get(0).getDelivery(),
                d
        );

        ChangePassageOrder cpo = new ChangePassageOrderImpl();

        try {
            List<PassagePoint> newCircuit = cpo.solve(pr, circuit, d, requests.get(0).getPickup());
            fail("Should throw an exception");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testNewPositionAfterDepotShouldFail() {
        List<Request> requests = Arrays.asList(
                new Request(
                        new DurationPassagePoint(null, 0, PassagePointType.PICKUP),
                        new DurationPassagePoint(null, 0, PassagePointType.DELIVERY)
                ),
                new Request(
                        new DurationPassagePoint(null, 0, PassagePointType.PICKUP),
                        new DurationPassagePoint(null, 0, PassagePointType.DELIVERY)
                )
        );

        Depot d = new Depot(null, new Depot.Time(0, 0, 0));

        PlanningRequest pr = new PlanningRequest(requests, d);

        List<PassagePoint> circuit = Arrays.asList(
                d,
                requests.get(0).getPickup(),
                requests.get(1).getPickup(),
                requests.get(1).getDelivery(),
                requests.get(0).getDelivery(),
                d
        );

        ChangePassageOrder cpo = new ChangePassageOrderImpl();

        try {
            List<PassagePoint> newCircuit = cpo.solve(pr, circuit, requests.get(0).getPickup(), d);
            fail("Should throw an exception");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testPrecedenceConstraint() {
        List<Request> requests = Arrays.asList(
                new Request(
                        new DurationPassagePoint(null, 0, PassagePointType.PICKUP),
                        new DurationPassagePoint(null, 0, PassagePointType.DELIVERY)
                ),
                new Request(
                        new DurationPassagePoint(null, 0, PassagePointType.PICKUP),
                        new DurationPassagePoint(null, 0, PassagePointType.DELIVERY)
                )
        );

        Depot d = new Depot(null, new Depot.Time(0, 0, 0));

        PlanningRequest pr = new PlanningRequest(requests, d);

        List<PassagePoint> circuit = Arrays.asList(
                d,
                requests.get(0).getPickup(),
                requests.get(1).getPickup(),
                requests.get(1).getDelivery(),
                requests.get(0).getDelivery(),
                d
        );

        ChangePassageOrder cpo = new ChangePassageOrderImpl();

        try {
            List<PassagePoint> newCircuit = cpo.solve(pr, circuit, requests.get(0).getPickup(), requests.get(0).getDelivery());
            fail("Should throw an exception");
        } catch (Exception e) {
            assertTrue(true);
        }
    }
}