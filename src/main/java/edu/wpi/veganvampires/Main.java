package edu.wpi.veganvampires;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

  public static void main(String[] args) throws Exception {
    System.out.println("This is master");
    Vdb.CreateDB();
    App.launch(App.class, args);
  }
}
