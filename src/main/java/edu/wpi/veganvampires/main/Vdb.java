package edu.wpi.veganvampires.main;

import edu.wpi.veganvampires.dao.*;
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
  public static final LabRequestDao labRequestDao = new LabRequestDao();
  public static final MedicineDeliveryDao medicineDeliveryDao = new MedicineDeliveryDao();
  public static final MealRequestDao mealRequestDao = new MealRequestDao();
  public static MapManager mapManager;

  public enum Database {
    Location,
    EquipmentDelivery,
    MedicineDelivery,
    ReligiousRequest,
    MealDelivery,
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
      currentPath += "/src/main/resources/edu/wpi/veganvampires";
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
    createMedicineDeliveryDB();
    createMealDeliveryDB();

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
        System.out.println("SAVING LOC");
        saveToLocationDB();
        break;
      case EquipmentDelivery:
        saveToEquipmentDB();
        break;
      case MedicineDelivery:
        saveToMedicineDeliveryDB();
        break;
      case MealDelivery:
        saveToMealDeliveryDB();
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
  public static void createLabRequestDB() throws Exception {
    FileReader fr = new FileReader(currentPath + "/LabRequest.csv");
    BufferedReader br = new BufferedReader(fr);
    String splitToken = ","; // what we split the csv file with
    ArrayList<LabRequest> labs = new ArrayList<>();
    // equipment = new ArrayList<>();
    String headerLine = br.readLine();
    while ((line = br.readLine()) != null) // should create a database based on csv file
    {
      String[] data = line.split(splitToken);
      LabRequest newLab =
          new LabRequest(
              Integer.parseInt(data[0]),
              Integer.parseInt(data[1]),
              data[2],
              data[3],
              data[4],
              data[5]);
      labs.add(newLab);
    }
    labRequestDao.setAllLabRequests(labs);
    System.out.println("Location database made");

    mapManager = MapManager.getManager();
  }

  /**
   * Saves the location DB
   *
   * @throws IOException
   */
  public static void saveToLabDB() throws IOException {
    FileWriter fw = new FileWriter(currentPath + "/LabRequestBackup.csv");
    BufferedWriter bw = new BufferedWriter(fw);
    // nodeID	xcoord	ycoord	floor	building	nodeType	longName	shortName
    bw.append("nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName");
    for (LabRequest l : labRequestDao.getAllLabRequests()) {
      String[] outputData = {
        l.getLocation().getNodeID(),
        String.valueOf(l.getHospitalEmployee().getHospitalID()),
        String.valueOf(l.getPatient().getPatientID()),
        l.getPatient().getFirstName(),
        l.getPatient().getLastName(),
        l.getLab()
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
   * Create the medicine delivery database
   *
   * @throws Exception
   */
  public static void createMedicineDeliveryDB() throws Exception {
    FileReader fr = new FileReader(currentPath + "/MedicineDelivery.csv");
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
    FileWriter fw = new FileWriter(currentPath + "/MedicineDelivery.csv");
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
    FileReader fr = new FileReader(currentPath + "/TowerLocations.csv");
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
    FileWriter fw = new FileWriter(currentPath + "/LocationsBackup.csv");
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
    FileWriter fw = new FileWriter(currentPath + "/MedEquipReq.csv");
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
    FileReader fr = new FileReader(currentPath + "/MedEquipReq.CSV");
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

  /**
   * Create the meal delivery database
   *
   * @throws Exception
   */
  public static void createMealDeliveryDB() throws Exception {
    FileReader fr = new FileReader(currentPath + "/MealDelivery.csv");
    BufferedReader br = new BufferedReader(fr);
    String splitToken = ","; // what we split the csv file with
    ArrayList<MealRequest> mealRequests = new ArrayList<>();
    // equipment = new ArrayList<>();
    String headerLine = br.readLine();
    while ((line = br.readLine()) != null) // should create a database based on csv file
    {
      String[] data = line.split(splitToken);
      MealRequest newDelivery =
          new MealRequest(
              data[0],
              data[1],
              data[2],
              Integer.parseInt(data[3]),
              Integer.parseInt(data[4]),
              data[5],
              data[6],
              data[7]);
      mealRequests.add(newDelivery);
    }
    mealRequestDao.setAllMealDeliveries(mealRequests);
    System.out.println("Meal delivery database made");
  }

  private static void saveToMealDeliveryDB() throws IOException {
    FileWriter fw = new FileWriter(currentPath + "/MealDelivery.csv");
    BufferedWriter bw = new BufferedWriter(fw);
    bw.append(
        "patientFirstName,patientLastName,roomNumber,patientID,hospitalID,mealName,allergy,requestDetails");

    for (MealRequest mealRequest : mealRequestDao.getAllMealDeliveries()) {
      String[] outputData = {
        mealRequest.getPatientFirstName(),
        mealRequest.getPatientLastName(),
        mealRequest.getRoomNumber(),
        String.valueOf(mealRequest.getPatientID()),
        String.valueOf(mealRequest.getHospitalID()),
        mealRequest.getMealName(),
        mealRequest.getAllergy(),
        mealRequest.getRequestDetails()
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
}
