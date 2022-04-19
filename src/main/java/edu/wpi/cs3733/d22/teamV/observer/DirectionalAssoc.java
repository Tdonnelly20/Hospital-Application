package edu.wpi.cs3733.d22.teamV.observer;

import java.util.ArrayList;

public abstract class DirectionalAssoc {
  ArrayList<DirectionalAssoc> observers = new ArrayList<>();

  public void updateAllObservers() {
    for (int i = 0; i < observers.size(); i++) {
      observers.get(i).update(this);
      DirectionalAssoc.release(observers.get(i), this);
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

  // remove all associations from this observer
  public void releaseAll() {
    for (int i = 0; i < observers.size(); i++) {
      observers.get(i).update(this);
      DirectionalAssoc.release(observers.get(i), this);
    }
  }

  // remove association between two observers
  public static void release(DirectionalAssoc observer1, DirectionalAssoc observer2) {
    observer1.observers.remove(observer2);
    observer2.observers.remove(observer1);
  }

  // Create association between two observers
  public static void link(DirectionalAssoc observer1, DirectionalAssoc observer2) {
    observer1.observers.add(observer2);
    observer2.observers.add(observer1);
  }

  // Update all observers with new info
  public abstract void update(DirectionalAssoc directionalAssoc);
}
