package edu.wpi.cs3733.d22.teamV.interfaces;

import java.util.ArrayList;

public interface Observer {
  ArrayList<Subject> subjects = new ArrayList<>();

  void update(Subject subject);
}
