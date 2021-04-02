package fr.insa.lyon.ifa1.algo;

import fr.insa.lyon.ifa1.models.request.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ChangePassageOrderImplTest {

    private PlanningRequest pr;
    private List<PassagePoint> circuit;
    private ChangePassageOrder cpo;
    private List<Request> requests;
    private Depot d;

    @Before
    public void setUp() throws Exception {
        requests = Arrays.asList(
                new Request(
                        new DurationPassagePoint(null, 0, PassagePointType.PICKUP),
                        new DurationPassagePoint(null, 0, PassagePointType.DELIVERY)
                ),
                new Request(
                        new DurationPassagePoint(null, 0, PassagePointType.PICKUP),
                        new DurationPassagePoint(null, 0, PassagePointType.DELIVERY)
                )
        );

        d = new Depot(null, new Depot.Time(0, 0, 0));

        pr = new PlanningRequest(requests, d);

        circuit = Arrays.asList(
                d,
                requests.get(0).getPickup(),
                requests.get(1).getPickup(),
                requests.get(1).getDelivery(),
                requests.get(0).getDelivery(),
                d
        );

        cpo = new ChangePassageOrderImpl();
    }

    @Test
    public void testSolveTargetBeforeNew() {
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
        try {
            List<PassagePoint> newCircuit = cpo.solve(pr, circuit, d, requests.get(0).getPickup());
            fail("Should throw an exception");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testNewPositionAfterDepotShouldFail() {
        try {
            List<PassagePoint> newCircuit = cpo.solve(pr, circuit, requests.get(0).getPickup(), d);
            fail("Should throw an exception");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testPrecedenceConstraint() {
        try {
            List<PassagePoint> newCircuit = cpo.solve(pr, circuit, requests.get(0).getPickup(), requests.get(0).getDelivery());
            fail("Should throw an exception");
        } catch (Exception e) {
            assertTrue(true);
        }
    }
}