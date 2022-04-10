package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.interfaces.DaoInterface;
import edu.wpi.cs3733.d22.teamV.serviceRequest.SanitationRequest;
import edu.wpi.cs3733.d22.teamV.serviceRequest.ServiceRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SanitationRequestDao extends DaoInterface {
  private static ArrayList<SanitationRequest>
      allSanitationRequests; // A local list of all sanitation requests, updated via Vdb

  /** Initialize the arraylist */
  public SanitationRequestDao() {
    allSanitationRequests = new ArrayList<SanitationRequest>();
    // TODO: Add info from the database to the local arraylist
  }

  public List<SanitationRequest> getAllSanitationRequests() {
    return allSanitationRequests;
  }

  /**
   * Receive a medicine delivery from the controller, store it locally, then send it to Vdb
   *
   * @param patientFirstName
   * @param patientLastName
   * @param patientID
   * @param hospitalID
   * @param roomLocation
   * @param hazardName
   * @param requestDetails
   */
  public void addSanitationRequest(
      String patientFirstName,
      String patientLastName,
      int patientID,
      int hospitalID,
      String hazardName,
      String roomLocation,
      String requestDetails) {
    SanitationRequest newSanitationRequest =
        new SanitationRequest(
            patientFirstName,
            patientLastName,
            patientID,
            hospitalID,
            roomLocation,
            hazardName,
            requestDetails);

    System.out.println("Adding to local arraylist...");
    allSanitationRequests.add(newSanitationRequest); // Store a local copy
    updateSanitationRequestDB(newSanitationRequest); // Store on database
  }

  // Send to the database
  private void updateSanitationRequestDB(SanitationRequest newSanitationRequest) {
    System.out.println("Sending to database...");
  }

  public void removeSanitationRequest() {} // TODO

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
  public void setAllServiceRequests(ArrayList<? extends ServiceRequest> serviceRequests)
      throws SQLException {}
}
