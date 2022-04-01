package edu.wpi.veganvampires.dao;

import edu.wpi.veganvampires.objects.LaundryRequest;
import edu.wpi.veganvampires.interfaces.LaundryRequestImpl;
import edu.wpi.veganvampires.Vdb;
import java.util.ArrayList;
import java.util.List;

public class LaundryRequestDao implements LaundryRequestImpl {
  private static ArrayList<LaundryRequest> allLaundryRequests;

  /** Initialize the array list */
  public LaundryRequestDao() {
    allLaundryRequests = new ArrayList<LaundryRequest>();
    // TODO: Add info from the database to the local arraylist
  }

  @Override
  public List<LaundryRequest> getAllLaundryRequests() {
    return allLaundryRequests;
  }

  @Override
  public void addLaundryRequest(
      String employeeID,
      String patientID,
      String firstName,
      String lastName,
      int roomNumber,
      String details) {
    LaundryRequest newLaundryRequest =
        new LaundryRequest(employeeID, patientID, firstName, lastName, roomNumber, details);

    System.out.println("Adding to local arraylist...");
    allLaundryRequests.add(newLaundryRequest);
    updateLaundryRequestDB(newLaundryRequest);
  }

  private void updateLaundryRequestDB(LaundryRequest newLaundryRequest) {
    System.out.println("Sending to database...");
    Vdb.addLaundryRequest(newLaundryRequest);
  }

  @Override
  public void removeLaundryRequest() {} // TODO
}
