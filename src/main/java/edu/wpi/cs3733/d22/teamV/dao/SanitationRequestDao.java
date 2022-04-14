package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.interfaces.DaoInterface;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.VApp;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.servicerequests.SanitationRequest;
import edu.wpi.cs3733.d22.teamV.servicerequests.ServiceRequest;
import java.io.*;
import java.io.IOException;
import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.Predicate;

public class SanitationRequestDao extends DaoInterface {
  private static ArrayList<SanitationRequest>
      allSanitationRequests; // A local list of all sanitation requests, updated via Vdb

  /** Initialize the arraylist */
  public SanitationRequestDao() throws SQLException, IOException {
    allSanitationRequests = new ArrayList<SanitationRequest>();
    createSQLTable();
    loadFromCSV();
  }

  @Override
  public void loadFromCSV() throws IOException, SQLException {
    String line = "";
    FileReader fr = new FileReader(VApp.currentPath + "/SanitationRequest.CSV");
    BufferedReader br = new BufferedReader(fr);
    String headerLine = br.readLine();
    String splitToken = ",";
    ArrayList<SanitationRequest> requests = new ArrayList<>();
    while ((line = br.readLine()) != null) // should create a database based on csv file
    {
      String[] data;
      data = line.split(splitToken);
      SanitationRequest request =
          new SanitationRequest(
              Integer.parseInt(data[0]), Integer.parseInt(data[1]), data[2], data[3], data[4]);
      requests.add(request);
    }
    allSanitationRequests = requests;
  }

  @Override
  public void saveToCSV() throws IOException {
    FileWriter fw = new FileWriter(VApp.currentPath + "/SanitationRequest.csv");
    BufferedWriter bw = new BufferedWriter(fw);
    bw.append("PatientID,EmpID,Location,Hazard,Details,serviceID");

    for (ServiceRequest request : getAllServiceRequests()) {
      SanitationRequest sanitationRequest = (SanitationRequest) request;
      String[] outputData = {
        Integer.toString(sanitationRequest.getPatientID()),
        Integer.toString(sanitationRequest.getHospitalID()),
        sanitationRequest.getRoomLocation(),
        sanitationRequest.getHazardName(),
        sanitationRequest.getRequestDetails(),
        Integer.toString(sanitationRequest.getServiceID())
      };
      bw.append("/n");
      for (String s : outputData) {
        bw.append(s);
        bw.append(',');
      }
    }
    bw.close();
    fw.close();
  }

  @Override
  public void createSQLTable() throws SQLException {
    String query = "";
    Connection connection = Vdb.Connect();
    Statement statement = connection.createStatement();
    DatabaseMetaData meta = connection.getMetaData();
    ResultSet set = meta.getTables(null, null, "SANITATIONREQUESTS", new String[] {"TABLE"});
    if (!set.next()) {
      statement.execute(
          "CREATE TABLE SANITATIONREQUESTS(pID int, empID int, roomLocation char(40), hazard char(30), details char(150),serviceID int)");
    } else {
      statement.execute("DROP TABLE SANITATIONREQUESTS");
      createSQLTable();
      return;
    }
  }

  @Override
  public void addToSQLTable(ServiceRequest Request) throws SQLException {
    SanitationRequest newSanitationRequest = (SanitationRequest) Request;
    Connection connection = Vdb.Connect();
    String query = "INSERT INTO SANITATIONREQUESTS VALUES(?,?,?,?,?,?)";
    PreparedStatement statement = connection.prepareStatement(query);
    statement.setInt(1, newSanitationRequest.getPatientID());
    statement.setInt(2, newSanitationRequest.getHospitalID());
    statement.setString(3, newSanitationRequest.getRoomLocation());
    statement.setString(4, newSanitationRequest.getHazardName());
    statement.setString(5, newSanitationRequest.getRequestDetails());
    statement.setInt(6, newSanitationRequest.getServiceID());
  }

  @Override
  public void updateServiceRequest(ServiceRequest request, int serviceID) throws SQLException {
    SanitationRequest newRequest = (SanitationRequest) request;
    request.setServiceID(serviceID);
    int index = -1;
    for (int i = 0; i < allSanitationRequests.size(); i++) {
      if (allSanitationRequests.get(i).getServiceID() == request.getServiceID()) {
        index = i;
        break;
      }
    }
    allSanitationRequests.set(index, newRequest);
    updateSQLTable(request);
  }
  // fname char(15),lname char(15), pID int, empID int, roomLocation char(40), hazard char(30),
  // details char(150),serviceID int)");
  public void updateSQLTable(ServiceRequest request) throws SQLException {
    // also needs to add to csv
    SanitationRequest newRequest = (SanitationRequest) request;
    Connection connection = Vdb.Connect();
    String query =
        "UPDATE SANITATIONREQUESTS"
            + "SET pID=?,empID=?,roomLocation=?,hazard=?,details=?"
            + "WHERE serviceID=?";
    PreparedStatement statement = connection.prepareStatement(query);
    statement.setInt(1, newRequest.getPatientID());
    statement.setInt(2, newRequest.getHospitalID());
    statement.setString(3, newRequest.getRoomLocation());
    statement.setString(4, newRequest.getHazardName());
    statement.setString(5, newRequest.getRequestDetails());
    statement.setInt(6, newRequest.getServiceID());
  }

  @Override
  public void removeFromSQLTable(ServiceRequest request) throws IOException, SQLException {
    int serviceID = request.getServiceID();
    Connection connection = Vdb.Connect();
    String query = "DELETE FROM SANITATIONREQUESTS" + "WHERE serviceID=?";
    PreparedStatement statement = connection.prepareStatement(query);
    statement.setInt(1, serviceID);
  }

  @Override
  public void addServiceRequest(ServiceRequest request) throws IOException, SQLException {
    SanitationRequest newRequest = (SanitationRequest) request;
    request.setServiceID(RequestSystem.getServiceID());
    newRequest.setServiceID(RequestSystem.getServiceID()); //
    allSanitationRequests.add(newRequest);
    addToSQLTable(request);
    saveToCSV();
  }

  @Override
  public void removeServiceRequest(ServiceRequest request) throws IOException, SQLException {
    int serviceID = request.getServiceID();
    Predicate<SanitationRequest> condition =
        SanitationRequest -> SanitationRequest.getServiceID() == serviceID;
    allSanitationRequests.removeIf(condition);
    removeFromSQLTable(request);
  }

  @Override
  public ArrayList<? extends ServiceRequest> getAllServiceRequests() {
    return allSanitationRequests;
  }

  @Override
  public void setAllServiceRequests(ArrayList<? extends ServiceRequest> serviceRequests)
      throws SQLException {
    allSanitationRequests.clear();
    for (ServiceRequest r : serviceRequests) {
      SanitationRequest request = (SanitationRequest) r;
      allSanitationRequests.add(request);
    }
    createSQLTable();
  }
}
