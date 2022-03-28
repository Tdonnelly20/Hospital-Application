package edu.wpi.veganvampires;

public class Main {

  public static void main(String[] args) throws Exception {
    Vdb database = new Vdb();
    database.CreateDB();
    VApp.launch(VApp.class, args);
  }
}
