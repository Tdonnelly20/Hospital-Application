package edu.wpi.cs3733.d22.teamV.interfaces;

import edu.wpi.cs3733.d22.teamV.ServiceRequests.LaundryRequest;
import java.sql.SQLException;
import java.util.ArrayList;

public interface LaundryRequestImpl {

  ArrayList<LaundryRequest> getAllLaundryRequests();

  void addLaundryRequest(
      String userID,
      String patientID,
      String firstName,
      String lastName,
      int roomNumber,
      String details)
      throws SQLException;

  void removeLaundryRequest(String laundry) throws SQLException;
}
