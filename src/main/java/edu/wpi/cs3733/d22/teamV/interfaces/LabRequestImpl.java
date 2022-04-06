package edu.wpi.cs3733.d22.teamV.interfaces;

import edu.wpi.cs3733.d22.teamV.ServiceRequests.LabRequest;
import java.util.List;

public interface LabRequestImpl {

  List<LabRequest> getAllLabRequests();

  void addLabRequest(
      int userID, int patientID, String firstName, String lastName, String lab, String status);

  void removeLabRequest(int userID);
}
