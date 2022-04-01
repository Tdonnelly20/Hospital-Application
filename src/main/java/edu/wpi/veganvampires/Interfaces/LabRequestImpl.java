package edu.wpi.veganvampires.Interfaces;

import edu.wpi.veganvampires.Features.LabRequest;
import java.util.List;

public interface LabRequestImpl {

  List<LabRequest> getAllLabRequests();

  void addLabRequest(
      int userID, int patientID, String firstName, String lastName, String lab, String status);

  void removeLabRequest();
}
