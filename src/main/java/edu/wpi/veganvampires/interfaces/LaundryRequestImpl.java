package edu.wpi.veganvampires.interfaces;

import edu.wpi.veganvampires.objects.LaundryRequest;
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
