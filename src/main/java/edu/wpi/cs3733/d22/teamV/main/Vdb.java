package edu.wpi.cs3733.d22.teamV.main;

import edu.wpi.cs3733.d22.teamV.controllers.PopupController;
import edu.wpi.cs3733.d22.teamV.map.MapManager;
import java.io.File;
import java.sql.*;

public class Vdb {
  public static RequestSystem requestSystem = RequestSystem.getSystem();

  private static String line; // receives a line from b
  private static boolean isClient = false;
  private static String ip;
  private static String serverPath;
  private static Connection connection;
  public static MapManager mapManager = MapManager.getManager();
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
    requestSystem.getMaxIDs();
    mapManager.init();
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
    if (connection != null) {
      return connection;
    } else {
      System.out.println("Connection is null");
      return null;
    }
  }

  public static int setUpConnection() {
    String URL;
    try {
      if (!isClient) {
        URL = "jdbc:derby:VDB";

      } else {
        URL = "jdbc:derby://" + ip + "/" + serverPath;
      }
      System.out.println("URL IS " + URL);
      System.out.println(new File(URL).getAbsolutePath());
      connection = DriverManager.getConnection(URL, "admin", "admin");
    } catch (SQLException e) {
      System.out.println("Connection failed. Check output console.");
      e.printStackTrace();
      return 0;
    }
    return 1;
  }

  public static void setIsClient(boolean c) {
    isClient = c;
  }

  public static void setIP(String IPV4) {
    ip = IPV4;
  }

  public static void setServerPath(String server) {
    String path = "";
    for (int i = 0; i < server.length(); i++) {
      if (Character.compare(server.charAt(i), '\"') != 0) {
        if (Character.compare(server.charAt(i), '\\') == 0) {
          path += "//";
        } else {
          path += server.charAt(i);
        }
      }
    }
    serverPath = path;
    System.out.println("Path is" + path);
  }

  public static void setServerIP(String IPV4) {
    ip = IPV4;
  }
}
