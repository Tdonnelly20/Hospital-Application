package edu.wpi.veganvampires.Interfaces;

import edu.wpi.veganvampires.Features.SanitationRequest;
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
}
