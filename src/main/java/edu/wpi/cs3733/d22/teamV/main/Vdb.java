package edu.wpi.cs3733.d22.teamV.main;

import edu.wpi.cs3733.d22.teamV.dao.*;
import edu.wpi.cs3733.d22.teamV.dao.EquipmentDeliveryDao;
import edu.wpi.cs3733.d22.teamV.manager.MapManager;
import edu.wpi.cs3733.d22.teamV.servicerequests.ServiceRequest;
import java.sql.*;
import java.util.ArrayList;

public class Vdb {
  public static final String currentPath = returnPath();
  private static String line; // receives a line from br
  private static int serviceIDCounter = 0;
  // Make all DAO's here, NOT in the controllers
  public static final LocationDao locationDao = new LocationDao();
  public static final EquipmentDao equipmentDao = new EquipmentDao();
  public static final EquipmentDeliveryDao equipmentDeliveryDao = new EquipmentDeliveryDao();

  public static final MedicineDeliveryDao medicineDeliveryDao = new MedicineDeliveryDao();
  public static final LabRequestDao labRequestDao = new LabRequestDao();
  public static final InternalPatientTransportationDao internalPatientTransportationDao =
      new InternalPatientTransportationDao();
  public static MapManager mapManager;
  public static EmployeeDao employeeDao;

  public static int getServiceID() {
    return serviceIDCounter++;
  }

  public static void getMaxServiceID() {
    int highestID = serviceIDCounter;
    ArrayList<ServiceRequest> allServiceRequests = new ArrayList<ServiceRequest>();
    // ADD YO SERVICE REQUESTS UNDER MINE YO
    allServiceRequests.addAll(medicineDeliveryDao.getAllServiceRequests());
    allServiceRequests.addAll(equipmentDeliveryDao.getAllServiceRequests());
    allServiceRequests.addAll(labRequestDao.getAllServiceRequests());

    for (ServiceRequest request : allServiceRequests) {
      if (request.getServiceID() > highestID) {
        highestID = request.getServiceID();
      }
    }
    serviceIDCounter = highestID;
  }
  /**
   * Returns the location of the CSVs
   *
   * @return currentPath
   */
  public static String returnPath() {
    // TeamVeganVampires\src\main\resources\edu\wpi\cs3733\d22\teamV
    String currentPath = System.getProperty("user.dir");
    if (currentPath.contains("teamV") || currentPath.contains("TeamVeganVampires")) {
      int position = currentPath.indexOf("teamV") + 51;
      if (currentPath.length() > position) {
        currentPath = currentPath.substring(0, position);
      }
      currentPath += "\\src\\main\\resources\\edu\\wpi\\cs3733\\d22\\teamV";
      System.out.println(currentPath);
    }
    return currentPath;
  }

  /**
   * Initializes all databases and connects to them
   *
   * @throws Exception
   */
  public static void createAllDB() throws Exception {
    mapManager = MapManager.getManager();
    getMaxServiceID();

    System.out.println("-------Embedded Apache Derby Connection Testing --------");
    try {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
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
    try {
      String URL = "jdbc:derby:VDB;";
      Connection connection = DriverManager.getConnection(URL, "admin", "admin");
      return connection;
    } catch (SQLException e) {
      System.out.println("Connection failed. Check output console.");
      e.printStackTrace();
      return null;
    }
  }
}
