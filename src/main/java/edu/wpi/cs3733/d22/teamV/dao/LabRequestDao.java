package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.interfaces.DaoInterface;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.VApp;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.servicerequests.LabRequest;
import edu.wpi.cs3733.d22.teamV.servicerequests.ServiceRequest;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class LabRequestDao extends DaoInterface {
  private static ArrayList<LabRequest> allLabRequests;

  /** Initialize the array list */
  public LabRequestDao() {
    allLabRequests = new ArrayList<>();
    createSQLTable();
    loadFromCSV();
  }

  // DaoInterface Methods
  public void loadFromCSV() {
    try {

      FileReader fr = new FileReader(VApp.currentPath + "/LabRequests.csv");
      BufferedReader br = new BufferedReader(fr);
      String splitToken = ","; // what we split the csv file with
      ArrayList<LabRequest> labRequests = new ArrayList<>();

      String headerLine = br.readLine();
      String line;

      while ((line = br.readLine()) != null) // should create a database based on csv file
      {
        String[] data = line.split(splitToken);
        LabRequest newDelivery =
            new LabRequest(
                Integer.parseInt(data[0]),
                Integer.parseInt(data[1]),
                data[2],
                data[3],
                data[4],
                Integer.parseInt(data[5]),
                data[6]);
        newDelivery.setServiceID(Integer.parseInt(data[5]));
        allLabRequests.add(newDelivery);
      }
      setAllServiceRequests(allLabRequests);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void saveToCSV() {
    try {

      FileWriter fw = new FileWriter(VApp.currentPath + "/LabRequests.csv");
      BufferedWriter bw = new BufferedWriter(fw);
      bw.append("userID,patientID,nodeID,lab,status,serviceID");

      for (ServiceRequest request : getAllServiceRequests()) {

        LabRequest labRequest = (LabRequest) request;

        String[] outputData = {
          String.valueOf(labRequest.getUserID()),
          String.valueOf(labRequest.getPatientID()),
          labRequest.getLocation().getNodeID(),
          labRequest.getLab(),
          labRequest.getStatus(),
          String.valueOf(labRequest.getServiceID()),
          labRequest.getTimeMade().toString()
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
      ResultSet set = meta.getTables(null, null, "LABS", new String[] {"TABLE"});
      String query = "";

      if (!set.next()) {
        query =
            "CREATE TABLE LABS(userID int, patientID int, nodeID char(50), lab varchar(50), status varchar(50), serviceID int,date_time timestamp)";
        statement.execute(query);

      } else {
        query = "DROP TABLE LABS";
        statement.execute(query);
        createSQLTable(); // rerun the method to generate new tables
        return;
      }

      for (LabRequest labRequest : allLabRequests) {
        addToSQLTable(labRequest);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void addToSQLTable(ServiceRequest request) {
    try {

      Connection connection = Vdb.Connect();
      LabRequest labRequest = (LabRequest) request;
      String query = "INSERT INTO LABS VALUES(?,?,?,?,?,?,?)";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setInt(1, labRequest.getEmployeeID());
      statement.setInt(2, labRequest.getPatientID());
      statement.setString(3, labRequest.getLocation().getNodeID());
      statement.setString(4, labRequest.getLab());
      statement.setString(5, labRequest.getStatus());
      statement.setInt(6, labRequest.getServiceID());
      statement.setTimestamp(7, labRequest.getTimeMade());
      statement.executeUpdate(); // uninit params
      /*
           LabRequest labRequest = (LabRequest) request;

           String query = "";
           Connection connection = Vdb.Connect();
           assert connection != null;
           Statement statement = connection.createStatement();

           query =
               "INSERT INTO LABS("
                   + "userID,patientID,nodeID,lab,status,serviceID,date_time) VALUES "
                   + "("
                   + labRequest.getUserID()
                   + ", "
                   + labRequest.getPatientID()
                   + ", '"
                   + labRequest.getLocation().getNodeID()
                   + "', '"
                   + labRequest.getLab()
                   + "', '"
                   + labRequest.getStatus()
                   + "',"
                   + labRequest.getServiceID()
                   + "','"
                   + labRequest.getTimeMade()
                   + ")";

           statement.execute(query);

      */
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void updateServiceRequest(ServiceRequest request, int serviceID) {
    LabRequest labRequest = (LabRequest) request;
    labRequest.setServiceID(serviceID);
    removeServiceRequest(labRequest);
    allLabRequests.add(labRequest);
    addToSQLTable(labRequest);
    saveToCSV();
  }

  public void removeFromSQLTable(ServiceRequest request) {
    try {

      String query = "";
      Connection connection = Vdb.Connect();
      assert connection != null;
      Statement statement = connection.createStatement();

      query = "DELETE FROM LABS WHERE serviceID = " + request.getServiceID();
      statement.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void addServiceRequest(ServiceRequest request) {
    int serviceID = RequestSystem.getServiceID();
    LabRequest labRequest = (LabRequest) request;
    labRequest.setServiceID(serviceID);
    allLabRequests.add(labRequest); // Store a local copy

    addToSQLTable(request);
    saveToCSV();
  }

  public void removeServiceRequest(ServiceRequest request) {
    LabRequest labRequest = (LabRequest) request;
    allLabRequests.removeIf(value -> value.getServiceID() == labRequest.getServiceID());
    request.detachAll();
    removeFromSQLTable(request);
    saveToCSV();
  }

  public ArrayList<? extends ServiceRequest> getAllServiceRequests() {
    return allLabRequests;
  }

  public void setAllServiceRequests(ArrayList<? extends ServiceRequest> serviceRequests) {
    // Set all medicine deliveries
    allLabRequests = new ArrayList<>();

    // Convert to subtype
    for (ServiceRequest request : serviceRequests) {
      // Cast to subtype
      LabRequest delivery = (LabRequest) request;
      allLabRequests.add(delivery);
      try {
        createSQLTable();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public void removeLabRequest(int userID) {
    // System.out.println("Removing from arraylist...");
    allLabRequests.removeIf(l -> l.getPatient().getPatientID() == userID);

    try {
      // System.out.println("Removing from database...");
      Connection connection;
      connection = Vdb.Connect();
      Statement exampleStatement = connection.createStatement();
      for (LabRequest l : allLabRequests)
        exampleStatement.execute("DELETE FROM LOCATIONS WHERE userID = l.getUserID()");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
