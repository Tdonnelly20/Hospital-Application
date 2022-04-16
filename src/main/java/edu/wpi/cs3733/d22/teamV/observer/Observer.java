package edu.wpi.cs3733.d22.teamV.observer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface Observer {
  ArrayList<Subject> subjects = new ArrayList<>();

  void update(Subject subject) throws SQLException, IOException;
}
