package edu.wpi.cs3733.d22.teamV.main;

import edu.wpi.cs3733.d22.teamV.ServiceRequests.LabRequest;
import edu.wpi.cs3733.d22.teamV.ServiceRequests.ServiceRequest;
import edu.wpi.cs3733.d22.teamV.dao.*;
import edu.wpi.cs3733.d22.teamV.dao.EquipmentDeliveryDao;
import edu.wpi.cs3733.d22.teamV.manager.MapManager;
import java.io.*;
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

  public enum Database {
    Location,
    EquipmentDelivery,
    MedicineDelivery,
    ReligiousRequest,
    MealRequest,
    LabRequest,
    SanitationRequest
  }

  public static int getServiceID() {
    return serviceIDCounter++;
  }

  public static void getMaxServiceID() {
    int highestID = serviceIDCounter;
    ArrayList<ServiceRequest> allServiceRequests = new ArrayList<ServiceRequest>();
    // ADD YO SERVICE REQUESTS UNDER MINE YO
    allServiceRequests.addAll(medicineDeliveryDao.getAllServiceRequests());
    allServiceRequests.addAll(equipmentDeliveryDao.getAllServiceRequests());

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
    String currentPath = System.getProperty("user.dir");
    if (currentPath.contains("teamV") || currentPath.contains("TeamVeganVampires")) {
      int position = currentPath.indexOf("teamV") + 49;
      if (currentPath.length() > position) {
        currentPath = currentPath.substring(0, position);
      }
      currentPath += "\\src\\main\\resources\\edu\\wpi\\cs3733\\d22\\teamV";
    }
    return currentPath;
  }

  /**
   * Initializes all databases and connects to them
   *
   * @throws Exception
   */
  public static void createAllDB() throws Exception {
    createLabTable();
    createLabDB();
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

  /**
   * Enter an enumerated type, it will save it
   *
   * @param database
   * @throws Exception
   */
  public static void saveToFile(Database database) throws Exception { // updates all csv files
    switch (database) {
      case LabRequest:
        saveToLabDB();
        break;
      default:
        System.out.println(database + ": Unknown enumerated type!");
        break;
    }
  }

  public static void createLabTable() throws SQLException {

    try {
      // substitute your database name for myDB
      Connection connection = Vdb.Connect();
      assert connection != null;
      Statement newStatement = connection.createStatement();
      DatabaseMetaData meta = connection.getMetaData();
      ResultSet set = meta.getTables(null, null, "LABS", new String[] {"TABLE"});
      if (!set.next()) {
        newStatement.execute(
            "CREATE TABLE LABS ("
                + "UserID int, "
                + "PatientID int, "
                + "FirstName char(20),"
                + "LastName char(20),"
                + "Lab char(20),"
                + "Status char(20))");
      }
    } catch (Exception e) {
      System.out.println("Connection failed. Check output console.");
      e.printStackTrace();
    }
  }

  private static void createLabDB() throws IOException {
    FileReader fr = new FileReader(currentPath + "\\LabRequest.CSV");
    BufferedReader br = new BufferedReader(fr);
    String headerLine = br.readLine();
    String splitToken = ",";
    ArrayList<LabRequest> labs = new ArrayList<>();
    while ((line = br.readLine()) != null) // should create a database based on csv file
    {
      String[] data;
      data = line.split(splitToken);
      LabRequest l =
          new LabRequest(
              Integer.parseInt(data[0]),
              Integer.parseInt(data[1]),
              data[2],
              data[3],
              data[4],
              data[5]);
      labs.add(l);
    }
    LabRequestDao.setAllLabRequests(labs);
  }

  // Add to Medicine Delivery SQL Table
  public static void addToLabTable(
      int userID, int patientID, String firstName, String lastName, String lab, String status)
      throws SQLException {
    String query = "";
    Connection connection = Vdb.Connect();
    assert connection != null;
    Statement statement = connection.createStatement();

    query =
        "INSERT INTO Labs("
            + "userId, patientID, firstName, lastName, lab, status) VALUES "
            + "("
            + userID
            + ", "
            + patientID
            + ", '"
            + firstName
            + "', '"
            + lastName
            + "', '"
            + lab
            + "', '"
            + status
            + "'"
            + ")";

    statement.execute(query);

    // Print out all the current entries...
    query = "SELECT userId, patientID, firstName, lastName, lab, status FROM Labs";

    ResultSet resultSet = statement.executeQuery(query);

    // A string array to contain the names of all the header values so I don't have to type this
    // bullshit out again
    String[] headerVals =
        new String[] {"userID", "patientID", "firstName", "lastName", "lab", "status"};

    // Print out the result
    while (resultSet.next()) {
      for (String headerVal : headerVals) {
        // System.out.print(resultSet.getString(headerVal).trim() + ", ");
      }
    }
  }

  private static void saveToLabDB() throws IOException {
    FileWriter fw = new FileWriter(currentPath + "\\LabRequest.csv");
    BufferedWriter bw = new BufferedWriter(fw);
    bw.append("UserID,PatientID,First Name,Last Name,Lab Type,Status");
    for (LabRequest l : labRequestDao.getAllLabRequests()) {
      String[] outputData = {
        String.valueOf(l.getUserID()),
        String.valueOf(l.getPatient().getPatientID()),
        l.getPatient().getFirstName(),
        l.getPatient().getLastName(),
        l.getLab(),
        l.getStatus()
      };
      bw.append("\n");
    }
    bw.close();
    fw.close();
  }
}
