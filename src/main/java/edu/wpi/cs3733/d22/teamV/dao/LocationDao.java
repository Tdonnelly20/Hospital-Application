package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.interfaces.DaoInterface;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import edu.wpi.cs3733.d22.teamV.serviceRequest.ServiceRequest;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class LocationDao extends DaoInterface {
  private static ArrayList<Location> allLocations;

  public LocationDao() {
    allLocations = new ArrayList<>();
    try {
      loadFromCSV();
      createSQLTable();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static ArrayList<Location> getAllLocations() {
    return allLocations;
  }

  public Location getLocation(String nodeID) {
    for (Location l : allLocations) {
      if (l.getNodeID().equals(nodeID)) {
        return l;
      }
    }
    System.out.print("Unable to find node:" + nodeID);
    return null;
  }

  public void addLocation(Location location) {
    allLocations.add(location);
    try {
      saveToCSV();
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      addToSQLTable(location);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void deleteLocation(String nodeID) throws SQLException, IOException {
    allLocations.removeIf(location -> location.getNodeID().equals(nodeID));
    saveToCSV();
    removeFromSQLTable(nodeID);
  }

  public void setAllLocations(ArrayList<Location> locations) {
    allLocations = locations;
  }

  public void saveToBackupCSV() throws IOException {
    FileWriter fw = new FileWriter(Vdb.currentPath + "/LocationsBackup.csv");
    BufferedWriter bw = new BufferedWriter(fw);
    // nodeID	xcoord	ycoord	floor	building	nodeType	longName	shortName
    bw.append("nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName");
    for (Location l : getAllLocations()) {
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
    System.out.println("Overwritten backup locations!");
  }

  public void loadBackupLocations() throws IOException {
    String line = "";
    FileReader fr = new FileReader(Vdb.currentPath + "/LocationsBackup.csv");
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
    setAllLocations(locations);
    System.out.println("Location database reloaded and saved from backup!");
    saveToCSV();
  }

  public void loadFromCSV() throws IOException {
    String line = "";
    String file = Vdb.currentPath + "/TowerLocations.csv";
    System.out.println(file);
    FileReader fr = new FileReader(file);
    BufferedReader br = new BufferedReader(fr);
    String splitToken = ","; // what we split the csv file with
    ArrayList<Location> locations = new ArrayList<>();
    // equipment = new ArrayList<>();
    // C:\Users\Trevor\Documents\GitHub\TeamVeganVampires\src\main\resources\edu\wpi\cs3733\d22\teamV
    // C:\Users\Trevor\Documents\GitHub\TeamVeganVampires\src\main\resources\edu\wpi\cs3733\d22\teamV
    //
    String headerLine = br.readLine();
    while ((line = br.readLine()) != null) // should create a database based on csv file
    {
      String[] data = line.split(splitToken);
      Location newLoc =
          new Location(
              data[0],
              Double.parseDouble(data[1]),
              Double.parseDouble(data[2]),
              data[3],
              data[4],
              data[5],
              data[6],
              data[7]);
      locations.add(newLoc);
    }
    setAllLocations(locations);
  }

  public void saveToCSV() throws IOException {
    FileWriter fw = new FileWriter(Vdb.currentPath + "/TowerLocations.csv");
    BufferedWriter bw = new BufferedWriter(fw);
    // nodeID	xcoord	ycoord	floor	building	nodeType	longName	shortName
    bw.append("nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName");
    for (Location l : getAllLocations()) {
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

  public void createSQLTable() throws SQLException {
    Connection connection = Vdb.Connect();
    assert connection != null;
    Statement statement = connection.createStatement();
    DatabaseMetaData meta = connection.getMetaData();
    ResultSet set = meta.getTables(null, null, "LOCATIONS", new String[] {"TABLE"});
    if (!set.next()) {
      statement.execute(
          "CREATE TABLE Locations(nodeID char(20), xCoord int, yCoord int, floor char(10), building char(20), nodeType char(10), longName char(60), shortName char(30))");

    } else {
      statement.execute("DROP TABLE LOCATIONS");
      createSQLTable();
      return;
    }

    for (Location location : allLocations) {
      addToSQLTable(location);
    }
  }

  @Override
  public void addToSQLTable(ServiceRequest request) throws SQLException {}

  @Override
  public void removeFromSQLTable(ServiceRequest request) throws IOException, SQLException {}

  @Override
  public void addServiceRequest(ServiceRequest request) throws IOException, SQLException {}

  @Override
  public void removeServiceRequest(ServiceRequest request) throws IOException, SQLException {}

  @Override
  public ArrayList<? extends ServiceRequest> getAllServiceRequests() {
    return null;
  }

  @Override
  public void setAllServiceRequests(ArrayList<? extends ServiceRequest> serviceRequests)
      throws SQLException {}

  public void addToSQLTable(Location location) throws SQLException {
    try {
      Connection connection = Vdb.Connect();
      Statement exampleStatement = connection.createStatement();
      exampleStatement.execute(
          "INSERT INTO LOCATIONS(nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName) VALUES ('"
              + location.getNodeID()
              + "',"
              + location.getXCoord()
              + ","
              + location.getYCoord()
              + ",'"
              + location.getFloor()
              + "','"
              + location.getBuilding()
              + "','"
              + location.getNodeType()
              + "','"
              + location.getLongName()
              + "','"
              + location.getShortName()
              + "')");
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void removeFromSQLTable(String nodeID) throws SQLException {
    String query = "";
    Connection connection = Vdb.Connect();
    assert connection != null;
    Statement statement = connection.createStatement();
    query = "DELETE FROM LOCATIONS WHERE nodeID = '" + nodeID + "'";
    statement.executeUpdate(query);
  }
}
