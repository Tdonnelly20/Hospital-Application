package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.interfaces.DaoInterface;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.VApp;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.servicerequests.LaundryRequest;
import edu.wpi.cs3733.d22.teamV.servicerequests.ServiceRequest;
import java.io.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class LaundryRequestDao extends DaoInterface {

  private static ArrayList<LaundryRequest> allLaundryRequests;

  /** Initialize the array list */
  public LaundryRequestDao() throws SQLException, IOException {
    allLaundryRequests = new ArrayList<LaundryRequest>();
    createSQLTable();
    loadFromCSV();
  }

  public LaundryRequestDao(ArrayList<LaundryRequest> allLaundryRequests) {
    this.allLaundryRequests = allLaundryRequests;
  }
  // New Stuff
  public void loadFromCSV() throws IOException, SQLException {
    FileReader fr = new FileReader(VApp.currentPath + "/LaundryRequest.csv");
    BufferedReader br = new BufferedReader(fr);
    String splitToken = ","; // what we split the csv file with
    ArrayList<LaundryRequest> laundryRequests = new ArrayList<>();

    String headerLine = br.readLine();
    String line;

    while ((line = br.readLine()) != null) // should create a database based on csv file
    {
      String[] data = line.split(splitToken);
      LaundryRequest newDelivery =
          new LaundryRequest(
              Integer.parseInt(data[0]), Integer.parseInt(data[1]), data[2], data[3], data[4]);
      newDelivery.setServiceID(Integer.parseInt(data[5]));
      laundryRequests.add(newDelivery);
    }

    setAllServiceRequests(laundryRequests);
  }

  public void saveToCSV() throws IOException {
    FileWriter fw = new FileWriter(VApp.currentPath + "/LaundryRequest.csv");
    BufferedWriter bw = new BufferedWriter(fw);
    bw.append("userID,patientID,roomNumber,details,status,serviceID");

    for (ServiceRequest request : getAllServiceRequests()) {

      LaundryRequest laundryRequest = (LaundryRequest) request;

      String[] outputData = {
        String.valueOf(laundryRequest.getHospitalEmployee().getEmployeeID()),
        String.valueOf(laundryRequest.getPatient().getPatientID()),
        String.valueOf(laundryRequest.getLocation().getNodeID()),
        String.valueOf(laundryRequest.getDetails()),
        String.valueOf(laundryRequest.getStatus()),
        String.valueOf(laundryRequest.getServiceID())
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
    ResultSet set = meta.getTables(null, null, "LAUNDRY", new String[] {"TABLE"});
    String query = "";

    if (!set.next()) {
      query =
          "CREATE TABLE LAUNDRY(userID int, patientID int, roomNumber char(50), details char(100), status char(100), serviceID int)";
      statement.execute(query);

    } else {
      query = "DROP TABLE LAUNDRY";
      statement.execute(query);
      createSQLTable(); // rerun the method to generate new tables
      return;
    }

    for (LaundryRequest laundryRequest : allLaundryRequests) {
      addToSQLTable(laundryRequest);
    }
  }

  public void addToSQLTable(ServiceRequest request) throws SQLException {
    LaundryRequest laundryRequest = (LaundryRequest) request;

    String query = "";
    Connection connection = Vdb.Connect();
    assert connection != null;
    Statement statement = connection.createStatement();

    query =
        "INSERT INTO LAUNDRY("
            + "userID,patientID,roomNumber,details,status,serviceID) VALUES "
            + "("
            + laundryRequest.getHospitalEmployee().getEmployeeID()
            + ","
            + laundryRequest.getPatient().getPatientID()
            + ", '"
            + laundryRequest.getLocation().getNodeID()
            + "','"
            + laundryRequest.getDetails()
            + "','"
            + laundryRequest.getStatus()
            + "',"
            + laundryRequest.getServiceID()
            + ")";

    statement.execute(query);
  }

  @Override
  public void updateServiceRequest(ServiceRequest request, int serviceID)
      throws SQLException, IOException {
    LaundryRequest laundryRequest = (LaundryRequest) request;
    laundryRequest.setServiceID(serviceID);
    removeServiceRequest(laundryRequest);
    allLaundryRequests.add(laundryRequest);
    addToSQLTable(laundryRequest);
    saveToCSV();
  }

  public void removeFromSQLTable(ServiceRequest request) throws IOException, SQLException {
    String query = "";
    Connection connection = Vdb.Connect();
    assert connection != null;
    Statement statement = connection.createStatement();

    query = "DELETE FROM LAUNDRY WHERE serviceID = " + request.getServiceID();
    statement.execute(query);
  }

  public void addServiceRequest(ServiceRequest request) throws IOException, SQLException {
    int serviceID = RequestSystem.serviceIDCounter;
    LaundryRequest laundryRequest = (LaundryRequest) request;
    laundryRequest.setServiceID(serviceID);
    allLaundryRequests.add(laundryRequest); // Store a local copy

    addToSQLTable(request);
    saveToCSV();
  }

  public void removeServiceRequest(ServiceRequest request) throws IOException, SQLException {
    LaundryRequest laundryRequest = (LaundryRequest) request;
    allLaundryRequests.removeIf(value -> value.getServiceID() == laundryRequest.getServiceID());
    removeFromSQLTable(request);
    saveToCSV();
  }

  public ArrayList<? extends ServiceRequest> getAllServiceRequests() {
    return allLaundryRequests;
  }

  public void setAllServiceRequests(ArrayList<? extends ServiceRequest> serviceRequests)
      throws SQLException {
    // Set all medicine deliveries
    allLaundryRequests = new ArrayList<>();

    // Convert to subtype
    for (ServiceRequest request : serviceRequests) {
      // Cast to subtype
      LaundryRequest delivery = (LaundryRequest) request;
      allLaundryRequests.add(delivery);
    }
  }
}
