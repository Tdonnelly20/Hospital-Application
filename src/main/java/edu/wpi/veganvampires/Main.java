package edu.wpi.veganvampires;

public class Main {

  public static void main(String[] args) throws Exception {
    Vdb.CreateDB();
    VApp.launch(VApp.class, args);
  }
}
