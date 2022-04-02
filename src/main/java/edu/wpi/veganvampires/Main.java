package edu.wpi.veganvampires;

import edu.wpi.veganvampires.main.VApp;
import edu.wpi.veganvampires.main.Vdb;

public class Main {

  public static void main(String[] args) throws Exception {
    VApp.launch(VApp.class, args);
    System.out.println("DB Loc size is " + Vdb.locations.size());
  }
}
