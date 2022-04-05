package edu.wpi.veganvampires.main;

import edu.wpi.veganvampires.dao.EquipmentDeliveryDao;
import edu.wpi.veganvampires.dao.LabRequestDao;
import edu.wpi.veganvampires.dao.LocationDao;
import edu.wpi.veganvampires.dao.MedicineDeliveryDao;
import edu.wpi.veganvampires.manager.MapManager;
import edu.wpi.veganvampires.objects.*;
import edu.wpi.veganvampires.objects.Location;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class Vdb {
  private static final String currentPath = returnPath();
  private static String line; // receives a line from br

  // Make all DAO's here, NOT in the controllers
  public static final EquipmentDeliveryDao equipmentDeliveryDao = new EquipmentDeliveryDao();
  public static final LocationDao locationDao = new LocationDao();
  public static final MedicineDeliveryDao medicineDeliveryDao = new MedicineDeliveryDao();
  public static final LabRequestDao labRequestDao = new LabRequestDao();
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
  /**
   * Returns the location of the CSVs
   *
   * @return currentPath
   */
  public static String returnPath() {
    String currentPath = System.getProperty("user.dir");
    if (currentPath.contains("TeamVeganVampires")) {
      int position = currentPath.indexOf("TeamVeganVampires") + 17;
      if (currentPath.length() > position) {
        currentPath = currentPath.substring(0, position);
      }
      currentPath += "\\src\\main\\resources\\edu\\wpi\\veganvampires";
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
    createEquipmentDB();
    createMedicineDeliveryTable();
    createMedicineDeliveryDB();
    createLabTable();
    createLabDB();
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
    Connection connection = Connect();

    try {
      // substitute your database name for myDB
      Statement exampleStatement = connection.createStatement();
      DatabaseMetaData meta = connection.getMetaData();
      ResultSet set = meta.getTables(null, null, "LOCATIONS", new String[] {"TABLE"});
      if (!set.next()) {
        System.out.println("WE MAKInG TABLES");
        exampleStatement.execute(
            "CREATE TABLE Locations(nodeID int, xCoord int, yCoord int, floor char(10), building char(20), nodeType char(10), longName char(60), shortName char(30))");
      } else {
        System.out.println("We already got tables?");
      }
    } catch (SQLException e) {
      System.out.println("Connection failed. Check output console.");
      e.printStackTrace();
      return;
    } catch (Exception e) {
      System.out.println("Connection failed. Check output console.");
      e.printStackTrace();
    }
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
      case EquipmentDelivery:
        saveToEquipmentDB();
        break;
      case MedicineDelivery:
        saveToMedicineDeliveryCSV();
        break;
      case LabRequest:
        saveToLabDB();
      default:
        System.out.println("Unknown enumerated type!");
        break;
    }
  }

  /**
   * Create the medicine delivery database
   *
   * @throws Exception
   */
  public static void createMedicineDeliveryDB() throws Exception {
    FileReader fr = new FileReader(currentPath + "\\MedicineDelivery.csv");
    BufferedReader br = new BufferedReader(fr);
    String splitToken = ","; // what we split the csv file with
    ArrayList<MedicineDelivery> medicineDeliveries = new ArrayList<>();
    // equipment = new ArrayList<>();
    String headerLine = br.readLine();

    String query = "";

    // Connecting to DB
    Connection connection = Vdb.Connect();
    Statement statement = connection.createStatement();

    while ((line = br.readLine()) != null) // should create a database based on csv file
    {
      String[] data = line.split(splitToken);
      MedicineDelivery newDelivery =
          new MedicineDelivery(
              data[0],
              data[1],
              data[2],
              Integer.parseInt(data[3]),
              Integer.parseInt(data[4]),
              data[5],
              data[6],
              data[7]);
      medicineDeliveries.add(newDelivery);

      // add to SQL table
      addToMedicineTable(
          data[0],
          data[1],
          data[2],
          Integer.parseInt(data[3]),
          Integer.parseInt(data[4]),
          data[5],
          data[6],
          data[7]);
    }

    // Add to local arraylist
    medicineDeliveryDao.setAllMedicineDeliveries(medicineDeliveries);
  }

  /**
   * Create the SQL Table for the medicine delivery service request
   *
   * @throws SQLException
   */
  public static void createMedicineDeliveryTable() throws SQLException {
    String query = "";
    try {

      // Connect to database and find Medicines table
      Connection connection = Connect();
      Statement statement = connection.createStatement();
      DatabaseMetaData meta = connection.getMetaData();
      ResultSet set = meta.getTables(null, null, "MEDICINES", new String[] {"TABLE"});

      // Create the table if not created yet, and recreate it if it has already been created...
      if (!set.next()) {
        System.out.println("Creating Medicine Delivery SQL Tables...");

        query =
            "CREATE TABLE Medicines( "
                + "patientFirstName CHAR(60), "
                + "patientLastName CHAR(60), "
                + "roomNumber  CHAR(60), "
                + "patientID INT, "
                + "hospitalID INT, "
                + "medicineName CHAR(40), "
                + "dosage CHAR(40), "
                + "requestDetails char(200))";

        statement.execute(query);

      } else {
        statement.execute("DROP TABLE MEDICINES");
        System.out.println("Tables already found! Dropping...");
        createMedicineDeliveryTable();
        return;
      }

    } catch (SQLException e) {
      System.out.println("Connection failed. Check output console.");
      e.printStackTrace();

    } catch (Exception e) {
      System.out.println("Connection failed. Check output console.");
      e.printStackTrace();
    }
  }

  // A method I (Matt) created to test the Medicine Delivery SQL Table, it is a good reference for
  // SQL Commands
  private static void testMedicineDeliverySQLTable() throws SQLException {
    String query = "";
    String test = "Matt";
    Connection connection = Connect();
    Statement statement = connection.createStatement();
    // Insert a sample entry into the database
    query =
        "INSERT INTO Medicines("
            + "patientFirstName, patientLastName, roomNumber, patientID, hospitalID, medicineName, dosage, requestDetails) VALUES "
            + "('"
            + test
            + "', 'Hendrickson', 'Fuller Labs', 123, 123, 'Adderall', '100 mg', 'Taken twice a day') ";

    statement.execute(query);

    // Result must be selected before removing!
    query =
        "SELECT patientFirstName, patientLastName, roomNumber, patientID, hospitalID, medicineName, dosage, requestDetails FROM Medicines";

    ResultSet resultSet = statement.executeQuery(query);

    // A string array to contain the names of all the header values so I don't have to type this
    // bullshit out again
    String[] headerValues =
        new String[] {
          "patientFirstName",
          "patientLastName",
          "roomNumber",
          "patientID",
          "hospitalID",
          "medicineName",
          "dosage",
          "requestDetails"
        };

    // Print out the result
    while (resultSet.next()) {
      for (int i = 0; i < headerValues.length; i++) {
        System.out.print(resultSet.getString(headerValues[i]).trim() + ", ");
      }
      System.out.println();
    }

    // Remove from table
    query = "DELETE FROM Medicines WHERE patientFirstName = 'Matt'";
    int num = statement.executeUpdate(query);

    System.out.println("Number of records deleted are: " + num);
  }

  private static void saveToMedicineDeliveryCSV() throws IOException {
    FileWriter fw = new FileWriter(currentPath + "\\MedicineDelivery.csv");
    BufferedWriter bw = new BufferedWriter(fw);
    bw.append(
        "patientFirstName,patientLastName,roomNumber,patientID,hospitalID,medicineName,dosage,requestDetails");

    // get all medicine deliveries
    for (MedicineDelivery medicineDelivery : medicineDeliveryDao.getAllMedicineDeliveries()) {
      String[] outputData = {
        medicineDelivery.getPatientFirstName(),
        medicineDelivery.getPatientLastName(),
        medicineDelivery.getRoomNumber(),
        String.valueOf(medicineDelivery.getPatientID()),
        String.valueOf(medicineDelivery.getHospitalID()),
        medicineDelivery.getMedicineName(),
        medicineDelivery.getDosage(),
        medicineDelivery.getRequestDetails()
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

  // Add to Medicine Delivery SQL Table
  public static void addToMedicineTable(
      String patientFirstName,
      String patientLastName,
      String roomNumber,
      int patientID,
      int hospitalID,
      String medicineName,
      String dosage,
      String requestDetails)
      throws SQLException {
    String query = "";
    Connection connection = Vdb.Connect();
    Statement statement = connection.createStatement();

    query =
        "INSERT INTO Medicines("
            + "patientFirstName, patientLastName, roomNumber, patientID, hospitalID, medicineName, dosage, requestDetails) VALUES "
            + "('"
            + patientFirstName
            + "', '"
            + patientLastName
            + "', '"
            + roomNumber
            + "', "
            + patientID
            + ", "
            + hospitalID
            + ", '"
            + medicineName
            + "', '"
            + dosage
            + "', '"
            + requestDetails
            + "'"
            + ")";

    System.out.println(query);
    statement.execute(query);

    // Print out all the current entries...
    query =
        "SELECT patientFirstName, patientLastName, roomNumber, patientID, hospitalID, medicineName, dosage, requestDetails FROM Medicines";

    ResultSet resultSet = statement.executeQuery(query);

    // A string array to contain the names of all the header values so I don't have to type this
    // bullshit out again
    String[] headerVals =
        new String[] {
          "patientFirstName",
          "patientLastName",
          "roomNumber",
          "patientID",
          "hospitalID",
          "medicineName",
          "dosage",
          "requestDetails"
        };

    // Print out the result
    while (resultSet.next()) {
      for (int i = 0; i < headerVals.length; i++) {
        System.out.print(resultSet.getString(headerVals[i]).trim() + ", ");
      }
      System.out.println();
    }
  }

  /**
   * Create the location database
   *
   * @throws Exception
   */
  public static void createLocationDB() throws Exception {
    FileReader fr = new FileReader(currentPath + "\\TowerLocations.csv");
    BufferedReader br = new BufferedReader(fr);
    String splitToken = ","; // what we split the csv file with
    ArrayList<Location> locations = new ArrayList<>();
    // equipment = new ArrayList<>();
    String headerLine = br.readLine();
    while ((line = br.readLine()) != null) // should create a database based on csv file
    {
      String[] data = line.split(splitToken);
      Location newLoc =
          new Location(
              data[0],
              Integer.parseInt(data[1]),
              Integer.parseInt(data[2]),
              data[3],
              data[4],
              data[5],
              data[6],
              data[7]);
      locations.add(newLoc);
    }
    locationDao.setAllLocations(locations);
    System.out.println("Location database made");

    mapManager = MapManager.getManager();
  }

  /**
   * Saves the location DB
   *
   * @throws IOException
   */
  public static void saveToLocationDB() throws IOException {
    FileWriter fw = new FileWriter(currentPath + "\\LocationsBackup.csv");
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

  /**
   * Saves the equipmentDB
   *
   * @throws IOException
   */
  private static void saveToEquipmentDB() throws IOException {
    FileWriter fw = new FileWriter(currentPath + "\\MedEquipReq.csv");
    BufferedWriter bw = new BufferedWriter(fw);
    bw.append("Name,Description,Location,Count");
    for (EquipmentDelivery e : equipmentDeliveryDao.getAllEquipmentDeliveries()) {
      String[] outputData = {
        e.getLocation(), e.getEquipment(), e.getNotes(), String.valueOf(e.getQuantity())
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

  /**
   * Initialize the equipment database
   *
   * @throws IOException
   */
  private static void createEquipmentDB() throws IOException {
    FileReader fr = new FileReader(currentPath + "\\MedEquipReq.CSV");
    BufferedReader br = new BufferedReader(fr);
    String headerLine = br.readLine();
    String splitToken = ",";
    ArrayList<EquipmentDelivery> equipment = new ArrayList<>();
    while ((line = br.readLine()) != null) // should create a database based on csv file
    {
      String[] data;
      data = line.split(splitToken);
      for (String s : data) System.out.println(s);
      EquipmentDelivery e =
          new EquipmentDelivery(data[0], data[1], data[2], Integer.parseInt(data[3]));
      equipment.add(e);
    }
    equipmentDeliveryDao.setAllEquipmentDeliveries(equipment);
    System.out.println("Equipment database made");
  }

  private static void createEquipmentTable() throws Exception {
    Connection connection = Connect();
    try {
      Statement exampleStatement = connection.createStatement();
      DatabaseMetaData meta = connection.getMetaData();
      ResultSet set = meta.getTables(null, null, "EQUIPMENT", new String[] {"TABLE"});
      if (!set.next()) {
        System.out.println("WE MAKInG TABLES");
        exampleStatement.execute(
            "CREATE TABLE EQUIPMENT(location char(50), name char(30), description char(100), count int)");
      } else {
        exampleStatement.execute("DROP TABLE EQUIPMENT");
        exampleStatement.execute(
            "CREATE TABLE EQUIPMENT(location char(50), name char(30), description char(100), count int)");
      }
    } catch (SQLException e) {
      System.out.println("Connection failed. Check output console.");
      e.printStackTrace();
      return;
    } catch (Exception e) {
      System.out.println("Connection failed. Check output console.");
      e.printStackTrace();
    }

    ArrayList<EquipmentDelivery> equipment = equipmentDeliveryDao.getAllEquipmentDeliveries();
    int i = 0;
    System.out.println("ADDING " + equipment.size() + " EQUIPMENT");
    String test = "\'";
    PreparedStatement pSTMT =
        connection.prepareStatement("INSERT INTO EQUIPMENT VALUES (?, ?, ?, ?)");
    while (equipment.size() > i) {
      EquipmentDelivery ed = equipment.get(i);
      System.out.println(
          "Loc: "
              + ed.getLocation()
              + "  Eq: "
              + ed.getEquipment()
              + " Notes: "
              + ed.getNotes()
              + " QNT : "
              + ed.getQuantity());
      pSTMT.setString(1, ed.getLocation());
      pSTMT.setString(2, ed.getEquipment());
      pSTMT.setString(3, ed.getNotes());
      pSTMT.setInt(4, ed.getQuantity());
      pSTMT.executeUpdate();
      i++;
    }

    Statement exampleStatement = connection.createStatement();
    System.out.println("BREAK");
    ResultSet rs = exampleStatement.executeQuery("SELECT * FROM EQUIPMENT");

    System.out.println("Apache Derby connection established!");
    while (rs.next()) {
      System.out.println("THIS IS A Equipment");
      System.out.println("Loc: " + rs.getString("location"));
      System.out.println("Name: " + rs.getString("name"));
      System.out.println("Desc: " + rs.getString("description"));
      System.out.println("CNT: " + rs.getString("count"));
      System.out.println(" ");
    }
    System.out.println("HERE");
  }

  public static void createLabTable() throws SQLException {

    try {
      // substitute your database name for myDB
      Connection connection = Vdb.Connect();
      assert connection != null;
      Statement newStatement = connection.createStatement();
      DatabaseMetaData meta = connection.getMetaData();
      ResultSet set = meta.getTables(null, null, "LOCATIONS", new String[] {"TABLE"});
      if (!set.next()) {
        System.out.println("WE MAKInG TABLES");
        newStatement.execute(
            "CREATE TABLE LABS ("
                + "UserID int, "
                + "PatientID int, "
                + "FirstName char[20],"
                + "LastName char[20],"
                + "Lab char[20],"
                + "Status char[20])");
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
      for (String s : data) System.out.println(s);
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
        "INSERT INTO Medicines("
            + "userId, patientID, firstName, lastName, lab, status) VALUES "
            + "('"
            + userID
            + "', '"
            + patientID
            + "', '"
            + firstName
            + "', "
            + lastName
            + ", "
            + lab
            + ", '"
            + status
            + "'"
            + ")";

    System.out.println(query);
    statement.execute(query);

    // Print out all the current entries...
    query = "SELECT userId, patientID, firstName, lastName, lab, status FROM Medicines";

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
        // String.valueOf(l.getUserID()),
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
