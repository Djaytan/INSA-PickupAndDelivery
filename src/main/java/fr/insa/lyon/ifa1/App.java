package fr.insa.lyon.ifa1;

import fr.insa.lyon.ifa1.controller.GeoMapController;
import fr.insa.lyon.ifa1.controller.PlanningRequestController;
import fr.insa.lyon.ifa1.controller.ViewController;

import java.util.logging.Logger;

/** Hello world! */
public class App {

  private static final Logger LOGGER = Logger.getLogger(App.class.getName());
  private static final Class[] CONTROLLERS = new Class[] {
          ViewController.class,
          GeoMapController.class,
          PlanningRequestController.class
  };

  public static void main(String[] args) {

    LOGGER.info("Starting");

    LOGGER.info("Initializing controllers");
    for(Class controller : App.CONTROLLERS) {

      try { controller.getDeclaredConstructor().newInstance(); }
      catch(NoSuchMethodException ex) { System.out.println("Missing constructor in controller " + controller.getName()); }
      catch(Exception ex) {System.out.println("Failed to initialize controller " + controller.getName()); }

    }

    LOGGER.info("Running view");
    ViewController.getInstance().run();

  }

}
