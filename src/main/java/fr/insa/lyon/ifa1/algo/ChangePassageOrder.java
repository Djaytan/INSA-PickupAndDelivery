package fr.insa.lyon.ifa1.algo;

import fr.insa.lyon.ifa1.models.request.PassagePoint;
import fr.insa.lyon.ifa1.models.request.PlanningRequest;

import java.util.List;

public interface ChangePassageOrder {
    List<PassagePoint> solve(PlanningRequest pr, List<PassagePoint> circuit, PassagePoint target, PassagePoint putTargetAfterThisPoint) throws Exception;
}
