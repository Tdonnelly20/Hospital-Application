package edu.wpi.veganvampires.main;

import edu.wpi.veganvampires.objects.*;
import edu.wpi.veganvampires.objects.Location;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class Vdb {
  private static final String currentPath = returnPath();
  private static String line; // receives a line from br
  public static ArrayList<Location> locations;
  public static ArrayList<EquipmentDelivery> equipment;

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

  public static void CreateDB() throws Exception {
    FileReader fr = new FileReader(currentPath + "\\TowerLocations.csv");
    BufferedReader br = new BufferedReader(fr);
    String splitToken = ","; // what we split the csv file with
    locations = new ArrayList<>();
    equipment = new ArrayList<>();
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
    System.out.println("Location database made");
  }

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

  public static void SaveToFile() throws Exception { // updates all csv files
    String currentPath = returnPath();
    FileWriter fw = new FileWriter(currentPath + "\\LocationsBackup.csv");
    BufferedWriter bw = new BufferedWriter(fw);
    // nodeID	xcoord	ycoord	floor	building	nodeType	longName	shortName
    bw.append("nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName");
    for (Location l : locations) {
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
    fw = new FileWriter(currentPath + "\\MedEquipReq.csv");
    bw = new BufferedWriter(fw);
    bw.append("Name,Description,Location,Count");
    for (EquipmentDelivery e : equipment) {
      String[] outputData = {
        e.getLocation(), e.getEquipment(), e.getNotes(), String.valueOf(e.getQuantity())
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

  public static void createEquipmentDB() throws IOException {
    FileReader fr = new FileReader(currentPath + "\\MedEquipReq.CSV");
    BufferedReader br = new BufferedReader(fr);
    String headerLine = br.readLine();
    String splitToken = ",";
    while ((line = br.readLine()) != null) // should create a database based on csv file
    {
      String[] data;
      data = line.split(splitToken);
      EquipmentDelivery e =
          new EquipmentDelivery(data[0], data[1], data[2], Integer.parseInt(data[3]));
      equipment.add(e);
    }
    System.out.println("Equipment database made");
  }

  public static void addLocation(Location newLocation) {}

  public static void addEquipmentDelivery(EquipmentDelivery newEquipmentDelivery)
      throws IOException {}

  public static void addMedicineDelivery(MedicineDelivery newMedicineDelivery) {}

  public static void addSanitationRequest(SanitationRequest newSanitationRequest) {}

  public static void addReligiousRequest(ReligiousRequest newReligiousRequest) {}

  public static void addMealRequest(MealRequest mealRequest) {}

  public static void addLabRequest(LabRequest labRequest) {}
}
