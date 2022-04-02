package edu.wpi.veganvampires;

import edu.wpi.veganvampires.main.VApp;
import edu.wpi.veganvampires.main.Vdb;

public class Main {

  public static void main(String[] args) throws Exception {
    Vdb.CreateDB();
    System.out.println("DB Loc size is " + Vdb.locations.size());
    VApp.launch(VApp.class, args);
  }
}
