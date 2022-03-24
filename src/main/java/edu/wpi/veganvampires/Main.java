package edu.wpi.veganvampires;

public class Main {

  public static void main(String[] args) throws Exception {
    System.out.println("This is master");
    Vdb.CreateDB();
    App.launch(App.class, args);
  }
}
