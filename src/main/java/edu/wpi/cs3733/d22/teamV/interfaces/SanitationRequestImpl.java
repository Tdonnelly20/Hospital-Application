package edu.wpi.cs3733.d22.teamV.interfaces;

import edu.wpi.cs3733.d22.teamV.ServiceRequests.SanitationRequest;
import java.util.List;

public interface SanitationRequestImpl {

  List<SanitationRequest> getAllSanitationRequests();

  void addSanitationRequest(
      String patientFirstName,
      String patientLastName,
      int patientID,
      int hospitalID,
      String roomLocation,
      String hazardName,
      String requestDetails);

  void removeSanitationRequest();

  // void updateSanitationRequest()
}
