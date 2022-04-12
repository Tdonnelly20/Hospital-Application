package edu.wpi.cs3733.d22.teamV.main;

import edu.wpi.cs3733.d22.teamV.controllers.MapController;
import edu.wpi.cs3733.d22.teamV.controllers.PopupController;
import edu.wpi.cs3733.d22.teamV.dao.*;
import edu.wpi.cs3733.d22.teamV.manager.MapManager;
import java.sql.*;

public class Vdb {
  public static RequestSystem requestSystem = RequestSystem.getSystem();

  private static String line; // receives a line from b
  private static boolean isClient = false;
  public static MapManager mapManager = MapManager.getManager();
  public static MapController mapController = MapController.getController();
  public static PopupController popupController = PopupController.getController();

  /**
   * Returns the location of the CSVs
   *
   * @return currentPath
   */

  /**
   * Initializes all databases and connects to them
   *
   * @throws Exception
   */
  public void createAllDB() throws Exception {
    requestSystem.init();
    System.out.println(requestSystem);
    requestSystem.getMaxIDs();
    mapManager.init();
    mapController.init();
    popupController.init();

    System.out.println("-------Apache Derby Connection Testing --------");
    try {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
      Class.forName("org.apache.derby.jdbc.ClientDriver");
    } catch (ClassNotFoundException e) {
      System.out.println("Apache Derby Driver not found. Add the classpath to your module.");
      System.out.println("For IntelliJ do the following:");
      System.out.println("File | Project Structure, Modules, Dependency tab");
      System.out.println("Add by clicking on the green plus icon on the right of the window");
      System.out.println(
          "Select JARs or directories. Go to the folder where the database JAR is located");
      System.out.println("Click OK, now you can compile your program and run it.");
      e.printStackTrace();
      return;
    }

    System.out.println("Apache Derby driver registered!");
  }

  /**
   * Return a connection to the database
   *
   * @return
   */
  public static Connection Connect() {
    String URL;
    try {
      if (!isClient) {
        URL = "jdbc:derby:VDB;";

      } else {
        URL = "jdbc:derby://130.215.13.157/C:/Users/tucke/IdeaProjects/TeamVeganVampires/VDB";
      }
      Connection connection = DriverManager.getConnection(URL, "admin", "admin");
      return connection;
    } catch (SQLException e) {
      System.out.println("Connection failed. Check output console.");
      e.printStackTrace();
      return null;
    }
  }
}
