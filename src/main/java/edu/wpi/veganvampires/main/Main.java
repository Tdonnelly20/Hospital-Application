package edu.wpi.veganvampires.main;

public class Main {

  public static void main(String[] args) throws Exception {
    Vdb.CreateDB();
    System.out.println("DB Loc size is " + Vdb.locations.size());
    VApp.launch(VApp.class, args);
  }
}
