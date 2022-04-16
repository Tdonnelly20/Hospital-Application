package edu.wpi.cs3733.d22.teamV.observer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class Subject {
  ArrayList<Observer> observers = new ArrayList<>();

  public void updateAllObservers() throws SQLException, IOException {
    for (Observer observer : observers) {
      observer.update(this);
    }
  }

  public void attach(Observer observer) {
    observers.add(observer);
  }
}
