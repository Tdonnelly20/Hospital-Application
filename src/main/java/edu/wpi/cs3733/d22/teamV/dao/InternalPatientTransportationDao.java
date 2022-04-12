package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.interfaces.DaoInterface;
import edu.wpi.cs3733.d22.teamV.main.VApp;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.servicerequests.InternalPatientTransportation;
import edu.wpi.cs3733.d22.teamV.servicerequests.ServiceRequest;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class InternalPatientTransportationDao extends DaoInterface {

  // A local list of all internal patient transportations, updated via Vdb
  private static ArrayList<InternalPatientTransportation> allInternalPatientTransportations;

  /** Initialize the arraylist */
  public InternalPatientTransportationDao() {
    allInternalPatientTransportations = new ArrayList<InternalPatientTransportation>();
  }

  public void setAllInternalPatientTransportations(
      ArrayList<InternalPatientTransportation> internalPatientTransportations) {
    allInternalPatientTransportations = internalPatientTransportations;
  }

  public ArrayList<InternalPatientTransportation> getInternalPatientTransportations() {
    return allInternalPatientTransportations;
  }

  /**
   * Receive an internal patient transportation from the controller, store it locally, then send it
   * to Vdb
   *
   * @param patientID
   * @param nodeID
   * @param requestDetails
   */
  public void addInternalPatientTransportation(
      String nodeID, int patientID, int hospitalID, String requestDetails) {
    InternalPatientTransportation newInternalPatientTransportation =
        new InternalPatientTransportation(nodeID, patientID, hospitalID, requestDetails);

    System.out.println("Adding to local arraylist...");
    allInternalPatientTransportations.add(newInternalPatientTransportation); // Store a local copy
  }

  @Override
  public void loadFromCSV() throws IOException, SQLException {
    String line = "";
    FileReader fr = new FileReader(VApp.currentPath + "\\PatientTransportations.CSV");
    BufferedReader br = new BufferedReader(fr);
    String headerLine = br.readLine();
    String splitToken = ",";
    ArrayList<InternalPatientTransportation> transportations = new ArrayList<>();
    while ((line = br.readLine()) != null) // should create a database based on csv file
    {
      String[] data;
      data = line.split(splitToken);
      InternalPatientTransportation transportation =
          new InternalPatientTransportation(
              data[0], Integer.parseInt(data[1]), Integer.parseInt(data[2]), data[3]);
      transportation.setServiceID(Integer.parseInt(data[4]));
      transportations.add(transportation);
    }
    allInternalPatientTransportations = transportations;
  }

  @Override
  public void saveToCSV() throws IOException {
    FileWriter fw = new FileWriter(VApp.currentPath + "\\PatientTransportations.csv");
    BufferedWriter bw = new BufferedWriter(fw);
    bw.append("location,patientID,hospitalID,requestDetails,serviceID");

    for (ServiceRequest request : getAllServiceRequests()) {

      InternalPatientTransportation internalPatientTransportation =
          (InternalPatientTransportation) request;

      String[] outputData = {
        internalPatientTransportation.getNodeID(),
        String.valueOf(internalPatientTransportation.getPatientID()),
        String.valueOf(internalPatientTransportation.getHospitalID()),
        internalPatientTransportation.getRequestDetails(),
        String.valueOf(internalPatientTransportation.getServiceID())
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
  public void createSQLTable() throws SQLException {
    Connection connection = Vdb.Connect();
    Statement exampleStatement = connection.createStatement();
    DatabaseMetaData meta = connection.getMetaData();

    ResultSet set = meta.getTables(null, null, "PATIENTTRANSPORTATION", new String[] {"TABLE"});
    String query = "";

    if (!set.next()) {
      query =
          "CREATE TABLE PATIENTTRANSPORTATION(location char(50), patientID int, hospitalID int, requestDetails char(250), serviceID int)";
      exampleStatement.execute(query);
    } else {
      query = "DROP TABLE PATIENTTRANSPORTATION";
      exampleStatement.execute(query);
      createSQLTable();
      return;
    }
    for (InternalPatientTransportation internalPatientTransportation :
        allInternalPatientTransportations) {
      addToSQLTable(internalPatientTransportation);
    }
  }

  @Override
  public void addToSQLTable(ServiceRequest request) throws SQLException {
    InternalPatientTransportation internalPatientTransportation =
        (InternalPatientTransportation) request;

    String query = "";
    Connection connection = Vdb.Connect();
    assert connection != null;
    Statement statement = connection.createStatement();

    query =
        "INSERT INTO PATIENTTRANSPORTATION("
            + "location,patientID,hospitalID,requestDetails,serviceID) VALUES "
            + "('"
            + internalPatientTransportation.getNodeID()
            + "', "
            + internalPatientTransportation.getPatientID()
            + ", "
            + internalPatientTransportation.getServiceID()
            + ", '"
            + internalPatientTransportation.getRequestDetails()
            + "')";

    statement.execute(query);
  }

  @Override
  public void updateServiceRequest(ServiceRequest request, int serviceID)
      throws SQLException, IOException {
    InternalPatientTransportation transportation = (InternalPatientTransportation) request;
    transportation.setServiceID(serviceID);
    removeServiceRequest(transportation);
    allInternalPatientTransportations.add(transportation);
    addToSQLTable(transportation);
    saveToCSV();
  }

  @Override
  public void removeFromSQLTable(ServiceRequest request) throws IOException, SQLException {
    InternalPatientTransportation internalPatientTransportation =
        (InternalPatientTransportation) request;
    String query = "";
    Connection connection = Vdb.Connect();
    assert connection != null;
    Statement statement = connection.createStatement();

    query =
        "DELETE FROM PATIENTTRANSPORTATION WHERE serviceID = "
            + internalPatientTransportation.getServiceID();
    statement.execute(query);
  }

  @Override
  public void addServiceRequest(ServiceRequest request) throws IOException, SQLException {
    InternalPatientTransportation internalPatientTransportation =
        (InternalPatientTransportation) request;
    allInternalPatientTransportations.add(internalPatientTransportation);

    addToSQLTable(internalPatientTransportation);
    saveToCSV();
  }

  @Override
  public void removeServiceRequest(ServiceRequest request) throws IOException, SQLException {
    allInternalPatientTransportations.removeIf(
        value -> value.getPatientID() == request.getPatient().getPatientID());
    removeFromSQLTable(request);
    saveToCSV();
  }

  @Override
  public ArrayList<? extends ServiceRequest> getAllServiceRequests() {
    return getInternalPatientTransportations();
  }

  @Override
  public void setAllServiceRequests(ArrayList<? extends ServiceRequest> serviceRequests)
      throws SQLException {
    allInternalPatientTransportations = (ArrayList<InternalPatientTransportation>) serviceRequests;
  }
}
