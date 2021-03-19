package fr.insa.lyon.ifa1.cache;

import fr.insa.lyon.ifa1.models.request.Depot;
import fr.insa.lyon.ifa1.models.request.PlanningRequest;

/**
 * The static class in charges of the in memory storage of the {@link PlanningRequest} instance.
 *
 * @author IFA1
 */
public class PlanningRequestRegistry {

  private static PlanningRequest planningRequest;

  /**
   * Creates the {@link PlanningRequest} from the specified {@link Depot}.
   *
   * @param depot The {@link Depot} of the {@link PlanningRequest} to create.
   */
  public static void createPlanningRequest(Depot depot) {
    planningRequest = new PlanningRequest(depot);
  }

  /**
   * Returns the stored {@link PlanningRequest}.
   *
   * @return The stored {@link PlanningRequest}.
   */
  public static PlanningRequest getPlanningRequest() {
    return planningRequest;
  }
}
