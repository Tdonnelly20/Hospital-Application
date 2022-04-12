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
  }

  // DaoInterface Methods
  public void loadFromCSV() throws IOException, SQLException {

    FileReader fr = new FileReader(VApp.currentPath + "\\LabRequests.csv");
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
              Integer.parseInt(data[0]), Integer.parseInt(data[1]), data[2], data[3], data[4]);
      newDelivery.setServiceID(Integer.parseInt(data[5]));
      allLabRequests.add(newDelivery);
    }
    setAllServiceRequests(allLabRequests);
  }

  public void saveToCSV() throws IOException {
    FileWriter fw = new FileWriter(VApp.currentPath + "\\LabRequests.csv");
    BufferedWriter bw = new BufferedWriter(fw);
    bw.append("userID,patientID,firstName,lastName,lab,status,serviceID");

    for (ServiceRequest request : getAllServiceRequests()) {

      LabRequest labRequest = (LabRequest) request;

      String[] outputData = {
        String.valueOf(labRequest.getUserID()),
        String.valueOf(labRequest.getPatientID()),
        labRequest.getFirstName(),
        labRequest.getLastName(),
        labRequest.getLab(),
        labRequest.getStatus(),
        String.valueOf(labRequest.getServiceID())
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
    ResultSet set = meta.getTables(null, null, "LABS", new String[] {"TABLE"});
    String query = "";

    if (!set.next()) {
      query =
          "CREATE TABLE LABS(userID int, patientID int, firstName varchar(30), lastName varchar(30), lab varchar(50), status varchar(50), serviceID int)";
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
  }

  public void addToSQLTable(ServiceRequest request) throws SQLException {
    LabRequest labRequest = (LabRequest) request;

    String query = "";
    Connection connection = Vdb.Connect();
    assert connection != null;
    Statement statement = connection.createStatement();

    query =
        "INSERT INTO LABS("
            + "userID,patientID,firstName,lastName,lab,status,serviceID) VALUES "
            + "("
            + labRequest.getUserID()
            + ", "
            + labRequest.getPatientID()
            + ", '"
            + labRequest.getFirstName()
            + "',' "
            + labRequest.getLastName()
            + "', '"
            + labRequest.getLab()
            + "', '"
            + labRequest.getStatus()
            + "',"
            + labRequest.getServiceID()
            + ")";

    statement.execute(query);
  }

  @Override
  public void updateServiceRequest(ServiceRequest request, int serviceID)
      throws SQLException, IOException {
    LabRequest labRequest = (LabRequest) request;
    labRequest.setServiceID(serviceID);
    removeServiceRequest(labRequest);
    allLabRequests.add(labRequest);
    addToSQLTable(labRequest);
    saveToCSV();
  }

  public void removeFromSQLTable(ServiceRequest request) throws IOException, SQLException {
    String query = "";
    Connection connection = Vdb.Connect();
    assert connection != null;
    Statement statement = connection.createStatement();

    query = "DELETE FROM LABS WHERE serviceID = " + request.getServiceID();
    statement.execute(query);
  }

  public void addServiceRequest(ServiceRequest request) throws IOException, SQLException {
    int serviceID = RequestSystem.getServiceID();
    LabRequest labRequest = (LabRequest) request;
    labRequest.setServiceID(serviceID);
    allLabRequests.add(labRequest); // Store a local copy

    addToSQLTable(request);
    saveToCSV();
  }

  public void removeServiceRequest(ServiceRequest request) throws IOException, SQLException {
    LabRequest labRequest = (LabRequest) request;
    allLabRequests.removeIf(value -> value.getServiceID() == labRequest.getServiceID());
    removeFromSQLTable(request);
    saveToCSV();
  }

  public ArrayList<? extends ServiceRequest> getAllServiceRequests() {
    return allLabRequests;
  }

  public void setAllServiceRequests(ArrayList<? extends ServiceRequest> serviceRequests)
      throws SQLException {
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
      connection = DriverManager.getConnection("jdbc:derby:VDB;create=true", "admin", "admin");
      Statement exampleStatement = connection.createStatement();
      for (LabRequest l : allLabRequests)
        exampleStatement.execute("DELETE FROM LOCATIONS WHERE userID = l.getUserID()");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
