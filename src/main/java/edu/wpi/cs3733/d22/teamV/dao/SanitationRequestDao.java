package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.ServiceRequests.SanitationRequest;
import edu.wpi.cs3733.d22.teamV.ServiceRequests.ServiceRequest;
import edu.wpi.cs3733.d22.teamV.interfaces.DaoInterface;
import edu.wpi.cs3733.d22.teamV.main.Vdb;

import java.io.*;
import java.sql.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.Predicate;


public class SanitationRequestDao extends DaoInterface {
  private static ArrayList<SanitationRequest>
      allSanitationRequests; // A local list of all sanitation requests, updated via Vdb

  /** Initialize the arraylist */
  public SanitationRequestDao() {
    allSanitationRequests = new ArrayList<SanitationRequest>();
    // TODO: Add info from the database to the local arraylist
  }
  // String patientFirstName,
  //      String patientLastName,
  //      int patientID,
  //      int hospitalID,
  //      String roomLocation,
  //      String hazardName,
  //      String requestDetails
  @Override
  public void loadFromCSV() throws IOException, SQLException {
    String line = "";
    FileReader fr = new FileReader(Vdb.currentPath + "\\SanitationRequest.CSV");
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
                      data[0],
                      data[1],
                      Integer.parseInt(data[2]),
                      Integer.parseInt(data[3]),
                      data[4],
                      data[5],
                      data[6],
                      Integer.parseInt(data[7]));
      requests.add(request);
    }
    allSanitationRequests= requests;
  }

  @Override
  public void saveToCSV() throws IOException {
    FileWriter fw = new FileWriter(Vdb.currentPath + "\\SanitationRequest.csv");
    BufferedWriter bw = new BufferedWriter(fw);
    bw.append("FirstName,LastName,PatientID,EmpID,Location,Hazard,Details,serviceID");

    for (ServiceRequest request : getAllServiceRequests()) {
      SanitationRequest sanitationRequest = (SanitationRequest) request;
      String[] outputData = {
        sanitationRequest.getPatientFirstName(),
        sanitationRequest.getPatientLastName(),
        Integer.toString(sanitationRequest.getPatientID()),
        Integer.toString(sanitationRequest.getHospitalID()),
        sanitationRequest.getRoomLocation(),
        sanitationRequest.getHazardName(),
        sanitationRequest.getRequestDetails(),
        Integer.toString(sanitationRequest.getServiceID())
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
  //  //String patientFirstName,
  //  //      String patientLastName,
  //  //      int patientID,
  //  //      int hospitalID,
  //  //      String roomLocation,
  //  //      String hazardName,
  //  //      String requestDetails
  // serviceID
  @Override
  public void createSQLTable() throws SQLException {
    String query = "";
    Connection connection = Vdb.Connect();
    Statement statement = connection.createStatement();
    DatabaseMetaData meta = connection.getMetaData();
    ResultSet set = meta.getTables(null, null, "SANITATIONREQUESTS", new String[] {"TABLE"});
    if (!set.next()) {
      statement.execute(
          "CREATE TABLE SANITATIONREQUESTS(fname char(15),lname char(15), pID int, empID int, roomLocation char(40), hazard char(30), details char(150),serviceID int)");
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
    String query = "INSERT INTO SANITATIONREQUESTS VALUES(?,?,?,?,?,?,?,?)";
    PreparedStatement statement = connection.prepareStatement(query);
    statement.setString(1, newSanitationRequest.getPatientFirstName());
    statement.setString(2, newSanitationRequest.getPatientLastName());
    statement.setInt(3, newSanitationRequest.getPatientID());
    statement.setInt(4, newSanitationRequest.getHospitalID());
    statement.setString(5, newSanitationRequest.getRoomLocation());
    statement.setString(6,newSanitationRequest.getHazardName());
    statement.setString(7,newSanitationRequest.getRequestDetails());
    statement.setInt(8, newSanitationRequest.getServiceID());
  }

  @Override
  public void updateRequest(ServiceRequest request) throws SQLException {
    SanitationRequest newRequest=(SanitationRequest) request;
    int index=-1;
    for (int i=0;i<allSanitationRequests.size();i++){
      if (allSanitationRequests.get(i).getServiceID()==request.getServiceID()){
        index=i;
        break;
      }
    }
    allSanitationRequests.set(index,newRequest);
    updateSQLTable(request);
  }
//fname char(15),lname char(15), pID int, empID int, roomLocation char(40), hazard char(30), details char(150),serviceID int)");
  @Override
  public void updateSQLTable(ServiceRequest request) throws SQLException{
    // also needs to add to csv
    SanitationRequest newRequest = (SanitationRequest) request;
    Connection connection = Vdb.Connect();
    String query =
            "UPDATE SANITATIONREQUESTS"
                    + "SET fname=?, lname=?,pID=?,empID=?,roomLocation=?,hazard=?,details=?"
                    + "WHERE serviceID=?";
    PreparedStatement statement = connection.prepareStatement(query);
    statement.setString(1, newRequest.getPatientFirstName());
    statement.setString(2, newRequest.getPatientLastName());
    statement.setInt(3, newRequest.getPatientID());
    statement.setInt(4, newRequest.getHospitalID());
    statement.setString(5, newRequest.getRoomLocation());
    statement.setString(6,newRequest.getHazardName());
    statement.setString(7,newRequest.getRequestDetails());
    statement.setInt(8, newRequest.getServiceID());
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
    request.setServiceID(Vdb.getServiceID());
    newRequest.setServiceID(Vdb.getServiceID());//
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
