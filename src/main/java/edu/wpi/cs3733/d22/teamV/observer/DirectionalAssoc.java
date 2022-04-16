package edu.wpi.cs3733.d22.teamV.observer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class DirectionalAssoc {
  ArrayList<DirectionalAssoc> observers = new ArrayList<>();

  public void updateAllObservers() throws SQLException, IOException {
    System.out.println("Running update! for " + observers.size());
    for (int i = 0; i < 2; i++) {
      observers.get(i).update(this);
      System.out.println(i);
    }
  }

  // Tri directional attachment
  public static void link(
      DirectionalAssoc observer1, DirectionalAssoc observer2, DirectionalAssoc observer3) {
    observer1.observers.add(observer2);
    observer1.observers.add(observer3);
    System.out.println(observer1.observers.size());

    observer2.observers.add(observer1);
    observer2.observers.add(observer3);
    System.out.println(observer2.observers.size());

    observer3.observers.add(observer1);
    observer3.observers.add(observer2);
    System.out.println(observer3.observers.size());
  }

  public static void link(DirectionalAssoc observer1, DirectionalAssoc observer2) {
    observer1.observers.add(observer2);
    observer2.observers.add(observer1);
  }

  public abstract void update(DirectionalAssoc directionalAssoc);
}
