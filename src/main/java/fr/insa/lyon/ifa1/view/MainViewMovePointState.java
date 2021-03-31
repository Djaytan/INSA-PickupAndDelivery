package fr.insa.lyon.ifa1.view;

import fr.insa.lyon.ifa1.models.request.PassagePoint;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class MainViewMovePointState implements StateMainView {

  public PassagePoint pointToMove;

  @Override
  public void canvasClick(MainView context) {

  }

  public MainViewMovePointState(PassagePoint pp) {
    pointToMove = pp;
  }
}
