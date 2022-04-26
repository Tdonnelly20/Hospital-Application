package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.interfaces.DaoInterface;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.VApp;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.servicerequests.LabRequest;
import edu.wpi.cs3733.d22.teamV.servicerequests.RobotRequest;
import edu.wpi.cs3733.d22.teamV.servicerequests.ServiceRequest;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class RobotDao extends DaoInterface {
  private static ArrayList<RobotRequest> allRobotRequests;

  /** Initialize the array list */
  public RobotDao() {
    allRobotRequests = new ArrayList<>();
    loadFromCSV();
    createSQLTable();
  }

  // DaoInterface Methods
  public void loadFromCSV() {
    try {
      FileReader fr = new FileReader(VApp.currentPath + "\\RobotRequest.csv");
      BufferedReader br = new BufferedReader(fr);
      String splitToken = ","; // what we split the csv file with
      ArrayList<LabRequest> labRequests = new ArrayList<>();

      String headerLine = br.readLine();
      String line;

      while ((line = br.readLine()) != null) // should create a database based on csv file
      {
        String[] data = line.split(splitToken);
        RobotRequest newRobot =
            new RobotRequest(
                Integer.parseInt(data[0]),
                Integer.parseInt(data[1]),
                data[2],
                data[3],
                data[4],
                Integer.parseInt(data[5]),
                data[6]);
        newRobot.setServiceID(Integer.parseInt(data[5]));
        allRobotRequests.add(newRobot);
      }
      setAllServiceRequests(allRobotRequests);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void saveToCSV() {
    try {
      FileWriter fw = new FileWriter(VApp.currentPath + "\\RobotRequest.csv");
      BufferedWriter bw = new BufferedWriter(fw);
      bw.append("employeeID,botID,nodeID,details,status,serviceID");

      for (ServiceRequest request : getAllServiceRequests()) {

        RobotRequest robotRequest = (RobotRequest) request;

        String[] outputData = {
          String.valueOf(robotRequest.getEmployeeID()),
          String.valueOf(robotRequest.getBotID()),
          robotRequest.getNodeID(),
          robotRequest.getDetails(),
          robotRequest.getStatus(),
          String.valueOf(robotRequest.getServiceID()),
          robotRequest.getTimeMade().toString()
        };
        bw.append("\n");
        for (String s : outputData) {
          bw.append(s);
          bw.append(',');
        }
      }

      bw.close();
      fw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void createSQLTable() {
    try {
      Connection connection = Vdb.Connect();
      assert connection != null;
      Statement statement = connection.createStatement();
      DatabaseMetaData meta = connection.getMetaData();
      ResultSet set = meta.getTables(null, null, "ROBOTS", new String[] {"TABLE"});
      String query = "";
      if (!set.next()) {
        query =
            "CREATE TABLE ROBOTS(employeeID int, botID int, nodeID varchar(30), details varchar(30), status varchar(50), serviceID int, date_time timestamp)";
        statement.execute(query);

      } else {
        query = "DROP TABLE ROBOTS";
        statement.execute(query);
        createSQLTable(); // rerun the method to generate new tables
        return;
      }

      for (RobotRequest robotRequest : allRobotRequests) {
        addToSQLTable(robotRequest);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void addToSQLTable(ServiceRequest request) {

    try {
      Connection connection = Vdb.Connect();
      RobotRequest robotRequest = (RobotRequest) request;
      String query = "INSERT INTO ROBOTS VALUES(?,?,?,?,?,?,?)";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setInt(1, robotRequest.getEmployeeID());
      statement.setInt(2, robotRequest.getBotID());
      statement.setString(3, robotRequest.getLocation().getNodeID());
      statement.setString(4, robotRequest.getDetails());
      statement.setString(5, robotRequest.getStatus());
      statement.setInt(6, robotRequest.getServiceID());
      statement.setTimestamp(7, robotRequest.getTimeMade());
      statement.executeUpdate(); // uninit params
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void updateServiceRequest(ServiceRequest request, int serviceID) {
    RobotRequest robotRequest = (RobotRequest) request;
    robotRequest.setServiceID(serviceID);
    removeServiceRequest(robotRequest);
    allRobotRequests.add(robotRequest);
    addToSQLTable(robotRequest);
    saveToCSV();
  }

  public void removeFromSQLTable(ServiceRequest request) {
    try {
      String query = "";
      Connection connection = Vdb.Connect();
      assert connection != null;
      Statement statement = null;
      statement = connection.createStatement();
      query = "DELETE FROM ROBOTS WHERE serviceID = " + request.getServiceID();
      statement.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void addServiceRequest(ServiceRequest request) {
    addToSQLTable(request);
    int serviceID = RequestSystem.getServiceID();
    RobotRequest robotRequest = (RobotRequest) request;
    robotRequest.setServiceID(serviceID);
    allRobotRequests.add(robotRequest); // Store a local copy
    saveToCSV();
  }

  public void removeServiceRequest(ServiceRequest request) {
    RobotRequest robotRequest = (RobotRequest) request;
    allRobotRequests.removeIf(value -> value.getServiceID() == robotRequest.getServiceID());
    request.detachAll();
    removeFromSQLTable(request);
    saveToCSV();
  }

  public ArrayList<? extends ServiceRequest> getAllServiceRequests() {
    return allRobotRequests;
  }

  public void setAllServiceRequests(ArrayList<? extends ServiceRequest> serviceRequests) {
    // Set all medicine deliveries
    allRobotRequests = new ArrayList<>();

    // Convert to subtype
    for (ServiceRequest request : serviceRequests) {
      // Cast to subtype
      RobotRequest robotRequest = (RobotRequest) request;
      allRobotRequests.add(robotRequest);
      try {
        createSQLTable();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
