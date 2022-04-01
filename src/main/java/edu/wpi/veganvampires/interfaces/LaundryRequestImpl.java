package edu.wpi.veganvampires.interfaces;

import edu.wpi.veganvampires.objects.LaundryRequest;
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
