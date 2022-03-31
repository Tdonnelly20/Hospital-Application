package edu.wpi.veganvampires.Interfaces;

import edu.wpi.veganvampires.Features.LaundryRequest;
import java.util.List;

public interface LaundryRequestImpl {

  List<LaundryRequest> getAllLaundryRequests();

  void addLaundryRequest(
      String employeeID,
      String patientID,
      String firstName,
      String lastName,
      int roomNumber,
      String details);

  void removeLaundryRequest();
}
