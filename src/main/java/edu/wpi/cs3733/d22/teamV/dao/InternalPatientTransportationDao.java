package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.ServiceRequests.InternalPatientTransportation;
import edu.wpi.cs3733.d22.teamV.ServiceRequests.ServiceRequest;
import edu.wpi.cs3733.d22.teamV.interfaces.DaoInterface;
import java.io.IOException;
import java.sql.SQLException;
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
   * @param patientFirstName
   * @param patientLastName
   * @param patientID
   * @param roomNumber
   * @param requestDetails
   */
  public void addInternalPatientTransportation(
      String patientFirstName,
      String patientLastName,
      String roomNumber,
      int patientID,
      int hospitalID,
      String requestDetails) {
    InternalPatientTransportation newInternalPatientTransportation =
        new InternalPatientTransportation(
            patientFirstName, patientLastName, roomNumber, patientID, hospitalID, requestDetails);

    System.out.println("Adding to local arraylist...");
    allInternalPatientTransportations.add(newInternalPatientTransportation); // Store a local copy
  }

  @Override
  public void loadFromCSV() throws IOException, SQLException {}

  @Override
  public void saveToCSV() throws IOException {}

  @Override
  public void createSQLTable() throws SQLException {}

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
  public void setAllServiceRequests(ArrayList<? extends ServiceRequest> serviceRequests) throws SQLException {}
}
