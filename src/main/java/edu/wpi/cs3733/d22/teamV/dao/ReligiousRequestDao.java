package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.ServiceRequests.ReligiousRequest;
import edu.wpi.cs3733.d22.teamV.ServiceRequests.ServiceRequest;
import edu.wpi.cs3733.d22.teamV.interfaces.DaoInterface;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.function.Predicate;

public class ReligiousRequestDao implements DaoInterface {
  private static ArrayList<ReligiousRequest>
      allReligiousRequest; // A local list of all religious requests, updated via Vdb

  /** Initialize the arraylist */
  public ReligiousRequestDao() {
    allReligiousRequest = new ArrayList<ReligiousRequest>();
    // TODO: Add info from the database to the local arraylist
  }
  //
  //      String firstName,
  //      String lastName,
  //      int patientID,
  //      int userID,
  //      String religion,
  //      String specialRequests)
  @Override
  public void createSQLTable() throws SQLException {
    String query = "";
    Connection connection = Vdb.Connect();
    Statement statement = connection.createStatement();
    DatabaseMetaData meta = connection.getMetaData();
    ResultSet set = meta.getTables(null, null, "RELIGIOUSREQUESTS", new String[] {"TABLE"});
    if (!set.next()) {
      statement.execute(
          "CREATE TABLE RELIGIOUSREQUESTS(fname char(15),lname char(15), pID int, empID int, religion char(35), request char(200), serviceID int)");
    } else {
      statement.execute("DROP TABLE RELIGIOUSREQUESTS");
      createSQLTable();
      return;
    }
  }

  @Override
  public void saveToCSV() throws IOException {
    FileWriter fw = new FileWriter(Vdb.currentPath + "\\ReligiousRequest.csv");
    BufferedWriter bw = new BufferedWriter(fw);
    bw.append("FirstName,LastName,PatientID,EmpID,Religion,Details,serviceID");

    for (ServiceRequest request : getAllServiceRequests()) {

      ReligiousRequest religiousRequest = (ReligiousRequest) request;
      //      String firstName,
      //      String lastName,
      //      int patientID,
      //      int userID,
      //      String religion,
      //      String specialRequests,
      //      int serviceID) {
      String[] outputData = {
        religiousRequest.getPatientFirstName(),
        religiousRequest.getPatientLastName(),
        Integer.toString(religiousRequest.getPatientID()),
        Integer.toString(religiousRequest.getEmpID()),
        religiousRequest.getReligion(),
        religiousRequest.getSpecialRequests(),
        Integer.toString(religiousRequest.getServiceID())
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

  @Override
  public void loadFromCSV() throws IOException {
    String line = "";
    FileReader fr = new FileReader(Vdb.currentPath + "\\ReligiousRequest.CSV");
    BufferedReader br = new BufferedReader(fr);
    String headerLine = br.readLine();
    String splitToken = ",";
    ArrayList<ReligiousRequest> requests = new ArrayList<>();
    while ((line = br.readLine()) != null) // should create a database based on csv file
    {
      String[] data;
      data = line.split(splitToken);
      ReligiousRequest request =
          new ReligiousRequest(
              data[0],
              data[1],
              Integer.parseInt(data[2]),
              Integer.parseInt(data[3]),
              data[4],
              data[5],
              Integer.parseInt(data[6]));
      requests.add(request);
    }
    allReligiousRequest = requests;
  }

  @Override
  public void addToSQLTable(ServiceRequest Request) throws SQLException {
    ReligiousRequest newReligiousRequest = (ReligiousRequest) Request;
    Connection connection = Vdb.Connect();
    String query = "INSERT INTO RELIGIOUSREQUESTS VALUES(?,?,?,?,?,?,?)";
    PreparedStatement statement = connection.prepareStatement(query);
    statement.setString(1, newReligiousRequest.getPatientFirstName());
    statement.setString(2, newReligiousRequest.getPatientLastName());
    statement.setInt(3, newReligiousRequest.getEmpID());
    statement.setInt(4, newReligiousRequest.getPatientID());
    statement.setString(5, newReligiousRequest.getReligion());
    statement.setString(6, newReligiousRequest.getSpecialRequests());
    statement.setInt(7, newReligiousRequest.getServiceID());
  }

  @Override
  public void removeFromSQLTable(ServiceRequest request) throws IOException, SQLException {
    int serviceID = request.getServiceID();
    Connection connection = Vdb.Connect();
    String query = "DELETE FROM RELIGIOUSREQUESTS" + "WHERE serviceID=?";
    PreparedStatement statement = connection.prepareStatement(query);
    statement.setInt(1, serviceID);
  }

  @Override
  public void addServiceRequest(ServiceRequest request) throws IOException, SQLException {
    ReligiousRequest newReligiousRequest = (ReligiousRequest) request;

    System.out.println("Adding to local arraylist...");
    allReligiousRequest.add(newReligiousRequest); // Store a local copy
    updateRequest(newReligiousRequest); // Store on database
    addToSQLTable(request);
  }

  @Override
  public void removeServiceRequest(ServiceRequest request) throws IOException, SQLException {
    int serviceID = request.getServiceID();
    Predicate<ReligiousRequest> condition =
        religiousRequest -> religiousRequest.getServiceID() == serviceID;
    allReligiousRequest.removeIf(condition);
    removeFromSQLTable(request);
  }

  @Override
  public ArrayList<? extends ServiceRequest> getAllServiceRequests() {
    return allReligiousRequest;
  }

  @Override
  public void setAllServiceRequests(ArrayList<? extends ServiceRequest> serviceRequests)
      throws SQLException {
    allReligiousRequest.clear();
    for (ServiceRequest r : serviceRequests) {
      ReligiousRequest request = (ReligiousRequest) r;
      allReligiousRequest.add(request);
    }
    createSQLTable();
  }

  @Override
  public void updateRequest(ServiceRequest Request) throws SQLException {
    // also needs to add to csv
    ReligiousRequest newReligiousRequest = (ReligiousRequest) Request;
    Connection connection = Vdb.Connect();
    String query =
        "UPDATE RELIGIOUSREQUESTS"
            + "SET firstName=?, lastName=?,patientID=?,userID=?,religion=?,specialRequests=?"
            + "WHERE serviceID=?";
    PreparedStatement statement = connection.prepareStatement(query);
    statement.setString(1, newReligiousRequest.getPatientFirstName());
    statement.setString(2, newReligiousRequest.getPatientLastName());
    statement.setInt(3, newReligiousRequest.getEmpID());
    statement.setInt(4, newReligiousRequest.getPatientID());
    statement.setString(5, newReligiousRequest.getReligion());
    statement.setString(6, newReligiousRequest.getSpecialRequests());
    statement.setInt(7, newReligiousRequest.getServiceID());
  }
}
