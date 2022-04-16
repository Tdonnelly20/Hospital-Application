package edu.wpi.cs3733.d22.teamV.observer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class DirectionalAssoc {
  ArrayList<DirectionalAssoc> observers = new ArrayList<>();

  public void updateAllObservers() throws SQLException, IOException {
    System.out.println("Running update! for " + observers.size());
    for (DirectionalAssoc observer : observers) {
      observer.update(this);
    }
  }

  // Tri directional attachment
  public static void link(
      DirectionalAssoc observer1, DirectionalAssoc observer2, DirectionalAssoc observer3) {
    observer1.observers.add(observer2);
    observer1.observers.add(observer3);

    observer2.observers.add(observer1);
    observer2.observers.add(observer3);

    observer3.observers.add(observer1);
    observer3.observers.add(observer2);
  }

  public static void link(DirectionalAssoc observer1, DirectionalAssoc observer2) {
    observer1.observers.add(observer2);
    observer2.observers.add(observer1);
  }

  public abstract void update(DirectionalAssoc directionalAssoc);
}
