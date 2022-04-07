package edu.wpi.cs3733.d22.teamV.main;

import edu.wpi.cs3733.d22.teamV.ServiceRequests.LabRequest;
import edu.wpi.cs3733.d22.teamV.dao.*;
import edu.wpi.cs3733.d22.teamV.dao.EquipmentDeliveryDao;
import edu.wpi.cs3733.d22.teamV.manager.MapManager;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class Vdb {
  public static final String currentPath = returnPath();
  private static String line; // receives a line from br
  private int serviceIDCounter = 0;
  // Make all DAO's here, NOT in the controllers
  public static final EquipmentDao equipmentDao = new EquipmentDao();
  public static final EquipmentDeliveryDao equipmentDeliveryDao = new EquipmentDeliveryDao();
  public static final LocationDao locationDao = new LocationDao();
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

  public int getServiceID() {
    return serviceIDCounter++;
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
    createLocationDB();
    createLabTable();
    createLabDB();
    mapManager = MapManager.getManager();
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
    Connection connection;

    try {
      // substitute your database name for myDB
      connection = Connect();
      Statement exampleStatement = connection.createStatement();
      DatabaseMetaData meta = connection.getMetaData();
      ResultSet set = meta.getTables(null, null, "LOCATIONS", new String[] {"TABLE"});
      if (!set.next()) {
        System.out.println("WE MAKInG TABLES");
        exampleStatement.execute(
            "CREATE TABLE Locations(nodeID int, xCoord int, yCoord int, floor char(10), building char(20), nodeType char(10), longName char(60), shortName char(30))");
      } else {
        System.out.println("We already got tables?");
        System.out.println("listing tables");
        System.out.println("RS " + set.getString(1));
        System.out.println("RS " + set.getString(2));
        System.out.println("RS " + set.getString(3));
        System.out.println("RS " + set.getString(4));
        System.out.println("RS " + set.getString(5));
        System.out.println("RS " + set.getString(6));
        while (set.next()) {
          System.out.println("RS " + set.getString(1));
          System.out.println("RS " + set.getString(2));
          System.out.println("RS " + set.getString(3));
          System.out.println("RS " + set.getString(4));
          System.out.println("RS " + set.getString(5));
          System.out.println("RS " + set.getString(6));
        }
      }
    } catch (SQLException e) {
      System.out.println("Connection failed. Check output console.");
      e.printStackTrace();
      return;
    } catch (Exception e) {
      System.out.println("Connection failed. Check output console.");
      e.printStackTrace();
    }
    System.out.println("Apache Derby connection established!");

    System.out.println(LocationDao.getAllLocations());
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
      case Location:
        saveToLocationDB();
        break;
      case LabRequest:
        saveToLabDB();
        break;
      default:
        System.out.println("Unknown enumerated type!");
        break;
    }
  }

  /**
   * Create the location database
   *
   * @throws Exception
   */
  public static void createLocationDB() throws Exception {}

  public static void createLocationTable() throws SQLException {}

  public static void addToLocationTable(
      String userID,
      int xCoord,
      int yCoord,
      String floor,
      String building,
      String nodeType,
      String longName,
      String shortName)
      throws SQLException {
    String query = "";
    Connection connection = Vdb.Connect();
    assert connection != null;
    Statement statement = connection.createStatement();

    query =
        "INSERT INTO Locations("
            + "userId, xCoord, yCoord, floor, building, nodeType, longName, shortName) VALUES "
            + "('"
            + userID
            + "', "
            + xCoord
            + ", "
            + yCoord
            + ", '"
            + floor
            + "',' "
            + building
            + "', '"
            + nodeType
            + "','"
            + longName
            + "','"
            + shortName
            + "')";

    System.out.println(query);
    statement.execute(query);

    // Print out all the current entries...
    query =
        "SELECT userId, xCoord, yCoord, floor, building, nodeType, longName, shortName FROM Locations";

    ResultSet resultSet = statement.executeQuery(query);

    // A string array to contain the names of all the header values so I don't have to type this
    // bullshit out again
    String[] headerVals =
        new String[] {
          "userID", "xCoord", "yCoord", "floor", "building", "nodeType", "longName", "shortName"
        };

    // Print out the result
    while (resultSet.next()) {
      for (String headerVal : headerVals) {
        System.out.print(resultSet.getString(headerVal).trim() + ", ");
      }
      System.out.println();
    }
  }

  /**
   * Saves the location DB
   *
   * @throws IOException
   */
  public static void saveToLocationDB() throws IOException {}

  public static void saveToBackupLocationDB() throws IOException {
    FileWriter fw = new FileWriter(currentPath + "\\TowerLocations.csv");
    BufferedWriter bw = new BufferedWriter(fw);
    // nodeID	xcoord	ycoord	floor	building	nodeType	longName	shortName
    bw.append("nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName");
    for (Location l : locationDao.getAllLocations()) {
      String[] outputData = {
        l.getNodeID(),
        String.valueOf(l.getXCoord()),
        String.valueOf(l.getYCoord()),
        l.getFloor(),
        l.getBuilding(),
        l.getNodeType(),
        l.getLongName(),
        l.getShortName(),
      };
      bw.append("\n");
      for (String s : outputData) {
        bw.append(s);
        bw.append(',');
      }
    }
    bw.close();
    fw.close();
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
        System.out.println("WE MAKInG TABLES");
        newStatement.execute(
            "CREATE TABLE LABS ("
                + "UserID int, "
                + "PatientID int, "
                + "FirstName char(20),"
                + "LastName char(20),"
                + "Lab char(20),"
                + "Status char(20))");
      } else {
        System.out.println("We already got tables?");
        System.out.println("listing tables");
        System.out.println("RS " + set.getString(1));
        System.out.println("RS " + set.getString(2));
        System.out.println("RS " + set.getString(3));
        System.out.println("RS " + set.getString(4));
        System.out.println("RS " + set.getString(5));
        System.out.println("RS " + set.getString(6));
        while (set.next()) {
          System.out.println("RS " + set.getString(1));
          System.out.println("RS " + set.getString(2));
          System.out.println("RS " + set.getString(3));
          System.out.println("RS " + set.getString(4));
          System.out.println("RS " + set.getString(5));
          System.out.println("RS " + set.getString(6));
        }
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
      System.out.println(line);
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
    System.out.println("Lab database made");
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

    System.out.println(query);
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
        System.out.print(resultSet.getString(headerVal).trim() + ", ");
      }
      System.out.println();
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
      for (String s : outputData) {
        bw.append(s);
        bw.append(',');
        System.out.println(s);
      }
    }
    bw.close();
    fw.close();
  }
}
