package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.ServiceRequests.LaundryRequest;
import edu.wpi.cs3733.d22.teamV.ServiceRequests.ServiceRequest;
import edu.wpi.cs3733.d22.teamV.interfaces.DaoInterface;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class LaundryRequestDao extends DaoInterface {

  private static ArrayList<LaundryRequest> allLaundryRequests;

  /** Initialize the array list */
  public LaundryRequestDao() {
    allLaundryRequests = new ArrayList<LaundryRequest>();
  }

  public LaundryRequestDao(ArrayList<LaundryRequest> allLaundryRequests) {
    this.allLaundryRequests = allLaundryRequests;
  }
  // New Stuff
  public void loadFromCSV() throws IOException, SQLException {
    FileReader fr = new FileReader(Vdb.currentPath + "\\LaundryRequest.csv");
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
              Integer.parseInt(data[0]),
              Integer.parseInt(data[1]),
              data[2],
              data[3],
              Integer.parseInt(data[4]),
              data[5]);
      newDelivery.setServiceID(Integer.parseInt(data[5]));
      laundryRequests.add(newDelivery);
    }

    setAllServiceRequests(laundryRequests);
  }

  public void saveToCSV() throws IOException {
    FileWriter fw = new FileWriter(Vdb.currentPath + "\\LaundryRequest.csv");
    BufferedWriter bw = new BufferedWriter(fw);
    bw.append("userID,patientID,firstName,lastName,roomNumber,details,serviceID");

    for (ServiceRequest request : getAllServiceRequests()) {

      LaundryRequest laundryRequest = (LaundryRequest) request;

      String[] outputData = {
        String.valueOf(laundryRequest.getUserID()),
        String.valueOf(laundryRequest.getPatientID()),
        laundryRequest.getFirstName(),
        laundryRequest.getLastName(),
        String.valueOf(laundryRequest.getRoomNumber()),
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
          "CREATE TABLE LAUNDRY(userID int, patientID int, firstName char(50), lastName char(50), roomNumber int, details char(100), serviceID int)";
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
            + "userID,patientID,firstName,lastName,roomNumber,details,serviceID) VALUES "
            + "("
            + laundryRequest.getUserID()
            + ","
            + laundryRequest.getPatientID()
            + ", '"
            + laundryRequest.getFirstName()
            + "','"
            + laundryRequest.getLastName()
            + "',"
            + laundryRequest.getRoomNumber()
            + ",'"
            + laundryRequest.getDetails()
            + "',"
            + laundryRequest.getServiceID()
            + ")";

    statement.execute(query);
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
    int serviceID = Vdb.getServiceID();
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
