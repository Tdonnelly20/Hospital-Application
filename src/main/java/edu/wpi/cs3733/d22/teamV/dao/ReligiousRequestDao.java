package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.ServiceRequests.ReligiousRequest;
import edu.wpi.cs3733.d22.teamV.interfaces.ReligiousRequestImpl;
import java.util.ArrayList;
import java.util.List;

public class ReligiousRequestDao implements ReligiousRequestImpl {
  private static ArrayList<ReligiousRequest>
      allReligiousRequest; // A local list of all religious requests, updated via Vdb

  /** Initialize the arraylist */
  public ReligiousRequestDao() {
    allReligiousRequest = new ArrayList<ReligiousRequest>();
    // TODO: Add info from the database to the local arraylist
  }

  @Override
  public List<ReligiousRequest> getAllReligiousRequest() {
    return allReligiousRequest;
  }

  /**
   * Receive a religious request from the controller, store it locally, then send it to Vdb
   *
   * @param firstName
   * @param lastName
   * @param patientID
   * @param userID
   * @param Christian
   * @param Jewish
   * @param Protestant
   * @param Islam
   * @param Muslim
   * @param Buddhist
   * @param Hindu
   * @param Other
   * @param specialRequests
   */
  @Override
  public void addReligiousRequest(
      String firstName,
      String lastName,
      int patientID,
      int userID,
      boolean Christian,
      boolean Jewish,
      boolean Protestant,
      boolean Islam,
      boolean Muslim,
      boolean Buddhist,
      boolean Hindu,
      boolean Other,
      String specialRequests) {
    ReligiousRequest newReligiousRequest =
        new ReligiousRequest(
            firstName,
            lastName,
            patientID,
            userID,
            Christian,
            Jewish,
            Protestant,
            Islam,
            Muslim,
            Buddhist,
            Hindu,
            Other,
            specialRequests);

    System.out.println("Adding to local arraylist...");
    allReligiousRequest.add(newReligiousRequest); // Store a local copy
    updateReligiousRequestDB(newReligiousRequest); // Store on database
  }

  //
  @Override
  public List<ReligiousRequest> allReligiousRequest() {
    return null;
  }

  //
  @Override
  public void addReligousRequest(
      String firstName,
      String lastName,
      int patientID,
      int userID,
      boolean Christian,
      boolean Jewish,
      boolean Protestant,
      boolean Islam,
      boolean Muslim,
      boolean Buddhist,
      boolean Hindu,
      boolean Other,
      String specialRequests) {

    updateReligiousRequestDB(
        new ReligiousRequest(
            firstName,
            lastName,
            patientID,
            userID,
            Christian,
            Jewish,
            Protestant,
            Islam,
            Muslim,
            Buddhist,
            Hindu,
            Other,
            specialRequests));
  }

  // Send to the database
  private void updateReligiousRequestDB(ReligiousRequest newReligiousRequest) {
    System.out.println("Sending to database...");
  }

  @Override
  public void removeReligousRequest() {}
}
