package edu.wpi.cs3733.d22.teamV.interfaces;

import java.util.ArrayList;
import java.util.List;

public abstract class Subject {
  List<Observer> observers = new ArrayList<>();

  public void attach(Observer observer) {
    observers.add(observer);
  }

  public void notifyAllObservers() {
    for (Observer observer : observers) {
      observer.update(this);
    }
  }
}
