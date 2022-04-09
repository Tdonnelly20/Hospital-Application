package edu.wpi.cs3733.d22.teamV.interfaces;

import edu.wpi.cs3733.d22.teamV.ServiceRequests.ReligiousRequest;
import java.sql.SQLException;
import java.util.List;

public interface ReligiousRequestImpl {

  List<ReligiousRequest> getAllReligiousRequest();

  void addReligiousRequest(
      String firstName,
      String lastName,
      int patientID,
      int userID,
      String religion,
      String specialRequests,
      int serviceID)
      throws SQLException;

  List<ReligiousRequest> allReligiousRequest();

  //
  void updateReligiousRequest(
      String firstName,
      String lastName,
      int patientID,
      int userID,
      String religion,
      String specialRequests,
      int serviceID)
      throws SQLException;

  void removeReligousRequest(int serviceID) throws SQLException;
}
