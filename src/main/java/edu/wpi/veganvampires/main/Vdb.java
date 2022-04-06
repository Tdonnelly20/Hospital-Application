package edu.wpi.veganvampires.main;

import edu.wpi.veganvampires.dao.*;
import edu.wpi.veganvampires.dao.EquipmentDao;
import edu.wpi.veganvampires.dao.EquipmentDeliveryDao;
import edu.wpi.veganvampires.dao.InternalPatientTransportationDao;
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
    createEquipmentTable();
    createMedicineDeliveryDB();
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
      case EquipmentDelivery:
        saveToEquipmentDB();
        break;
      case MedicineDelivery:
        saveToMedicineDeliveryDB();
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
    }
    medicineDeliveryDao.setAllMedicineDeliveries(medicineDeliveries);
    System.out.println("Medicine delivery database made");
  }

  private static void saveToMedicineDeliveryDB() throws IOException {
    FileWriter fw = new FileWriter(currentPath + "\\MedicineDelivery.csv");
    BufferedWriter bw = new BufferedWriter(fw);
    bw.append(
        "patientFirstName,patientLastName,roomNumber,patientID,hospitalID,medicineName,dosage,requestDetails");

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

  /**
   * Initialize the equipment database
   *
   * @throws IOException
   */
  private static void createEquipmentDB() throws IOException {
    // this does listofequipment
    FileReader fr = new FileReader(currentPath + "\\ListofEquipment.CSV");
    BufferedReader br = new BufferedReader(fr);
    String headerLine = br.readLine();
    String splitToken = ",";
    ArrayList<Equipment> equipment = new ArrayList<>();
    // int ID, String name, floor,double x, double y, String description, Boolean isDirty) {
    while ((line = br.readLine()) != null) // should create a database based on csv file
    {
      String[] data;
      data = line.split(splitToken);
      for (String s : data) System.out.println(s);
      Equipment e =
          new Equipment(
              data[0],
              data[1],
              data[2],
              Double.parseDouble(data[3]),
              Double.parseDouble(data[4]),
              data[5],
              Boolean.parseBoolean(data[6]));
      equipment.add(e);
    }
    equipmentDao.setAllEquipment(equipment);
    System.out.println("Equipment database made");

    // int ID, String name, double x, double y, String description, Boolean isDirty)
    // this does deliverytable
    fr = new FileReader(currentPath + "\\MedEquipReq.CSV");
    br = new BufferedReader(fr);
    headerLine = br.readLine();
    ArrayList<EquipmentDelivery> equipmentDelivery = new ArrayList<>();
    // int userID, String nodeID,  String equipment, String notes, int quantity, String status,int
    // pID, String fname, String lname)
    while ((line = br.readLine()) != null) // should create a database based on csv file
    {
      String[] data;
      data = line.split(splitToken);
      for (String s : data) System.out.println(s);
      EquipmentDelivery ed;
      if (data.length == 6) { // no patient
        ed =
            new EquipmentDelivery(
                Integer.parseInt(data[0]),
                data[1],
                data[2],
                data[3],
                Integer.parseInt(data[4]),
                data[5]);
      } else {
        ed =
            new EquipmentDelivery(
                Integer.parseInt(data[0]),
                data[1],
                data[2],
                data[3],
                Integer.parseInt(data[4]),
                data[5],
                Integer.parseInt(data[6]),
                data[7],
                data[8]);
      }

      equipmentDelivery.add(ed);
    }
    equipmentDeliveryDao.setAllEquipmentDeliveries(equipmentDelivery);
    System.out.println("Equipment Delivery database made");
  }

  private static void createEquipmentTable() throws Exception {

    // makes equipment table and equipdeliverytable
    Connection connection = Connect();
    // equipment table
    // ID	Name	X	Y	Description	isDirty
    try {
      // substitute your database name for myDB
      Statement exampleStatement = connection.createStatement();
      DatabaseMetaData meta = connection.getMetaData();
      ResultSet set = meta.getTables(null, null, "EQUIPMENT", new String[] {"TABLE"});
      if (!set.next()) {
        exampleStatement.execute(
            "CREATE TABLE EQUIPMENT(ID char(15),name char(40), floor char(2),x int, y int,description char(100), isDirty boolean)");
      } else {
        exampleStatement.execute("DROP TABLE EQUIPMENT");
        exampleStatement.execute(
            "CREATE TABLE EQUIPMENT(ID char(15),name char(40), floor char(2),x int, y int,description char(100), isDirty boolean)");
      }
    } catch (SQLException e) {
      System.out.println("Connection failed. Check output console.");
      e.printStackTrace();
      return;
    } catch (Exception e) {
      System.out.println("Connection failed. Check output console.");
      e.printStackTrace();
    }

    ArrayList<Equipment> equipment = equipmentDao.getAllEquipment();
    int i = 0;
    System.out.println("ADDING " + equipment.size() + " EQUIPMENT");
    String test = "\'";
    PreparedStatement pSTMT =
        connection.prepareStatement("INSERT INTO EQUIPMENT VALUES (?, ?, ?, ?,?,?,?)");
    // int ID, String name, floor,double x, double y, String description, Boolean isDirty) {
    while (equipment.size() > i) {
      Equipment e = equipment.get(i);
      pSTMT.setString(1, e.getID());
      pSTMT.setString(2, e.getName());
      pSTMT.setString(3, e.getFloor());
      pSTMT.setDouble(4, e.getX());
      pSTMT.setDouble(5, e.getY());
      pSTMT.setString(6, e.getDescription());
      pSTMT.setBoolean(7, e.getIsDirty());
      pSTMT.executeUpdate();
      i++;
    }

    // making delivery table
    // int eID, int pID, String fname, String lname, String location, String equipment, String
    // notes, int quantity, String status
    try {
      // substitute your database name for myDB
      Statement exampleStatement = connection.createStatement();
      DatabaseMetaData meta = connection.getMetaData();
      ResultSet set = meta.getTables(null, null, "EQUIPMENTDELIVERY", new String[] {"TABLE"});
      if (!set.next()) {
        System.out.println("WE MAKInG TABLES");
        exampleStatement.execute(
            "CREATE TABLE EQUIPMENTDELIVERY(EmpID int,location char(50), equipment char(30), notes char(100), count int, status char(40), PID int, pFname char(25), pLname char(25))");
      } else {
        exampleStatement.execute("DROP TABLE EQUIPMENTDELIVERY");
        exampleStatement.execute(
            "CREATE TABLE EQUIPMENTDELIVERY(EmpID int,location char(50), equipment char(30), notes char(100), count int, status char(40), PID int, pFname char(25), pLname char(25))");
      }
    } catch (SQLException e) {
      System.out.println("Connection failed. Check output console.");
      e.printStackTrace();
      return;
    } catch (Exception e) {
      System.out.println("Connection failed. Check output console.");
      e.printStackTrace();
    }

    ArrayList<EquipmentDelivery> equipmentDelivery =
        equipmentDeliveryDao.getAllEquipmentDeliveries();
    i = 0;

    while (equipmentDelivery.size() > i) {
      EquipmentDelivery ed = equipmentDelivery.get(i);
      if (ed.getPatient() == null) { // no pataient
        pSTMT =
            connection.prepareStatement("INSERT INTO EQUIPMENTDELIVERY VALUES (?, ?, ?, ?,?,?)");
      } else {

        pSTMT =
            connection.prepareStatement(
                "INSERT INTO EQUIPMENTDELIVERY VALUES (?, ?, ?, ?,?,?,?,?,?)");
      }
      pSTMT.setInt(1, ed.getHospitalEmployee().getHospitalID());
      pSTMT.setString(2, ed.getLocation().getNodeID());
      pSTMT.setString(3, ed.getEquipment());
      pSTMT.setString(4, ed.getNotes());
      pSTMT.setInt(5, ed.getQuantity());
      pSTMT.setString(6, ed.getStatus());
      if (ed.getPatient() != null) {
        pSTMT.setInt(7, ed.getPatient().getPatientID());
        pSTMT.setString(8, ed.getPatient().getFirstName());
        pSTMT.setString(9, ed.getPatient().getLastName());
      }
      pSTMT.executeUpdate();
      i++;
    }
    Statement exampleStatement = connection.createStatement();
    ResultSet rs = exampleStatement.executeQuery("SELECT * FROM EQUIPMENTDELIVERY");

    System.out.println("Apache Derby connection established!");
  }

  private static void saveToEquipmentDB() throws IOException {
    FileWriter fw = new FileWriter(currentPath + "\\ListofEquipment.csv");
    BufferedWriter bw = new BufferedWriter(fw);
    bw.append("ID,Name,Floor,X,Y,Description,isDirty");
    // ID	Name	X	Y	Description	isDirty
    for (Equipment e : equipmentDao.getAllEquipment()) {
      String[] outputData = {
        e.getID(),
        e.getName(),
        e.getFloor(),
        Double.toString(e.getX()),
        Double.toString(e.getY()),
        e.getDescription(),
        Boolean.toString(e.getIsDirty())
      };
      bw.append("\n");
      for (String s : outputData) {
        bw.append(s);
        bw.append(',');
        System.out.println(s);
      }
    }
    fw = new FileWriter(currentPath + "\\MedEquipReq.csv");
    bw = new BufferedWriter(fw);
    // EmpID	Location	Name	Notes	Count	Status	PatientID	Fname	Lname
    bw.append("EmpID,Location,Name,Notes,Count,Status,PatientID,Fname,Lname");
    for (EquipmentDelivery e : equipmentDeliveryDao.getAllEquipmentDeliveries()) {
      String[] outputData;
      if (e.getPatient() == null) {
        outputData =
            new String[] {
              Integer.toString(e.getHospitalEmployee().getHospitalID()),
              e.getLocation().getNodeID(),
              e.getEquipment(),
              e.getNotes(),
              Integer.toString(e.getQuantity()),
              e.getStatus()
            };
      } else {
        Patient p = e.getPatient();
        outputData =
            new String[] {
              Integer.toString(e.getHospitalEmployee().getHospitalID()),
              e.getLocation().getNodeID(),
              e.getEquipment(),
              e.getNotes(),
              Integer.toString(e.getQuantity()),
              e.getStatus(),
              Integer.toString(p.getPatientID()),
              p.getFirstName(),
              p.getLastName()
            };
      }
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
