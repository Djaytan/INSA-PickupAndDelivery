package fr.insa.lyon.ifa1.algo;

import fr.insa.lyon.ifa1.models.request.PassagePoint;
import fr.insa.lyon.ifa1.models.request.PlanningRequest;

import java.util.List;

public interface ChangePassageOrder {
  /**
   *
   * Change the priority of a passage point
   *
   * @param pr The Planning Request
   * @param circuit The original circuit
   * @param target The passage point to change
   * @param putTargetAfterThisPoint You put the target after this passage point
   * @return The new circuit
   */
    List<PassagePoint> solve(PlanningRequest pr, List<PassagePoint> circuit, PassagePoint target, PassagePoint putTargetAfterThisPoint) throws Exception;
}
