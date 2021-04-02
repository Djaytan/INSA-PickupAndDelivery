package fr.insa.lyon.ifa1.algo;

import fr.insa.lyon.ifa1.models.request.*;

import java.awt.print.Pageable;
import java.security.InvalidParameterException;
import java.util.*;

public class ChangePassageOrderImpl implements ChangePassageOrder {
    @Override
    public List<PassagePoint> solve(PlanningRequest pr, List<PassagePoint> circuit, PassagePoint target, PassagePoint putTargetAfterThisPoint) throws Exception {
        if (!circuit.contains(target) || circuit.get(0).equals(target) || circuit.get(circuit.size() - 1).equals(target))
            throw new InvalidParameterException("Invalid target");
        if (!circuit.contains(putTargetAfterThisPoint) || circuit.get(circuit.size() - 1).equals(putTargetAfterThisPoint))
            throw new InvalidParameterException("Invalid new position for target");

        List<PassagePoint> copyCircuit = new ArrayList<>(circuit);
        copyCircuit.remove(target);
        copyCircuit.add(copyCircuit.indexOf(putTargetAfterThisPoint) + 1, target);

        if (!checkCircuitConstraint(pr, copyCircuit)){
            throw new InvalidParameterException("New position for target doesn't respect precedence constraints");
        }

        return copyCircuit;
    }

    private boolean checkCircuitConstraint(PlanningRequest pr, List<PassagePoint> circuit)
    {
        for (Request r : pr.getRequests()) {
            if (circuit.indexOf(r.getDelivery()) < circuit.indexOf(r.getPickup())) {
                return false;
            }
        }
        return true;
    }


}
