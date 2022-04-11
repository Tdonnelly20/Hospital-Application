package edu.wpi.cs3733.d22.teamV;

import edu.wpi.cs3733.d22.teamV.main.VApp;
import edu.wpi.cs3733.d22.teamV.main.Vdb;

public class Main {

  public static void main(String[] args) throws Exception {
    VApp.launch(VApp.class, args);
    System.out.println("DB Loc size is " + Vdb.requestSystem.getLocations().size());
  }
}
