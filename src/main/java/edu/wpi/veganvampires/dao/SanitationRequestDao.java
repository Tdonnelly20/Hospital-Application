package edu.wpi.veganvampires.dao;

import edu.wpi.veganvampires.interfaces.SanitationRequestImpl;
import edu.wpi.veganvampires.main.Vdb;
import edu.wpi.veganvampires.objects.SanitationRequest;
import java.util.ArrayList;
import java.util.List;

public class SanitationRequestDao implements SanitationRequestImpl {
  private static ArrayList<SanitationRequest>
      allSanitationRequests; // A local list of all sanitation requests, updated via Vdb

  /** Initialize the arraylist */
  public SanitationRequestDao() {
    allSanitationRequests = new ArrayList<SanitationRequest>();
    // TODO: Add info from the database to the local arraylist
  }

  @Override
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
  @Override
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
    Vdb.addSanitationRequest(newSanitationRequest);
  }

  @Override
  public void removeSanitationRequest() {} // TODO
}
